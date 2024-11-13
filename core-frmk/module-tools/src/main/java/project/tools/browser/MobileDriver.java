package project.tools.browser;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import project.tools.ConfigurationManager;

import java.net.MalformedURLException;
import java.net.URL;

import static project.tools.ConfigurationManager.*;
import static project.tools.EnvManager.DEFAULT_APPIUM_URL;
import static project.tools.EnvManager.REMOTE_URL;

public class MobileDriver {

    private static RemoteWebDriver startRemoteIos(DesiredCapabilities capabilities) {
        RemoteWebDriver driver;
        try {
            driver = new IOSDriver(new URL(REMOTE_URL), capabilities);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return driver;
    }

    public static WebDriver startIos() {
        return (Browser.IS_REMOTE) ? startRemoteIos(iosNativeAppCapabilities()) : startIos(iosNativeAppCapabilities());
    }

    private static WebDriver startIos(DesiredCapabilities capabilities) {
        IOSDriver driver;
        String appiumUrl = System.getProperty(APPIUM_URL_VARIABLE);

        if (appiumUrl == null) {
            appiumUrl = DEFAULT_APPIUM_URL;
        }
        try {
            driver = new IOSDriver(new URL(appiumUrl), capabilities);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return driver;
    }

    public static DesiredCapabilities iosNativeAppCapabilities() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.APP, ConfigurationManager.getTestAppAbsolutePath());
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
        caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, ConfigurationManager.getIosVersion());
        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, ConfigurationManager.getDevice());
        // temporarily replaced with RunConsoleCommand.rotateDevice(), as the device can not be rotated using capabilities in Xcode 16
        //caps.setCapability(MobileCapabilityType.ORIENTATION, ScreenOrientation.LANDSCAPE);
        caps.setCapability(IOSMobileCapabilityType.SHOW_XCODE_LOG, false);
        caps.setCapability(MobileCapabilityType.LANGUAGE, "EN");
        caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, ConfigurationManager.getMobileSessionTimeoutVariable());
        caps.setCapability(IOSMobileCapabilityType.USE_NEW_WDA, false);
        caps.setCapability(IOSMobileCapabilityType.USE_PREBUILT_WDA, false);
        caps.setCapability(IOSMobileCapabilityType.INTER_KEY_DELAY, 200);
        caps.setCapability(IOSMobileCapabilityType.SEND_KEY_STRATEGY, "grouped");
        caps.setCapability("wdaEventloopIdleDelay", 3);
        caps.setCapability("locationServicesAuthorized", false);
        caps.setCapability("locationServicesEnabled", false);
        caps.setCapability("includeSafariInWebviews", true);
        caps.setCapability("safariGarbageCollect", true);
        caps.setCapability(IOSMobileCapabilityType.CONNECT_HARDWARE_KEYBOARD, false);
        caps.setCapability("useJSONSource", true);
        caps.setCapability("forceMjsonwp", true);
        caps.setCapability("shouldTerminateApp", true);
        if(System.getProperty(TEST_APP_NAME_VARIABLE) != null){
            caps.setCapability("waitForQuiescence", true);
            caps.setCapability("reduceMotion", true);
            caps.setCapability("waitForIdleTimeout", 180);
        } else {
            caps.setCapability("waitForQuiescence", false);
        }

        String locationServicesEnabled = System.getProperty("locationServicesEnabled");
        if (locationServicesEnabled == null || locationServicesEnabled.equals("false")) {
            caps.setCapability("locationServicesEnabled", false);
        }

        String realDeviceUDID = System.getProperty("UDID");
        if (realDeviceUDID != null) {
            caps.setCapability(MobileCapabilityType.UDID, realDeviceUDID);
        }

        String fullReset = System.getProperty("fullReset");
        if ((fullReset != null) && fullReset.equalsIgnoreCase("SYNC")) {
            caps.setCapability(MobileCapabilityType.NO_RESET, true);
            caps.setCapability(MobileCapabilityType.CLEAR_SYSTEM_FILES, true);
            caps.setCapability(MobileCapabilityType.FULL_RESET, false);

        } else if((fullReset != null) && fullReset.equalsIgnoreCase("TRUE")){
            caps.setCapability(MobileCapabilityType.NO_RESET, false);
            caps.setCapability(MobileCapabilityType.CLEAR_SYSTEM_FILES, true);
            caps.setCapability(MobileCapabilityType.FULL_RESET, true);
        } else {
            caps.setCapability(MobileCapabilityType.NO_RESET, false);
            caps.setCapability(MobileCapabilityType.CLEAR_SYSTEM_FILES, true);
            caps.setCapability(MobileCapabilityType.FULL_RESET, false);
        }

        return caps;
    }
}
