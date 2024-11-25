package StepDefinations;

import Utils.ExtentReportManager;
import Utils.TestContext;
import com.aventstack.extentreports.ExtentTest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonStepDefination {

    public TestContext testContext;
    public RequestSpecification request;
    public static final Logger log = LogManager.getLogger(CommonStepDefination.class);
    public Response response;
    public ExtentTest test;

    public CommonStepDefination(TestContext context) {
        this.testContext = context;
        this.test = ExtentReportManager.getTest();
    }



    @Given("user sets up the base uri")
    public void user_sets_up_the_base_uri() {
        try {
            testContext.requestSetup();
            test.info("Base URI set up  " );
        } catch (Exception e) {
            log.error("Failed to set up base URI " );
            throw e;
        }

    }
    @When("user sends a GET request to endpoint {string}")
    public void user_sends_a_get_request_to_endpoint(String endpoint) {
        try {
            // Fetch the request from TestContext
            request = testContext.getRequest();

            // Replace placeholders dynamically based on entries in TestContext
            Pattern placeholderPattern = Pattern.compile("\\{([^}]+)\\}");
            Matcher matcher = placeholderPattern.matcher(endpoint);

            while (matcher.find()) {
                String placeholder = matcher.group(1);
                String value = testContext.getAttribute(placeholder); // Fetch value from TestContext
                if (value != null) {
                    endpoint = endpoint.replace("{" + placeholder + "}", value);
                } else {
                    log.warn("No value found in TestContext for placeholder: " + placeholder);
                }
            }

            log.info("Sending GET request to endpoint: " + endpoint);

            // Send the GET request
            response = request.get(endpoint);

            // Log response time
            long responseTime = response.time();
            test.info("Response time: " + responseTime + " ms");
            log.info("Response time: " + responseTime + " ms");

            // Store and log the response
            testContext.setResponse(response);
            test.info("Response body is :\n<pre>" + response.getBody().asPrettyString() + "</pre>");
            log.info("Response body is :\n<pre>" + response.getBody().asPrettyString() + "</pre>");

        } catch (Exception e) {
            log.error("Failed to send GET request to endpoint: " + endpoint, e);
            throw e;
        }


    }
    @Then("user receives response code as {int}")
    public void user_recieves_response_code_as(Integer expectedStatusCode) {
        try {
            if (testContext.getResponse() == null) {
                log.error("Response is null");
                throw new NullPointerException("Response object in testContext is null");
            }

            String responseBody = testContext.getResponse().getBody().asPrettyString();
            int actualStatusCode = testContext.getResponse().getStatusCode();

            if (test == null) {
                log.error("ExtentTest object 'test' is null");
                throw new NullPointerException("ExtentTest object 'test' is null");
            }

            test.info("Response Status code: " + actualStatusCode);
            log.info("Validating response status code. Expected: " + expectedStatusCode + ", Actual: " + actualStatusCode);

            Assert.assertEquals(actualStatusCode, expectedStatusCode);

        } catch (AssertionError e) {
            log.error("Response code mismatch. Expected: " + expectedStatusCode + ", but got: " + (testContext.getResponse() != null ? testContext.getResponse().getStatusCode() : "null"), e);
            throw e;
        } catch (Exception e) {
            log.error("Error while validating response code", e);
            throw e;
        }


    }
    @Then("user validates the response body as JSON")
    public void user_validates_the_response_body_as_json() {
        try {
            // Ensure the response is available
            response = testContext.getResponse();
            if (response == null) {
                log.error("Response is null. Ensure a request has been sent before validation.");
                throw new NullPointerException("Response is null.");
            }

            // Validate if the response body is in JSON format
            boolean isJson = testContext.isResponseBodyJson();
            if (isJson) {
                log.info("Validation passed: Response body is in JSON format.");
                test.info("Validation passed: Response body is in JSON format.");
            } else {
                log.error("Validation failed: Response body is NOT in JSON format.");
                test.fail("Validation failed: Response body is NOT in JSON format.");
                throw new AssertionError("Response body is not in JSON format.");
            }
        } catch (Exception e) {
            log.error("Error during response body validation.", e);
            test.fail("Error during response body validation: " + e.getMessage());
            throw e;
        }
    }

}
