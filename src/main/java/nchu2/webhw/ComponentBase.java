package nchu2.webhw;

import nchu2.webhw.service.DatabaseLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class ComponentBase {
    @Autowired
    protected DatabaseLog logger;
}
