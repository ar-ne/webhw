package nchu2.webhw;

import com.rits.cloning.Cloner;
import nchu2.webhw.properties.mapping.UserType;
import nchu2.webhw.service.DatabaseLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.SecurityContext;

/**
 * 所有Component的父类
 */
@Component
public abstract class ComponentBase {
    @Autowired
    protected DatabaseLog dbLogger;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final Cloner cloner = new Cloner();

    /**
     * 获取登录名
     *
     * @param authentication 身份
     * @return 登录名
     */
    public static String getLoginName(Authentication authentication) {
        return ((User) authentication.getPrincipal()).getUsername();
    }

    public static UserType getUserType(Authentication authentication) {
        return ((UserType.Authority) authentication.getAuthorities().toArray()[0]).getUserType();
    }


    public static UserType getUserType(SecurityContext context) {
        return getUserType(getAuthentication(context));
    }

    private static Authentication getAuthentication(SecurityContext context) {
        return (Authentication) context.getUserPrincipal();
    }

    public <T> T deepClone(T o) {
        return cloner.deepClone(o);
    }

    /**
     * 获取登录名
     *
     * @param context 按照JAX-RS规范获取登录名
     * @return 登录名
     */
    public static String getLoginName(SecurityContext context) {
        return context.getUserPrincipal().getName();
    }
}
