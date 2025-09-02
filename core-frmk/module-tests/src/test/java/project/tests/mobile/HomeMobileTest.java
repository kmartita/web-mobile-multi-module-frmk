package project.tests.mobile;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import project.mobile.screens.HomeScreen;
import project.tests.AbstractMobileTest;

import static org.assertj.core.api.Assertions.assertThat;

@Owner("Marta Kravchuk")
@Feature("Mobile")
@Story("Screen")
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
                .isFalse(); //test
    }
}