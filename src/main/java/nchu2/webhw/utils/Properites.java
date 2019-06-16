package nchu2.webhw.utils;

import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Properites {
    public static final Set<String> PUBLIC_PAGES;

    static {
        String[] strings = {"notification", "profile"};
        HashSet<String> hashSet = new HashSet<>(Arrays.asList(strings));
        PUBLIC_PAGES = Collections.unmodifiableSet(hashSet);
    }

    public enum UserType {
        Customer, Manager, Staff;

        public static class Converter implements org.jooq.Converter<Integer, UserType> {

            @Override
            public UserType from(Integer databaseObject) {
                switch (databaseObject) {
                    case 0:
                        return Customer;
                    case 1:
                        return Manager;
                    case 2:
                        return Staff;
                }
                return null;
            }

            @Override
            public Integer to(UserType userObject) {
                switch (userObject) {
                    case Customer:
                        return 0;
                    case Manager:
                        return 1;
                    case Staff:
                        return 2;
                }
                return null;
            }

            @Override
            public Class<Integer> fromType() {
                return Integer.class;
            }

            @Override
            public Class<UserType> toType() {
                return UserType.class;
            }
        }

        public Authority getAuthority() {
            return new Authority(this);
        }


        public static class Authority implements GrantedAuthority {
            private UserType userType;

            Authority(UserType userType) {
                this.userType = userType;
            }

            @Override
            public String getAuthority() {
                return userType.name();
            }

            public UserType getUserType() {
                return userType;
            }
        }
    }

    //    #show显示 #hide隐藏 #editable可编辑 #uneditable不可编辑 #direct直接显示(SEX=1转为SEX=男) #indirect编码后显示
    public static class FieldFlag {
        public static class Visibility {
            public static final boolean SHOW = true;
            public static final boolean HIDE = false;
        }

        public static class Editability {
            public static final boolean EDITABLE = true;
            public static final boolean UNEDITABLE = false;
        }

        public static class ShowType {
            public static final boolean DIRECT = true;
            public static final boolean INDIRECT = false;
        }

    }
}
