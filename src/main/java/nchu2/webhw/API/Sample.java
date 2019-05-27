package nchu2.webhw.API;

import nchu2.webhw.tables.pojos.Customer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("sample")
public class Sample extends APIBase{
    @GET
    @Path("customer")
    @Produces(MediaType.APPLICATION_JSON)
    public Customer getCustomer(){
        return new Customer();
    }
}
