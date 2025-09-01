package project.tests.web;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import project.tests.AbstractWebTest;
import project.web.pages.HomePage;

import static org.assertj.core.api.Assertions.assertThat;

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
                .isTrue();
    }
}
