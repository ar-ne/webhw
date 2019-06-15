package nchu2.webhw.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:/flags.properties")
public class Flags {

    @Autowired
    private Environment env;

    public static final char TRUE = 'T';
    public static final char FALSE = 'F';

    /**
     * 获取表中字段对应的FieldFlags
     *
     * @param tableName 对应表的名字
     * @param fieldName 字段名
     * @return Field Flags
     */
    public FieldFlag getFielfFlag(String tableName, String fieldName) {
        String flagStr = env.getProperty(String.format("field.%s.%s", tableName, fieldName));
        System.out.println(String.format("field.%s.%s", tableName, fieldName));
        FieldFlag fieldFlag = new FieldFlag();
        fieldFlag.setVisibility(c2b(flagStr.charAt(0)));
        fieldFlag.setEditability(c2b(flagStr.charAt(1)));
        fieldFlag.setShowType(c2b(flagStr.charAt(2)));
        return fieldFlag;
    }

    /**
     * 将T/F转为bool
     *
     * @param c char字符
     * @return 对应的boolean值
     */
    public static boolean c2b(char c) {
        return c == TRUE;
    }

    @Data
    public static class FieldFlag {
        public boolean visibility;
        public boolean editability;
        public boolean showType;
    }
}
