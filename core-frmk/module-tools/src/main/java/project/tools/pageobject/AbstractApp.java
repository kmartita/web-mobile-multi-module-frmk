package project.tools.pageobject;

import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import project.tools.ConfigurationManager;
import project.tools.Platform;
import project.tools.drivers.Drivers;

public abstract class AbstractApp {

    private static final ThreadLocal<Drivers> WEB = ThreadLocal.withInitial(Drivers::new);
    private static final ThreadLocal<Drivers> MOBILE = ThreadLocal.withInitial(Drivers::new);

    protected AbstractApp() {
    }

    public static ThreadLocal<Drivers> getBrowser() {
        return WEB;
    }

    public static ThreadLocal<Drivers> getMobile() {
        return MOBILE;
    }

    private static WebDriver getMobileDriver() { return getMobile().get().getMobileDriver(); }

    private static WebDriver getBrowserDriver() {
        return getBrowser().get().getBrowserDriver();
    }

    public static WebDriver getDriver() {
        Platform platform = ConfigurationManager.getPlatform();
        return switch (platform) {
            case IOS -> getMobileDriver();
            case THIS_OS -> getBrowserDriver();
            default -> throw new RuntimeException(String.format("\nPlatform '%s' is not supported\n", platform));
        };
    }

    public static void closeMobile() {
        Allure.step("Close mobile application");
        getMobile().get().quit();
        getMobile().remove();
        MOBILE.remove();
    }

    public static void closeBrowser() {
        Allure.step("Close web application");
        getBrowser().get().quit();
        getBrowser().remove();
        WEB.remove();
    }
}