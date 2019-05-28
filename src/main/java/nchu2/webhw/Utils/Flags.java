package nchu2.webhw.Utils;

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

    /**
     * 获取表中字段对应的FieldFlags
     *
     * @param tableName 对应表的名字
     * @param fieldName 字段名
     * @return Field Flags
     * @throws Exception NPE和NotValid
     */
    public FieldFlag getFielfFlag(String tableName, String fieldName) throws Exception {
        String flagStr = env.getProperty(String.format("field.%s.%s", tableName, fieldName));
        if (flagStr.length() != 3) throw new Exception("Not a valid flag string");
        FieldFlag fieldFlag = new FieldFlag();
        fieldFlag.setVisibility(String.valueOf(flagStr.charAt(0)));
        fieldFlag.setEditability(String.valueOf(flagStr.charAt(1)));
        fieldFlag.setShowType(String.valueOf(flagStr.charAt(2)));
        return fieldFlag;
    }

    @Data
    public static class FieldFlag {
        public String visibility;
        public String editability;
        public String showType;
    }
}
