package runners;

import actions.Actions;
import actions.AllureReportGenerator;
import io.qameta.allure.Allure;
import org.testng.annotations.*;
import ui.LoginPage;
import lombok.extern.slf4j.Slf4j;
import ui.ProductPage;
import utils.RetryAnalyzer;

@Slf4j
public class UiRunner extends Actions{

    LoginPage login = new LoginPage();
    ProductPage product = new ProductPage();

    @BeforeMethod
    public void setUp() {
        log.info("Iniciando setUp");
        setUpDriver();
    }

    @Test(priority = 1, enabled = true, retryAnalyzer = RetryAnalyzer.class)
    public void LoginExistoso() {
        Allure.step("Inicio de sesión con credenciales correctas", ()->{
            String username = properties.getProperty("user");
            String password = properties.getProperty("password");
            login.login(username, password);
            assertMessage("*","Products");
        });
    }

    @Test(priority = 2, enabled = true, retryAnalyzer = RetryAnalyzer.class)
    public void LoginFallido() {
        Allure.step("Inicio de sesión con credenciales incorrectas", ()->{
            log.info("Iniciando prueba: LoginFallido");
            String username = properties.getProperty("userfallido");
            String password = properties.getProperty("password");
            login.login(username, password);
            assertMessage("*","this user has been locked");
        });
    }

    @Test(priority = 3, enabled = true, retryAnalyzer = RetryAnalyzer.class)
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

    @Test(priority = 4, enabled = true, retryAnalyzer = RetryAnalyzer.class)
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

    @Test(priority = 5, enabled = true, retryAnalyzer = RetryAnalyzer.class)
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

    @Test(priority = 6, enabled = true, retryAnalyzer = RetryAnalyzer.class)
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

    @Test(priority = 7, enabled = true, retryAnalyzer = RetryAnalyzer.class)
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

    @Test(priority = 8, enabled = true, retryAnalyzer = RetryAnalyzer.class)
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

    @Test(priority = 9, enabled = true, retryAnalyzer = RetryAnalyzer.class)
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

    @Test(priority = 10, enabled = true, retryAnalyzer = RetryAnalyzer.class)
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
