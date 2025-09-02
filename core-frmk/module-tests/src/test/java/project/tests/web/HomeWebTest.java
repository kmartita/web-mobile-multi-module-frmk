package project.tests.web;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import project.tests.AbstractWebTest;
import project.web.pages.HomePage;

import static org.assertj.core.api.Assertions.assertThat;

@Owner("Marta Kravchuk")
@Feature("Web")
@Story("Page")
public class HomeWebTest extends AbstractWebTest {

    private HomePage homePage;

    @BeforeClass
    public void beforeTest() {
        homePage = web.openHomePage();
    }

    @Test
    public void verifyPageIsLoaded() {
        assertThat(homePage.isOpened())
                .as("Login page should be opened.")
                .isFalse(); //test
    }
}
