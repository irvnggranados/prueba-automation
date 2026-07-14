package actions;

import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

@Slf4j
public class Actions {

    // Driver
    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    protected static ThreadLocal<WebDriverWait> wait = new ThreadLocal<>();

    // Configuración
    protected static final Properties properties = new Properties();

    // Cargar propiedades
    static {
        try (InputStream input = Actions.class.getClassLoader()
                .getResourceAsStream("environments/qa.properties")) {
            if (input == null) {
                throw new RuntimeException("No se encontró qa.properties en resources");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error cargando qa.properties", e);
        }
    }

    // Driver
    public void setUpDriver() {

        String browser = properties.getProperty("browser", "chrome");
        boolean headless = Boolean.parseBoolean(properties.getProperty("headless", "false"));
        int explicitWait = Integer.parseInt(properties.getProperty("explicit.wait", "15"));
        boolean maximize = Boolean.parseBoolean(properties.getProperty("maximize", "true"));

        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();

            // Ajustes comunes
            options.addArguments("--guest");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-popup-blocking");
            options.addArguments("--disable-save-password-bubble");
            options.addArguments("--disable-translate");
            options.addArguments("--disable-infobars");
            options.addArguments("--disable-extensions");
            options.addArguments("--no-first-run");
            options.addArguments("--no-default-browser-check");

            Map<String, Object> prefs = new HashMap<>();
            prefs.put("safebrowsing.enabled", true);
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            options.setExperimentalOption("prefs", prefs);

            // Diferencia entre local y docker
            if (headless) {
                // Flags obligatorias en Docker/Linux
                options.addArguments("--headless=new");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-gpu");
                options.addArguments("--window-size=1920,1080");
            } else {
                // En local abre ventana normal
                if (maximize) {
                    options.addArguments("--start-maximized");
                }
            }

            driver.set(new ChromeDriver(options));

        } else {
            throw new UnsupportedOperationException("Browser no soportado: " + browser);
        }

        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(
                Integer.parseInt(properties.getProperty("implicit.wait", "5"))
        ));

        wait.set(new WebDriverWait(driver.get(), Duration.ofSeconds(explicitWait)));

        // Navegar a la URL base
        driver.get().get(properties.getProperty("base.url"));
        log.info("Navegando a la URL base: {}", properties.getProperty("base.url"));
    }

    public WebDriver getDriver() {
        return driver.get();
    }

    public void tearDown() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
            wait.remove();
            log.info("Driver cerrado y recursos liberados");
        }
    }

    // Waits
    protected void waitForPresence(By locator) {
        log.debug("Esperando presencia de: {}", locator);
        wait.get().until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected void waitForVisibility(By locator) {
        log.debug("Esperando visibilidad de: {}", locator);
        wait.get().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void waitForClickable(By locator) {
        log.debug("Esperando que sea clickeable: {}", locator);
        wait.get().until(ExpectedConditions.elementToBeClickable(locator));
    }

    // Actions
    protected void click(By locator) {
        log.debug("Intentando click en: {}", locator);
        waitForClickable(locator);
        getDriver().findElement(locator).click();
        log.info("Click realizado en: {}", locator);
    }

    protected void setText(By locator, String text) {
        log.debug("Intentando enviar texto '{}' al campo {}", text, locator);
        waitForVisibility(locator);
        WebElement element = getDriver().findElement(locator);
        element.clear();
        element.sendKeys(text);
        log.info("Texto '{}' enviado correctamente al campo {}", text, locator);
    }

    protected void clear(By locator) {
        log.debug("Limpiando campo: {}", locator);
        waitForVisibility(locator);
        getDriver().findElement(locator).clear();
        log.info("Campo limpiado: {}", locator);
    }

    protected String getText(By locator) {
        waitForVisibility(locator);
        String text = getDriver().findElement(locator).getText();
        log.info("Texto obtenido de {}: {}", locator, text);
        return text;
    }

    protected String getAttribute(By locator, String attribute) {
        waitForVisibility(locator);
        String value = getDriver().findElement(locator).getAttribute(attribute);
        log.info("Atributo '{}' de {}: {}", attribute, locator, value);
        return value;
    }

    protected boolean isDisplayed(By locator) {
        try {
            boolean displayed = getDriver().findElement(locator).isDisplayed();
            log.info("Elemento {} está visible: {}", locator, displayed);
            return displayed;
        } catch (NoSuchElementException e) {
            log.warn("Elemento {} no encontrado", locator);
            return false;
        }
    }

    // Browser
    protected void scrollIntoView(By locator) {
        log.debug("Haciendo scroll hacia: {}", locator);
        WebElement element = getDriver().findElement(locator);
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected void scrollToTop() {
        log.debug("Haciendo scroll al inicio");
        ((JavascriptExecutor) getDriver()).executeScript("window.scrollTo(0,0)");
    }

    protected void scrollToBottom() {
        log.debug("Haciendo scroll al final");
        ((JavascriptExecutor) getDriver()).executeScript("window.scrollTo(0,document.body.scrollHeight)");
    }

    // Evidencias
    protected void takeScreenshot(String description) {
        log.debug("Tomando screenshot: {}", description);
        TakesScreenshot ts = (TakesScreenshot) getDriver();
        byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
        log.info("Screenshot tomado: {} ({} bytes)", description, screenshot.length);
    }

    // Validación de mensajes
    protected void assertMessage(String element, String expectedText) {
        log.debug("Validando mensaje esperado: '{}'", expectedText);
        try {
            WebDriverWait localWait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
            WebElement mensaje = localWait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("(//" + element + "[contains(text(),'" + expectedText + "')])[1]")
                    )
            );

            String actualText = mensaje.getText();
            if (!actualText.contains(expectedText)) {
                log.error(" Se esperaba: '{}' pero se encontró: '{}'", expectedText, actualText);
                throw new AssertionError("Se esperaba: '" + expectedText +
                        "' pero se encontró: '" + actualText + "'");
            }
            log.info(" Mensaje validado correctamente: {}", actualText);
        } catch (TimeoutException e) {
            log.error(" No se encontró el mensaje esperado: '{}'", expectedText);
            throw new AssertionError("No se encontró el mensaje esperado: '" + expectedText + "'");
        }
    }

    public void takeScreenshotAllure(String descripcion) {

        byte[] screenshot =
                ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);

        Allure.addAttachment(
                descripcion,
                "image/png",
                new ByteArrayInputStream(screenshot),
                ".png"
        );
    }
    protected void assertMessages(String element, String expectedText, int count) {
        log.debug("Validando que el mensaje '{}' aparezca {} veces", expectedText, count);

        try {
            WebDriverWait localWait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));

            // Buscar todos los elementos que coincidan
            List<WebElement> mensajes = localWait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(
                            By.xpath("//" + element + "[contains(text(),'" + expectedText + "')]")
                    )
            );

            int actualCount = mensajes.size();

            if (actualCount != count) {
                log.error("Se esperaba que '{}' apareciera {} veces, pero se encontró {} veces",
                        expectedText, count, actualCount);
                throw new AssertionError("Se esperaba que '" + expectedText +
                        "' apareciera " + count + " veces, pero se encontró " + actualCount);
            }

            log.info("Mensaje '{}' validado correctamente, aparece {} veces", expectedText, actualCount);

        } catch (TimeoutException e) {
            log.error("No se encontró el mensaje esperado: '{}'", expectedText);
            throw new AssertionError("No se encontró el mensaje esperado: '" + expectedText + "'");
        }
    }
}