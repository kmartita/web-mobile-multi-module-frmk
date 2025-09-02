package project.web.pages;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import project.tools.pageobject.htmlelements.ITextInput;
import project.tools.pageobject.htmlelements.Input;
import project.tools.pageobject.AbstractWebPage;
import project.tools.utils.WebElementUtils;

public class HomePage extends AbstractWebPage {

    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    public ITextInput passwordField() {
        return new Input(passwordField, "Password field");
    }

    public boolean isOpened(){
        Allure.step("Check is \"Password field\" present");
        return passwordField().isShown();
    }

    @Override
    protected void waitUntilLoaded() {
        tryWaitUntil(() -> WebElementUtils.isElementClickable(usernameField));
    }
}
