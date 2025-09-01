package project.app;

import io.qameta.allure.Step;
import project.tools.pageobject.AbstractApp;
import project.web.pages.HomePage;

import static project.tools.ConfigurationManager.getEnvironment;
import static project.tools.ConfigurationManager.getPlatform;
import static project.tools.EnvManager.BASE_URL;

public class Web extends AbstractApp {

    @Step("Start web-application session")
    public void startWebSession() {
        System.out.printf("PLATFORM: '%s'%n", getPlatform());
        System.out.printf("ENVIRONMENT: '%s'%n", getEnvironment());

        if (getInstanceDriver().getTitle().isEmpty()) {
            getInstanceDriver().get(BASE_URL);
        }
        getInstanceDriver().manage().deleteAllCookies();
    }

    @Step("Open 'Home Page'")
    public HomePage openHomePage() {
        startWebSession();
        return new HomePage();
    }

}
