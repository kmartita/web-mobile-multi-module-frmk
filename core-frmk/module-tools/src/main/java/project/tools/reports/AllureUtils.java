package project.tools.reports;

import io.qameta.allure.Step;
import lombok.experimental.UtilityClass;
import org.testng.Reporter;

@UtilityClass
public final class AllureUtils {

    @Step("{0}")
    public static void step(String step) {
        Reporter.log("\nLOG - " + step, true);
    }

}
