package nchu2.webhw.Controller;

import nchu2.webhw.ComponentBase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class Indexs extends ComponentBase {
    @GetMapping("/")
    public String splash(Model model, String redirect) {
        model.addAttribute("direction", redirect);
        return "splash";
    }

    @GetMapping("login")
    public String index() {
        return "login";
    }
}
