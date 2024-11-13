package project.web.page;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import project.tools.pageobject.element.ITextInput;
import project.tools.pageobject.element.Input;
import project.tools.utils.WebElementUtils;

public class LoginPage extends GenericPage{

    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @Step
    public ITextInput passwordField() {
        return new Input(passwordField, "Password field");
    }

    @Step
    public boolean isOpened(){
        return passwordField().isShown();
    }

    @Override
    protected void waitUntilLoaded() {
        tryWaitUntil(() -> WebElementUtils.isClickable(usernameField));
    }
}
