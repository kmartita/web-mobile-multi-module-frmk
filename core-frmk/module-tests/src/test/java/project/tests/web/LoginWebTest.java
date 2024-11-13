package project.tests.web;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import project.tests.AbstractWebTest;
import project.web.page.LoginPage;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginWebTest extends AbstractWebTest {

    private LoginPage loginPage;

    @BeforeClass
    public void beforeTest() {
        loginPage = web.openLoginPage();
    }

    @Test
    public void verifyPageIsLoaded() {
        assertThat(loginPage.isOpened())
                .as("Login page should be opened.")
                .isTrue();
    }
}
