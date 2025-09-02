package project.tests;

import com.github.rsheremeta.AllureEnv;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.xml.XmlSuite;
import project.app.Web;
import project.tools.ConfigurationManager;
import project.tools.pageobject.AbstractApp;
import project.tools.reports.BaseHTMLReporter;

import java.util.HashMap;
import java.util.Map;

import static project.tools.ConfigurationManager.*;
import static project.tools.EnvManager.BASE_URL;
import static project.tools.drivers.Drivers.quitAllDrivers;

public abstract class AbstractWebTest extends GenericTest {

    protected static final Web web = new Web();

    @BeforeSuite(alwaysRun = true)
    public void suiteSetup(ITestContext context) {
        startFillClassRunResult(context);

        int threadCount = ConfigurationManager.getThreadCount();
        if (threadCount == -1)
            return;
        XmlSuite suite = context.getSuite().getXmlSuite();
        suite.setParallel(XmlSuite.ParallelMode.TESTS);
        suite.setThreadCount(threadCount);
        suite.setGroupByInstances(Boolean.TRUE);
    }

    @AfterClass(alwaysRun = true)
    public void afterClass(ITestContext context) {
        allClassesStatuses.put(this.getClass().getName(), "true");

        try {
            Web.closeBrowser();

        } finally {
            if(!BaseHTMLReporter.getMapWithTestAndClasses().isEmpty()) {
                BaseHTMLReporter.getMapWithTestAndClasses().get(context.getName()).put(this.getClass().getName(),"finished");

                long testsLeft = BaseHTMLReporter.getMapWithTestAndClasses().get(context.getName()).values().stream().filter(o -> o.equalsIgnoreCase("started")).count();
                int percentage = (int) ((float) testsLeft/(BaseHTMLReporter.getMapWithTestAndClasses().get(context.getName()).size()) * 100);

                System.out.println("=====================================");
                System.out.printf("Tests left: %s%n", percentage);
                System.out.println("=====================================");

                System.out.println("=====================================");
                for(String keyTestName : BaseHTMLReporter.getMapWithTestAndClasses().get(context.getName()).keySet()) {
                    if(BaseHTMLReporter.getMapWithTestAndClasses().get(context.getName()).get(keyTestName).equals("started"))
                        System.out.printf("Not started test class [%s]%n", keyTestName);
                }
                System.out.println("=====================================");
            }
        }
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite(ITestContext context) {
        quitAllDrivers();
        System.out.println("=====================================");
        System.out.printf("Is fully automated: %s%n", isFullyAutomated());

        Map<String, String> envData = new HashMap<>();
        envData.put("Platform:", getPlatform().toString());
        envData.put("Environment:", getEnvironment().name());
        envData.put("Browser:", getBrowser().toString());
        envData.put("Base URL:", BASE_URL);

        AllureEnv.createAllureEnvironmentFile(envData);
    }

    @Override
    public AbstractApp getTestedAppInstance() {
        return web;
    }
}
