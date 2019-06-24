package nchu2.webhw;

import nchu2.webhw.service.DatabaseLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.SecurityContext;

@Component
public abstract class ComponentBase {
    @Autowired
    protected DatabaseLog logger;

    public static String getLoginName(Authentication authentication) {
        return ((User) authentication.getPrincipal()).getUsername();
    }

    public static String getLoginName(SecurityContext context) {
        return context.getUserPrincipal().getName();
    }
}
