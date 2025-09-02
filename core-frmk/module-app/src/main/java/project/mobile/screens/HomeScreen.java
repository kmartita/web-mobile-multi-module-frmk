package project.mobile.screens;

import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebElement;
import project.tools.pageobject.htmlelements.ILabel;
import project.tools.pageobject.htmlelements.Label;
import project.tools.pageobject.AbstractMobileScreen;

public class HomeScreen extends AbstractMobileScreen {

    @iOSXCUITFindBy(iOSNsPredicate = "type = 'XCUIElementTypeStaticText' and label == 'Hello, world!'")
    private WebElement helloWord;

    public ILabel screenGreeting() {
        return new Label(helloWord, "'Hello, world!' label");
    }

    public boolean isOpened() {
        Allure.step("Check is \"Hello, world!\" label present");
        return screenGreeting().isShown();
    }

    @Override
    protected void waitUntilLoaded() { switchToNativeView(); }
}
