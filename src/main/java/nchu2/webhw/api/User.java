package nchu2.webhw.api;

import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@RestController
@Path("user")
public class User extends APIBase {
    @GET
    public String hi() {
        return "hi";
    }
}
