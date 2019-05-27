package nchu2.webhw.Service;

import nchu2.webhw.ComponentBase;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

@Service
abstract class ServiceBase extends ComponentBase {
    protected final DSLContext dsl;
    protected final DatabaseLog log;

    public ServiceBase(DSLContext dsl, DatabaseLog log) {
        this.dsl = dsl;
        this.log = log;
    }
}
