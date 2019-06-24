package nchu2.webhw.service;

import nchu2.webhw.model.tables.daos.ProductionDao;
import nchu2.webhw.model.tables.pojos.Log;
import nchu2.webhw.model.tables.pojos.Production;
import nchu2.webhw.utils.LogMsgHelper;
import org.jooq.impl.DAOImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;

import static nchu2.webhw.properties.Vars.POJO_DAO_MAPPER;
import static nchu2.webhw.properties.Vars.POJO_DAO_TABLE;

@Service
public class CommonCRUD extends ServiceBase {

    @SuppressWarnings("unchecked")
    @Transactional
    public Object newRecord(Object o, String loginName) {
        try {
            Class c = o.getClass();
            ((DAOImpl) POJO_DAO_MAPPER.get(c).newInstance(dsl.configuration())).insert(o);
            Log log = LogMsgHelper.CRUD.newOperation(POJO_DAO_TABLE.get(c), loginName, LogMsgHelper.CRUD.Operation.insert, o);
            logger.log(log);
            return o;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Production getProduction(Integer id) {
        return new ProductionDao(dsl.configuration()).fetchOneByPId(id);
    }
}
