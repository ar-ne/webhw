package nchu2.webhw;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@SpringBootApplication
public class WebhwApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebhwApplication.class, args);
    }

    @Component
    @ApplicationPath("api")
    public class JerseyConfig extends ResourceConfig {
        public JerseyConfig() {
            packages("nchu2.webhw.API");
        }
    }

}
