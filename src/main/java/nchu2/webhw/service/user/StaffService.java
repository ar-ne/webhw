package nchu2.webhw.service.user;

import nchu2.webhw.model.User;
import nchu2.webhw.model.tables.daos.StaffDao;
import nchu2.webhw.model.tables.pojos.Staff;
import nchu2.webhw.properites.UserType;
import nchu2.webhw.service.LoginService;
import nchu2.webhw.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class StaffService extends UserService implements UserService.Register<Staff> {

    @Override
    public Staff register(String loginName, String plainTextPass, User user) throws LoginService.LoginNameExistsException {
        Staff staff = (Staff) createNewUser(loginName, plainTextPass, UserType.Staff, user);
        new StaffDao(dsl.configuration()).insert(staff);
        return staff;
    }
}