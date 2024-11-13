package project.tools.pageobject.mobile;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import project.tools.pageobject.AbstractApp;

import java.time.Duration;

public abstract class AbstractMobileApp extends AbstractApp {

    public void closeApp() {
        getIOSDriver().closeApp();
    }

    public void launchApp() {
        getIOSDriver().launchApp();
    }

    public void runInBackground(int seconds) {
        getIOSDriver().runAppInBackground(Duration.ofSeconds(seconds));
    }

    public static IOSDriver getIOSDriver() {
        return (IOSDriver) getInstanceDriver();
    }

    public static AppiumDriver getAppiumDriver() {
        return getIOSDriver();
    }
}
