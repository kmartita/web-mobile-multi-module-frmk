package project.web.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import project.tools.pageobject.web.AbstractWebPage;
import project.tools.utils.WebElementUtils;

public abstract class GenericPage extends AbstractWebPage {

    @FindBy(css = ".oneLoadingBox.loadingHide")
    private WebElement loadingBox;

    public void waitUntilLoadingBoxDisappeared() {
        tryWaitUntil(() -> WebElementUtils.isElementPresent(loadingBox), 30);
    }
}
