package nchu2.webhw.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static nchu2.webhw.Utils.Properites.Tamplet.Customer;

@Controller
@RequestMapping(Customer)
public class Customer {
    @GetMapping("/")
    public String index() {
        return "priv/Customer/index";
    }
}
