package project.tools.browser;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;

public interface IBrowser {

    WebDriver getWebDriver(MutableCapabilities desiredCapabilities);
    MutableCapabilities getCapabilities();
}
