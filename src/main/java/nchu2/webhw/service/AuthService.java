package nchu2.webhw.service;

import nchu2.webhw.model.tables.daos.LoginDao;
import nchu2.webhw.model.tables.pojos.Login;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService extends ServiceBase implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        Login login = new LoginDao(dsl.configuration()).fetchOneByLoginname(loginName);
        if (login == null) throw new UsernameNotFoundException("login name:" + loginName);
        return new User(login.getLoginname(), login.getPass(), Collections.singletonList(login.getType().getAuthority()));
    }
}