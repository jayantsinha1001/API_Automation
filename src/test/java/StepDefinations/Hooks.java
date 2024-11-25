package StepDefinations;

import Utils.ExtentReportManager;
import com.aventstack.extentreports.ExtentTest;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    public ExtentTest test ;

    @Before
    public void beforeScenario(Scenario scenario) {
        // Create and initialize the ExtentTest for each scenario
        test = ExtentReportManager.createTest(scenario.getName());
        test.info("Starting Scenario: " + scenario.getName() + ", Test initialized: " + (test != null));

        if (test == null) {
            throw new RuntimeException("ExtentTest was not initialized properly.");
        }
    }

    @After
    public void afterScenario(Scenario scenario) {
        ExtentTest test = ExtentReportManager.getTest();
        String scenarioName = scenario.getName() != null ? scenario.getName() : "Unnamed Scenario";

        if (test == null) {
            System.err.println("ExtentTest was null for scenario: " + scenarioName);
        } else {
            if (scenario.isFailed()) {
                test.fail("Scenario failed: " + scenarioName);
            } else {
                test.pass("Scenario passed: " + scenarioName);
            }
        }

        // Flush
        ExtentReportManager.flushReports();
        ExtentReportManager.removeTest();
        System.out.println("Finished Scenario: " + scenarioName);
    }
}
