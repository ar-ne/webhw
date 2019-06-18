package nchu2.webhw.controller;

import nchu2.webhw.ComponentBase;
import nchu2.webhw.model.tables.pojos.Customer;
import nchu2.webhw.model.tables.pojos.Manager;
import nchu2.webhw.model.tables.pojos.Staff;
import nchu2.webhw.properites.UserType;
import nchu2.webhw.service.UserService;
import nchu2.webhw.service.user.CustomerService;
import nchu2.webhw.service.user.ManagerService;
import nchu2.webhw.service.user.StaffService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static nchu2.webhw.properites.Vars.PUBLIC_PAGES;

@Controller
@RequestMapping("/")
public class Router extends ComponentBase {
    private final UserService userService;
    private final CustomerService customerService;
    private final ManagerService managerService;
    private final StaffService staffService;

    public Router(UserService userService, CustomerService customerService, ManagerService managerService, StaffService staffService) {
        this.userService = userService;
        this.customerService = customerService;
        this.managerService = managerService;
        this.staffService = staffService;
    }

    @GetMapping("")
    public String splash(Model model, String redirect) {
        model.addAttribute("direction", redirect);
        return "framework/splash";
    }

    @Controller
    @RequestMapping("priv")
    public class UserSpace extends ComponentBase {
        @GetMapping("")
        public String router() {
            return "redirect:/priv/index";
        }

        @GetMapping("{page}")
        public String router(@PathVariable("page") String page, Model model, Authentication authentication) {
            User user = (User) authentication.getPrincipal();
            UserType userType = ((UserType.Authority) user.getAuthorities().toArray()[0]).getUserType();
            model.addAttribute("userType", userType.name());
            model.addAttribute("page", page);
            model.addAttribute("user", userService.getUser(user.getUsername()));
            if (PUBLIC_PAGES.contains(page))
                return String.format("pub/%s", page);
            return String.format("priv/%s/%s", userType.name(), page);
        }
    }

    @Controller
    @RequestMapping("admin")
    public class AdminSpace {
        @GetMapping("")
        public String index() {
            return "redirect:/admin/index";
        }

        @GetMapping("{page}")
        public String router(@PathVariable("page") String page, Model model) {
            model.addAttribute("page", page);
            return String.format("admin/%s", page);
        }

        @PostMapping("addUser")
        @ResponseBody
        public String addUser(HttpServletResponse response, String username, String password, String type) {
            response.setStatus(205);
            switch (UserType.valueOf(type)) {
                case Staff:
                    return staffService.register(username, password, new Staff().setName(username)).toString();
                case Manager:
                    return managerService.register(username, password, new Manager().setName(username)).toString();
                case Customer:
                    return customerService.register(username, password, new Customer().setName(username)).toString();
            }
            return "";
        }
    }

    @Controller
    public class AuthSpace extends ComponentBase {
        @GetMapping("login")
        public String index(Authentication authentication, Model model) {
            if (authentication != null && authentication.isAuthenticated())
                model.addAttribute("login", "login");
            return "pub/login";
        }

        @GetMapping("logout")
        public String logout() {
            return "pub/logout";
        }

        @GetMapping("signup")
        public String signup() {
            return "pub/signup";
        }
    }
}
