package nchu2.webhw.API;

import nchu2.webhw.Webhw;
import nchu2.webhw.WebhwApplication;
import nchu2.webhw.tables.pojos.Customer;
import org.jooq.Field;
import org.jooq.Table;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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

    @POST
    @Path("{classname}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getSample(@PathParam("classname") String classname) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return Class.forName("nchu2.webhw.tables.pojos." + classname).newInstance();
    }

}
