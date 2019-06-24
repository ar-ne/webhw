package nchu2.webhw;

import nchu2.webhw.utils.Auth;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.ws.rs.ApplicationPath;

@SpringBootApplication
@EnableCaching
public class WebhwApplication {

    public static void main(String[] args) {
        new ConsoleThread().start();
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

    @Bean
    public LogoutSuccessHandler logoutHandler() {
        return new Auth.LogoutHandler();
    }

    @Configuration
    @ApplicationPath("api")
    public static class JerseyConfig extends ResourceConfig {
        public JerseyConfig() throws ClassNotFoundException {
            final ClassPathScanningCandidateComponentProvider scanner =
                    new ClassPathScanningCandidateComponentProvider(false);
            scanner.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
            for (final BeanDefinition resourceBean : scanner.findCandidateComponents("nchu2.webhw.api")) {
                final Class resourceClass = getClass().getClassLoader().loadClass(resourceBean.getBeanClassName());
                register(resourceClass);
            }
        }
    }

    @Configuration
    public static class ThymeleafConfig {
        public ThymeleafConfig(SpringTemplateEngine engine) {
            ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
            resolver.setPrefix("templates/");
            resolver.setSuffix(".html");
            resolver.setOrder(engine.getTemplateResolvers().size());
            resolver.setCharacterEncoding("UTF-8");
            engine.addTemplateResolver(resolver);
        }
    }

    @Configuration
    @EnableWebSecurity
    public class WebSecurity extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/scripts/*", "/webpack/*", "/fonts/*", "/signup", "/error").permitAll()
                    .antMatchers("/priv/**", "/api/user/**", "/api/i18n/**").hasAnyRole("Customer", "Manager", "Staff")
                    .antMatchers("/admin/**", "/api/admin/**", "/api/i18n/**", "/actuator/**").hasIpAddress("127.0.0.1")
                    .antMatchers("/admin/**", "/api/admin/**", "/api/i18n/**", "/actuator/**").hasIpAddress("0:0:0:0:0:0:0:1")
                    .and().formLogin().loginPage("/login").successHandler(authSuccessHandler()).failureHandler(authFailureHandler()).permitAll()
                    .and().logout().logoutSuccessHandler(logoutHandler());
        }
    }
}
