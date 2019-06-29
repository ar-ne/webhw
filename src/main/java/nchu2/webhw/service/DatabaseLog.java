package nchu2.webhw.service;

import nchu2.webhw.model.tables.daos.LogDao;
import nchu2.webhw.model.tables.pojos.Log;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * 数据库日志
 */
@Service
public class DatabaseLog {
    private final LogDao dao;

    public DatabaseLog(DSLContext dsl) {
        this.dao = new LogDao(dsl.configuration());
    }

    public void log(String loginName, String operation, String result) {
        Log log = new Log();
        log.setLoginname(loginName);
        log.setOperation(operation);
        log.setResult(result);
        log.setTime(new Timestamp(System.currentTimeMillis()));
        dao.insert(log);
    }

    public void log(String message) {
        Log log = new Log();
        log.setResult(message);
        log.setTime(new Timestamp(System.currentTimeMillis()));
        dao.insert(log);
    }

    public void log(Log log) {
        dao.insert(log);
    }
}
