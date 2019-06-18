package nchu2.webhw.service.user;

import nchu2.webhw.model.User;
import nchu2.webhw.model.tables.daos.ManagerDao;
import nchu2.webhw.model.tables.pojos.Manager;
import nchu2.webhw.properites.UserType;
import nchu2.webhw.service.LoginService;
import nchu2.webhw.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class ManagerService extends UserService implements UserService.Register<Manager> {

    @Override
    public Manager register(String loginName, String plainTextPass, User user) throws LoginService.LoginNameExistsException {
        Manager manager = (Manager) createNewUser(loginName, plainTextPass, UserType.Manager, user);
        new ManagerDao(dsl.configuration()).insert(manager);
        return manager;
    }
}