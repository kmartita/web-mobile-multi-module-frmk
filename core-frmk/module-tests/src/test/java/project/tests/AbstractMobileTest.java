package project.tests;

import com.github.rsheremeta.AllureEnv;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.qameta.allure.Step;
import org.apache.commons.lang.time.StopWatch;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import project.app.Mobile;
import project.tools.ConfigurationManager;
import project.tools.pageobject.AbstractApp;
import project.tools.reports.BaseHTMLReporter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static project.tools.ConfigurationManager.*;
import static project.tools.drivers.Drivers.quitAllDrivers;
import static project.tools.pageobject.AbstractMobileApp.getIOSDriver;

public abstract class AbstractMobileTest extends GenericTest implements IAppTest {

    protected final Mobile mobile = new Mobile();
    private AppiumDriverLocalService service = null;

    @Step
    public static void removeMobileApp() {
        removeApp(getAppBundleId());
    }

    @Step
    public static void removeApp(String bundleId) {
        if (getIOSDriver().isAppInstalled(bundleId)) {
            getIOSDriver().removeApp(bundleId);
            Mobile.closeMobile();
        }
    }

    @Step
    private void startAppiumServer() {
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withArgument(GeneralServerFlag.LOG_LEVEL, "error")
                .usingPort(4723);

        service = AppiumDriverLocalService.buildService(builder);
        service.start();

        System.out.printf("Appium server started at: '%s'%n", service.getUrl());
    }

    @Step
    private void stopAppiumServer() {
        if (service != null && service.isRunning()) {
            service.stop();
            System.out.println("Appium server stopped.");
        } else {
            System.out.println("Appium server is not running.");
        }
    }

    @BeforeSuite(alwaysRun = true)
    @Step
    public void suiteSetup(ITestContext context) {
        startAppiumServer();
        startFillClassRunResult(context);
    }

    @AfterClass(alwaysRun = true)
    public void afterClass(ITestContext context) {
        StopWatch watch1 = new StopWatch();
        watch1.start();
        allClassesStatuses.put(this.getClass().getName(), "true");

        try {
            removeMobileApp();

        } finally {
            watch1.stop();
            String duration = String.valueOf(watch1.getTime() / 1000.0);
            System.out.println("=====================================");
            System.out.printf("Duration of after test call [%s] is [%s]%n", this.getClass().getSimpleName(), duration);
            System.out.println("=====================================");

            StopWatch watch2 = new StopWatch();
            watch2.start();

            if(!BaseHTMLReporter.getMapWithTestAndClasses().isEmpty()) {
                BaseHTMLReporter.getMapWithTestAndClasses().get(context.getName()).put(this.getClass().getName(),"finished");

                double testsLeft = (double) getCountOfClassesByStatus(BaseHTMLReporter.getMapWithTestAndClasses(), "started");
                double testsExecuted = (double) getCountOfClassesByStatus(BaseHTMLReporter.getMapWithTestAndClasses(), "finished");

                System.out.printf("TEST LEFT: %s%n", testsLeft);
                System.out.printf("TEST EXECUTED: %s%n", testsExecuted);

                double percentage = testsLeft / (testsLeft + testsExecuted) * 100;

                System.out.println("=====================================");
                System.out.printf("Tests left: %s%n", percentage);
                System.out.println("=====================================");
            }

            watch2.stop();
            String duration1 = String.valueOf(watch2.getTime() / 1000.0);
            System.out.printf("Duration of after data class fill : %s%n", duration1);
        }
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite(ITestContext context) {
        quitAllDrivers();
        System.out.println("=====================================");
        System.out.printf("Is fully automated: %s%n", isFullyAutomated());

        if(ConfigurationManager.isRunAppiumFromCode()){
            stopAppiumServer();
        }

        Map<String, String> envData = new HashMap<>();
        envData.put("Approach:", "MOBILE");
        envData.put("Platform:", getPlatform().toString());
        envData.put("Environment:", getEnvironment().name());
        envData.put("Device:", getDevice());
        envData.put("iOS version:", getIosVersion());

        AllureEnv.createAllureEnvironmentFile(envData);
    }

    private long getCountOfClassesByStatus(Map<String, Map<String,String>> map, String status) {
        return map.keySet().parallelStream().map(o -> map.get(o).values())
                .collect(Collectors.toList()).parallelStream().collect(ArrayList::new, List::addAll, List::addAll)
                .parallelStream().filter(o -> o.equals(status)).count();
    }

    @Override
    public AbstractApp getTestedAppInstance() {
        return mobile;
    }
}
