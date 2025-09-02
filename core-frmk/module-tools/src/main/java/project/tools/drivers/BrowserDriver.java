package project.tools.drivers;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import project.tools.ConfigurationManager;
import project.tools.browser.IBrowser;

import static java.lang.String.format;

public class BrowserDriver {

    public static WebDriver startBrowserDriver() {
        IBrowser driver = ConfigurationManager.getBrowser();
        MutableCapabilities capabilities = driver.getCapabilities();

        try {
            return driver.getWebDriver(capabilities);

        } catch (Exception e) {
            throw new RuntimeException(format("Failed to start web driver for: %s", driver), e);
        }
    }
}
