package project.tools.reports;

import lombok.Getter;
import org.testng.ITestListener;
import org.uncommons.reportng.HTMLReporter;

import java.util.HashMap;
import java.util.Map;

public class BaseHTMLReporter extends HTMLReporter implements ITestListener {

    @Getter
    private static final Map<String, Map<String,String>> mapWithTestAndClasses = new HashMap<>();

}
