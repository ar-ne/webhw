package nchu2.webhw.properties.mapping;

public enum MessageType {
    Broadcast, Unicast,LiveUpdate;

    public static class Converter implements org.jooq.Converter<Integer, MessageType> {
        @Override
        public MessageType from(Integer databaseObject) {
            switch (databaseObject) {
                case 0:
                    return Broadcast;
                case 1:
                    return Unicast;
            }
            throw new RuntimeException("DBO is not a valid MessageType: " + databaseObject);
        }

        @Override
        public Integer to(MessageType userObject) {
            switch (userObject) {
                case Broadcast:
                    return 0;
                case Unicast:
                    return 1;
            }
            throw new RuntimeException("UO is not a valid MessageType: " + userObject);
        }

        @Override
        public Class<Integer> fromType() {
            return Integer.class;
        }

        @Override
        public Class<MessageType> toType() {
            return MessageType.class;
        }
    }
}
