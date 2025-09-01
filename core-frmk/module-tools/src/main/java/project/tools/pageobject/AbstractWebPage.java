package project.tools.pageobject;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class AbstractWebPage extends AbstractPage {

    @Attachment("Current URL")
    @Step("Reads current URL")
    public final String getCurrentUrl() {
        return AbstractApp.getInstanceDriver().getCurrentUrl();
    }

    @Attachment("Page Source")
    @Step("Reads page source")
    public final String getPageSource() {
        return "Page Source:\n" + AbstractApp.getInstanceDriver().getPageSource();
    }

    @Attachment("Browser Title")
    @Step("Reads title in the browser title bar")
    public final String getBrowserTitle() {
        return AbstractApp.getInstanceDriver().getTitle();
    }

    @Step
    public final void refreshCurrentPage() {
        AbstractApp.getInstanceDriver().navigate().refresh();
        tryWaitUntil(this::isPageStateReady);
    }

    public boolean isPageStateReady() {
        return getJavascriptExecutor().executeScript("return document.readyState").equals("complete");
    }


    // ************** WINDOW & TAB HANDLING **************** //
    @Step
    public final String getWindowHandle() {
        return AbstractApp.getInstanceDriver().getWindowHandle();
    }

    @Step
    public Set<String> getWindowHandles() {
        return AbstractApp.getInstanceDriver().getWindowHandles();
    }

    @Step
    public final void closeCurrentWindow() {
        AbstractApp.getInstanceDriver().close();
    }

    public void switchToWindow(String nameOrHandle) {
        AbstractApp.getInstanceDriver().switchTo().window(nameOrHandle);
    }

    @Step
    public void switchToFirstWindow(){
        List<String> windows = new ArrayList<>(getWindowHandles());
        switchToWindow(windows.get(0));
    }

    @Step
    public void switchToLastWindow() {
        tryWaitUntil(() -> getWindowHandles().size() > 1, 8);
        List<String> windows = new ArrayList<>(getWindowHandles());
        switchToWindow(windows.get(windows.size() - 1));
    }

    @Step
    public void openNewTab() {
        getJavascriptExecutor().executeScript("window.open()");
    }

    @Step
    public void switchToNextTab() {
        List<String> windowHandles = new ArrayList<>(getWindowHandles());

        if (windowHandles.size() == 1)
            switchToWindow(windowHandles.get(0));

        int index = windowHandles.indexOf(getWindowHandle());
        int nextIndex = ++index > windowHandles.size() - 1 ? 0 : index;

        switchToWindow(windowHandles.get(nextIndex));
    }

    @Step
    public void switchToPreviousTab() {
        List<String> windowHandles = new ArrayList<>(getWindowHandles());

        if (windowHandles.size() == 1)
            switchToWindow(windowHandles.get(0));

        int index = windowHandles.indexOf(getWindowHandle());
        int prevIndex = --index < 0 ? windowHandles.size() - 1 : index;

        switchToWindow(windowHandles.get(prevIndex));
    }

    @Step
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
        AbstractApp.getInstanceDriver().switchTo().frame(index);
    }

    protected void switchToFrame(String nameOrId) {
        AbstractApp.getInstanceDriver().switchTo().frame(nameOrId);
    }

    protected void switchToFrame(WebElement frameElement) {
        getWebDriverWait().until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
    }

    @Step
    protected void switchToDefaultContent() {
        AbstractApp.getInstanceDriver().switchTo().defaultContent();
    }



    public JavascriptExecutor getJavascriptExecutor() {
        return (JavascriptExecutor) AbstractApp.getInstanceDriver();
    }
}
