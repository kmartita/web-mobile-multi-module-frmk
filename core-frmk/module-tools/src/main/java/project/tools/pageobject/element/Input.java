package project.tools.pageobject.element;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import project.tools.pageobject.AbstractGuiElement;
import project.tools.utils.WebElementUtils;

import java.util.Optional;

import static project.tools.TagsAndAttributes.VALUE_ATTR;

public class Input extends AbstractGuiElement implements ITextInput {

    public Input(WebElement element) {
        super(element);
    }

    public Input(WebElement element, String name) {
        super(element, name);
    }

    @Override
    public void type(String text) {
        waitUntil(() -> getWrappedElement().isDisplayed());
        getWrappedElement().click();
        getWrappedElement().sendKeys(text);
    }

    @Override
    public void clear() {
        try {
            getWebDriverWait().until(ExpectedConditions.elementToBeClickable(getWrappedElement()));
        } catch (TimeoutException | NoSuchElementException e) {
            throw new IllegalStateException("\n" + super.name + " should be shown before typing.\n", e);
        }
        getWrappedElement().clear();
    }

    @Override
    public void clearAndType(String text) {
        try {
            getWebDriverWait().until(ExpectedConditions.elementToBeClickable(getWrappedElement()));
        } catch (TimeoutException | NoSuchElementException e) {
            throw new IllegalStateException("\n" + super.name + " should be shown before typing.\n", e);
        }
        ITextInput.super.clearAndType(text);
    }

    @Override
    public String getValue() {
        return Optional.of(WebElementUtils.getValueOf(getWrappedElement())).orElse(StringUtils.EMPTY);
    }

    protected void setValue(WebElement element, String keysToSend) {
        WebElementUtils.clearAndSendKeysTo(element, keysToSend);
        try {
            getWebDriverWait().until(ExpectedConditions.attributeToBe(element, VALUE_ATTR, keysToSend));
        } catch (TimeoutException e) {
            throw new TimeoutException(String.format("Value '%s' was not set to input field.\n", keysToSend), e);
        }
    }

    @Override
    public void setInputValue(String text) {
        setValue(getWrappedElement(), text);
    }
}
