package project.tools.reports;

import org.testng.ITestResult;
import org.uncommons.reportng.ReportNGUtils;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class ReportUtils extends ReportNGUtils {

    public static final String SCREENSHOT_RESULT_ATTRIBUTE = "screenshot";
    public static final String TEST_DATA_RESULT_ATTRIBUTE = "data";
    public static final String URL_RESULT_ATTRIBUTE = "URL";
    public static final String WEB_APP_LOGGER_RESULT_ATTRIBUTE = "applogger";
    public static final String TEST_CASE_ID_ATTRIBUTE = "testcaseid";

    @Override
    public List<String> getTestOutput(ITestResult result) {
        List<String> output = super.getTestOutput(result);

        // Test Case ID
        String testCaseId = (String) result.getAttribute(TEST_CASE_ID_ATTRIBUTE);
        if (isNotEmpty(testCaseId)) {
            output.add("<b>ID</b>: " + testCaseId + "<br>");
        }

        // Add screenshot
        String screenshot = (String) result.getAttribute(SCREENSHOT_RESULT_ATTRIBUTE);
        if (isNotEmpty(screenshot)) {
            output.add("<a href=\"../" + screenshot + "\">Screenshot</a>");
        }

        // Add IWebAppTest instance logger content
        String testData = (String) result.getAttribute(TEST_DATA_RESULT_ATTRIBUTE);
        if (isNotEmpty(testData)) {
            output.add("<br>Test Data: " + testData);
        }

        // Add IWebApp instance logger content
        String tappLogs = (String) result.getAttribute(WEB_APP_LOGGER_RESULT_ATTRIBUTE);
        if (isNotEmpty(tappLogs)) {
            output.add(tappLogs + "<br>");
        }

        // Add URL information
        String url = (String) result.getAttribute(URL_RESULT_ATTRIBUTE);
        if (isNotEmpty(url)) {
            output.add(url + "<br>");
        }

        return output;
    }
}
