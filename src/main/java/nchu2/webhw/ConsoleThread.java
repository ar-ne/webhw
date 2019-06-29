package nchu2.webhw;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 控制台
 */
public class ConsoleThread extends Thread {
    private static Map<String, Runnable> commands;

    static {
        commands = new HashMap<>();
        commands.put("halt", () -> System.exit(-1));
        commands.put("help", () -> System.out.println(commands));
        commands = Collections.unmodifiableMap(commands);
    }

    @Override
    public void run() {

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            if (commands.containsKey(line))
                commands.get(line).run();
            else System.out.println(String.format("Command %s not found", line));
        }

    }
}
