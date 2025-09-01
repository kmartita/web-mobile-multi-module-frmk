package project.tools.drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import project.tools.ConfigurationManager;
import project.tools.Platform;
import project.tools.reports.AllureUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.lang.String.*;
import static java.lang.Thread.currentThread;

public class Drivers {

    private static final Map<Long, WebDriver> DRIVERS_PER_THREAD = new ConcurrentHashMap<>(4);
    private static final Collection<Thread> ALL_DRIVERS_THREADS = new ConcurrentLinkedQueue<>();

    private WebDriver driver;

    public WebDriver getBrowserDriver() {
        if (driver != null)
            return driver;
        return driver = startNewInstance();
    }

    public WebDriver getMobileDriver() {
        if (driver != null)
            return driver;
        return driver = startNewInstance();
    }

    private WebDriver startNewInstance() {
        Platform platform = ConfigurationManager.getPlatform();
        WebDriver webDriver;

        switch (platform) {
            case IOS:
                webDriver = MobileDriver.startIosDriver();
                break;
            case THIS_OS:
                webDriver = BrowserDriver.startBrowserDriver();
                break;
            default:
                throw new RuntimeException(format("Unsupported platform: %s", platform));
        }
        rememberDriver(webDriver);
        return webDriver;
    }

    private static void rememberDriver(WebDriver webDriver) {
        DRIVERS_PER_THREAD.put(currentThread().getId(), webDriver);
        ALL_DRIVERS_THREADS.add(currentThread());
    }

    public void quit() {
        try {
            if (this.driver != null)
                this.driver.quit();
        } finally {
            this.driver = null;
        }
    }

    private static void quitDriver(Thread thread) {
        long threadId = thread.getId();
        ALL_DRIVERS_THREADS.remove(thread);
        WebDriver webdriver = DRIVERS_PER_THREAD.remove(threadId);

        if (webdriver != null) {
            try {
                System.out.printf("Quit '%s' for thread: '%s'%n", webdriver, threadId);
                webdriver.quit();
            } catch (WebDriverException e) {
                AllureUtils.step(format("Failed to quit driver: %s", e.getMessage()));
            }
        }
    }

    public static void quitAllDrivers() {
        ALL_DRIVERS_THREADS.forEach(Drivers::quitDriver);
    }

}
