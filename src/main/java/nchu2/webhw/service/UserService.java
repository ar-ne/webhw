package nchu2.webhw.service;

import nchu2.webhw.model.User;
import nchu2.webhw.model.tables.daos.CustomerDao;
import nchu2.webhw.model.tables.daos.ManagerDao;
import nchu2.webhw.model.tables.daos.StaffDao;
import nchu2.webhw.model.tables.pojos.Customer;
import nchu2.webhw.model.tables.pojos.Login;
import nchu2.webhw.model.tables.pojos.Manager;
import nchu2.webhw.model.tables.pojos.Staff;
import nchu2.webhw.properites.UserType;
import nchu2.webhw.properites.Vars;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceBase {
    private final LoginService loginService;

    public UserService(LoginService loginService) {
        this.loginService = loginService;
    }

    private User createNewUser(String loginName, String plainTextPass, UserType userType, User user) throws LoginService.LoginNameExistsException {
        Login login = loginService.newLoginWithPassword(loginName, plainTextPass, userType);
        user.setId(login.getLoginid());
        logger.log(String.format("Welcome new %s with login id: %s", userType.name(), login.getLoginid()));
        return user;
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

    public interface GetUserDetails<T extends User> {
        T getUser(Long id);
    }

    @Service
    public class CustomerService extends ServiceBase implements Register<Customer>, GetUserDetails<Customer> {
        @Override
        public Customer register(String loginName, String plainTextPass, User user) throws LoginService.LoginNameExistsException {
            return (Customer) createNewUser(loginName, plainTextPass, UserType.Customer, user);
        }

        @Override
        @Cacheable(value = Vars.CacheValues.user)
        public Customer getUser(Long id) {
            return new CustomerDao(dsl.configuration()).fetchOneById(id);
        }
    }

    @Service
    public class ManagerService extends ServiceBase implements Register<Manager>, GetUserDetails<Manager> {
        @Override
        public Manager register(String loginName, String plainTextPass, User user) throws LoginService.LoginNameExistsException {
            return (Manager) createNewUser(loginName, plainTextPass, UserType.Manager, user);
        }

        @Override
        @Cacheable(value = Vars.CacheValues.user)
        public Manager getUser(Long id) {
            return new ManagerDao(dsl.configuration()).fetchOneById(id);
        }
    }

    @Service
    public class StaffService extends ServiceBase implements Register<Staff>, GetUserDetails<Staff> {
        @Override
        public Staff register(String loginName, String plainTextPass, User user) throws LoginService.LoginNameExistsException {
            return (Staff) createNewUser(loginName, plainTextPass, UserType.Staff, user);
        }

        @Override
        public Staff getUser(Long id) {
            return new StaffDao(dsl.configuration()).fetchOneById(id);
        }
    }
}
