package project.tools.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RunConsoleCommand {

    private static void runConsoleCommand(String[] cmd) {
        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                }
            }
            int exitCode = process.waitFor();
            System.out.printf("Exited with code: %s%n", exitCode);

        } catch (Exception e) {
            System.out.printf("Error: %s%n", e.getMessage());
        }
    }

    //TODO: System Preferences → Security & Privacy → Privacy → Accessibility: Terminal + IntelliJ
    public static void rotateDevice() {
        String[] cmd = {
                "osascript", "-e",
                "tell application \"Simulator\" to activate",
                "-e",
                "delay 1",
                "-e",
                "tell application \"System Events\" to click menu item \"Rotate Right\" " +
                "of menu 1 of menu bar item \"Device\" of menu bar 1 of application process \"Simulator\""
        };
        runConsoleCommand(cmd);
    }
}
