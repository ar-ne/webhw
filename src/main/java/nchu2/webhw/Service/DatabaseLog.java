package nchu2.webhw.Service;

import nchu2.webhw.tables.daos.LogDao;
import nchu2.webhw.tables.pojos.Log;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class DatabaseLog {
    private final LogDao dao;

    public DatabaseLog(DSLContext dsl) {
        this.dao = new LogDao(dsl.configuration());
    }

    public void log(Long user, String operation, String result) {
        Log logs = new Log();
        logs.setUserId(user);
        logs.setOperation(operation);
        logs.setResult(result);
        logs.setTime(new Timestamp(new Date().getTime()));
        dao.insert(logs);
    }
}
