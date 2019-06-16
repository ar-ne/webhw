package nchu2.webhw.model;

import nchu2.webhw.service.AuthService;

import java.io.Serializable;

public abstract class User implements Serializable {
    public abstract User setId(Long id);

    public abstract String getName();

    public interface Register {
        /**
         * 注册一个新的用户
         *
         * @param loginName     登录名
         * @param user          用户信息
         * @param plainTextPass 密码（明文）
         * @return 新注册用户的信息
         * @throws AuthService.LoginNameExistsException 登录名已被占用
         */
        User register(String loginName, String plainTextPass, User user) throws AuthService.LoginNameExistsException;
    }
}
