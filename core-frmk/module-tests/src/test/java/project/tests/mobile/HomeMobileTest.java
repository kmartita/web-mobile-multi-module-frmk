package project.tests.mobile;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import project.mobile.screens.HomeScreen;
import project.tests.AbstractMobileTest;

import static org.assertj.core.api.Assertions.assertThat;

public class HomeMobileTest extends AbstractMobileTest {

    private HomeScreen homeScreen;

    @BeforeClass
    public void beforeTest() {
        homeScreen = mobile.openHomeScreen();
    }

    @Test
    public void verifyScreenIsLoaded() {
        assertThat(homeScreen.isOpened())
                .as("Home screen should be opened.")
                .isTrue();
    }
}