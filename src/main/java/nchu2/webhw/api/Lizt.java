package nchu2.webhw.api;

import nchu2.webhw.model.tables.daos.ProductionDao;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RestController
@Path("lizt")
public class Lizt extends APIBase {
    @GET
    @Path("Production")
    @Produces(MediaType.APPLICATION_JSON)
    public List production() {
        return new ProductionDao(dsl.configuration()).findAll();
    }

}
