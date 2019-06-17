package nchu2.webhw;

import nchu2.webhw.utils.Auth;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@SpringBootApplication
@EnableCaching
public class WebhwApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebhwApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationSuccessHandler authSuccessHandler() {
        return new Auth.LoginHandler();
    }

    @Bean
    public AuthenticationFailureHandler authFailureHandler() {
        return new Auth.LoginHandler();
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
                    .antMatchers("/login", "/logout", "/scripts/*", "/webpack/*", "/fonts/*", "/signup", "/error").permitAll()
                    .antMatchers("/priv/**", "/api/user/**").hasAnyRole("Customer", "Manager", "Staff")
                    .antMatchers("/admin/**", "/api/admin/**").hasIpAddress("0.0.0.0")
                    .and().formLogin().loginPage("/login").successHandler(authSuccessHandler()).failureHandler(authFailureHandler()).permitAll()
                    .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login")
                    .and();
        }
    }
}
