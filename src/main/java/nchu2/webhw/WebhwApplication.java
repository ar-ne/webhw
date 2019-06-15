package nchu2.webhw;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
            packages("nchu2.webhw.api");
        }
    }

    @Configuration
    @EnableWebSecurity
    public class WebSecurity extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/scripts/*", "/webpack/*", "/fonts/*", "/signup", "/error").permitAll()
                    .antMatchers("/api/**", "/priv/**").authenticated()
                    .and().formLogin().loginPage("/login").permitAll()
                    .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login")
                    .and();
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
