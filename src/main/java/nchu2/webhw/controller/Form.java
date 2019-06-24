package nchu2.webhw.controller;

import nchu2.webhw.ComponentBase;
import nchu2.webhw.model.User;
import nchu2.webhw.model.tables.pojos.*;
import nchu2.webhw.properties.Status;
import nchu2.webhw.properties.mapping.Sex;
import nchu2.webhw.properties.mapping.UserType;
import nchu2.webhw.service.CommonCRUD;
import nchu2.webhw.service.FakeAccountBalanceService;
import nchu2.webhw.service.LoginService;
import nchu2.webhw.service.UserService;
import nchu2.webhw.service.user.CustomerService;
import nchu2.webhw.service.user.ManagerService;
import nchu2.webhw.service.user.StaffService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Map;

import static nchu2.webhw.properties.Vars.TYPE_POJO_MAP;
import static nchu2.webhw.properties.mapping.Sex.Female;
import static nchu2.webhw.properties.mapping.Sex.Male;
import static nchu2.webhw.utils.LogMsgHelper.getTimestamp;
import static nchu2.webhw.utils.Tools.pick;
import static nchu2.webhw.utils.Tools.rStr;

@Controller
@RequestMapping("/")
public class Form extends ComponentBase {
    private final CustomerService customerService;
    private final ManagerService managerService;
    private final StaffService staffService;
    private final UserService userService;
    private final CommonCRUD commonCRUD;
    private final LoginService loginService;
    private final FakeAccountBalanceService accountBalanceService;

    public Form(CustomerService customerService, ManagerService managerService, StaffService staffService, UserService userService, CommonCRUD commonCRUD, LoginService loginService, FakeAccountBalanceService accountBalanceService) {
        this.customerService = customerService;
        this.managerService = managerService;
        this.staffService = staffService;
        this.userService = userService;
        this.commonCRUD = commonCRUD;
        this.loginService = loginService;
        this.accountBalanceService = accountBalanceService;
    }

    @Controller
    @RequestMapping("priv")
    public class UserSpace extends ComponentBase {
        @PostMapping("loan")
        @ResponseBody
        public String loan(Loan loan, HttpServletResponse resp, Authentication auth) {
            loan.setLoginname(getLoginName(auth));
            loan.setTime(getTimestamp());
            loan = (Loan) commonCRUD.newRecord(loan, getLoginName(auth));
            resp.setStatus(Status.showSuccessMsg);
            return "申请提交成功，流水号: " + loan.getLId();
        }

        @PostMapping("advice")
        @ResponseBody
        public String advice(Opinion opinion, HttpServletResponse resp, Authentication auth) {
            opinion.setLoginname(getLoginName(auth));
            opinion.setTime(getTimestamp());
            opinion = (Opinion) commonCRUD.newRecord(opinion, getLoginName(auth));
            resp.setStatus(Status.showSuccessMsg);
            return "提交成功，流水号: " + opinion.getOId();
        }

        @PostMapping("ticket")
        @ResponseBody
        public String ticket(Ticket ticket, HttpServletResponse resp, Authentication auth) {
            ticket.setLoginname(getLoginName(auth));
            ticket.setTime(getTimestamp());
            ticket = (Ticket) commonCRUD.newRecord(ticket, getLoginName(auth));
            resp.setStatus(Status.showSuccessMsg);
            return "提交成功，流水号: " + ticket.getTId();
        }

        @PostMapping("order")
        @ResponseBody
        public String order(Production production, double money, int dur, HttpServletResponse response, Authentication authentication) {
            if (money < production.getPmin()) {
                response.setStatus(Status.showFailMsg);
                return "金额不满最低投入";
            }
            Orders order = new Orders();
            order.setPId(production.getPId());
            order.setLoginname(getLoginName(authentication));
            order.setBuyduration(dur);
            order.setBuytime(getTimestamp());
            order.setBuymoney(money);
            double bal = accountBalanceService.pay(money);
            if (bal < 0) {
                response.setStatus(Status.showFailMsg);
                return "购买失败，账户余额不足: " + accountBalanceService.getBal();
            }
            order = (Orders) commonCRUD.newRecord(order, getLoginName(authentication));
            response.setStatus(Status.showSuccessMsg);
            return String.format("购买成功！订单编号: %s", order.getOrId());
        }

