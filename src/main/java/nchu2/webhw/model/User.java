package nchu2.webhw.model;

import nchu2.webhw.properties.mapping.Sex;

import java.io.Serializable;
import java.sql.Timestamp;

public abstract class User implements Serializable {
    public abstract String getLoginname();

    public abstract User setLoginname(String loginname);

    public abstract String getName();

    public abstract User setName(String name);

    public abstract String getPhonenum();

    public abstract User setPhonenum(String phonenum);

    public abstract Integer getAge();

    public abstract User setAge(Integer age);

    public abstract Sex getSex();

    public abstract User setSex(Sex sex);

    public abstract String getPersonid();

    public abstract User setPersonid(String personid);

    public abstract String getAdress();

    public abstract User setAdress(String adress);

    public abstract Timestamp getJointime();

    public abstract User setJointime(Timestamp jointime);

    public Integer getWorkyear() {
        return null;
    }

    public User setWorkyear(Integer workyear) {
        return null;
    }
}
