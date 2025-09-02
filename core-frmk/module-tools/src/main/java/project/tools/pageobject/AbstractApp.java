package project.tools.pageobject;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
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

    public static WebDriver getMobileDriver() {
        return getMobile().get().getMobileDriver();
    }

    private static WebDriver getBrowserDriver() {
        return getBrowser().get().getBrowserDriver();
    }

    public static WebDriver getInstanceDriver() {
        Platform platform = ConfigurationManager.getPlatform();
        switch(platform) {
            case IOS : return getMobileDriver();
            case THIS_OS : return getBrowserDriver();
            default : throw new RuntimeException(String.format("\nPlatform [%s] is not supported\n", platform));
        }
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

     /*public static ThreadLocal<Browser> getBrowserInstance() {
        Platform platform = ConfigurationManager.getPlatform();
        switch(platform) {
            case IOS : return getMobile();
            case THIS_OS : return getBrowser();
            default : throw new RuntimeException(String.format("\nPlatform [%s] is not supported\n", platform));
        }
    }*/

    /*public static boolean isDriverExisted() {
        return getBrowserInstance().get().isOpened();
    }
*/
    /*@Override
    public File getScreenshot() {
        if (isDriverExisted()) {
            return ((TakesScreenshot) getInstanceDriver()).getScreenshotAs(OutputType.FILE);
        }
        return null;
    }*/