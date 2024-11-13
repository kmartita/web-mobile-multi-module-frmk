package project.tests.mobile;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import project.mobile.screen.LoginScreen;
import project.tests.AbstractMobileTest;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginMobileTest extends AbstractMobileTest {

    private LoginScreen loginScreen;

    @BeforeClass
    public void beforeTest() {
        loginScreen = mobile.openLoginScreen();
    }

    @Test
    public void verifyScreenIsLoaded() {
        assertThat(loginScreen.isOpened())
                .as("Login screen should be opened.")
                .isTrue();
    }
}
