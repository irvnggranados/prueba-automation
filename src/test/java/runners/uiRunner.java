package runners;

import actions.Actions;
import actions.AllureReportGenerator;
import io.qameta.allure.Allure;
import org.testng.annotations.*;
import ui.loginPage;
import lombok.extern.slf4j.Slf4j;
import utils.RetryAnalyzer;

@Slf4j
public class uiRunner extends Actions{

    loginPage login = new loginPage();

    @BeforeMethod
    public void setUp() {
        log.info("Iniciando setUp");
        setUpDriver();
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void LoginExistoso() {
        Allure.step("Inicio de sesión con credenciales correctas", ()->{
            String username = properties.getProperty("user");
            String password = properties.getProperty("password");
            login.login(username, password);
            //assertMessage("Products");
            assertMessage("j23gh iuewgwehgwekujhwegyuwejgwe");
        });
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void LoginFallido() {
        Allure.step("Inicio de sesión con credenciales incorrectas", ()->{
            log.info("Iniciando prueba: LoginFallido");
            String username = properties.getProperty("userfallido");
            String password = properties.getProperty("password");
            login.login(username, password);
            assertMessage("this user has been locked");
        });
    }

    @AfterMethod
    public void tearDownTest() {
        log.info("Ejecutando tearDown");
        tearDown();
    }

    @AfterSuite
    public void generarReporte() {
        AllureReportGenerator.generaReport();
    }
}
