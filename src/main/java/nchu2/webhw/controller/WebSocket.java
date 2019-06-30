package nchu2.webhw.controller;

import nchu2.webhw.ComponentBase;
import nchu2.webhw.model.tables.daos.NotificationDao;
import nchu2.webhw.model.tables.daos.ReceiveDao;
import nchu2.webhw.model.tables.pojos.Notification;
import nchu2.webhw.model.tables.pojos.Receive;
import nchu2.webhw.properties.mapping.UserType;
import org.jooq.DSLContext;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WebSocket extends ComponentBase {
    private final SimpMessagingTemplate template;
    private final DSLContext dsl;

    public WebSocket(SimpMessagingTemplate template, DSLContext dsl) {
        this.template = template;
        this.dsl = dsl;
    }

    @MessageMapping("read")
    public void markAsRead(Long[] nIDs, Principal principal) {
        logger.debug("mark as read => IDs = {}, user = {}", nIDs, principal.getName());
        List<Receive> recs = new ReceiveDao(dsl.configuration()).fetchByNId(nIDs);
        for (Receive rec : recs) {
            rec.setNRead(true);
        }
        new ReceiveDao(dsl.configuration()).update(recs);
    }

    public void broadcast(Notification notification, UserType channel) {
        logger.debug("Send broadcast => channel = {}, notification = {}", channel, notification);
        template.convertAndSend("/topic/bc/" + channel, notification);
    }

    public void liveNotify() {
        logger.debug("Send live notify !");
        template.convertAndSend("/topic/live", "NX");
    }

    public void unicast(Notification n, String receiver) {
        logger.debug("Send to user => receiver = {}, notification = {}", receiver, n);
        template.convertAndSendToUser(receiver, "/topic/uc", n);
    }

    @MessageMapping("sync")
    @SendToUser("/topic/sync")
    public List findAll(Principal principal) {
        List<Receive> receives = new ReceiveDao(dsl.configuration()).fetchByReceiver(principal.getName());
        ArrayList<Long> nIDs = new ArrayList<>(receives.size());
        for (Receive receive : receives)
            if (receive.getNRead() == null || !receive.getNRead())
                nIDs.add(receive.getNId());
        logger.debug("Client request sync => user = {}, IDs = {}", principal.getName(), nIDs);
        return new NotificationDao(dsl.configuration()).fetchByNId(nIDs.toArray(new Long[0]));
    }
}
