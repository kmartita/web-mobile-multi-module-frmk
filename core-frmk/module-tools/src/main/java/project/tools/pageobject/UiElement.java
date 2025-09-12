package project.tools.pageobject;

import org.openqa.selenium.WrapsElement;

public interface UiElement extends WrapsElement {

    boolean exists();

    boolean isShown();

    default boolean isHidden() {
        return !isShown();
    }

    boolean isEnabled();

    default boolean isDisabled() {
        return !isEnabled();
    }

    void click();

    String getText();

    String getAttribute(String attributeName);

    String getCssValue(String cssValue);
}
