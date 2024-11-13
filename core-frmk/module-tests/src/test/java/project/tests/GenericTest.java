package project.tests;

import org.assertj.core.api.SoftAssertions;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.asserts.SoftAssert;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlTest;
import project.tools.reports.BaseHTMLReporter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Listeners(BaseHTMLReporter.class)
public abstract class GenericTest implements IAppTest {

    // AssertJ soft assert
    public SoftAssertions softly;

    // TestNG soft assert
    public SoftAssert soft;

    protected static Map<String, String> allClassesStatuses = new HashMap<>();

    public void startFillClassRunResult(ITestContext result) {
        allClassesStatuses.clear();

        List<XmlTest> listWithXMLTests = result.getCurrentXmlTest().getSuite().getTests();
        for (XmlTest xmlTest : listWithXMLTests) {
            for (XmlClass xmlClass : xmlTest.getClasses()) {
                allClassesStatuses.put(xmlClass.getName(), "false");
            }
        }
    }

    public synchronized boolean isFullyAutomated() {
        System.out.printf("All test classes: %s%n", allClassesStatuses);

        for (String className : allClassesStatuses.keySet())
            if (allClassesStatuses.get(className).equals("false"))
                return false;
        return true;
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        soft = new SoftAssert();
        softly = new SoftAssertions();
    }
}