        @PostMapping("addProduction")
        @ResponseBody
        public String addProduction(Production production, HttpServletResponse response, Authentication authentication) {
            response.setStatus(Status.showSuccessMsg);
            if (production.getRisk() == null) {
                response.setStatus(Status.showFailMsg);
                return "Risk must set!";
            }
            production.setLoginname(getLoginName(authentication));
            production = (Production) commonCRUD.newRecord(production, getLoginName(authentication));
            return "添加成功, 产品编号: " + production.getPId();
        }

        @PostMapping("addIntroduction")
        @ResponseBody
        public String addIntroduction(Introduction introduction, HttpServletResponse response, Authentication authentication) {
            response.setStatus(Status.showSuccessMsg);
            introduction.setLoginname(getLoginName(authentication));
            introduction.setPubtime(getTimestamp());
            introduction = (Introduction) commonCRUD.newRecord(introduction, getLoginName(authentication));
            return "添加成功, 介绍编号: " + introduction.getIId();
        }

        @PostMapping("profile")
        @ResponseBody
        public String profile(HttpServletResponse response, Authentication authentication, HttpServletRequest request) {
            try {
                response.setStatus(Status.showSuccessMsg);
                Map<String, String[]> pMap = request.getParameterMap();
                Class clazz = TYPE_POJO_MAP.get(loginService.getLoginType(getLoginName(authentication)));
                User userObj = (User) clazz.newInstance();
                User dbObj = userService.getUser(authentication);
                for (Field field : clazz.getDeclaredFields()) {
                    if (!pMap.containsKey(field.getName())) continue;
                    boolean acc = field.isAccessible();
                    field.setAccessible(true);
                    if (!field.getType().equals(String.class))
                        field.set(userObj, field.getType().getDeclaredMethod("valueOf", new Class[]{String.class}).invoke(null, pMap.get(field.getName())[0]));
                    else field.set(userObj, pMap.get(field.getName())[0]);
                    if (field.get(userObj).equals(field.get(dbObj))) continue;
                    field.set(dbObj, field.get(userObj));
                    field.setAccessible(acc);
                }
                userService.putUser(getLoginName(authentication), dbObj);
                return "更新成功，手动刷新查看变化";
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
            response.setStatus(Status.showFail);
            return "";
        }
    }

    @Controller
    @RequestMapping("admin")
    public class AdminSpace extends ComponentBase {
        @PostMapping("addUser")
        @ResponseBody
        public String addUser(HttpServletResponse response, String username, String password, String type) {
            response.setStatus(Status.showSuccessMsg);
            switch (UserType.valueOf(type)) {
                case Staff:
                    return staffService.register(username, password,
                            new Staff(username, rStr(), rStr(),
                                    (int) (Math.random() * 35 + 20),
                                    ((Sex) pick(Male, Female)),
                                    rStr(18), rStr(25),
                                    (int) (Math.random() * 10),
                                    new Timestamp((long) (System.currentTimeMillis() * (Math.random() + 0.85))))).toString();
                case Manager:
                    return managerService.register(username, password,
                            new Manager(username, rStr(), rStr(),
                                    (int) (Math.random() * 35 + 20),
                                    ((Sex) pick(Male, Female)),
                                    rStr(18), rStr(25),
                                    (int) (Math.random() * 10),
                                    new Timestamp((long) (System.currentTimeMillis() * (Math.random() + 0.85))))).toString();
                case Customer:
                    return customerService.register(username, password, new Customer(username, rStr(), rStr(),
                            (int) (Math.random() * 35 + 20),
                            ((Sex) pick(Male, Female)),
                            rStr(18), rStr(25),
                            new Timestamp((long) (System.currentTimeMillis() * (Math.random() + 0.85))))).toString();
            }
            response.setStatus(Status.showFailMsg);
            return "ERROR when adding";
        }
    }
}
