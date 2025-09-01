package project.tools.pageobject.htmlelements;

import project.tools.pageobject.GuiElement;

public interface ITextInput extends GuiElement {

    void type(String text);

    void clear();

    default void clearAndType(String text) {
        clear();
        type(text);
    }

    String getValue();

    void setInputValue(String keysToSend);
}
