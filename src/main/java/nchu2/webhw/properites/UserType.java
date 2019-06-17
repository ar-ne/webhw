package nchu2.webhw.properites;

import org.springframework.security.core.GrantedAuthority;

public enum UserType {
    Customer, Manager, Staff;

    public static final String prefix = "ROLE_";

    public Authority getAuthority() {
        return new Authority(this);
    }

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

    public static class Authority implements GrantedAuthority {
        private UserType userType;

        Authority(UserType userType) {
            this.userType = userType;
        }

        @Override
        public String getAuthority() {
            return prefix + userType.name();
        }

        public UserType getUserType() {
            return userType;
        }
    }
}