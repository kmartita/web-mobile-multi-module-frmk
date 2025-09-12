package project.app;

import io.qameta.allure.Allure;
import project.tools.pageobject.AbstractApp;
import project.web.pages.HomePage;

import static project.tools.ConfigurationManager.getEnvironment;
import static project.tools.ConfigurationManager.getPlatform;
import static project.tools.EnvManager.BASE_URL;

public class Web extends AbstractApp {

    public void startWebSession() {
        Allure.step("Start web application session");
        System.out.printf("PLATFORM: '%s'%n", getPlatform());
        System.out.printf("ENVIRONMENT: '%s'%n", getEnvironment());

        if (getDriver().getTitle().isEmpty()) {
            getDriver().get(BASE_URL);
        }
        getDriver().manage().deleteAllCookies();
    }

    public HomePage openHomePage() {
        Allure.step("Open 'Home Page'");
        startWebSession();
        return new HomePage();
    }

}
