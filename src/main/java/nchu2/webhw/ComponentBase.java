package nchu2.webhw;

import nchu2.webhw.service.DatabaseLog;
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
    protected DatabaseLog logger;

    /**
     * 获取登录名
     *
     * @param authentication 身份
     * @return 登录名
     */
    public static String getLoginName(Authentication authentication) {
        return ((User) authentication.getPrincipal()).getUsername();
    }

    /**
     * 获取登录名
     * @param context 按照JAX-RS规范获取登录名
     * @return 登录名
     */
    public static String getLoginName(SecurityContext context) {
        return context.getUserPrincipal().getName();
    }
}
