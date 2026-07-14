package api;

import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiClient {

    public static Response getRazas() {

        Response response = RestAssured
                .given()
                .baseUri("https://dog.ceo/api")
                .log().all()          // Request en consola
                .when()
                .get("/breeds/list/all")
                .then()
                .log().all()          // Response en consola
                .extract()
                .response();

        // Adjuntar Request
        Allure.addAttachment(
                "Request",
                "GET https://dog.ceo/api/breeds/list/all"
        );

        // Adjuntar Response
        Allure.addAttachment(
                "Response Body",
                "application/json",
                response.getBody().prettyPrint(),
                ".json"
        );

        // Adjuntar Headers
        Allure.addAttachment(
                "Response Headers",
                response.getHeaders().toString()
        );

        return response;
    }
}
