package nchu2.webhw.properties.mapping;

public enum Risk {
    None, Low, High;

    public static class Converter implements org.jooq.Converter<Integer, Risk> {
        @Override
        public Risk from(Integer databaseObject) {
            switch (databaseObject) {
                case 0:
                    return None;
                case 1:
                    return Low;
                case 2:
                    return High;
            }
            throw new RuntimeException("DBO not a valid 'Risk'");
        }

        @Override
        public Integer to(Risk userObject) {
            switch (userObject) {
                case None:
                    return 0;
                case Low:
                    return 1;
                case High:
                    return 2;
            }
            throw new RuntimeException("UO not a valid 'Risk'");
        }

        @Override
        public Class<Integer> fromType() {
            return Integer.class;
        }

        @Override
        public Class<Risk> toType() {
            return Risk.class;
        }
    }
}
