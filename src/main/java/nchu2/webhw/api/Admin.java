package nchu2.webhw.api;

import nchu2.webhw.model.Webhw;
import nchu2.webhw.model.tables.daos.*;
import nchu2.webhw.model.tables.pojos.Receive;
import nchu2.webhw.model.tables.records.ReceiveRecord;
import nchu2.webhw.properties.mapping.UserType;
import nchu2.webhw.utils.Tools;
import org.jooq.Field;
import org.jooq.Table;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
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
    public List<? extends nchu2.webhw.model.tables.pojos.Log> log() {
        return new LogDao(dsl.configuration()).findAll();
    }

    /**
     * 获取所有的用户
     *
     * @param userType 用户类型，若不是三种基本用户类型就获取登录信息
     * @return
     */
    @GET
    @Path("users/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<? extends java.io.Serializable> users(@PathParam("type") String userType) {
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
     *
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

    @GET
    @Path("test10000")
    public String test10000() {
        ReceiveDao dao = new ReceiveDao(dsl.configuration());
        StringBuilder stringBuilder = new StringBuilder();
        long begin = System.nanoTime();
        stringBuilder.append("Building datalist, begin at: ").append(begin).append("\n");
        for (int i = 0; i < 10000; i++)
            dao.insert(new Receive().setReceiver(String.valueOf(i)));
        long end = System.nanoTime();
        stringBuilder.append("done!, end at: ").append(end).append("\n");
        stringBuilder.append("Spend time: ").append(new Date(end - begin)).append("\n");
        return stringBuilder.toString();
    }

    @GET
    @Path("pt1")
    public String ptest10000() {
        long x = System.nanoTime();
        ReceiveRecord receiveRecord = new ReceiveRecord();
        List<ReceiveRecord> list = new ArrayList<ReceiveRecord>();
        for (int i = 0; i < 10000; i++) {
            list.add(deepClone(receiveRecord));
        }
        return String.format("1000000 , time = %s", Tools.nano2second(System.nanoTime() - x));
    }

    @GET
    @Path("xtest10000")
    public String xtest10000() {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Receive> list = new ArrayList<>(10000);
        dsl.configuration().settings().setReturnRecordToPojo(false);
        stringBuilder.append("Building datalist, begin at: ").append(System.nanoTime()).append("\n");
        for (int i = 0; i < 10000; i++)
            list.add(new Receive().setReceiver(String.valueOf(i)));
        stringBuilder.append("Generate done: ").append(System.nanoTime());
        new ReceiveDao(dsl.configuration()).insert(list);
        stringBuilder.append("done!, end at: ").append(System.nanoTime()).append("\n");
        return stringBuilder.toString();
    }
}
