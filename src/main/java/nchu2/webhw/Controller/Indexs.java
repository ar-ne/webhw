package nchu2.webhw.Controller;

import nchu2.webhw.ComponentBase;
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
        return "pub/splash";
    }

    @GetMapping("login")
    public String index() {
        return "pub/login";
    }


    @GetMapping("priv/{userType}")
    public String router(@PathVariable("userType") String userType) {
        return String.format("redirect:/priv/%s/index", userType);
    }


    @GetMapping("priv/{userType}/{page}")
    public String router(@PathVariable("userType") String userType, @PathVariable("page") String page, Model model) {
        model.addAttribute("userType", userType);
        model.addAttribute("page", page);
        return String.format("priv/%s/%s", userType, page);
    }

}
