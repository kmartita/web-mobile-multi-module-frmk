package project.tools.pageobject;

import io.qameta.allure.Step;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import project.tools.ConfigurationManager;
import project.tools.Platform;
import project.tools.browser.Browser;

import java.io.File;

public abstract class AbstractApp implements IApp {

    private static final ThreadLocal<Browser> WEB = ThreadLocal.withInitial(Browser::new);
    private static final ThreadLocal<Browser> MOBILE = ThreadLocal.withInitial(Browser::new);

    protected AbstractApp() {
    }

    public static ThreadLocal<Browser> getBrowser() {
        return WEB;
    }

    public static ThreadLocal<Browser> getMobile() {
        return MOBILE;
    }

    public static WebDriver getMobileDriver() {
        return getMobile().get().getMobileDriver();
    }

    private static WebDriver getBrowserDriver() {
        return getBrowser().get().getDriver();
    }

    public static WebDriver getInstanceDriver() {
        Platform platform = ConfigurationManager.getPlatform();
        switch(platform) {
            case IOS : return getMobileDriver();
            case THIS_OS : return getBrowserDriver();
            default : throw new RuntimeException(String.format("\nPlatform [%s] is not supported\n", platform));
        }
    }

    public static ThreadLocal<Browser> getBrowserInstance() {
        Platform platform = ConfigurationManager.getPlatform();
        switch(platform) {
            case IOS : return getMobile();
            case THIS_OS : return getBrowser();
            default : throw new RuntimeException(String.format("\nPlatform [%s] is not supported\n", platform));
        }
    }

    @Step
    public static void closeMobile() {
        getMobile().get().quit();
        getMobile().remove();
        MOBILE.remove();
    }

    @Step
    public static void closeBrowser() {
        getBrowser().get().quit();
        getBrowser().remove();
        WEB.remove();
    }

    public static boolean isDriverExisted() {
        return getBrowserInstance().get().isOpened();
    }

    @Override
    public File getScreenshot() {
        if (isDriverExisted()) {
            return ((TakesScreenshot) getInstanceDriver()).getScreenshotAs(OutputType.FILE);
        }
        return null;
    }
}
