package nchu2.webhw.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static nchu2.webhw.Utils.Properites.Tamplet.Manager;

@Controller
@RequestMapping(Manager)
public class Manager {
    @GetMapping("/")
    public String index() {
        return "priv/Manager/index";
    }


}