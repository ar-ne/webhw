package nchu2.webhw.controller;

import nchu2.webhw.ComponentBase;
import nchu2.webhw.properties.mapping.UserType;
import nchu2.webhw.service.CommonCRUD;
import nchu2.webhw.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static nchu2.webhw.properties.Vars.PUBLIC_PAGES;

/**
 * 本项目的神奇之处
 * 页面路由，将对应的页面，对应到相应的view
 */
@Controller
@RequestMapping("/")
public class Router extends ComponentBase {

    private final UserService userService;
    private final CommonCRUD commonCRUD;

    public Router(UserService userService, CommonCRUD commonCRUD) {
        this.userService = userService;
        this.commonCRUD = commonCRUD;
    }

    /**
     * 跳转到首屏，负责重定向
     *
     * @param model
     * @param redirect 重定向位置
     * @return 跳转页面
     */
    @GetMapping("")
    public String splash(Model model, String redirect) {
        model.addAttribute("direction", redirect);
        return "framework/splash";
    }

    /**
     * 用户的页面路由
     */
    @Controller
    @RequestMapping("priv")
    public class UserSpace extends ComponentBase {
        @GetMapping("")
        public String router() {
            return "redirect:/priv/index";
        }

        /**
         * 功能页面
         * @param page 对应页面名
         * @param model
         * @param authentication 身份
         * @param arg 页面参数
         * @return
         */
        @GetMapping("{page}")
        public String router(@PathVariable("page") String page, Model model, Authentication authentication, String arg) {
            User user = (User) authentication.getPrincipal(); //获取访问的用户
            UserType userType = ((UserType.Authority) user.getAuthorities().toArray()[0]).getUserType();//获取访问的用户类型

            model.addAttribute("userType", userType.name());
            model.addAttribute("page", page);
            model.addAttribute("user", userService.getUser(user.getUsername()));

            if (PUBLIC_PAGES.contains(page)) {
                attachData(model, page, arg);
                return String.format("pub/%s", page);  //转成字符串
            }
            return String.format("priv/%s/%s", userType.name(), page);
        }

        /**
         * 按需求往页面里添加属性
         * @param model
         * @param PAGE 页面
         * @param args 参数
         */
        public void attachData(Model model, String PAGE, String args) {
            switch (PAGE) {
                case "production": //附加产品的详细信息
                    model.addAttribute("data", commonCRUD.getProduction(Integer.valueOf(args)));
            }
        }
    }

    @Controller
    @RequestMapping("admin")
    public static class AdminSpace {
        @GetMapping("")
        public String index() {
            return "redirect:/admin/index";
        }

        @GetMapping("{page}")
        public String router(@PathVariable("page") String page, Model model) {
            model.addAttribute("page", page);
            return String.format("admin/%s", page);
        }
    }

    @Controller
    public static class AuthSpace extends ComponentBase {
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

        @GetMapping("signup")
        public String signup() {
            return "pub/signup";
        }
    }
}
