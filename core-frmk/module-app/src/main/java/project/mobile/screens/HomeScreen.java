package project.mobile.screens;

import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import project.tools.pageobject.htmlelements.ILabel;
import project.tools.pageobject.htmlelements.Label;
import project.tools.pageobject.AbstractMobileScreen;

public class HomeScreen extends AbstractMobileScreen {

    @iOSXCUITFindBy(iOSNsPredicate = "type = 'XCUIElementTypeStaticText' and label == 'Hello, world!'")
    private WebElement helloWord;

    @Step
    public ILabel screenTitle() {
        return new Label(helloWord, "Title");
    }

    @Step("isOpened")
    public boolean isOpened() {
        return screenTitle().isShown();
    }

    @Override
    protected void waitUntilLoaded() { }
}
