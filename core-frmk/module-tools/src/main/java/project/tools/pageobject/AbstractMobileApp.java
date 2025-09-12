package project.tools.pageobject;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;

public abstract class AbstractMobileApp extends AbstractApp {

    public static IOSDriver getIOSDriver() {
        return (IOSDriver) getDriver();
    }

    public static AppiumDriver getAppiumDriver() {
        return getIOSDriver();
    }
}
