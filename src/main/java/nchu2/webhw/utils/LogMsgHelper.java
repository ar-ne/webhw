package nchu2.webhw.utils;

import nchu2.webhw.model.tables.pojos.Log;
import nchu2.webhw.model.tables.pojos.Login;
import org.jooq.Table;

import java.sql.Timestamp;

public class LogMsgHelper {
    public static Timestamp getTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static class Auth {
        public static Log newLogin(Login login) {
            Log log = new Log();
            log.setOperation(String.format("Add new login info=>loginName: %s, passwd: masked, type: %s", login.getLoginname(), login.getType()));
            log.setTime(getTimestamp());
            return log;
        }

        public static Log loginAttempt(String name, boolean isAuthenticated) {
            Log log = new Log();
            log.setLoginname(name);
            log.setResult("isAuthenticated=" + isAuthenticated);
            log.setOperation("User login");
            log.setTime(getTimestamp());
            return log;
        }
    }

    public static class CRUD {
        public static Log newOperation(Table table, String loginName, Operation op, Object o) {
            Log log = new Log();
            log.setOperation(op.name() + " on table " + table.getName());
            log.setLoginname(loginName);
            log.setResult(o.toString().substring(o.toString().length() >= 200 ? 200 : o.toString().length()));
            log.setTime(getTimestamp());
            return log;
        }

        public enum Operation {
            insert, edit, delete
        }
    }
}
