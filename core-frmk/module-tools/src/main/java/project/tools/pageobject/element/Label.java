package project.tools.pageobject.element;

import org.openqa.selenium.WebElement;
import project.tools.pageobject.AbstractGuiElement;

public class Label extends AbstractGuiElement implements ILabel {

    public Label(WebElement wrappedElement, String name) {
        super(wrappedElement, name);
    }

    public Label(WebElement element) {
        super(element);
    }

}
