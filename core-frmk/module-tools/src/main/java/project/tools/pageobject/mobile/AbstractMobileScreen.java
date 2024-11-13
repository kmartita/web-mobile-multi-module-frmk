package project.tools.pageobject.mobile;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.WebElement;
import project.tools.pageobject.AbstractApp;
import project.tools.pageobject.AbstractPage;
import project.tools.utils.WebElementUtils;
import project.tools.utils.MobileElementUtils;

public abstract class AbstractMobileScreen extends AbstractPage {

    @Getter
    @iOSXCUITFindBy(accessibility = "PopoverDismissRegion")
    private WebElement popoverDismissRegion;

    public boolean isPopoverShown() {
        return WebElementUtils.isElementPresent(popoverDismissRegion);
    }

    public void dismissPopover() {
        if (isPopoverShown()) {
            MobileElementUtils.hideKeyboard();
            tryShortWaitUntil(() -> WebElementUtils.isClickable(getPopoverDismissRegion()));
            MobileElementUtils.tap(getPopoverDismissRegion());
        }
    }

    @Step
    public void switchToNativeView() {
        IOSDriver appiumDriver = (IOSDriver) AbstractApp.getInstanceDriver();
        appiumDriver.context("NATIVE_APP");
    }

    public WebElement findElementByAccessibility(String accessibilityId) {
        return AbstractMobileApp.getIOSDriver().findElement(AppiumBy.accessibilityId(accessibilityId));
    }
}
