package nchu2.webhw.Utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties("field.flag")
@PropertySource("classpath:/fieldFlags.properties")
public class FieldFlag {
    private final Map<Integer, Integer> priorityMap = new HashMap<>();

    public Map<Integer, Integer> getPriority() {
        return priorityMap;
    }

    public void process() {
        Integer myPriority = priorityMap.get(10);
        // use it here
    }

    @Data
    public static class FieldFlagAttr {
        public static String visibility;
        public static String editability;
        public static String showType;
    }
}
