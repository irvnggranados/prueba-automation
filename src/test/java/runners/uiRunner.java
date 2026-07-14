package runners;

import actions.Actions;
import actions.AllureReportGenerator;
import io.qameta.allure.Allure;
import org.testng.annotations.*;
import ui.loginPage;
import lombok.extern.slf4j.Slf4j;
import ui.productPage;
import utils.RetryAnalyzer;

@Slf4j
public class uiRunner extends Actions{

    loginPage login = new loginPage();
    productPage product = new productPage();

    @BeforeMethod
    public void setUp() {
        log.info("Iniciando setUp");
        setUpDriver();
    }

    @Test(enabled = true, retryAnalyzer = RetryAnalyzer.class)
    public void LoginExistoso() {
        Allure.step("Inicio de sesión con credenciales correctas", ()->{
            String username = properties.getProperty("user");
            String password = properties.getProperty("password");
            login.login(username, password);
            assertMessage("*","Products");
        });
    }

    @Test(enabled = true, retryAnalyzer = RetryAnalyzer.class)
    public void LoginFallido() {
        Allure.step("Inicio de sesión con credenciales incorrectas", ()->{
            log.info("Iniciando prueba: LoginFallido");
            String username = properties.getProperty("userfallido");
            String password = properties.getProperty("password");
            login.login(username, password);
            assertMessage("*","this user has been locked");
        });
    }

    @Test(enabled = true, retryAnalyzer = RetryAnalyzer.class)
    public void addOneProduct() {
        Allure.step("Inicio de sesión con credenciales correctas", ()->{
            String username = properties.getProperty("user");
            String password = properties.getProperty("password");
            login.login(username, password);
            assertMessage("*","Products");
        });

        Allure.step("Agregar un producto al carrito", ()->{
            product.addOneProduct();
            assertMessage("span","1");
        });

    }

    @Test(enabled = true, retryAnalyzer = RetryAnalyzer.class)
    public void addProducts() {
        Allure.step("Inicio de sesión con credenciales correctas", ()->{
            String username = properties.getProperty("user");
            String password = properties.getProperty("password");
            login.login(username, password);
            assertMessage("*","Products");
        });

        Allure.step("Agregar múltiples productos al carrito", ()->{
            product.addProducts(3);
            assertMessage("span","3");
        });

    }

    @Test(enabled = true, retryAnalyzer = RetryAnalyzer.class)
    public void checkOneProduct() {
        Allure.step("Inicio de sesión con credenciales correctas", ()->{
            String username = properties.getProperty("user");
            String password = properties.getProperty("password");
            login.login(username, password);
            assertMessage("*","Products");
        });

        Allure.step("Agregar un producto al carrito", ()->{
            product.addOneProduct();
            assertMessage("span","1");
        });

        Allure.step("Ir al carrito", ()->{
            product.goToCart();
            assertMessage("div","1");
        });

    }

    @Test(enabled = true, retryAnalyzer = RetryAnalyzer.class)
    public void checkProducts() {
        Allure.step("Inicio de sesión con credenciales correctas", ()->{
            String username = properties.getProperty("user");
            String password = properties.getProperty("password");
            login.login(username, password);
            assertMessage("*","Products");
        });

        Allure.step("Agregar múltiples productos al carrito", ()->{
            product.addProducts(5);
            assertMessage("span","5");
        });

        Allure.step("Ir al carrito", ()->{
            product.goToCart();
            assertMessages("*", "Remove", 5);
        });

    }

    @Test(enabled = true, retryAnalyzer = RetryAnalyzer.class)
    public void removeOneProduct() {
        Allure.step("Inicio de sesión con credenciales correctas", ()->{
            String username = properties.getProperty("user");
            String password = properties.getProperty("password");
            login.login(username, password);
            assertMessage("*","Products");
        });

        Allure.step("Agregar múltiples productos al carrito", ()->{
            product.addProducts(2);
            assertMessage("span","2");
        });

        Allure.step("Ir al carrito", ()->{
            product.goToCart();
            assertMessages("*", "Remove", 2);
        });

        Allure.step("Eliminar un producto del carrito", ()->{
            product.removeOneProduct();
        });

    }

    @Test(enabled = true, retryAnalyzer = RetryAnalyzer.class)
    public void removeProducts() {
        Allure.step("Inicio de sesión con credenciales correctas", ()->{
            String username = properties.getProperty("user");
            String password = properties.getProperty("password");
            login.login(username, password);
            assertMessage("*","Products");
        });

        Allure.step("Agregar múltiples productos al carrito", ()->{
            product.addProducts(2);
            assertMessage("span","2");
        });

        Allure.step("Ir al carrito", ()->{
            product.goToCart();
            assertMessages("*", "Remove", 2);
        });

        Allure.step("Eliminar un producto del carrito", ()->{
            product.removeProducts(2);
        });

    }

    @Test(enabled = true, retryAnalyzer = RetryAnalyzer.class)
    public void validateCheckoutForm() {
        Allure.step("Inicio de sesión con credenciales correctas", ()->{
            String username = properties.getProperty("user");
            String password = properties.getProperty("password");
            login.login(username, password);
            assertMessage("*","Products");
        });

        Allure.step("Agregar múltiples productos al carrito", ()->{
            product.addProducts(2);
            assertMessage("span","2");
        });

        Allure.step("Ir al carrito", ()->{
            product.goToCart();
            assertMessages("*", "Remove", 2);
        });

        Allure.step("Hacer clic en el botón Checkout", ()->{
            product.clickButtonCheckout();
            assertMessage("*","Checkout: Your Information");
        });

        Allure.step("Hacer clic en el botón Continuar", ()->{
            product.clickButtonContinue();
            assertMessage("*","Error");
        });
    }

    @Test(enabled = true, retryAnalyzer = RetryAnalyzer.class)
    public void fillCheckoutForm() {
        Allure.step("Inicio de sesión con credenciales correctas", ()->{
            String username = properties.getProperty("user");
            String password = properties.getProperty("password");
            login.login(username, password);
            assertMessage("*","Products");
        });

        Allure.step("Agregar múltiples productos al carrito", ()->{
            product.addProducts(2);
            assertMessage("span","2");
        });

        Allure.step("Ir al carrito", ()->{
            product.goToCart();
            assertMessages("*", "Remove", 2);
        });

        Allure.step("Hacer clic en el botón Checkout", ()->{
            product.clickButtonCheckout();
            assertMessage("*","Checkout: Your Information");
        });

        Allure.step("Hacer clic en el botón Continuar", ()->{
            product.fillCheckOutForm(properties.getProperty("name"),properties.getProperty("lastName"), properties.getProperty("postalCode") );
            assertMessage("*","Thank you for your order!");
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
