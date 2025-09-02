package project.tools;

import io.github.cdimascio.dotenv.Dotenv;

import java.nio.file.Paths;
import java.util.Arrays;

import static java.lang.String.format;
import static project.tools.ConfigurationManager.getEnvironment;

public class EnvManager {

    private static final Dotenv dotenv;
    static {
        EnvType env = getEnvironment();
        dotenv = loadEnvFile(env);
    }

    public static final String BASE_URL = get("BASE_URL");
    public static final String APPIUM_URL = get("APPIUM_URL");
    public static final String APP_NAME = get("APP_NAME");
    public static final String APP_BUNDLE_ID = get("APP_BUNDLE_ID");

    public static String get(String key) {
        String value = dotenv.get(key);
        return (value != null) ? value : System.getenv(key);
    }

    private static Dotenv loadEnvFile(EnvType env) {
        String baseDir = Paths.get(PathToFile.getRootOfProject(), "config").toString();

        String filename = switch (env) {
            case DEVELOPMENT -> ".env.dev";
            case TEST -> ".env.test";
        };

        return Dotenv.configure()
                .directory(baseDir)
                .filename(filename)
                .load();
    }

    public enum EnvType {
        TEST("test"), DEVELOPMENT("dev");

        private final String name;

        EnvType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static EnvType from(String envStr) {
            return Arrays.stream(EnvType.values())
                    .filter(env -> env.getName().equalsIgnoreCase(envStr))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(format("\nUnsupported environment: '%s'\n", envStr)));
        }
    }
}
