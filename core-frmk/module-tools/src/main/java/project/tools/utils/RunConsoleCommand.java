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

    public static void setDefaultGpsLocation() {
        String[] cmd = {"osascript", "-e",
                "on menu_click(mList)\n" +
                "    local appName, topMenu, r\n" +
                "\n" +
                "    -- Validate our input\n" +
                "    if mList's length < 3 then error \"Menu list is not long enough\"\n" +
                "\n" +
                "    -- Set these variables for clarity and brevity later on\n" +
                "    set {appName, topMenu} to (items 1 through 2 of mList)\n" +
                "    set r to (items 3 through (mList's length) of mList)\n" +
                "\n" +
                "    -- This overly-long line calls the menu_recurse function with\n" +
                "    -- two arguments: r, and a reference to the top-level menu\n" +
                "    tell application \"System Events\" to my menu_click_recurse(r, ((process appName)'s ¬\n" +
                "        (menu bar 1)'s (menu bar item topMenu)'s (menu topMenu)))\n" +
                "end menu_click\n" +
                "\n" +
                "on menu_click_recurse(mList, parentObject)\n" +
                "    local f, r\n" +
                "\n" +
                "    -- `f` = first item, `r` = rest of items\n" +
                "    set f to item 1 of mList\n" +
                "    if mList's length > 1 then set r to (items 2 through (mList's length) of mList)\n" +
                "\n" +
                "    -- either actually click the menu item, or recurse again\n" +
                "    tell application \"System Events\"\n" +
                "        if mList's length is 1 then\n" +
                "            click parentObject's menu item f\n" +
                "        else\n" +
                "            my menu_click_recurse(r, (parentObject's (menu item f)'s (menu f)))\n" +
                "        end if\n" +
                "    end tell\n" +
                "end menu_click_recurse\n" +
                "\n" +
                "application \"Simulator\" activate    \n" +
                "delay 0.2\n" +
                "menu_click({\"Simulator\",\"Features\", \"Location\", \"Apple\"})\n"
        };
        runConsoleCommand(cmd);
    }
}
