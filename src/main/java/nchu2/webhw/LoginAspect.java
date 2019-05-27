package nchu2.webhw;

import nchu2.webhw.Service.DatabaseLog;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoginAspect {
    private final DatabaseLog databaseLog;

    public LoginAspect(DatabaseLog databaseLog) {
        this.databaseLog = databaseLog;
    }

    class LoginSession{
        long id;
    }
}
