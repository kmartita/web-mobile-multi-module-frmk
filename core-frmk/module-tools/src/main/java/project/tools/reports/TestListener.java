package project.tools.reports;

import io.qameta.allure.Allure;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static project.tools.reports.BaseHTMLReporter.FAILED_SCREENSHOT_PATH_ATTRIBUTE;
import static project.tools.reports.BaseHTMLReporter.createScreenshot;

public class TestListener implements IInvokedMethodListener {

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        if (method.isTestMethod()) {
            if (result.getStatus() == ITestResult.FAILURE) {
                attachScreenshotToAllure(result);
            }
        }
    }

    private void attachScreenshotToAllure(ITestResult result) {
        String path = createScreenshot(result);
        try {
            File input = new File(path);
            Allure.addAttachment("Failed Screenshot", "image/png", new FileInputStream(input), "png");
            result.setAttribute(FAILED_SCREENSHOT_PATH_ATTRIBUTE, path);

        } catch (FileNotFoundException e) {
            System.err.println("Unable to add a screenshot: " + e.getMessage());
        }
    }
}
