package nchu2.webhw.service;

import nchu2.webhw.utils.LogMsgHelper;
import nchu2.webhw.utils.Properites;
import nchu2.webhw.model.tables.daos.LoginDao;
import nchu2.webhw.model.tables.pojos.Login;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService extends ServiceBase implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    public AuthService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        Login login = new LoginDao(dsl.configuration()).fetchOneByLoginname(loginName);
        if (login == null) throw new UsernameNotFoundException("login name:" + loginName);
        return new User(login.getLoginname(), login.getPass(), Collections.singletonList(login.getType().getAuthority()));
    }

    /**
     * 创建新的客户登录信息
     *
     * @param password  密码（明文）
     * @param loginName 登录名
     * @param userType  登录类型
     * @return 新添加的登录信息
     * @throws LoginNameExistsException 登录名已被占用
     */
    public Login newLoginWithPassword(String loginName, String password, Properites.UserType userType) throws LoginNameExistsException {
        Login login = new Login();
        login.setPass(password);
        login.setLoginname(loginName);
        login.setType(userType);
        return newLogin(login);
    }

    /**
     * @param login 待添加的登录信息
     * @return 被添加的登录信息
     * @throws LoginNameExistsException 登录名已被占用
     */
    public Login newLogin(Login login) throws LoginNameExistsException {
        LoginDao dao = new LoginDao(dsl.configuration());
        if (dao.existsById(login.getLoginname()))
            throw new LoginNameExistsException("login name exists:" + login.getLoginname());
        login.setPass(passwordEncoder.encode(login.getPass()));
        dao.insert(login);
        logger.log(LogMsgHelper.Auth.newLogin(login));
        return login;
    }


    public class LoginNameExistsException extends Exception {
        public LoginNameExistsException(String message) {
            super(message);
        }
    }
}