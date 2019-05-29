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

    @GetMapping("bought")
    public String bought() {
        return "priv/Customer/bought";
    }

    @GetMapping("loan")
    public String loan() {
        return "priv/Customer/loan";
    }

    @GetMapping("advice")
    public String advice(){
        return "priv/Customer/advice";
    }

    @GetMapping("ticket")
    public String ticket(){
        return "priv/Customer/ticket";
    }

    @GetMapping("result")
    public String result(){
        return "priv/Customer/result";
    }

    @GetMapping("map")
    public String map(){
        return "priv/Customer/map";
    }
}
