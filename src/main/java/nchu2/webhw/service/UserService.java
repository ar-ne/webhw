package nchu2.webhw.service;

import nchu2.webhw.model.User;
import nchu2.webhw.model.tables.daos.CustomerDao;
import nchu2.webhw.model.tables.daos.ManagerDao;
import nchu2.webhw.model.tables.daos.StaffDao;
import nchu2.webhw.model.tables.pojos.Customer;
import nchu2.webhw.model.tables.pojos.Login;
import nchu2.webhw.model.tables.pojos.Manager;
import nchu2.webhw.model.tables.pojos.Staff;
import nchu2.webhw.properties.Vars;
import nchu2.webhw.properties.mapping.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceBase {
    @Autowired
    protected LoginService loginService;

    @Cacheable(value = Vars.CacheValues.user, key = "getArgs()[0]")
    public User getUser(String loginname) {
        switch (loginService.getLoginType(loginname)) {
            case Customer:
                return new CustomerDao(dsl.configuration()).fetchOneByLoginname(loginname);
            case Manager:
                return new ManagerDao(dsl.configuration()).fetchOneByLoginname(loginname);
            case Staff:
                return new StaffDao(dsl.configuration()).fetchOneByLoginname(loginname);
        }
        throw new RuntimeException("Error when trying to get user's detail");
    }

    public User getUser(Authentication authentication) {
        String login = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        return getUser(login);
    }

    protected User createNewUser(String loginName, String plainTextPass, UserType userType, User user) throws LoginService.LoginNameExistsException {
        Login login = loginService.newLoginWithPassword(loginName, plainTextPass, userType);
        user.setLoginname(login.getLoginname());
        logger.log(String.format("Welcome new %s with login name: %s", userType.name(), login.getLoginname()));
        return user;
    }

    @CachePut(value = Vars.CacheValues.user, key = "getArgs()[0]")
    public User putUser(String loginName, User dbObj) {
        switch (loginService.getLoginType(loginName)) {
            case Staff:
                new StaffDao(dsl.configuration()).update((Staff) dbObj);
                break;
            case Manager:
                new ManagerDao(dsl.configuration()).update(((Manager) dbObj));
                break;
            case Customer:
                new CustomerDao(dsl.configuration()).update(((Customer) dbObj));
        }
        logger.log("Profile update: " + loginName);
        return dbObj;
    }

    public interface Register<T extends User> {
        /**
         * 注册一个新的用户
         *
         * @param loginName     登录名
         * @param user          用户信息
         * @param plainTextPass 密码（明文）
         * @return 新注册用户的信息
         * @throws LoginService.LoginNameExistsException 登录名已被占用
         */
        T register(String loginName, String plainTextPass, User user) throws LoginService.LoginNameExistsException;
    }


}
