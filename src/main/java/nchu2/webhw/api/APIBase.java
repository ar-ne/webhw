package nchu2.webhw.api;

import nchu2.webhw.ComponentBase;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class APIBase extends ComponentBase {
    @Autowired
    DSLContext dsl;

}
