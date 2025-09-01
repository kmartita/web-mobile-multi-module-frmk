package project.tools.utils;

import io.appium.java_client.AppiumBy;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.WebElement;
import project.tools.pageobject.AbstractMobileApp;

import java.util.List;

import static io.appium.java_client.touch.offset.ElementOption.element;

@UtilityClass
public final class MobileElementUtils {

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
