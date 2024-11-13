package project.app;

import io.qameta.allure.Step;
import org.openqa.selenium.Dimension;
import project.mobile.screen.LoginScreen;
import project.tools.pageobject.mobile.AbstractMobileApp;
import project.tools.utils.RunConsoleCommand;

import static project.tools.ConfigurationManager.*;

public class Mobile extends AbstractMobileApp {

    private static final String MOBILE = "mobile";
    private static final String IOS = "ios";

    public boolean deviceOrientationIsPortrait() {
        Dimension dimension = getIOSDriver().manage().window().getSize();
        System.out.printf("width: %d, height: %d%n", dimension.getWidth(), dimension.getHeight());
        return dimension.getWidth() < dimension.getHeight();
    }

    @Step
    public void createIOSSession() {
        System.setProperty(MOBILE, IOS);

        System.out.printf("PLATFORM: '%s'%n", getPlatform());
        System.out.printf("ENVIRONMENT: '%s'%n", getEnvironment());
        System.out.printf("DEVICE: '%s'%n", getDevice());
        System.out.printf("iOS VERSION: '%s'%n", getIosVersion());
        getIOSDriver();
        System.out.println("Current device orientation: ");
        System.out.println(getIOSDriver().getOrientation().value());

        if (deviceOrientationIsPortrait()) {
            RunConsoleCommand.rotateDevice(); // Doesn't work. By manually
            System.out.println("Device orientation after rotate: ");
            System.out.println(getIOSDriver().getOrientation().value());
        }
    }

    @Step
    public LoginScreen openLoginScreen() {
        createIOSSession();
        return new LoginScreen();
    }
}
