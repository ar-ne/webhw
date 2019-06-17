package nchu2.webhw.utils;

import nchu2.webhw.model.tables.pojos.Log;
import nchu2.webhw.model.tables.pojos.Login;

import java.sql.Timestamp;

public class LogMsgHelper {
    public static class Auth {
        public static Log newLogin(Login login) {
            Log log = new Log();
            log.setOperation(String.format("Add new login info=>loginName: %s, passwd: masked, type: %s", login.getLoginname(), login.getType()));
            log.setTime(new Timestamp(System.currentTimeMillis()));
            return log;
        }

        public static Log loginAttempt(String name, boolean isAuthenticated) {
            Log log = new Log();
            log.setLoginname(name);
            log.setResult("isAuthenticated" + isAuthenticated);
            log.setOperation("User login");
            log.setTime(new Timestamp(System.currentTimeMillis()));
            return log;
        }
    }
}
