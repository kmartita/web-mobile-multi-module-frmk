package project.app;

import io.qameta.allure.Step;
import project.tools.pageobject.AbstractApp;
import project.web.page.LoginPage;

import static project.tools.ConfigurationManager.getEnvironment;
import static project.tools.ConfigurationManager.getPlatform;
import static project.tools.EnvManager.BASE_URL;

public class Web extends AbstractApp {

    @Step
    public LoginPage openLoginPage() {
        System.out.printf("PLATFORM: '%s'%n", getPlatform());
        System.out.printf("ENVIRONMENT: '%s'%n", getEnvironment());

        if (getInstanceDriver().getTitle().isEmpty()) {
            getInstanceDriver().get(BASE_URL);
        }
        getInstanceDriver().manage().deleteAllCookies();
        return new LoginPage();
    }
}
