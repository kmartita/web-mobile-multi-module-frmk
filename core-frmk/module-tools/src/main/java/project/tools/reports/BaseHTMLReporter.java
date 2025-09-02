package project.tools.reports;

import io.qameta.allure.TmsLink;
import io.qameta.allure.TmsLinks;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlTest;
import org.uncommons.reportng.HTMLReporter;
import project.tools.pageobject.AbstractApp;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static project.tools.reports.ReportUtils.SCREENSHOT_RESULT_ATTRIBUTE;
import static project.tools.reports.ReportUtils.TEST_CASE_ID_ATTRIBUTE;

public class BaseHTMLReporter extends HTMLReporter implements ITestListener {

    public static final String FAILED_SCREENSHOT_PATH_ATTRIBUTE = "failedScreenshotPath";

    private static final Map<String, Map<String, String>> mapWithTestAndClasses = new HashMap<>();

    public static Map<String, Map<String, String>> getMapWithTestAndClasses() {
        return mapWithTestAndClasses;
    }

    @Override
    public void onTestStart(ITestResult result) {
        try {
            System.out.printf("Start class: '%s'\n", result.getTestClass().getName());
            System.out.printf("Start method: '%s'\n", result.getName());
        } catch (Exception e) {
            System.out.printf(e.getMessage());
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
                System.out.printf("Test Case ID: '%s', Status: PASSED.\n", caseId);
            }
        });
        System.out.printf("Method: '%s', Status: PASSED.\n", testMethodName);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        testId(result);
        List<String> testCaseIds = getTestCaseId(result);
        String testMethodName = getTestMethodName(result);

        Object attr = result.getAttribute(FAILED_SCREENSHOT_PATH_ATTRIBUTE);
        String imageLink = attr != null ? attr.toString() : null;

        String originalMessage = Optional.ofNullable(result.getThrowable())
                .map(Throwable::getMessage)
                .orElse(StringUtils.EMPTY);

        testCaseIds.forEach(testCaseId -> {
            if (!StringUtils.isBlank(testCaseId)) {
                System.out.printf("Test Case ID: '%s', Status: FAILED.\n", testCaseId);
                System.out.printf("Screenshot Link: '%s'\n", imageLink);
                System.out.printf("Comment: Test FAILED. Step ('%s').\n", result.getName());
            }
        });

        try {
            System.out.printf("FAILED: '%s' : '%s'\n", result.getInstanceName(), result.getName());
            System.out.printf("Method '%s' run status is FAILED.\n", testMethodName);
            System.out.printf("Error message: '%s'\n", originalMessage);
            System.out.printf("Cause: '%s'\n", result.getThrowable().getCause());

        } catch (Exception ignored) {
            System.out.print("In BaseHTMLReporter#onTestFailure\n");
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testId(result);
        List<String> testCaseIds = getTestCaseId(result);
        String testMethodName = getTestMethodName(result);

        testCaseIds.forEach(testCaseId -> {
            if (!StringUtils.isBlank(testCaseId)) {
                System.out.printf("Test Case ID: '%s', Status: SKIPPED.\n", testCaseId);
                System.out.printf("Comment: Test SKIPPED. Step ('%s').\n", result.getName());
            }
        });

        try {
            System.out.printf("SKIPPED: '%s' : '%s'\n", result.getInstanceName(), result.getName());
            System.out.printf("Method '%s' run status is SKIPPED.\n", testMethodName);

        } catch (Exception e) {
            System.err.printf("In BaseHTMLReporter#onTestSkipped: %s\n", e.getMessage());
        }
    }

    @Override
    public void onStart(ITestContext context) {
        try {
            System.out.printf("Start test: '%s'\n", context.getName());
        } catch (Exception e) {
            System.out.printf("In BaseHTMLReporter#onStart: %s\n", e.getMessage());
        }

        for (XmlTest testName : context.getSuite().getXmlSuite().getTests()) {
            for (XmlClass xmlClass : testName.getXmlClasses()) {
                if (getMapWithTestAndClasses().containsKey(testName.getName())) {
                    getMapWithTestAndClasses().get(testName.getName()).put(xmlClass.getName(), "started");
                } else {
                    getMapWithTestAndClasses().put(testName.getName(), new HashMap<>());
                }
            }
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        try {
            System.out.printf("Finish test: '%s'\n", context.getName());
        } catch (Exception e) {
            System.out.printf("In BaseHTMLReporter#onFinish: %s\n", e.getMessage());
        }
    }

    private String getTestMethodName(ITestResult result) {
        StringBuilder methodNameBuilder = new StringBuilder(result.getName());
        List<String> list = new ArrayList<>();
        Object instance = result.getInstance();

        if (instance instanceof IFetchParameters parameters) {
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

    private List<String> getTestCaseId(ITestResult result) {
        List<String> ids = new ArrayList<>();
        Method method = result.getMethod().getConstructorOrMethod().getMethod();

        if (Objects.nonNull(method.getAnnotation(TmsLinks.class))) {
            TmsLink[] tmsLinks = method.getAnnotation(TmsLinks.class).value();
            ids.addAll(Arrays.stream(tmsLinks)
                    .map(TmsLink::value)
                    .toList());
        } else if (Objects.nonNull(method.getAnnotation(TmsLink.class))) {
            ids.add(method.getAnnotation(TmsLink.class).value());
        }

        return ids;
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

    public static synchronized String createScreenshot(ITestResult result) {
        String relativePath = StringUtils.EMPTY;
        try {
            File srcFile = File.createTempFile("screenshot", ".jpg");
            byte[] screenshotBytes = AllureUtils.makeScreenshot(AbstractApp.getInstanceDriver());

            if (screenshotBytes != null) {
                FileUtils.writeByteArrayToFile(srcFile, screenshotBytes);
            } else {
                throw new IOException("Screenshot byte array is null");
            }

            String dateFolder = LocalDate.now().toString();
            String endMillis = String.valueOf(result.getEndMillis());

            Path filePath = Paths.get("html", dateFolder, endMillis + ".png");
            Path baseFolder = Paths.get("target", "surefire-reports");
            Path fullPath = baseFolder.resolve(filePath);

            try {
                Files.createDirectories(fullPath.getParent());
            } catch (IOException e) {
                System.err.println("Could not create a folder: " + fullPath.getParent().toAbsolutePath());
            }
            FileUtils.copyFile(srcFile, fullPath.toFile());

            result.setAttribute(SCREENSHOT_RESULT_ATTRIBUTE, fullPath.toString());
            relativePath = fullPath.toString();

        } catch (Exception e) {
            System.err.printf("In BaseHTMLReporter#createScreenshot: %s\n", e.getMessage());
        }
        return relativePath;
    }
}
