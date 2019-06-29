package nchu2.webhw;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import nchu2.webhw.utils.Auth;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
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
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.ws.rs.ApplicationPath;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 入口类和配置信息
 */
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
                    .antMatchers("/priv/**", "/api/user/**").hasAnyRole("Customer", "Manager", "Staff")
                    .antMatchers("/druid/**", "/admin/**", "/api/admin/**", "/api/i18n/**").hasIpAddress("0:0:0:0:0:0:0:1")
                    .and().csrf().requireCsrfProtectionMatcher(new RequestMatcher() {
                //放行这几种请求
                private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
                //放行rest请求，当然后面rest与web将会分开，到时这里可以删除
                private RegexRequestMatcher unprotectedMatcher = new RegexRequestMatcher("^/rest/.*", null);

                @Override
                public boolean matches(HttpServletRequest request) {
                    if (allowedMethods.matcher(request.getMethod()).matches()) {
                        return false;
                    }

                    String servletPath = request.getServletPath();
                    if (servletPath.contains("/druid")) {
                        return false;
                    }
                    return !unprotectedMatcher.matches(request);
                }

            })
                    .and().formLogin().loginPage("/login").successHandler(authSuccessHandler()).failureHandler(authFailureHandler()).permitAll()
                    .and().logout().logoutSuccessHandler(logoutHandler());
        }

    }

    @Configuration
    public class DruidConfig {

        @ConfigurationProperties(prefix = "spring.datasource")
        @Bean
        public DataSource druid() {
            return new DruidDataSource();
        }

        //配置监控
        //1、配置一个管理后台的Servlet
        @Bean
        public ServletRegistrationBean registrationBean() {
            ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");

            Map<String, String> map = new HashMap<>();
            map.put("loginUsername", "admin");
            map.put("loginPassword", "123456");
            map.put("allow", "127.0.0.1");
            //        //map.put("deny","192.168.15.21");
            bean.setInitParameters(map);
            return bean;
        }

        //2、配置一个监控的filter
        @Bean
        public FilterRegistrationBean filterRegistrationBean() {
            FilterRegistrationBean bean = new FilterRegistrationBean();
            bean.setFilter(new WebStatFilter());
            Map<String, String> map = new HashMap<>();
            map.put("exclusions", "*.js,*.css,*.css,/druid/");
            bean.setInitParameters(map);

            bean.setUrlPatterns(Arrays.asList("/*"));
            return bean;
        }

    }
}
