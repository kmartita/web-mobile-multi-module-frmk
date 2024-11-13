package project.mobile.screen;

import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import project.tools.pageobject.element.ILabel;
import project.tools.pageobject.element.Label;

public class LoginScreen extends GenericScreen {

    @iOSXCUITFindBy(iOSNsPredicate = "type = 'XCUIElementTypeStaticText' and label == 'Log In'")
    private WebElement screenTitle;

    @Step
    public ILabel screenTitle() {
        switchToNativeView();
        return new Label(screenTitle, "Title");
    }

    @Step
    public boolean isOpened() {
        return screenTitle().isShown();
    }

    @Override
    protected void waitUntilLoaded() {
         if(isNotificationAlertShown())
            acceptAlert();
        if(isAllowLocationAlertShown())
            allowLocationAccessAlert(); // Doesn't work. By manually
        if(isNotificationAlertShown())
            acceptAlert();
        if(isAlertShown())
            acceptAlert();
    }
}
