package nchu2.webhw.properties.mapping;

import org.jooq.Converter;

/**
 * 数据库中的Int转Boolean
 */
public class BoolConvert implements Converter<Integer, Boolean> {
    @Override
    public Boolean from(Integer databaseObject) {
        return databaseObject != 0;
    }

    @Override
    public Integer to(Boolean userObject) {
        if (userObject) return 1;
        else return 0;
    }

    @Override
    public Class<Integer> fromType() {
        return Integer.class;
    }

    @Override
    public Class<Boolean> toType() {
        return Boolean.class;
    }
}
