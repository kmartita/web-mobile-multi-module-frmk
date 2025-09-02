package project.app;

import io.qameta.allure.Step;
import org.openqa.selenium.Dimension;
import project.mobile.screens.HomeScreen;
import project.tools.Platform;
import project.tools.pageobject.AbstractMobileApp;
import project.tools.utils.RunConsoleCommand;

import static project.tools.ConfigurationManager.*;

public class Mobile extends AbstractMobileApp {

    public boolean deviceOrientationIsPortrait() {
        Dimension dimension = getIOSDriver().manage().window().getSize();
        System.out.printf("width: %d, height: %d%n", dimension.getWidth(), dimension.getHeight());
        return dimension.getWidth() < dimension.getHeight();
    }

    private void rotateDevice() {
        if (deviceOrientationIsPortrait()) {
            RunConsoleCommand.rotateDevice();
        }
    }

    @Step("Start iOS-application session")
    public void startIOSSession() {
        System.setProperty(MOBILE_VARIABLE, Platform.IOS.toString());

        System.out.printf("PLATFORM: '%s'%n", getPlatform());
        System.out.printf("ENVIRONMENT: '%s'%n", getEnvironment());
        System.out.printf("DEVICE: '%s'%n", getDevice());
        System.out.printf("iOS VERSION: '%s'%n", getIosVersion());

        getIOSDriver();
        rotateDevice();
    }

    @Step("Open 'Home Screen'")
    public HomeScreen openHomeScreen() {
        startIOSSession();
        return new HomeScreen();
    }

}
