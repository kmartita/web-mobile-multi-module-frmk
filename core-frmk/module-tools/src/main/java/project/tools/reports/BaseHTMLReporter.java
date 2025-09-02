package project.tools.reports;

import io.qameta.allure.TmsLink;
import io.qameta.allure.TmsLinks;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlTest;
import org.uncommons.reportng.HTMLReporter;
import project.tools.pageobject.AbstractApp;

import java.io.File;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static project.tools.reports.ReportUtils.SCREENSHOT_RESULT_ATTRIBUTE;
import static project.tools.reports.ReportUtils.TEST_CASE_ID_ATTRIBUTE;

public class BaseHTMLReporter extends HTMLReporter implements ITestListener {

    private static final String UTILS_KEY = "utils";
    private static final ReportUtils REPORT_UTILS = new ReportUtils();

    protected VelocityContext createContext() {
        VelocityContext context = super.createContext();
        context.put(UTILS_KEY, REPORT_UTILS);
        return context;
    }

    private static final Map<String, Map<String,String>> mapWithTestAndClasses = new HashMap<>();

    public static Map<String, Map<String, String>> getMapWithTestAndClasses() {
        return mapWithTestAndClasses;
    }

    @Override
    public void onTestStart(ITestResult result) {
        try {
            System.out.printf("Start class: '%s'", result.getTestClass().getName());
            System.out.printf("Start method: '%s'", result.getName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testId(result);
        List<String> testCaseIds = getTestCaseId(result);
        String testMethodName = getTestMethodName(result);
        testCaseIds.forEach(testCaseId -> {
            if (!StringUtils.isBlank(testCaseId)) {
                int caseId = Integer.parseInt(testCaseId);
                System.out.printf("Test Case ID: '%s', Status: PASSED.", caseId);
            }
        });
        System.out.printf("Method: '%s', Status: PASSED.", testMethodName);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        testId(result);
        List<String> testCaseIds = getTestCaseId(result);
        String testMethodName = getTestMethodName(result);
        String imageLink = createScreenshot(result, "Failure Screenshot");

        String originalMessage = Optional.ofNullable(result.getThrowable())
                .map(Throwable::getMessage)
                .orElse("");

        testCaseIds.forEach(testCaseId -> {
            if (!StringUtils.isBlank(testCaseId)) {
                System.out.println("Test Case ID: " + testCaseId + ", Status: Failed");
                System.out.println("Screenshot link: " + imageLink);
                System.out.println("Comment: Test failed. Step (" + result.getName() + ")");
            }
        });

        try {
            System.out.println("Failed: " + result.getInstanceName() + " : " + result.getName());
            System.out.println("Method [" + testMethodName + "] run status is FAILED");
            System.out.println("Error message: " + originalMessage);
            System.out.println("Cause: " + result.getThrowable().getCause());

        } catch (Exception ignored) {
            System.out.println("In BaseHTMLReporter#onTestFailure");
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // Визначення ідентифікаторів тестів
        testId(result);
        List<String> testCaseIds = getTestCaseId(result);
        String testMethodName = getTestMethodName(result);
        String imageLink = createScreenshot(result, "Skip Screenshot");

        // Вивід оригінального повідомлення
        String originalMessage = Optional.ofNullable(result.getThrowable())
                .map(Throwable::getMessage)
                .orElse("");

        // Обробка кожного ідентифікатора тесту
        testCaseIds.forEach(testCaseId -> {
            if (!StringUtils.isBlank(testCaseId)) {
                System.out.println("Test Case ID: " + testCaseId + ", Status: Skipped");
                System.out.println("Screenshot link: " + imageLink);
                System.out.println("Comment: Test skipped. Step (" + result.getName() + ")");
            }
        });

        // Вивід результатів
        try {
            System.err.println("Skipped: " + result.getInstanceName() + " : " + result.getName());
            System.err.println("Method [" + testMethodName + "] run status is SKIPPED");
        } catch (Exception e) {
            System.err.println("In BaseHTMLReporter#onTestSkipped: " + e.getMessage());
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStart(ITestContext context) {
        try {
            System.out.println("Start test: " + context.getName());
        } catch (Exception e) {
            System.out.println("In BaseHTMLReporter#onStart:\n" + e.getMessage());
        }

        for(XmlTest testName : context.getSuite().getXmlSuite().getTests()) {
            for(XmlClass xmlClass : testName.getXmlClasses()) {
                if(getMapWithTestAndClasses().containsKey(testName.getName())) {
                    getMapWithTestAndClasses().get(testName.getName()).put(xmlClass.getName(), "started");
                }else {
                    getMapWithTestAndClasses().put(testName.getName(), new HashMap<>());
                }
            }
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        try {
            System.out.println("Finish: " + context.getName());
        } catch (Exception e) {
            System.out.println("In BaseHTMLReporter#onFinish:\n" + e.getMessage());
        }
    }

    private void testId(ITestResult result) {
        try {
            Method method = result.getMethod().getConstructorOrMethod().getMethod();
            TmsLink id = method.getAnnotation(TmsLink.class);
            if (Objects.nonNull(id)) {
                result.setAttribute(TEST_CASE_ID_ATTRIBUTE, id.value());
            }

        } catch (Exception e) {
            System.err.printf("In BaseHTMLReporter#testId: %s\n", e.getMessage());
        }
    }

    private List<String> getTestCaseId(ITestResult result) {
        List<String> ids = new ArrayList<>();
        Method method = result.getMethod().getConstructorOrMethod().getMethod();

        if (Objects.nonNull(method.getAnnotation(TmsLinks.class))) {
            TmsLink[] tmsLinks = method.getAnnotation(TmsLinks.class).value();
            ids.addAll(Arrays.stream(tmsLinks)
                    .map(TmsLink::value)
                    .collect(Collectors.toList()));
        }

        else if (Objects.nonNull(method.getAnnotation(TmsLink.class))) {
            ids.add(method.getAnnotation(TmsLink.class).value());
        }

        return ids;
    }

    private String getTestMethodName(ITestResult result) {
        StringBuilder methodNameBuilder = new StringBuilder(result.getName());
        List<String> list = new ArrayList<>();
        Object instance = result.getInstance();

        if (instance instanceof IFetchParameters) {
            IFetchParameters parameters = (IFetchParameters) instance;
            for (Object eachParameter : parameters.getTestClassParameters()) {
                list.add(String.valueOf(eachParameter).replace(" ", ""));
            }
            if (!list.isEmpty()) {
                methodNameBuilder.append("_arg=").append(String.join("_arg=", list));
            }
        } else {
            if (result.getParameters().length != 0) {
                String params = Arrays.stream(result.getParameters())
                        .map(Object::toString)
                        .collect(Collectors.joining("_"));
                methodNameBuilder.append("_").append(params);
            }
        }

        return methodNameBuilder.toString();
    }

    private String createScreenshot(ITestResult result, String name) {
        String relativePathToFile = "";

        try {
            File srcFile = File.createTempFile("screenshot", ".jpg");
            FileUtils.writeByteArrayToFile(srcFile, AllureUtils.makeScreenshot(name, AbstractApp.getInstanceDriver()));

            String baseFolder = "target" + File.separator + "surefire-reports" + File.separator;
            String screenFilePath = "html" + File.separator + LocalDate.now() + File.separator + result.getEndMillis()
                                    + ".png";
            relativePathToFile = baseFolder + screenFilePath;
            FileUtils.copyFile(srcFile, new File(relativePathToFile));
            result.setAttribute(SCREENSHOT_RESULT_ATTRIBUTE, screenFilePath);

        } catch (Exception e) {
            System.err.printf("In BaseHTMLReporter#createScreenshot: %s\n", e.getMessage());
        }
        return relativePathToFile;
    }

}
