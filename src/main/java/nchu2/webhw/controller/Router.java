package nchu2.webhw.controller;

import nchu2.webhw.ComponentBase;
import nchu2.webhw.properties.mapping.UserType;
import nchu2.webhw.service.CommonCRUD;
import nchu2.webhw.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static nchu2.webhw.properties.Vars.PUBLIC_PAGES;

@Controller
@RequestMapping("/")
public class Router extends ComponentBase {
    private final UserService userService;
    private final CommonCRUD commonCRUD;

    public Router(UserService userService, CommonCRUD commonCRUD) {
        this.userService = userService;
        this.commonCRUD = commonCRUD;
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
        public String router(@PathVariable("page") String page, Model model, Authentication authentication, String arg) {
            User user = (User) authentication.getPrincipal();
            UserType userType = ((UserType.Authority) user.getAuthorities().toArray()[0]).getUserType();
            model.addAttribute("userType", userType.name());
            model.addAttribute("page", page);
            model.addAttribute("user", userService.getUser(user.getUsername()));
            if (PUBLIC_PAGES.contains(page)) {
                attachData(model, page, arg);
                return String.format("pub/%s", page);
            }
            return String.format("priv/%s/%s", userType.name(), page);
        }

        public void attachData(Model model, String PAGE, String args) {
            switch (PAGE) {
                case "production":
                    model.addAttribute("data", commonCRUD.getProduction(Integer.valueOf(args)));
            }
        }
    }

    @Controller
    @RequestMapping("admin")
    public static class AdminSpace {
        @GetMapping("")
        public String index() {
            return "redirect:/admin/index";
        }

        @GetMapping("{page}")
        public String router(@PathVariable("page") String page, Model model) {
            model.addAttribute("page", page);
            return String.format("admin/%s", page);
        }
    }

    @Controller
    public static class AuthSpace extends ComponentBase {
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
