package nchu2.webhw.api;

import nchu2.webhw.model.Webhw;
import nchu2.webhw.model.tables.daos.*;
import nchu2.webhw.properties.mapping.UserType;
import org.jooq.Field;
import org.jooq.Table;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RestController
@Path("admin")
public class Admin extends APIBase {
    @GET
    @Path("log")
    @Produces(MediaType.APPLICATION_JSON)
    public List log() {
        return new LogDao(dsl.configuration()).findAll();
    }

    @GET
    @Path("users/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    public List users(@PathParam("type") String userType) {
        if (userType.equalsIgnoreCase("login"))
            return new LoginDao(dsl.configuration()).findAll();
        switch (UserType.valueOf(userType)) {
            case Customer:
                return new CustomerDao(dsl.configuration()).findAll();
            case Manager:
                return new ManagerDao(dsl.configuration()).findAll();
            case Staff:
                return new StaffDao(dsl.configuration()).findAll();
        }
        return new LoginDao(dsl.configuration()).findAll();
    }

    @GET
    @Path("fields")
    public String list() {
        StringBuilder builder = new StringBuilder();
        for (Table<?> table : Webhw.WEBHW.getTables()) {
            for (Field<?> field : table.fields()) {
                builder.append(table.getName()).append(".").append(field.getName().toLowerCase()).append("\n");
            }
        }
        return builder.toString();
    }
}
