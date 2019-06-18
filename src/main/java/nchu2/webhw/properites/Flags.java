package nchu2.webhw.properites;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:/flags.properties")
public class Flags {

    public static final char TRUE = 'T';
    public static final char FALSE = 'F';
    @Autowired
    private Environment env;

    /**
     * 将T/F转为bool
     *
     * @param c char字符
     * @return 对应的boolean值
     */
    public static boolean c2b(char c) {
        return c == TRUE;
    }

    /**
     * 获取表中字段对应的FieldFlags
     *
     * @param tableName 对应表的名字
     * @param fieldName 字段名
     * @return Field Flags
     */
    public Field getFieldFlag(String tableName, String fieldName) {
        String flagStr = env.getProperty(String.format("field.%s.%s", tableName, fieldName));
        if (flagStr == null) {
//            System.out.println(String.format("field not exist: field.%s.%s", tableName, fieldName));
            return new Field(true, false, true);
        }
//        System.out.println(String.format("field.%s.%s", tableName, fieldName));

        Field field = new Field();
        field.setVisibility(c2b(flagStr.charAt(0)));
        field.setEditability(c2b(flagStr.charAt(1)));
        field.setShowType(c2b(flagStr.charAt(2)));
        return field;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Field {
        public boolean visibility;
        public boolean editability;
        public boolean showType;
    }

}
