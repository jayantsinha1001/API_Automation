package Utils;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class RestAssuredRequestFilter implements Filter {
    public static final Logger LOG = LogManager.getLogger(RestAssuredRequestFilter.class);
    public static boolean isScenarioRunning = true;
    public static final List<Response> responseList = new ArrayList<>();

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);
        if (isScenarioRunning) {
            responseList.add(response);
        }
        return response;
    }

    public static void setScenarioRunning(boolean isRunning) {
        isScenarioRunning = isRunning;
    }

//    public static void printAllResponses() {
//        LOG.info("Printing all collected responses:");
//        for (Response response : responseList) {
//            LOG.info("----------------------------------------------------------------");
//            LOG.info(" Response Status => " + response.getStatusLine() +
//                    "\n Response Header =>\n" + response.getHeaders() +
//                    "\n Response Body => " + response.getBody().prettyPrint());
//            LOG.info("----------------------------------------------------------------");
//        }
//        responseList.clear();
//    }
}
