package nchu2.webhw.service;

import nchu2.webhw.ComponentBase;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 所有服务的父类
 */
@Service
abstract class ServiceBase extends ComponentBase {
    @Autowired
    protected DSLContext dsl;
    @Autowired
    protected NotificationService notification;
}
