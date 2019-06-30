package nchu2.webhw.service;

import nchu2.webhw.ComponentBase;
import nchu2.webhw.controller.WebSocket;
import nchu2.webhw.model.Tables;
import nchu2.webhw.model.tables.daos.LoginDao;
import nchu2.webhw.model.tables.daos.NotificationDao;
import nchu2.webhw.model.tables.daos.ReceiveDao;
import nchu2.webhw.model.tables.pojos.Login;
import nchu2.webhw.model.tables.pojos.Notification;
import nchu2.webhw.model.tables.pojos.Receive;
import nchu2.webhw.model.tables.records.ReceiveRecord;
import nchu2.webhw.properties.CRUDOperation;
import nchu2.webhw.properties.MessageParam;
import nchu2.webhw.properties.mapping.MessageType;
import nchu2.webhw.properties.mapping.UserType;
import org.jooq.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static nchu2.webhw.properties.Vars.DB_BROADCAST_TRIGGER;

@Service
public class NotificationService extends ComponentBase {
    private final NotificationDao nDao;
    private final ReceiveDao rnDao;
    private final DSLContext dsl;
    private final WebSocket ws;

    public NotificationService(DSLContext dsl, WebSocket ws) {
        this.dsl = dsl;
        this.nDao = new NotificationDao(dsl.configuration());
        this.rnDao = new ReceiveDao(dsl.configuration());
        this.ws = ws;
    }

    /**
     * 公告消息，推送给所有用户
     * 旧的消息不会自动推送给新注册用户
     *
     * @param n 消息内容
     */
    public void broadcast(UserType target, Notification n) {
        n.setType(MessageType.Broadcast);
        List<Login> loginList = new LoginDao(dsl.configuration()).fetchByType(target);
        ArrayList<ReceiveRecord> records = new ArrayList<>(loginList.size());
        new NotificationDao(dsl.configuration()).insert(n);
        ReceiveRecord receiveRecord = new ReceiveRecord().setNId(n.getNId());
        for (Login login : loginList) {
            receiveRecord.changed(true);
            records.add(deepClone(receiveRecord.setReceiver(login.getLoginname())));
        }
        batchInsert(Tables.RECEIVE, records);
        ws.broadcast(n, target);
    }

    public void unicast(String receiverLogin, Notification n) {
        new NotificationDao(dsl.configuration()).insert(n);
        new ReceiveDao(dsl.configuration()).insert(new Receive()
                .setNId(n.getNId())
                .setReceiver(receiverLogin)
        );
        ws.unicast(n, receiverLogin);
    }

    public void batchInsert(Table<? extends Record> table, Collection<? extends Record> records) {
        Object insert = dsl.insertInto(table);
        for (Record record : records) {
            insert = ((InsertSetMoreStep) insert).newRecord();
            insert = ((InsertSetStep) insert).set(record);
        }
        insert = ((InsertSetMoreStep) insert).execute();
    }

    public void dbTrigger(CRUDOperation operation, Object o, String loginName) {
        Class clazz = o.getClass();
        MessageParam[] mps = DB_BROADCAST_TRIGGER.get(operation).get(clazz);
        if (mps == null) return;
        for (MessageParam mp : mps) {
            switch (mp.getMessageType()) {
                case Broadcast:
                    broadcast(mp.getChannel(), mp.getNotification(o, loginName));
                    break;
                case Unicast:
                    unicast(mp.getReceiver(o), mp.getNotification(o, loginName));
                    break;
                case LiveUpdate:
                    ws.liveNotify();
                    break;
            }
        }
    }

}
