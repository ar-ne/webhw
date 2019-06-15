package nchu2.webhw.service;

import nchu2.webhw.utils.Properites;
import nchu2.webhw.model.User;
import nchu2.webhw.model.tables.pojos.Login;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceBase {
    private final AuthService authService;

    public UserService(AuthService authService) {
        this.authService = authService;
    }

    @Service
    public class CustomerService extends ServiceBase implements User.Register {
        @Override
        public User register(String loginName, String plainTextPass, User user) throws AuthService.LoginNameExistsException {
            return createNewUser(loginName, plainTextPass, user, Properites.UserType.Customer);
        }
    }

    @Service
    public class ManagerService extends ServiceBase implements User.Register {
        @Override
        public User register(String loginName, String plainTextPass, User user) throws AuthService.LoginNameExistsException {
            return createNewUser(loginName, plainTextPass, user, Properites.UserType.Manager);
        }
    }

    @Service
    public class StaffService extends ServiceBase implements User.Register {
        @Override
        public User register(String loginName, String plainTextPass, User user) throws AuthService.LoginNameExistsException {
            return createNewUser(loginName, plainTextPass, user, Properites.UserType.Staff);
        }
    }

    private User createNewUser(String loginName, String plainTextPass, User user, Properites.UserType userType) throws AuthService.LoginNameExistsException {
        Login login = authService.newLoginWithPassword(loginName, plainTextPass, userType);
        user.setId(login.getLoginid());
        logger.log(String.format("Welcome new %s with login id: %s", userType.name(), login.getLoginid()));
        return user;
    }
}
