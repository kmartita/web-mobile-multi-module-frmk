package project.tools.pageobject;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import project.tools.utils.AdditionalConditions;
import project.tools.utils.WebElementUtils;

import java.time.Duration;
import java.util.function.Supplier;

import static project.tools.utils.WaitUtil.DEFAULT_TIMEOUT_TO_WAIT;
import static project.tools.utils.WaitUtil.SHORT_TIMEOUT_TO_WAIT;

public abstract class AbstractPage {

    protected AbstractPage() {
        PageFactory.initElements(new AppiumFieldDecorator(AbstractApp.getInstanceDriver()), this);
        waitUntilLoaded();
    }

    protected WebDriverWait getWebDriverWait(long timeoutInSeconds) {
        return (WebDriverWait) new WebDriverWait(AbstractApp.getInstanceDriver(), Duration.ofSeconds(DEFAULT_TIMEOUT_TO_WAIT))
                .pollingEvery(Duration.ofMillis(500))
                .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    protected WebDriverWait getWebDriverWait() {
        return getWebDriverWait(DEFAULT_TIMEOUT_TO_WAIT);
    }

    protected void shortWaitUntil(Supplier<Boolean> condition) {
        getWebDriverWait(SHORT_TIMEOUT_TO_WAIT).until(AdditionalConditions.isTrue(condition));
    }

    protected void waitUntil(Supplier<Boolean> condition) {
        getWebDriverWait().until(AdditionalConditions.isTrue(condition));
    }

    protected void waitUntil(Supplier<Boolean> condition, int seconds) {
        getWebDriverWait(seconds).until(AdditionalConditions.isTrue(condition));
    }

    protected void tryWaitUntil(Supplier<Boolean> condition) {
        try {
            waitUntil(condition);
        } catch (TimeoutException e) {
            // Just return control to the calling code
        }
    }

    protected void tryWaitUntil(Supplier<Boolean> condition, int seconds) {
        try {
            waitUntil(condition, seconds);
        } catch (TimeoutException e) {
            // Just return control to the calling code
        }
    }

    protected abstract void waitUntilLoaded();

    // ************** ALERT HANDLING **************** //
    @Step
    public void acceptAlert() {
        tryWaitUntil(WebElementUtils::isAlertPresent);
        getAlert().accept();
    }

    @Step
    public void dismissAlert() {
        getAlert().dismiss();
    }

    @Step
    public String getAlertText() {
        shortWaitUntil(() -> !AbstractApp.getInstanceDriver().switchTo().alert().getText().isEmpty());
        return getAlert().getText();
    }

    private Alert getAlert() {
        return AbstractApp.getInstanceDriver().switchTo().alert();
    }

    @Step
    public Alert waitForAlert(int seconds) {
        return getWebDriverWait(seconds).until(ExpectedConditions.alertIsPresent());
    }

    @Step
    public boolean isAlertShown() {
        try {
            getAlert();
            return true;
        } catch (NoAlertPresentException exception) {
            return false;
        } catch (WebDriverException exception) {
            if (exception.getMessage().contains("An attempt was made to operate on a modal dialog when one was not open.")) {
                return false;
            } else {
                throw exception;
            }
        }
    }
}
