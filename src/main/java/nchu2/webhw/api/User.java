package nchu2.webhw.api;

import nchu2.webhw.model.tables.daos.LoanDao;
import nchu2.webhw.model.tables.daos.OpinionDao;
import nchu2.webhw.model.tables.daos.OrdersDao;
import nchu2.webhw.model.tables.daos.ProductionDao;
import nchu2.webhw.model.tables.pojos.Opinion;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@Path("user")
@RestController
public class User extends APIBase {
    @GET
    public String hi() {
        return "hi";
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
        return new OrdersDao(dsl.configuration()).fetchByLoginname(getLoginName(context));
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
    public List adviceL() {
        return new OpinionDao(dsl.configuration()).fetchByOAccept();
    }

    @POST
    @Path("advice/{action}")//action=x/v
    @Produces(TEXT_PLAIN)
    @Consumes(APPLICATION_JSON)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String adviceO(@PathParam("action") String action, Opinion[] opinions) {
        boolean act = action.equalsIgnoreCase("v");
        for (Opinion opinion : opinions) {
            opinion.setOAccept(act);
        }
        return String.format("完成，%s 条建议被 %s", opinions.length, act ? "提交上级" : "忽略");
    }
}
