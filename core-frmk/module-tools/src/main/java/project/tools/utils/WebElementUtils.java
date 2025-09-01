package project.tools.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.util.Strings;
import project.tools.pageobject.AbstractApp;

import java.time.Duration;

import static io.appium.java_client.pagefactory.utils.WebDriverUnpackUtility.unpackWebDriverFromSearchContext;
import static project.tools.TagsAndAttributes.VALUE_ATTR;
import static project.tools.utils.WaitUtil.DEFAULT_TIMEOUT_TO_WAIT;

@UtilityClass
public final class WebElementUtils {

    private WebDriverWait webDriverWait(WebElement element) {
        WebDriverWait waiter = new WebDriverWait(unpackWebDriverFromSearchContext(element), Duration.ofSeconds(DEFAULT_TIMEOUT_TO_WAIT));
        waiter.ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
        return waiter;
    }

    public static boolean isElementShown(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public boolean isElementPresent(WebElement element) {
        try {
            element.getText();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isElementEnabled(WebElement element) {
        try {
            return element.isEnabled();
        } catch (NoSuchElementException e) {
            return false;
        } catch (StaleElementReferenceException e) {
            return isElementEnabled(element);
        }
    }

    public boolean isElementClickable(WebElement element) {
        try {
            webDriverWait(element).until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAlertPresent() {
        try {
            AbstractApp.getInstanceDriver().switchTo().alert();
            return true;
        } catch (NoAlertPresentException ex) {
            return false;
        }
    }

    public String getValueOf(WebElement element) {
        String value;
        if (element.getAttribute(VALUE_ATTR) == null) {
            value = StringUtils.EMPTY;
        } else value = element.getAttribute(VALUE_ATTR);
        return value;
    }

    public void clear(WebElement element) {
        element.clear();
    }

    public void sendKeysTo(WebElement element, String keysToSend) {
        element.sendKeys(keysToSend);
    }

    public void clearAndSendKeysTo(WebElement element, String keysToSend) {
        clear(element);
        sendKeysTo(element, keysToSend);
    }

    public void clickOn(WebElement element) {
        try {
            element.click();
        } catch (WebDriverException e) {
            scrollIntoViewByCoordinate(element);
            element.click();
        }
    }

    private JavascriptExecutor getJavascriptExecutor(WebElement element) {
        return (JavascriptExecutor) unpackWebDriverFromSearchContext(element);
    }

    private void scrollIntoViewByCoordinate(WebElement e) {
        getJavascriptExecutor(e).executeScript("arguments[0].scrollIntoView(true); scrollBy(0,-100);", e);
    }

    public void clickJS(WebElement element) {
        getJavascriptExecutor(element).executeScript("arguments[0].click();", element);
    }

    public String getTextFrom(WebElement element) {
        String text = element.getText();
        return Strings.isNullOrEmpty(text) ? StringUtils.EMPTY : text;
    }
}
