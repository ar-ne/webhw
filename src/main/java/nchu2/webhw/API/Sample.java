package nchu2.webhw.API;

import nchu2.webhw.Webhw;
import nchu2.webhw.tables.pojos.Customer;
import org.jooq.Field;
import org.jooq.Table;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.LinkedList;
import java.util.List;

@Path("sample")
public class Sample extends APIBase {
    @GET
    @Path("customer")
    public Customer getCustomer() {
        return new Customer();
    }

    @GET
    @Path("customers")
    public List getCustomers() {
        List<Customer> customers = new LinkedList<>();
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        customers.add(new Customer());
        return customers;
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
}
