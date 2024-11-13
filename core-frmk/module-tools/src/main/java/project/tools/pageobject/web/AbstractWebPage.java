package project.tools.pageobject.web;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import project.tools.pageobject.AbstractApp;
import project.tools.pageobject.AbstractPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class AbstractWebPage extends AbstractPage {

    @FindBy(css = "body")
    private WebElement body;

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

    @Step("Checks if page contains text [{0}]")
    public final boolean contains(String textToFindOnPage) {
        String allTextOnPage = body.getText();
        return allTextOnPage.contains(textToFindOnPage);
    }

    @Step
    public final String getWindowHandle() {
        return AbstractApp.getInstanceDriver().getWindowHandle();
    }

    @Step
    public final void closeCurrentWindow() {
        AbstractApp.getInstanceDriver().close();
    }

    @Step
    public void closeWindowsExceptOfCurrent() {
        String currentHandle = AbstractApp.getInstanceDriver().getWindowHandle();

        for(String handle : AbstractApp.getInstanceDriver().getWindowHandles()) {
            if (!handle.equals(currentHandle)) {
                AbstractApp.getInstanceDriver().switchTo().window(handle);
                AbstractApp.getInstanceDriver().close();
            }
        }
        AbstractApp.getInstanceDriver().switchTo().window(currentHandle);
    }

    @Step
    public final void refreshCurrentPage() {
        AbstractApp.getInstanceDriver().navigate().refresh();
        waitUntilRequestsHaveFinished();
        tryWaitUntil(this::isPageStateReady);
    }

    public void waitUntilRequestsHaveFinished() {
        AbstractApp.getInstanceDriver().manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
        getJavascriptExecutor().executeAsyncScript("var callback = arguments[arguments.length - 1]; " +
                                                   "var xhr = new XMLHttpRequest(); xhr.open('POST', '/Ajax_call', true); " +
                                                   "xhr.onreadystatechange = function() { if (xhr.readyState == 4) {callback(xhr.responseText);}}; " +
                                                   "xhr.send();");
    }

    public boolean isPageStateReady() {
        return getJavascriptExecutor().executeScript("return document.readyState").equals("complete");
    }

    public JavascriptExecutor getJavascriptExecutor() {
        return (JavascriptExecutor) AbstractApp.getInstanceDriver();
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

    @Step
    public void switchToAnotherTab() {
        String currentWindowHandle = AbstractApp.getInstanceDriver().getWindowHandle();
        System.out.println("-" + currentWindowHandle);
        for (String windowHandle : AbstractApp.getInstanceDriver().getWindowHandles())
            if (!windowHandle.equals(currentWindowHandle)) {
                AbstractApp.getInstanceDriver().switchTo().window(windowHandle);
                System.out.println("--" + windowHandle);
            }
    }

    @Step
    public void switchToNextTab() {
        List<String> windowHandles = new ArrayList<>();
        windowHandles.addAll(AbstractApp.getInstanceDriver().getWindowHandles());

        if (windowHandles.size() == 1)
            AbstractApp.getInstanceDriver().switchTo().window(windowHandles.get(0));

        int index = windowHandles.indexOf(AbstractApp.getInstanceDriver().getWindowHandle());
        int nextIndex = ++index > windowHandles.size() - 1 ? 0 : index;

        AbstractApp.getInstanceDriver().switchTo().window(windowHandles.get(nextIndex));
    }

    @Step
    public void switchToPreviousTab() {
        List<String> windowHandles = new ArrayList<>();
        windowHandles.addAll(AbstractApp.getInstanceDriver().getWindowHandles());

        if (windowHandles.size() == 1)
            AbstractApp.getInstanceDriver().switchTo().window(windowHandles.get(0));

        int index = windowHandles.indexOf(AbstractApp.getInstanceDriver().getWindowHandle());
        int prevIndex = --index < 0 ? windowHandles.size() - 1 : index;

        AbstractApp.getInstanceDriver().switchTo().window(windowHandles.get(prevIndex));
    }

    public void switchToWindow(String nameOrHandle) {
        AbstractApp.getInstanceDriver().switchTo().window(nameOrHandle);
    }

    @Step
    public Set<String> getWindowHandles() {
        return AbstractApp.getInstanceDriver().getWindowHandles();
    }

    @Step
    public void openNewTab() {
        getJavascriptExecutor().executeScript("window.open()");
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
    public void closeCurrentWindowAndSwitchToFirstWindow() {
        this.closeCurrentWindow();
        switchToFirstWindow();
    }
}
