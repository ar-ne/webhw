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

/**
 * 后台管理的接口
 */
@Path("admin")
@RestController
public class Admin extends APIBase {
    /**
     * 获取日志
     *
     * @return 所有日志信息
     */
    @GET
    @Path("log")
    @Produces(MediaType.APPLICATION_JSON)
    public List log() {
        return new LogDao(dsl.configuration()).findAll();
    }

    /**
     * 获取所有的用户
     * @param userType 用户类型，若不是三种基本用户类型就获取登录信息
     * @return
     */
    @GET
    @Path("users/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    public List users(@PathParam("type") String userType) {
        if (userType.equalsIgnoreCase("login")) //检查用户类型是否为login
            return new LoginDao(dsl.configuration()).findAll();  //返回所有用户的登录信息
        switch (UserType.valueOf(userType)) {
            case Customer:
                return new CustomerDao(dsl.configuration()).findAll(); //若为顾客
            case Manager:
                return new ManagerDao(dsl.configuration()).findAll();
            case Staff:
                return new StaffDao(dsl.configuration()).findAll();
        }
        return new LoginDao(dsl.configuration()).findAll();
    }

    /**
     * 列出所有表格中的列名
     * @return 表格列名
     */
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
