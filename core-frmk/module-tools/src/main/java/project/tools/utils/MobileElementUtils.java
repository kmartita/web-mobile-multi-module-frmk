package project.tools.utils;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import project.tools.pageobject.AbstractMobileApp;

import java.util.List;

public final class MobileElementUtils {

    private MobileElementUtils() {}

    private boolean isHideKeyBoardButtonShown() {
        try {
            return getKeyboardButtons().stream().anyMatch(WebElementUtils::isElementShown);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    private List<WebElement> getKeyboardButtons() {
        return AbstractMobileApp.getAppiumDriver().findElements(AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeButton' and name == 'Hide keyboard'"));
    }

}
