package nchu2.webhw.properites;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Vars {
    public static final Set<String> PUBLIC_PAGES;

    static {
        String[] strings = new String[]{"notification", "profile"};
        PUBLIC_PAGES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(strings)));
    }

    public static class CacheValues {
        private static final String prefix = "CACHE_";
        public static final String user = prefix + "user";
    }
}
