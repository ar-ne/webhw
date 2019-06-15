package nchu2.webhw.api;

import nchu2.webhw.model.Webhw;
import nchu2.webhw.model.tables.pojos.Customer;
import org.jooq.Configuration;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.impl.DAOImpl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("sample")
public class Sample extends APIBase {

    @GET
    @Path("customer")
    public Customer getCustomer() {
        return new Customer();
    }

    @GET
    public Object getAllClass() {
        StringBuilder builder = new StringBuilder();
        for (Table<?> table : Webhw.WEBHW.getTables()) {
            for (Field<?> field : table.fields()) {
                builder.append(table.getName()).append(".").append(field.getName()).append("\n");
            }
        }
        return builder.toString();
    }

    @GET
    @Path("all/{table}")
    @Produces(MediaType.APPLICATION_JSON)
    public List all(@PathParam("table") String table) throws Exception {
        return ((DAOImpl) Class.forName(String.format("nchu2.webhw.tables.daos.%sDao", table)).getConstructor(Configuration.class).newInstance(dsl.configuration())).findAll();
    }
}
