package project.tools.browser;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static java.lang.String.format;
import static java.lang.String.join;
import static project.tools.Platform.THIS_OS;

public enum Browsers implements IBrowser {

    FIREFOX {
        private static final String GECKO_DRIVER_PATH_VARIABLE = "webdriver.gecko.driver";
        private final List<String> knownNames = Arrays.asList("firefox", "mozillafirefox", "mozilla_firefox");

        public FirefoxOptions getCapabilities() {
            FirefoxOptions options = new FirefoxOptions();
            options.addPreference("dom.webnotifications.enabled", false);
            options.addPreference(FirefoxDriver.Capability.MARIONETTE, "true");
            options.addPreference(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
            return options;
        }

        public WebDriver getWebDriver(MutableCapabilities capabilities) {
            if (StringUtils.isEmpty(System.getProperty(GECKO_DRIVER_PATH_VARIABLE)))
                System.setProperty(GECKO_DRIVER_PATH_VARIABLE, getPathToDriver("marionette", "geckodriver"));

            FirefoxOptions options = getCapabilities();
            WebDriverManager.firefoxdriver().setup();
            WebDriver localDriver = new FirefoxDriver(options);
            Browsers.setDimension(localDriver);
            return localDriver;
        }

        @Override
        protected boolean isKnownAs(String name) {
            return knownNames.contains(name);
        }
    },
    CHROME {
        private static final String CHROME_DRIVER_PATH_VARIABLE = "webdriver.chrome.driver";
        private final List<String> knownNames = Arrays.asList("chrome", "googlechrome", "google_chrome");

        public ChromeOptions getCapabilities() {
            ChromeOptions chromeOptions = new ChromeOptions();

            HashMap<String, Object> prefs = new HashMap<>();
            prefs.put("profile.default_content_settings.popups", 0);
            prefs.put("profile.default_content_settings.geolocation", 1);
            prefs.put("download.prompt_for_download", false);
            prefs.put("download.directory_upgrade", true);
            prefs.put("profile.password_manager_enabled", false);
            prefs.put("credentials_enable_service", false);

            chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            chromeOptions.setCapability("chrome.switcCreateDataForSmokeRegression.javahes", Collections.singletonList("--no-default-browser-check"));
            chromeOptions.setCapability(CapabilityType.PLATFORM_NAME, Platform.MAC);

            HashMap<String, Object> chromePreferences = new HashMap<>();
            chromePreferences.put("password_manager_enabled", false);
            chromePreferences.put("credentials_enable_service", false);
            chromePreferences.put("disable-infobars", true);
            chromeOptions.setCapability("chrome.prefs", chromePreferences);

            chromeOptions.setExperimentalOption("prefs", prefs);

            chromeOptions
                    .addArguments("--disable-extensions")
                    .addArguments("--disable-infobars")
                    .addArguments("--disable-notifications")
                    .addArguments("--allow-file-access-from-files")
                    .addArguments("--use-fake-device-for-media-stream")
                    .addArguments("--use-fake-ui-for-media-stream")
                    .addArguments("--disable-web-security")
                    .addArguments("--remote-allow-origins=*")
                    .addArguments("--enable-geolocation");
            return chromeOptions;
        }

        public WebDriver getWebDriver(MutableCapabilities capabilities) {
            if (StringUtils.isEmpty(System.getProperty(CHROME_DRIVER_PATH_VARIABLE)))
                System.setProperty(CHROME_DRIVER_PATH_VARIABLE, getPathToDriver("googlechrome", "chromedriver"));

            ChromeOptions options = getCapabilities();
            WebDriverManager.chromedriver().clearDriverCache().setup();
            WebDriverManager.chromedriver().clearResolutionCache().setup();

            WebDriver localDriver = new ChromeDriver(options);
            Browsers.setDimension(localDriver);
            localDriver.manage().window().maximize();
            return localDriver;
        }

        protected boolean isKnownAs(String name) {
            return knownNames.contains(name);
        }
    },
    SAFARI {
        private final List<String> knownNames = Collections.singletonList("safari");

        public SafariOptions getCapabilities() {
            SafariOptions options = new SafariOptions();
            options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
            options.setUseTechnologyPreview(false);
            return options;
        }

        public WebDriver getWebDriver(MutableCapabilities capabilities) {
            WebDriver localDriver = new SafariDriver(getCapabilities());
            localDriver.manage().window().maximize();
            return localDriver;
        }

        @Override
        protected boolean isKnownAs(String name) {
            return knownNames.contains(name);
        }
    };

    protected abstract boolean isKnownAs(String name);

    public String getPathToDriver(String driversFolder, String driverName) {
        return join(File.separator, getPathToDrivers(), getOsDir(), driversFolder, getArchDir(),
                driverName);
    }

    private String getPathToDrivers() {
        return join(File.separator, System.getProperty("user.dir"), "..", "selenium_standalone_binaries");
    }

    private static void setDimension(WebDriver driver) {
        int width = (int) (long) ((JavascriptExecutor) driver).executeScript("return screen.width;");
        int height = (int) (long) ((JavascriptExecutor) driver).executeScript("return screen.height;");
        driver.manage().window().setSize(new Dimension(width, height));
    }

    private static String getOsDir() {
        String os = THIS_OS.toString().toLowerCase();

        switch (os) {
            case "windows":
                return "windows";
            case "linux":
                return "linux";
            case "mac":
            case "mac os":
            case "mac os x":
                return "osx";
            default:
                throw new RuntimeException(String.format("\nUnknown OS: %s\n", os));
        }
    }

    private String getArchDir() {
        String osArch = System.getProperty("os.arch");
        return osArch.contains("64") ? "64bit" : "32bit";
    }

    public static Browsers from(String browser) {
        return Arrays.stream(Browsers.values())
                .filter(value -> value.isKnownAs(browser.trim().toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(format("\nUnsupported browser: '%s'\n", browser)));
    }
}
