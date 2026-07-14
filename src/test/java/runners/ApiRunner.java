package runners;

import actions.AllureReportGenerator;
import api.ApiClient;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;
import utils.RetryAnalyzer;

public class ApiRunner {

    ApiClient api = new ApiClient();

    @Test(enabled = true, retryAnalyzer = RetryAnalyzer.class)
    public void getTodasLasRazas() {

        Allure.step("Consultar endpoint de razas");

        Response response = ApiClient.getRazas();

        Assert.assertEquals(response.statusCode(),200);

    }


    @Test(enabled = true, retryAnalyzer = RetryAnalyzer.class)
    public void bulldogExists(){

        Response response = ApiClient.getRazas();

        Assert.assertEquals(response.statusCode(),200);

        Assert.assertTrue(
                response.jsonPath().getMap("message").containsKey("bulldog")
        );

    }

    @Test(enabled = true, retryAnalyzer = RetryAnalyzer.class)
    public void validarMessage(){

        Response response = ApiClient.getRazas();

        Assert.assertEquals(
                response.jsonPath().getString("status"),
                "success"
        );

    }


    @Test(enabled = true, retryAnalyzer = RetryAnalyzer.class)
    public void timepoRespuesta(){

        Response response = ApiClient.getRazas();

        Assert.assertTrue(
                response.time() < 2000
        );

    }

    @AfterSuite
    public void generarReporte() {
        AllureReportGenerator.generaReport();
    }

}
