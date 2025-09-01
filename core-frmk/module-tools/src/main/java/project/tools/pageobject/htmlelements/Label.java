package project.tools.pageobject.htmlelements;

import org.openqa.selenium.WebElement;
import project.tools.pageobject.AbstractUiElement;

public class Label extends AbstractUiElement implements ILabel {

    public Label(WebElement wrappedElement, String name) {
        super(wrappedElement, name);
    }

    public Label(WebElement element) {
        super(element);
    }

}
