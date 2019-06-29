package nchu2.webhw.api;

import nchu2.webhw.ComponentBase;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 所有API的父类
 */
public abstract class APIBase extends ComponentBase {
    @Autowired
    DSLContext dsl;  //数据库连接信息

}
