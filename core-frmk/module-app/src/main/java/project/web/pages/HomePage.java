package project.web.pages;

import io.qameta.allure.Allure;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import project.tools.pageobject.htmlelements.ILabel;
import project.tools.pageobject.AbstractWebPage;
import project.tools.pageobject.htmlelements.Label;
import project.tools.utils.WebElementUtils;

public class HomePage extends AbstractWebPage {

    @FindBy(css = "img[alt='Selenium Online Training']")
    private WebElement logo;

    public ILabel logo() {
        return new Label(logo, "'Selenium Online Training' img");
    }

    public boolean isOpened(){
        Allure.step("Check is \"Selenium Online Training\" image present");
        return logo().isShown();
    }

    @Override
    protected void waitUntilLoaded() {
        tryWaitUntil(() -> WebElementUtils.isElementClickable(logo));
    }
}
