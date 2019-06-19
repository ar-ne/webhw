package nchu2.webhw.utils;

import static java.lang.Math.random;

public class Tools {
    public static String rStr() {
        return rStr(8);
    }

    public static String rStr(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append((char) (random() > 0.45
                    ? (random() > 0.75 ? ' ' : ('a' + random() * 26))
                    : ('A' + random() * 26)));
        }
        return stringBuilder.toString();
    }

    public static Object pick(Object o1, Object o2) {
        if (random() > 0.5) return o1;
        else return o2;
    }

    public static Double getAccountBalance(String loginName) {
        return random() > 0.5 ? (random() * 100) / (random()) : random() * 1000;
    }
}
