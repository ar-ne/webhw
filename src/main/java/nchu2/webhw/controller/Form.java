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

/**
 * form表单提交
 */
@Controller
@RequestMapping("/")
public class Form extends ComponentBase {
    private final CustomerService customerService;
    private final ManagerService managerService;
    private final StaffService staffService;
    private final UserService userService;
    private final CommonCRUD commonCRUD;
    private final LoginService loginService;
    private final FakeAccountBalanceService accountBalanceService;  //账户余额

    public Form(CustomerService customerService, ManagerService managerService, StaffService staffService, UserService userService, CommonCRUD commonCRUD, LoginService loginService, FakeAccountBalanceService accountBalanceService) {
        this.customerService = customerService;
        this.managerService = managerService;
        this.staffService = staffService;
        this.userService = userService;
        this.commonCRUD = commonCRUD;
        this.loginService = loginService;
        this.accountBalanceService = accountBalanceService;
    }


    @PostMapping("signup")
    @ResponseBody
    public String signup(Customer customer, String password, HttpServletResponse response) {
        response.setStatus(Status.showSuccessMsg);
        customer.setJointime(getTimestamp()).setName(customer.getLoginname());
        customerService.register(customer.getLoginname(), password, customer);
        return "注册成功";
    }

    /**
     * 用户表单
     */
    @Controller
    @RequestMapping("priv")
    public class UserSpace extends ComponentBase {
        /**
         * 贷款申请
         *
         * @param loan 贷款信息
         * @param resp 响应
         * @param auth 身份
         * @return 操作结果
         */
        @PostMapping("loan")
        @ResponseBody
        public String loan(Loan loan, HttpServletResponse resp, Authentication auth) {
            loan.setLoginname(getLoginName(auth));
            loan.setTime(getTimestamp());
            loan = (Loan) commonCRUD.newRecord(loan, getLoginName(auth));
            resp.setStatus(Status.showSuccessMsg);
            return "申请提交成功，流水号: " + loan.getLId();
        }

        /**
         * 意见上交
         *
         * @param opinion 意见信息
         * @param resp    响应
         * @param auth    身份
         * @return
         */
        @PostMapping("advice")
        @ResponseBody
        public String advice(Opinion opinion, HttpServletResponse resp, Authentication auth) {
            opinion.setLoginname(getLoginName(auth));
            opinion.setTime(getTimestamp());
            opinion = (Opinion) commonCRUD.newRecord(opinion, getLoginName(auth));
            resp.setStatus(Status.showSuccessMsg);
            return "提交成功，流水号: " + opinion.getOId();
        }

        @PostMapping("answerTicket")
        @ResponseBody
        public String answerTicket(Ticket ticket, HttpServletResponse resp, Authentication auth) {
            ticket.setAnstime(getTimestamp())
                    .setStaLoginname(getLoginName(auth));
            ticket = (Ticket) commonCRUD.updateRecord(ticket, getLoginName(auth));
            resp.setStatus(Status.showSuccessMsg);
            return ticket.getTId() + " 提交成功";
        }

        /**
         * 投诉
         *
         * @param ticket 投诉内容
         * @param resp   响应
         * @param auth   身份
         * @return 操作结果
         */
        @PostMapping("ticket")
        @ResponseBody
        public String ticket(Ticket ticket, HttpServletResponse resp, Authentication auth) {
            ticket.setLoginname(getLoginName(auth));
            ticket.setTime(getTimestamp());
            ticket = (Ticket) commonCRUD.newRecord(ticket, getLoginName(auth));
            resp.setStatus(Status.showSuccessMsg);
            return "提交成功，流水号: " + ticket.getTId();
        }

        /**
         * 购买理财产品
         *
         * @param production     理财产品
         * @param money          购买金额
         * @param dur            购买时长
         * @param response       响应
         * @param authentication 身份
         * @return 操作结果
         */
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

        /**
         * 添加理财产品
         *
         * @param production     产品信息
         * @param response       响应
         * @param authentication 身份
         * @return 操作结果
         */
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

        /**
         * 添加业务介绍
         *
         * @param introduction   介绍信息
         * @param response       响应
         * @param authentication 身份
         * @return 操作结果
         */
        @PostMapping("addIntroduction")
        @ResponseBody
        public String addIntroduction(Introduction introduction, HttpServletResponse response, Authentication authentication) {
            response.setStatus(Status.showSuccessMsg);
            introduction.setLoginname(getLoginName(authentication));
            introduction.setPubtime(getTimestamp());
            introduction = (Introduction) commonCRUD.newRecord(introduction, getLoginName(authentication));
            return "添加成功, 介绍编号: " + introduction.getIId();
        }

        /**
         * 修改个人信息
         *
         * @param response       响应
         * @param authentication 身份
         * @param request        请求
         * @return 操作结果
         */
        @PostMapping("profile")
        @ResponseBody
        public String profile(HttpServletResponse response, Authentication authentication, HttpServletRequest request) {
            try {
                response.setStatus(Status.showSuccessMsg);
                Map<String, String[]> pMap = request.getParameterMap();
                Class clazz = TYPE_POJO_MAP.get(loginService.getLoginType(getLoginName(authentication)));  //获取对应用户类型的类类型

                User userObj = (User) clazz.newInstance();  //创建一个对应用户类型的实例
                User dbObj = userService.getUser(authentication); //从数据库获取用户的原始信息

                for (Field field : clazz.getDeclaredFields()) { //遍历类属性，包括私有属性

                    if (!pMap.containsKey(field.getName())) continue; //检查参数是否是类属性
                    boolean acc = field.isAccessible();  //获取属性原本是否可写
                    field.setAccessible(true); //设置可写

                    if (!field.getType().equals(String.class)) //检查类属性是否为Srting
                        field.set(userObj, field.getType().getDeclaredMethod("valueOf", new Class[]{String.class}).invoke(null, pMap.get(field.getName())[0])); //如果不是，调用对应类型的valueOf方法
                    else field.set(userObj, pMap.get(field.getName())[0]); //如果是，直接写入

                    // if (field.get(userObj).equals(field.get(dbObj))) continue; //如果原始信息和修改信息相等则不更新
                    field.set(dbObj, field.get(userObj));  //更新类属性

                    field.setAccessible(acc); //还原是否可写
                }
                userService.putUser(getLoginName(authentication), dbObj); //更新数据库
                return "更新成功，手动刷新查看变化";
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
            response.setStatus(Status.showFail);
            return "";
        }
    }

    /**
     * 后台管理
     */
    @Controller
    @RequestMapping("admin")
    public class AdminSpace extends ComponentBase {
        /**
         * 添加用户
         *
         * @param response 响应
         * @param username 用户名
         * @param password 密码
         * @param type     类型
         * @return 添加结果
         */
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
