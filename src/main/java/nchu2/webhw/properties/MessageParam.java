package nchu2.webhw.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import nchu2.webhw.model.tables.pojos.Notification;
import nchu2.webhw.model.tables.pojos.Opinion;
import nchu2.webhw.model.tables.pojos.Production;
import nchu2.webhw.model.tables.pojos.Ticket;
import nchu2.webhw.properties.mapping.MessageType;
import nchu2.webhw.properties.mapping.UserType;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class MessageParam {
    UserType channel;
    MessageType messageType;

    public String getReceiver(Object o) {
        if (o.getClass().equals(Ticket.class))
            return ((Ticket) o).getLoginname();
        if (o.getClass().equals(Opinion.class))
            return ((Opinion) o).getLoginname();
        return null;
    }

    public Notification getNotification(Object o, String loginName) {
        if (messageType.equals(MessageType.LiveUpdate)) return null;
        Notification notification = new Notification()
                .setSender(loginName)
                .setType(messageType)
                .setTime(new Timestamp(System.currentTimeMillis()));
        if (o.getClass().equals(Production.class)) {
            Production p = (Production) o;
            notification.setContent(p.getIntro());
            notification.setTitle("新产品发布了！");
            notification.setUrl("/priv/production?arg=" + p.getPId());
        }
        if (o.getClass().equals(Ticket.class)) {
            Ticket t = (Ticket) o;
            if (channel == UserType.Staff) {
                notification.setContent(t.getContent());
                notification.setTitle("有新的投诉待处理！");
                notification.setUrl("/priv/answerTicket?arg=" + t.getTId());
            }
            if (channel == UserType.Customer) {
                notification.setContent(t.getReply());
                notification.setTitle("有新的投诉处理结果！");
                notification.setUrl("/priv/answerTicket?arg=" + t.getTId());
            }
        }
        if (o.getClass().equals(Opinion.class)) {
            Opinion x = (Opinion) o;
            notification.setTitle(x.getOAccept() ? "意见审核通过" : "意见被拒绝");
            notification.setContent(x.getContent());
            notification.setUrl("#");
        }
        return notification;
    }
}
