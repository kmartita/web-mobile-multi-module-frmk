package project.tools;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import project.tools.browser.Browsers;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.*;
import static project.tools.EnvManager.APP_BUNDLE_ID;
import static project.tools.EnvManager.APP_NAME;

@UtilityClass
public class ConfigurationManager {

    public static final String MOBILE_PLATFORM_VARIABLE = "mobile";
    //private static final String IS_REMOTE_VARIABLE = "remote";
    private static final String BROWSER_SYSTEM_VARIABLE = "browser";
    private static final String APP_PATH_VARIABLE = "appPath";
    private static final String ENVIRONMENT_SYSTEM_VARIABLE = "env";
    public static final String TEST_APP_NAME_VARIABLE = "testapp";
    private static final String IOS_VERSION_VARIABLE = "iosVersion";
    private static final String DEVICE_VARIABLE = "device";
    private static final String MOBILE_SESSION_TIMEOUT_VARIABLE = "mTimeout";
    private static final String RUN_APPIUM_VARIABLE = "runAppium";
    private static final String THREAD_COUNT_SYSTEM_VARIABLE = "threads";
    public static final String APPIUM_URL_VARIABLE = "appiumURL";
    private static final String APP_BUNDLE_ID_VARIABLE = "bundleId";

    //private static final String DEFAULT_IOS_VERSION = "16.2";
    private static final String DEFAULT_IOS_VERSION = "18.6";
    private static final List<String> SUPPORTED_IOS_VERSIONS = Arrays.asList("16.2", "18.6");


    //private static final String DEVICE_NAME = "iPad Pro (11-inch) (4th generation)";
    private static final String DEVICE_NAME = "iPad (A16)";

   /* public static boolean isRemote() {
        String remoteValue = System.getProperty(IS_REMOTE_VARIABLE);
        return Boolean.parseBoolean(remoteValue);
    }*/

    public static Platform getPlatform() {
        Platform platform = Platform.THIS_OS;
        String platformName = System.getProperty(MOBILE_PLATFORM_VARIABLE);

        if (!StringUtils.isEmpty(platformName)) {
            platform = Platform.from(platformName);
        }
        return platform;
    }

    public static String getBrowserName() {
        return System.getProperty(BROWSER_SYSTEM_VARIABLE);
    }

    public static Browsers getDriverType() {
        Browsers driverType = Browsers.CHROME;
        String browserName = getBrowserName();

        if (!StringUtils.isEmpty(browserName)) {
            driverType = Browsers.from(browserName);
        }

        System.out.printf("BROWSER: '%s'\n", driverType);
        return driverType;
    }

    public static String getEnvironment() {
        String defaultEnvironment = System.getProperty(ENVIRONMENT_SYSTEM_VARIABLE);
        defaultEnvironment = (defaultEnvironment != null) ? defaultEnvironment.toLowerCase() : "regression";
        System.setProperty(ENVIRONMENT_SYSTEM_VARIABLE, defaultEnvironment);
        return defaultEnvironment;
    }

    public static String getTestAppName() {
        return System.getProperty(TEST_APP_NAME_VARIABLE, APP_NAME);
    }

    public static String getTestAppAbsolutePath() {
        String appPath = System.getProperty(APP_PATH_VARIABLE);
        if (!StringUtils.isEmpty(appPath))
            return appPath;

        String userDir = PathToFile.getRootOfProject();
        String fileSeparator = FileSystems.getDefault().getSeparator();

        String env = getEnvironment();
        String pathToApp;
        switch (env) {
            case "local" :
            case "regression" :
                pathToApp = join(fileSeparator, userDir, "app", getTestAppName());
                break;

            default : throw new IllegalStateException(format("\nUnknown environment has been provided [%s].\n", env));
        }

        File app = new File(pathToApp);
        return app.getAbsolutePath();
    }

    public static String getIosVersion() {
        String iosVersion = System.getProperty(IOS_VERSION_VARIABLE);
        if (StringUtils.isEmpty(iosVersion))
            iosVersion = DEFAULT_IOS_VERSION;

        boolean isSupported = SUPPORTED_IOS_VERSIONS.contains(iosVersion);

        if (!isSupported)
            throw new IllegalArgumentException(
                    String.format("\nUnsupported iOS version: %s. Please provide a version from the list: %s\n",
                            iosVersion, SUPPORTED_IOS_VERSIONS));

        return iosVersion;
    }

    public static String getDevice() {
        String value = System.getProperty(DEVICE_VARIABLE);
        if (StringUtils.isEmpty(value)) {
            return DEVICE_NAME;
        } else {
            return value;
        }
    }


//TODO - ?
    public static int getMobileSessionTimeoutVariable() {
        String mobileTimeout = System.getProperty(MOBILE_SESSION_TIMEOUT_VARIABLE);
        if (!StringUtils.isEmpty(mobileTimeout))
            return Integer.parseInt(mobileTimeout);
        else
            return 5000;
    }

    public static String getAppBundleId() {
        return System.getProperty(APP_BUNDLE_ID_VARIABLE, APP_BUNDLE_ID);
    }

    public static boolean isRunAppiumFromCode() {
        String createData = System.getProperty(RUN_APPIUM_VARIABLE);
        return StringUtils.isNoneEmpty(createData) && createData.equals("true");
    }

    public static int getThreadCount() {
        int count = -1;
        String threadCount = System.getProperty(THREAD_COUNT_SYSTEM_VARIABLE);
        if (StringUtils.isEmpty(threadCount))
            return count;

        if (!StringUtils.isNumeric(threadCount))
            throw new IllegalArgumentException("\nValue of the command-line parameter '" + THREAD_COUNT_SYSTEM_VARIABLE
                                               + "' must be a positive integer number\n");

        count = Integer.parseInt(threadCount);

        if (count <= 0)
            throw new IllegalArgumentException("\nValue of the command-line parameter '" + THREAD_COUNT_SYSTEM_VARIABLE
                                               + "' must be a positive integer number\n");

        return count;
    }
}
