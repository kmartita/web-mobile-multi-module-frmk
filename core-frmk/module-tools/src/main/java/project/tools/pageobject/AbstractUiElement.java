package project.tools.pageobject;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.Widget;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import project.tools.utils.AdditionalConditions;

import java.time.Duration;
import java.util.function.Supplier;

import project.tools.utils.WebElementUtils;

import static project.tools.utils.WaitUtil.DEFAULT_TIMEOUT_TO_WAIT;

public abstract class AbstractUiElement extends Widget implements GuiElement {

    protected String name;
    private FluentWait<WebDriver> waiter;

    public AbstractUiElement(WebElement element) {
        super(element);
        PageFactory.initElements(new AppiumFieldDecorator(element), this);
    }

    public AbstractUiElement(WebElement wrappedElement, String name) {
        this(wrappedElement);
        this.name = name;
    }

    protected FluentWait<WebDriver> getWebDriverWait() {
        if (waiter == null) {
            waiter = new FluentWait<>(getWrappedDriver());
        }
        return waiter.withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT_TO_WAIT))
                .ignoring(StaleElementReferenceException.class);
    }

    protected void waitUntil(Supplier<Boolean> condition) {
        getWebDriverWait().until(AdditionalConditions.isTrue(condition));
    }

    protected void tryWaitUntil(Supplier<Boolean> condition) {
        try {
            waitUntil(condition);
        } catch (TimeoutException e) {
            // Just return control to the calling code
        }
    }

    @Override
    public boolean exists() {
        try {
            getWrappedElement().isDisplayed();
            return true;
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }

    @Override
    public boolean isShown() {
        return WebElementUtils.isElementShown(getWrappedElement());
    }

    @Override
    public boolean isEnabled() {
        return WebElementUtils.isElementEnabled(getWrappedElement());
    }

    @Override
    public void click() {
        try {
            getWebDriverWait().until(ExpectedConditions.elementToBeClickable(getWrappedElement()));
        } catch (TimeoutException | NoSuchElementException e) {
            throw new IllegalStateException("\n" + this.name + " should be clickable before click.\n", e);
        }

        try {
            WebElementUtils.clickOn(getWrappedElement());
        } catch (WebDriverException e) {
            WebElementUtils.clickJS(getWrappedElement());
        }
    }

    @Override
    public String getText() {
        tryWaitUntil(() -> WebElementUtils.isElementShown(getWrappedElement()));
        return WebElementUtils.getTextFrom(getWrappedElement());
    }

    @Override
    public String getAttribute(String attributeName) {
        tryWaitUntil(() -> WebElementUtils.isElementShown(getWrappedElement()));
        return getWrappedElement().getAttribute(attributeName);
    }

    @Override
    public String getCssValue(String cssValue) {
        tryWaitUntil(() -> WebElementUtils.isElementShown(getWrappedElement()));
        return getWrappedElement().getCssValue(cssValue);
    }

}
