package nchu2.webhw.properties;

import nchu2.webhw.model.User;
import nchu2.webhw.model.Webhw;
import nchu2.webhw.properties.mapping.UserType;
import org.jooq.Table;

import java.lang.reflect.Constructor;
import java.util.*;

import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;
import static nchu2.webhw.properties.mapping.UserType.*;

/**
 * 常量
 */
public class Vars {
    public static final Set<String> PUBLIC_PAGES;  //公共页面集
    public static final Map<Class, Constructor> POJO_DAO_MAPPER; //用于将pojo转为dao的构造方法
    public static final Map<Class, Table<?>> POJO_DAO_TABLE; //pojo转table
    public static final Map<UserType, Class<? extends User>> TYPE_POJO_MAP; //用户类型和对应pojo的转换


    /**
     * 填充数据
     */
    static {
        String[] strings = new String[]{"notification", "profile", "production"};
        PUBLIC_PAGES = unmodifiableSet(new HashSet<>(Arrays.asList(strings)));

        HashMap<Class, Constructor> map = new HashMap<>();
        HashMap<Class, Table<?>> map1 = new HashMap<>();
        System.out.println("Building static map => Vars.POJO_DAO_MAPPER | Vars.POJO_DAO_MAPPER");
        for (Table<?> table : Webhw.WEBHW.getTables()) {
            String pojo = String.format("%s.pojos.%s", table.getClass().getPackage().getName(), table.getName());
            String dao = String.format("%s.daos.%sDao", table.getClass().getPackage().getName(), table.getName());
            System.out.println(pojo + " <=> " + dao);
            try {
                map.put(Class.forName(pojo), Class.forName(dao).getConstructor(org.jooq.Configuration.class));
                map1.put(Class.forName(pojo), table);
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        POJO_DAO_MAPPER = unmodifiableMap(map);
        POJO_DAO_TABLE = unmodifiableMap(map1);
        System.out.println("Built Vars.POJO_DAO_MAPPER...............");

        HashMap<UserType, Class<? extends User>> map2 = new HashMap<>();
        map2.put(Customer, nchu2.webhw.model.tables.pojos.Customer.class);
        map2.put(Manager, nchu2.webhw.model.tables.pojos.Manager.class);
        map2.put(Staff, nchu2.webhw.model.tables.pojos.Staff.class);
        TYPE_POJO_MAP = Collections.unmodifiableMap(map2);
    }

    public static class CacheValues {
        private static final String prefix = "CACHE_";
        public static final String user = prefix + "user";
        public static final String login = prefix + "login";
        public static final String production = prefix + "production";
    }
}
