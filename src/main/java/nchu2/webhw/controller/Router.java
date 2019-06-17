package nchu2.webhw.controller;

import nchu2.webhw.ComponentBase;
import nchu2.webhw.properites.UserType;
import nchu2.webhw.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static nchu2.webhw.properites.Vars.PUBLIC_PAGES;

@Controller
@RequestMapping("/")
public class Router extends ComponentBase {
    private final UserService userService;

    public Router(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String splash(Model model, String redirect) {
        model.addAttribute("direction", redirect);
        return "framework/splash";
    }

    @Controller
    @RequestMapping("priv")
    public class UserSpace extends ComponentBase {
        @GetMapping("/")
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
        @GetMapping("/")
        public String index() {
            return "redirect:/admin/index";
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
    }
}
