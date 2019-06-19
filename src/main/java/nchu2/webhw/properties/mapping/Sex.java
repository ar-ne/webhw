package nchu2.webhw.properties.mapping;

public enum Sex {
    Female, Male;

    public static class Converter implements org.jooq.Converter<Integer, Sex> {
        @Override
        public Sex from(Integer databaseObject) {
            switch (databaseObject) {
                case 0:
                    return Female;
                case 1:
                    return Male;
            }
            throw new RuntimeException("DBO is not a valid Sex: " + databaseObject);
        }

        @Override
        public Integer to(Sex userObject) {
            switch (userObject) {
                case Male:
                    return 1;
                case Female:
                    return 0;
            }
            throw new RuntimeException("UO is not a valid Sex: " + userObject);
        }

        @Override
        public Class<Integer> fromType() {
            return Integer.class;
        }

        @Override
        public Class<Sex> toType() {
            return Sex.class;
        }
    }
}
