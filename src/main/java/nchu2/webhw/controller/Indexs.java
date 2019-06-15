package nchu2.webhw.controller;

import nchu2.webhw.ComponentBase;
import nchu2.webhw.utils.Properites;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class Indexs extends ComponentBase {
    @GetMapping("/")
    public String splash(Model model, String redirect) {
        model.addAttribute("direction", redirect);
        return "framework/splash";
    }

    @GetMapping("login")
    public String index() {
        return "pub/login";
    }

    @GetMapping("logout")
    public String logout() {
        return "pub/logout";
    }


    @GetMapping("priv")
    public String router() {
        return "redirect:/priv/index";
    }


    @GetMapping("priv/{page}")
    public String router(@PathVariable("page") String page, Model model, Authentication authentication) {
        Properites.UserType userType = ((Properites.UserType.Authority) authentication.getAuthorities().toArray()[0]).getUserType();
        model.addAttribute("userType", userType.name());
        model.addAttribute("page", page);
        System.out.println(authentication);
        return String.format("priv/%s/%s", userType.name(), page);
    }

    @GetMapping("profile")
    public String profile() {
        return "pub/profile";
    }

    @GetMapping("notification")
    public String introduction() {
        return "pub/notification";
    }
}
