package nchu2.webhw.utils;

import nchu2.webhw.model.tables.pojos.Log;
import nchu2.webhw.model.tables.pojos.Login;
import nchu2.webhw.properties.CRUDOperation;
import org.jooq.Table;

import java.sql.Timestamp;

import static java.lang.Math.min;

/**
 * 用于生成日志信息
 */
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
        public static Log newOperation(Table table, String loginName, CRUDOperation op, Object o) {
            Log log = new Log();
            log.setOperation(op.name() + " on table " + table.getName());
            log.setLoginname(loginName);
            log.setResult(o.toString().substring(0, min(o.toString().length(), 200)));
            log.setTime(getTimestamp());
            return log;
        }
    }
}
