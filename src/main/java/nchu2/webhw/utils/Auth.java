package nchu2.webhw.utils;

import nchu2.webhw.ComponentBase;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Auth {
    public static class LoginHandler extends ComponentBase implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            logger.log(LogMsgHelper.Auth.loginAttempt(authentication.getName(), authentication.isAuthenticated()));
            response.getWriter().print("/priv/index");
            response.setStatus(201);
        }

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            response.getWriter().print("Wrong login");
            response.setStatus(403);
        }
    }

    public static class LogoutHandler implements LogoutSuccessHandler {

        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            response.getWriter().print("/priv/");
            response.setStatus(201);
        }
    }

    public class AuthAttemptListeners {

    }
}
