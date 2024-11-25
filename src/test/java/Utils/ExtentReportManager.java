package Utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportManager {

    public static ExtentReports extentReports;
    public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    public static synchronized ExtentReports getInstance() {
        if (extentReports == null) {
            String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            String reportName = "API_Report-" + timestamp + ".html";
            String reportPath = System.getProperty("user.dir") + File.separator + "Reports";

            // Ensure the Reports directory exists
            File reportDir = new File(reportPath);
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }

            ExtentSparkReporter htmlReporter = new ExtentSparkReporter(reportPath + File.separator + reportName);
            htmlReporter.config().setDocumentTitle("API Automation");
            htmlReporter.config().setReportName("API Automation Report");
            htmlReporter.config().setTheme(Theme.DARK);

            extentReports = new ExtentReports();
            extentReports.attachReporter(htmlReporter);
        }
        return extentReports;
    }

    public static synchronized ExtentTest createTest(String testName) {
        ExtentTest test = getInstance().createTest(testName);
        extentTest.set(test); // Set ThreadLocal for each scenario
        return test;
    }

    public static synchronized ExtentTest getTest() {
        return extentTest.get(); // Return ThreadLocal for each scenario
    }

    public static synchronized void flushReports() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }

    public static synchronized void removeTest() {
        extentTest.remove(); // Remove ThreadLocal to avoid memory leaks
    }
}
