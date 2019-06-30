package nchu2.webhw;

import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import nchu2.webhw.utils.Auth;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.ws.rs.ApplicationPath;
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
        return new BCryptPasswordEncoder();
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
    @EnableWebSocketMessageBroker
    public static class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

        @Override
        public void configureMessageBroker(MessageBrokerRegistry config) {
            config.enableSimpleBroker("/topic");
            config.setApplicationDestinationPrefixes("/app");
        }

        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            registry.addEndpoint("/ws").withSockJS();
        }
    }

    @Controller
    @Configuration
    @EnableWebSecurity
    public class WebSecurity extends WebSecurityConfigurerAdapter {
        private final Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/scripts/*", "/webpack/*", "/fonts/*", "/signup", "/error").permitAll()
                    .antMatchers("/priv/**", "/api/user/**").access("hasAnyRole('Customer', 'Manager', 'Staff')")
                    .antMatchers("/druid/**", "/admin/**", "/api/admin/**").access("hasIpAddress('0:0:0:0:0:0:0:1') or hasIpAddress('127.0.0.1')")
                    .antMatchers("/api/i18n/**", "/ws/**").access("hasAnyRole('Customer', 'Manager', 'Staff') or hasIpAddress('0:0:0:0:0:0:0:1') or hasIpAddress('127.0.0.1')")
                    .and().csrf().requireCsrfProtectionMatcher(request -> !allowedMethods.matcher(request.getMethod()).matches() && !(request.getServletPath().contains("/druid") || request.getServletPath().contains("/ws/")))
                    .and().formLogin().loginPage("/login").successHandler(authSuccessHandler()).failureHandler(authFailureHandler()).permitAll()
                    .and().logout().logoutSuccessHandler(logoutHandler());
        }

    }

    @Configuration
    @ConfigurationProperties(prefix = "spring.datasource")
    public static class DruidConfig {

        //2、配置一个监控的filter
        @Bean
        public WebStatFilter webStatFilter() {
            WebStatFilter webStatFilter = new WebStatFilter();
            webStatFilter.setProfileEnable(true);
            webStatFilter.setSessionStatEnable(true);
            return webStatFilter;
        }

        @Bean
        public Slf4jLogFilter logFilter() {
            Slf4jLogFilter logFilter = new Slf4jLogFilter();
            logFilter.setStatementExecutableSqlLogEnable(true);
            logFilter.setStatementLogEnabled(false);
            return logFilter;
        }

        @Bean
        public StatFilter statFilter() {
            StatFilter statFilter = new StatFilter();
            statFilter.setSlowSqlMillis(3000);
            statFilter.setLogSlowSql(true);
            statFilter.setMergeSql(true);
            return statFilter;
        }

        /**
         * sql防火墙过滤器配置
         */
        @Bean
        public WallFilter wallFilter(WallConfig wallConfig) {

            WallFilter wallFilter = new WallFilter();
            wallFilter.setConfig(wallConfig);
            wallFilter.setLogViolation(true);//对被认为是攻击的SQL进行LOG.error输出
            wallFilter.setThrowException(false);//对被认为是攻击的SQL抛出SQLException
            return wallFilter;
        }

        /**
         * sql防火墙配置
         */
        @Bean
        public WallConfig wallConfig() {

            WallConfig wallConfig = new WallConfig();
            wallConfig.setAlterTableAllow(false);
            wallConfig.setCreateTableAllow(false);
            wallConfig.setDeleteAllow(false);
            wallConfig.setMergeAllow(false);
            wallConfig.setDescribeAllow(false);
            wallConfig.setShowAllow(false);
            return wallConfig;
        }
    }
}
