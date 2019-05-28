package nchu2.webhw.Controller;
import static nchu2.webhw.Utils.Properites.Tamplet.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(Staff)
public class Staff {
    @GetMapping("/")
    public String index() {
        return "priv/Staff/index";
    }
}
