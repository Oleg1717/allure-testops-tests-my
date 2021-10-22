package cloud.autotests.allure.api.requests;

import cloud.autotests.allure.api.data.EndPoints;
import cloud.autotests.allure.api.helpers.RestAssuredFilter;
import cloud.autotests.allure.config.ConfigHelper;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserRequests {

    public static Response authorize() {
        return given()
                .baseUri(ConfigHelper.getApiBaseUri())
                .filter(RestAssuredFilter.withCustomTemplates())
                .header("X-XSRF-TOKEN", ConfigHelper.getXsrfToken())
                .cookie("XSRF-TOKEN", ConfigHelper.getXsrfToken())
                .formParam("username", ConfigHelper.getUserLogin())
                .formParam("password", ConfigHelper.getUserPassword())
                .when()
                .post(EndPoints.USER_LOGIN)
                .then()
                .statusCode(200)
                .extract().response();
    }
}