package ui;

import actions.Actions;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

@Slf4j
public class productPage extends Actions {

    By BTN_addOneToCart = By.xpath("(//button[text() = 'Add to cart'])[1]");
    By BTN_cartProducts = By.xpath("//a[@class='shopping_cart_link']");
    By BTN_removeProduct = By.xpath("(//button[text()='Remove'])[1]");
    By BTN_checkout = By.xpath("//button[@id='checkout']");
    By BTN_continue = By.xpath("//input[@id='continue']");
    By BTN_finish = By.xpath("//button[@id='finish']");
    //Fomulario de checkout
    By INP_firstName = By.xpath("//input[@id='first-name']");
    By INP_lastName = By.xpath("//input[@id='last-name']");
    By INP_postalCode = By.xpath("//input[@id='postal-code']");

    public void addOneProduct() {
        waitForPresence(BTN_addOneToCart);
        click(BTN_addOneToCart);
        takeScreenshotAllure("Captura después de agregar un producto al carrito");
    }

    public void addProducts(int numberOfProducts) {
        // Obtener todos los botones actuales
        List<WebElement> botones = getDriver().findElements(By.xpath("//button[text()='Add to cart']"));

        // Validar que haya suficientes productos
        if (botones.size() < numberOfProducts) {
            throw new AssertionError("Se pidieron " + numberOfProducts + " productos, pero solo hay " + botones.size());
        }

        // Recorrer de mayor a menor
        for (int i = numberOfProducts; i > 0; i--) {
            By BTN_addProductsToCart = By.xpath("(//button[text()='Add to cart'])[" + i + "]");
            waitForPresence(BTN_addProductsToCart);
            click(BTN_addProductsToCart);
            takeScreenshotAllure("Captura después de agregar el producto " + i + " al carrito");
        }
    }

    public void goToCart() {
        waitForPresence(BTN_cartProducts);
        click(BTN_cartProducts);
        takeScreenshotAllure("Captura después de ir al carrito");
    }

    public void removeOneProduct() {
        waitForPresence(BTN_removeProduct);
        click(BTN_removeProduct);
        takeScreenshotAllure("Captura después de eliminar el producto del carrito");
    }

    public void removeProducts(int numberOfProductsToRemove) {
        log.debug("Eliminando {} productos del carrito...", numberOfProductsToRemove);

        // Buscar todos los botones actuales
        List<WebElement> botones = getDriver().findElements(By.xpath("//button[text()='Remove']"));

        if (botones.size() < numberOfProductsToRemove) {
            throw new AssertionError("Se pidieron eliminar " + numberOfProductsToRemove +
                    " productos, pero solo hay " + botones.size());
        }

        // Eliminar de mayor a menor para evitar problemas de índices
        for (int i = numberOfProductsToRemove; i > 0; i--) {
            By BTN_removeProduct = By.xpath("(//button[text()='Remove'])[1]");
            waitForPresence(BTN_removeProduct);
            click(BTN_removeProduct);
            takeScreenshotAllure("Captura después de eliminar producto " + i);
        }

        log.info("{} productos fueron eliminados del carrito.", numberOfProductsToRemove);
    }

    public void clickButtonContinue() {
        waitForPresence(BTN_continue);
        click(BTN_continue);
        takeScreenshotAllure("Captura después de hacer clic en el botón Continuar");
    }

    public void clickButtonCheckout() {
        waitForPresence(BTN_checkout);
        click(BTN_checkout);
        takeScreenshotAllure("Captura después de hacer clic en el botón Checkout");
    }

    public void fillCheckOutForm(String firstName, String lastName, String postalCode) {
        waitForPresence(INP_firstName);
        setText(INP_firstName, firstName);
        setText(INP_lastName, lastName);
        setText(INP_postalCode, postalCode);
        takeScreenshotAllure("Captura después de llenar el formulario de checkout");
        clickButtonContinue();
        click(BTN_finish);
        takeScreenshotAllure("Captura después de hacer clic en el botón Finish");
    }
}
