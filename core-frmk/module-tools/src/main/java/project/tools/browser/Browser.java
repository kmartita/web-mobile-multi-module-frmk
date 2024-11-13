package project.tools.browser;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import project.tools.ConfigurationManager;
import project.tools.Platform;
import project.tools.reports.AllureUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.lang.String.*;
import static java.lang.Thread.currentThread;
import static project.tools.EnvManager.REMOTE_URL;

public class Browser {

    protected static final boolean IS_REMOTE = ConfigurationManager.isRemote();
    private static final Map<Long, WebDriver> DRIVERS_PER_THREAD = new ConcurrentHashMap<>(4);
    private static final Collection<Thread> ALL_DRIVERS_THREADS = new ConcurrentLinkedQueue<>();

    private WebDriver driver;

    public WebDriver getDriver() {
        if (driver != null)
            return driver;
        return driver = startNewInstance();
    }

    public WebDriver getMobileDriver() {
        if (driver != null)
            return driver;
        return driver = startNewInstance();
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

    public boolean isOpened() {
        return this.driver != null;
    }

    private static void rememberDriver(WebDriver webDriver) {
        DRIVERS_PER_THREAD.put(currentThread().getId(), webDriver);
        ALL_DRIVERS_THREADS.add(currentThread());
    }

    private WebDriver startDesktopWebBrowser() {
        BrowserType driverType = ConfigurationManager.getDriverType();
        MutableCapabilities capabilities = driverType.getCapabilities();

        try {
            if (IS_REMOTE)
                return startRemoteDriver(capabilities);

            return driverType.getWebDriver(capabilities);
        } catch (Exception e) {
            throw new RuntimeException(format("Failed to start web driver for: %s", driverType), e);
        }
    }

    private WebDriver startNewInstance() {
        Platform platform = ConfigurationManager.getPlatform();
        WebDriver webDriver;

        switch (platform) {
            case IOS:
                webDriver = MobileDriver.startIos();
                break;
            case THIS_OS:
                webDriver = startDesktopWebBrowser();
                break;
            default:
                throw new RuntimeException(format("Unsupported platform: %s", platform));
        }
        rememberDriver(webDriver);
        return webDriver;
    }

    private RemoteWebDriver startRemoteDriver(MutableCapabilities desiredCapabilities) {
        RemoteWebDriver remoteDriver;
        try {
            remoteDriver = new RemoteWebDriver(new URL(REMOTE_URL), desiredCapabilities);
            remoteDriver.setFileDetector(new LocalFileDetector());
            remoteDriver.manage().window().maximize();
            remoteDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        } catch (MalformedURLException e) {
            throw new RuntimeException(format("Invalid URL for Remote WebDriver: %s", REMOTE_URL), e);
        } catch (WebDriverException e) {
            throw new RuntimeException(format("Error starting Remote WebDriver: %s", e.getMessage()), e);
        }
        return remoteDriver;
    }

    public static void quitAllDrivers() {
        ALL_DRIVERS_THREADS.forEach(Browser::quitDriver);
    }

}
