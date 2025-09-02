package project.tools.reports;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import static project.tools.PathToFile.getRootOfProject;

public class TestListener implements IInvokedMethodListener {

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        if (method.isTestMethod()) {
            if (result.getStatus() == ITestResult.FAILURE) {
                Path path = Paths.get(getRootOfProject(), "module-tests", "target", "surefire-reports", "html",
                        LocalDate.now().toString(), result.getEndMillis() + ".png");

                /*AllureUtils.takeScreenshot(getPage(), path, "Failed Screenshot");
                result.setAttribute(FAILED_SCREENSHOT_PATH_ATTRIBUTE, path.toString());*/
            }
        }
    }
}
