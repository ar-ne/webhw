package nchu2.webhw.controller;

import nchu2.webhw.ComponentBase;
import nchu2.webhw.utils.Properites;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static nchu2.webhw.utils.Properites.PUBLIC_PAGES;

@Controller
@RequestMapping("/")
public class Router extends ComponentBase {
    @GetMapping("/")
    public String splash(Model model, String redirect) {
        model.addAttribute("direction", redirect);
        return "framework/splash";
    }

    @Controller
    public class UserPages extends ComponentBase {
        @GetMapping("priv")
        public String router() {
            return "redirect:/priv/index";
        }

        @GetMapping("priv/{page}")
        public String router(@PathVariable("page") String page, Model model, Authentication authentication) {
            Properites.UserType userType = ((Properites.UserType.Authority) authentication.getAuthorities().toArray()[0]).getUserType();
            model.addAttribute("userType", userType.name());
            model.addAttribute("page", page);
            model.addAttribute("userName");
            if (PUBLIC_PAGES.contains(page))
                return String.format("pub/%s", page);
            return String.format("priv/%s/%s", userType.name(), page);
        }
    }

    @Controller
    public class AuthPages extends ComponentBase {
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
