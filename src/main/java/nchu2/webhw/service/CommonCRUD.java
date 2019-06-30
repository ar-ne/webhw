package nchu2.webhw.service;

import nchu2.webhw.model.tables.daos.OpinionDao;
import nchu2.webhw.model.tables.daos.ProductionDao;
import nchu2.webhw.model.tables.daos.TicketDao;
import nchu2.webhw.model.tables.pojos.Log;
import nchu2.webhw.model.tables.pojos.Opinion;
import nchu2.webhw.model.tables.pojos.Production;
import nchu2.webhw.model.tables.pojos.Ticket;
import nchu2.webhw.properties.CRUDOperation;
import nchu2.webhw.utils.LogMsgHelper;
import org.jooq.impl.DAOImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;

import static nchu2.webhw.properties.Vars.POJO_DAO_MAPPER;
import static nchu2.webhw.properties.Vars.POJO_DAO_TABLE;

/**
 * 常用的增删改查
 */
@Service
public class CommonCRUD extends ServiceBase {
    public <T> T newRecord(T o, String loginName) {
        return doOperation(o, loginName, CRUDOperation.INSERT);
    }

    public <T> T updateRecord(T o, String loginName) {
        return doOperation(o, loginName, CRUDOperation.UPDATE);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    <T> T doOperation(T o, String loginName, CRUDOperation operation) {
        try {
            Class c = o.getClass();
            switch (operation) {
                case INSERT:
                    ((DAOImpl) POJO_DAO_MAPPER.get(c).newInstance(dsl.configuration())).insert(o);
                    break;
                case UPDATE:
                    ((DAOImpl) POJO_DAO_MAPPER.get(c).newInstance(dsl.configuration())).update(o);
                    break;
                case DELETE:
                    ((DAOImpl) POJO_DAO_MAPPER.get(c).newInstance(dsl.configuration())).delete(o);
                    break;
            }
            Log log = LogMsgHelper.CRUD.newOperation(POJO_DAO_TABLE.get(c), loginName, operation, o);
            dbLogger.log(log);
            notification.dbTrigger(operation, o, loginName);
            return o;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Production getProduction(Integer id) {
        return new ProductionDao(dsl.configuration()).fetchOneByPId(id);
    }

    public void updateOpinion(Opinion[] opinions) {
        new OpinionDao(dsl.configuration()).update(opinions);
    }

    public Ticket getTicket(Long id) {
        return new TicketDao(dsl.configuration()).fetchOneByTId(id);
    }
}
