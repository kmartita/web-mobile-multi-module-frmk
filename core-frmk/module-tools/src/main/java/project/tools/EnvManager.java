package project.tools;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EnvManager {

    private static final Dotenv ENV = Dotenv.configure()
            .directory(PathToFile.getRootOfProject())
            .load();

    public static final String APPIUM_URL = getEnvOption("APPIUM_URL");
    public static final String REMOTE_URL = getEnvOption("REMOTE_URL");
    public static final String BASE_URL = getEnvOption("BASE_URL");
    public static final String APP_NAME = getEnvOption("APP_NAME");
    public static final String APP_BUNDLE_ID = getEnvOption("APP_BUNDLE_ID");

    private static String getEnvOption(String option) {
        String value = ENV.get(option);
        return (value != null) ? value : System.getenv(option);
    }
}
