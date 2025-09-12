package project.tools.pageobject.htmlelements;

import project.tools.pageobject.UiElement;

public interface ITextInput extends UiElement {

    void type(String text);

    void clear();

    default void clearAndType(String text) {
        clear();
        type(text);
    }

    String getValue();

    void setInputValue(String keysToSend);
}
