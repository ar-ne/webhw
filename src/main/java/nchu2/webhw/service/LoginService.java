package nchu2.webhw.service;

import nchu2.webhw.model.tables.daos.LoginDao;
import nchu2.webhw.model.tables.pojos.Login;
import nchu2.webhw.properties.Vars;
import nchu2.webhw.properties.mapping.UserType;
import nchu2.webhw.utils.LogMsgHelper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService extends ServiceBase {
    private final PasswordEncoder passwordEncoder;

    public LoginService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Cacheable(value = Vars.CacheValues.login)
    public UserType getLoginType(String loginName) {
        return new LoginDao(dsl.configuration()).fetchOneByLoginname(loginName).getType();
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
    public Login newLoginWithPassword(String loginName, String password, UserType userType) throws
            LoginNameExistsException {
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


    public class LoginNameExistsException extends RuntimeException {
        public LoginNameExistsException(String message) {
            super(message);
        }
    }
}
