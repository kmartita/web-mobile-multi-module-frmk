package project.tools.pageobject;

import io.qameta.allure.Allure;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class AbstractWebPage extends AbstractPage {

    public final String getCurrentUrl() {
        Allure.step("Read current URL");
        Allure.attachment("data.txt", "The current URL.");
        return "URL:\n" + AbstractApp.getDriver().getCurrentUrl();
    }

    public final String getPageSource() {
        return "Page Source:\n" + AbstractApp.getDriver().getPageSource();
    }

    public final String getBrowserTitle() {
        return AbstractApp.getDriver().getTitle();
    }

    public final void refreshCurrentPage() {
        AbstractApp.getDriver().navigate().refresh();
        tryWaitUntil(this::isPageStateReady);
    }

    public boolean isPageStateReady() {
        return getJavascriptExecutor().executeScript("return document.readyState").equals("complete");
    }


    // ************** WINDOW & TAB HANDLING **************** //
    public final String getWindowHandle() {
        return AbstractApp.getDriver().getWindowHandle();
    }

    public Set<String> getWindowHandles() {
        return AbstractApp.getDriver().getWindowHandles();
    }

    public final void closeCurrentWindow() {
        AbstractApp.getDriver().close();
    }

    public void switchToWindow(String nameOrHandle) {
        AbstractApp.getDriver().switchTo().window(nameOrHandle);
    }

    public void switchToFirstWindow(){
        List<String> windows = new ArrayList<>(getWindowHandles());
        switchToWindow(windows.get(0));
    }

    public void switchToLastWindow() {
        tryWaitUntil(() -> getWindowHandles().size() > 1, 8);
        List<String> windows = new ArrayList<>(getWindowHandles());
        switchToWindow(windows.get(windows.size() - 1));
    }

    public void openNewTab() {
        getJavascriptExecutor().executeScript("window.open()");
    }

    public void switchToNextTab() {
        List<String> windowHandles = new ArrayList<>(getWindowHandles());

        if (windowHandles.size() == 1)
            switchToWindow(windowHandles.get(0));

        int index = windowHandles.indexOf(getWindowHandle());
        int nextIndex = ++index > windowHandles.size() - 1 ? 0 : index;

        switchToWindow(windowHandles.get(nextIndex));
    }

    public void switchToPreviousTab() {
        List<String> windowHandles = new ArrayList<>(getWindowHandles());

        if (windowHandles.size() == 1)
            switchToWindow(windowHandles.get(0));

        int index = windowHandles.indexOf(getWindowHandle());
        int prevIndex = --index < 0 ? windowHandles.size() - 1 : index;

        switchToWindow(windowHandles.get(prevIndex));
    }

    public void switchToAnotherTab() {
        String currentWindowHandle = getWindowHandle();
        System.out.println("-" + currentWindowHandle);

        for (String windowHandle : getWindowHandles())
            if (!windowHandle.equals(currentWindowHandle)) {
                switchToWindow(windowHandle);
                System.out.println("--" + windowHandle);
            }
    }


    // ************** FRAMES HANDLING **************** //
    protected void switchToFrame(int index) {
        AbstractApp.getDriver().switchTo().frame(index);
    }

    protected void switchToFrame(String nameOrId) {
        AbstractApp.getDriver().switchTo().frame(nameOrId);
    }

    protected void switchToFrame(WebElement frameElement) {
        getWebDriverWait().until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
    }

    protected void switchToDefaultContent() {
        AbstractApp.getDriver().switchTo().defaultContent();
    }



    public JavascriptExecutor getJavascriptExecutor() {
        return (JavascriptExecutor) AbstractApp.getDriver();
    }
}
