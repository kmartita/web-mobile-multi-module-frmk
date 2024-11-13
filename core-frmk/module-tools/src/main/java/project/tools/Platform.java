package project.tools;

public enum Platform {

    IOS("ios"), THIS_OS(System.getProperty("os.name"));

    private final String name;

    Platform(String name) {
        this.name = name;
    }

    public static Platform from(String string) {
        if (string != null) {
            String trimmed = string.trim();
            for (Platform candidate : Platform.values()) {
                if (trimmed.equalsIgnoreCase(candidate.name))
                    return candidate;
            }
        }
        throw new IllegalArgumentException(String.format("\nUnsupported platform: %s\n",  string));
    }

    @Override
    public String toString() {
        return this.name;
    }
}
