package nchu2.webhw.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import nchu2.webhw.model.Tables;
import nchu2.webhw.model.tables.daos.*;
import nchu2.webhw.model.tables.pojos.Opinion;
import nchu2.webhw.model.tables.pojos.Production;
import nchu2.webhw.properties.mapping.UserType;
import nchu2.webhw.service.CommonCRUD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.Serializable;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

/**
 * 三种用户的接口
 * 根据表名获取表中信息
 */
@Path("user")
@RestController
public class User extends APIBase {
    @Autowired
    private CommonCRUD commonCRUD;

    @GET
    public String hi() {
        return "hi";
    }

    @GET
    @Path("chart/{opt}")
    @Produces(APPLICATION_JSON)
//    @Cacheable(value = Vars.CacheValues.chart)
    public Object chart(@PathParam("opt") String opt) {
        if (opt.equalsIgnoreCase("loan")) {
            int[] counts = new int[6];
            for (int i = 0; i < counts.length - 1; i++)
                counts[i] = dsl.fetchCount(Tables.LOAN, Tables.LOAN.MONEY.between(i * 10000, (i + 1) * 10000 - 1));
            counts[5] = dsl.fetchCount(Tables.LOAN, Tables.LOAN.MONEY.greaterThan(50000));
            return new LoanChart(counts);
        }
        if (opt.equalsIgnoreCase("order")) {
            List<Production> productions = new ProductionDao(dsl.configuration()).findAll();
            int[] pID = new int[productions.size()];
            int[] orderCount = new int[productions.size()];
            for (int i = 0; i < pID.length; i++) {
                pID[i] = productions.get(i).getPId();
                orderCount[i] = dsl.fetchCount(Tables.ORDERS, Tables.ORDERS.P_ID.eq(productions.get(i).getPId()));
            }
            return new OrderChart(pID, orderCount);
        }
        return null;
    }

    @GET
    @Path("map")
    @Produces(APPLICATION_JSON)
    public List map() {
        return new MapDao(dsl.configuration()).findAll();
    }

    @GET
    @Path("production")
    @Produces(APPLICATION_JSON)
    public List production() {
        return new ProductionDao(dsl.configuration()).findAll();
    }

    @GET
    @Path("orders")
    @Produces(APPLICATION_JSON)
    public List orders(@Context SecurityContext context) {
        if (getUserType(context) != UserType.Customer) return new OrdersDao(dsl.configuration()).findAll();
        return new OrdersDao(dsl.configuration()).fetchByLoginname(getLoginName(context));
    }

    @GET
    @Path("ticket")
    @Produces(APPLICATION_JSON)
    public List ticket(@Context SecurityContext context) {
        if (getUserType(context) != UserType.Customer) return new TicketDao(dsl.configuration()).findAll();
        return new TicketDao(dsl.configuration()).fetchByLoginname(getLoginName(context));
    }

    @GET
    @Path("loan")
    @Produces(APPLICATION_JSON)
    public List loan() {
        return new LoanDao(dsl.configuration()).findAll();
    }

    @GET
    @Path("advice")
    @Produces(APPLICATION_JSON)
    public List adviceL(@Context SecurityContext context) {
        if (getUserType(context) != UserType.Customer) return new OpinionDao(dsl.configuration()).findAll();
        return new OpinionDao(dsl.configuration()).fetchByOAccept();
    }

    /**
     * 提交建议审核结果
     *
     * @param action   操作类型 x忽略，v提交上级
     * @param opinions 所有被选定的意见
     * @return
     */
    @POST
    @Path("advice/{action}")//action=x/v
    @Produces(TEXT_PLAIN)
    @Consumes(APPLICATION_JSON)
    public String adviceO(@PathParam("action") String action, @Context Response response, Opinion[] opinions) {
        boolean act = action.equalsIgnoreCase("v");
        for (Opinion opinion : opinions) opinion.setOAccept(act);
        commonCRUD.updateOpinion(opinions);
        return String.format("完成，%s 条建议被 %s", opinions.length, act ? "提交上级" : "忽略");
    }

    /**
     * 产品购买统计
     */
    @Data
    @AllArgsConstructor
    public static class OrderChart implements Serializable {
        private int[] productID;    //产品编号，放label里
        private int[] orderCount;        //购买人数，放data数组里
    }

    /**
     * 购买人数，左闭右开区间
     */
    @Data
    @AllArgsConstructor
    public static class LoanChart implements Serializable {
        public int[] counts;
    }
}
