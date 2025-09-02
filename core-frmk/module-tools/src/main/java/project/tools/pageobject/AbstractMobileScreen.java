package project.tools.pageobject;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;

public abstract class AbstractMobileScreen extends AbstractPage {

    public void switchToNativeView() {
        Allure.step("Switch to 'native' context");
        IOSDriver appiumDriver = AbstractMobileApp.getIOSDriver();
        appiumDriver.context("NATIVE_APP");
    }

    public WebElement findElementByAccessibility(String accessibilityId) {
        return AbstractMobileApp.getIOSDriver().findElement(AppiumBy.accessibilityId(accessibilityId));
    }
}
