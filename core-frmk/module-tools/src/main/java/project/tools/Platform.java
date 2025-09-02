package project.tools;

import java.util.Arrays;

public enum Platform {

    IOS("ios"), THIS_OS(System.getProperty("os.name"));

    private final String name;

    Platform(String name) {
        this.name = name;
    }

    public static Platform from(String string) {
        return Arrays.stream(Platform.values())
                .filter(candidate -> string.trim().equalsIgnoreCase(candidate.name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("\nUnsupported platform: %s\n", string)));
    }

    @Override
    public String toString() {
        return this.name;
    }
}
