package nchu2.webhw.utils;

import static java.lang.Math.random;

/**
 * 零散的小工具
 */
public class Tools {
    /**
     * 生成随机字符串长度为八
     *
     * @return
     */
    public static String rStr() {
        return rStr(8);
    }

    /**
     * 生成随机字符串长度随意
     * @param length
     * @return
     */
    public static String rStr(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            stringBuilder.append((char) (random() > 0.45
                    ? (random() > 0.75 ? ' ' : ('a' + random() * 26))
                    : ('A' + random() * 26)));
        }
        return stringBuilder.toString();
    }

    /**
     * 二选一
     * @param o1
     * @param o2
     * @return
     */
    public static Object pick(Object o1, Object o2) {
        if (random() > 0.5) return o1;
        else return o2;
    }
}
