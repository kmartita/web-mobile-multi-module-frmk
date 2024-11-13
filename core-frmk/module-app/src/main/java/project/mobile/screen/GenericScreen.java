package project.mobile.screen;

import io.qameta.allure.Step;
import project.tools.pageobject.mobile.AbstractMobileScreen;

public abstract class GenericScreen extends AbstractMobileScreen {

    @Step
    public boolean isNotificationAlertShown() {
        return isAlertShown() && getAlertText().contains("Would Like to Send You Notifications");
    }

    @Step
    public boolean isAllowLocationAlertShown() {
        return isAlertShown() && getAlertText().contains("your location?");
    }

    @Step
    public void allowLocationAccessAlert() {
        switchToNativeView();
        findElementByAccessibility("Allow While Using App").click();
    }
}
