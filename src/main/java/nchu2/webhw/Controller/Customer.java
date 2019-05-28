package nchu2.webhw.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static nchu2.webhw.Utils.Properites.Tamplet.Customer;

@Controller
@RequestMapping(Customer)
public class Customer {
    @GetMapping("")
    public String empty() {
        return "redirect:/Customer/index";
    }

    @GetMapping("index")
    public String index() {
        return "priv/Customer/index";
    }

    @GetMapping("buy")
    public String buy() {
        return "priv/Customer/buy";
    }

}
