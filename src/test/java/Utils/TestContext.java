package Utils;

import com.github.dzieciou.testing.curl.CurlRestAssuredConfigFactory;
import com.github.dzieciou.testing.curl.Options;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TestContext {

    @Setter
    @Getter
    private Response response;

    @Getter
    @Setter
    private String requestBody;

    private RequestSpecification requestSpecification;
    private final Map<String, Object> session = new HashMap<>();
    private static final String CONTENT_TYPE = PropertiesFile.getProperty("content.type");
    private static final String BASE_URI = PropertiesFile.getProperty("baseUri");


    public RequestSpecification requestSetup() {
        RestAssured.reset();
        Options options = Options.builder().logStacktrace().build();
        RestAssuredConfig config = CurlRestAssuredConfigFactory.createConfig(options);

        RestAssured.baseURI = BASE_URI;

        requestSpecification = RestAssured.given()
                .config(config)
                .filter(new RestAssuredRequestFilter())
                .contentType(CONTENT_TYPE)
                .accept(CONTENT_TYPE)
                .baseUri(BASE_URI);

        return requestSpecification;
    }

    public RequestSpecification getRequest() {
        if (requestSpecification == null) {
            throw new IllegalStateException("RequestSpecification is not initialized. Call requestSetup() first.");
        }
        return requestSpecification;
    }

    // Retrieve a string attribute from the session map
    public String getAttribute(String key) {
        Object value = session.get(key);
        return value != null ? value.toString() : null;
    }

    // Method to validate if the response body is in JSON format
    public boolean isResponseBodyJson() {
        if (response == null) {
            throw new IllegalStateException("Response is not available. Ensure a request has been sent.");
        }

        try {
            String responseBody = response.getBody().asString();
            JsonElement jsonElement = JsonParser.parseString(responseBody); // Parse response body
            return jsonElement.isJsonObject() || jsonElement.isJsonArray(); // Check if valid JSON
        } catch (Exception e) {
            return false;
        }
    }
}
