package project.tools;

import org.apache.commons.lang3.StringUtils;
import project.tools.browser.Browsers;
import project.tools.browser.IBrowser;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.Optional;

import static java.lang.String.*;
import static project.tools.EnvManager.*;
import static project.tools.EnvManager.APP_BUNDLE_ID;
import static project.tools.EnvManager.APP_NAME;

public class ConfigurationManager {

    public static final String MOBILE_VARIABLE = "mobile";
    private static final String BROWSER_VARIABLE = "browser";
    private static final String APP_PATH_VARIABLE = "appPath";
    private static final String ENV_VARIABLE = "env";
    public static final String APP_NAME_VARIABLE = "testApp";
    private static final String IOS_VERSION_VARIABLE = "iosVersion";
    private static final String DEVICE_VARIABLE = "device";
    private static final String RUN_APPIUM_VARIABLE = "runAppium";
    private static final String THREAD_COUNT_VARIABLE = "threads";
    public static final String APPIUM_URL_VARIABLE = "appiumURL";
    private static final String APP_BUNDLE_ID_VARIABLE = "bundleId";

    private static final String THIS_IOS_VERSION = "18.6";
    private static final String THIS_DEVICE_NAME = "iPad (A16)";

    private ConfigurationManager() {}

    public static Platform getPlatform() {
        return Optional.ofNullable(System.getProperty(MOBILE_VARIABLE))
                .filter(platform -> !StringUtils.isEmpty(platform))
                .map(Platform::from)
                .orElse(Platform.THIS_OS);
    }

    public static IBrowser getBrowser() {
        IBrowser defaultBrowser = Optional.ofNullable(System.getProperty(BROWSER_VARIABLE))
                .filter(browser -> !StringUtils.isEmpty(browser))
                .map(Browsers::from)
                .orElse(Browsers.CHROME);

        System.out.printf("BROWSER: '%s'\n", defaultBrowser);
        return defaultBrowser;
    }

    public static EnvType getEnvironment() {
        String env = System.getProperty(ENV_VARIABLE);
        env = (env != null && !env.isEmpty()) ? env.toLowerCase() : EnvType.TEST.getName();
        EnvType envType = EnvType.from(env);
        System.setProperty(ENV_VARIABLE, envType.getName().toLowerCase());
        return envType;
    }

    public static String getTestAppAbsolutePath() {
        String appPath = System.getProperty(APP_PATH_VARIABLE);
        if (!StringUtils.isEmpty(appPath))
            return appPath;

        String userDir = PathToFile.getRootOfProject();
        String fileSeparator = FileSystems.getDefault().getSeparator();

        EnvType env = getEnvironment();
        String pathToApp;
        switch (env) {
            case TEST :
            case DEVELOPMENT :
                pathToApp = join(fileSeparator, userDir, "app", System.getProperty(APP_NAME_VARIABLE, APP_NAME));
                break;

            default : throw new IllegalStateException(format("\nUnknown environment has been provided [%s].\n", env));
        }

        File app = new File(pathToApp);
        return app.getAbsolutePath();
    }

    public static String getIosVersion() {
        return Optional.ofNullable(System.getProperty(IOS_VERSION_VARIABLE))
                .orElse(THIS_IOS_VERSION);
    }

    public static String getDevice() {
        return Optional.ofNullable(System.getProperty(DEVICE_VARIABLE))
                .filter(v -> !StringUtils.isEmpty(v))
                .orElse(THIS_DEVICE_NAME);
    }

    public static String getAppBundleId() {
        return System.getProperty(APP_BUNDLE_ID_VARIABLE, APP_BUNDLE_ID);
    }

    public static boolean isRunAppiumFromCode() {
        return Optional.ofNullable(System.getProperty(RUN_APPIUM_VARIABLE))
                .filter(runAppium -> !StringUtils.isEmpty(runAppium))
                .map(String::toLowerCase)
                .map(string -> string.equals("true"))
                .orElse(false);
    }

    public static int getThreadCount() {
        int count = -1;
        String threadParam = System.getProperty(THREAD_COUNT_VARIABLE);

        if (StringUtils.isEmpty(threadParam))
            return count;

        if (!StringUtils.isNumeric(threadParam))
            throw new IllegalArgumentException("\nValue of the command-line parameter '" + THREAD_COUNT_VARIABLE
                                               + "' must be a positive integer number\n");
        count = Integer.parseInt(threadParam);
        System.out.printf("THREAD COUNT: '%s'\n", threadParam);

        if (count <= 0)
            throw new IllegalArgumentException("\nValue of the command-line parameter '" + THREAD_COUNT_VARIABLE
                                               + "' must be a positive integer number\n");
        return count;
    }
}
