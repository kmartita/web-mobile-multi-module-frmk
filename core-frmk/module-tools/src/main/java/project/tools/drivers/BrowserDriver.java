package project.tools.drivers;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import project.tools.ConfigurationManager;
import project.tools.browser.Browsers;

import static java.lang.String.format;

public class BrowserDriver {

    public static WebDriver startBrowserDriver() {
        Browsers driver = ConfigurationManager.getDriverType();
        MutableCapabilities capabilities = driver.getCapabilities();

        try {
            return driver.getWebDriver(capabilities);

        } catch (Exception e) {
            throw new RuntimeException(format("Failed to start web driver for: %s", driver), e);
        }
    }
}
