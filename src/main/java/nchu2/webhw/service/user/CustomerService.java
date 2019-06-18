package nchu2.webhw.service.user;

import nchu2.webhw.model.User;
import nchu2.webhw.model.tables.daos.CustomerDao;
import nchu2.webhw.model.tables.pojos.Customer;
import nchu2.webhw.properites.UserType;
import nchu2.webhw.service.LoginService;
import nchu2.webhw.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class CustomerService extends UserService implements UserService.Register<Customer> {

    @Override
    public Customer register(String loginName, String plainTextPass, User user) throws LoginService.LoginNameExistsException {
        Customer customer = (Customer) createNewUser(loginName, plainTextPass, UserType.Customer, user);
        new CustomerDao(dsl.configuration()).insert(customer);
        return customer;
    }
}



