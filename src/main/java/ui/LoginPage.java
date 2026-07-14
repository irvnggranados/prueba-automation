package ui;

import actions.Actions;
import org.openqa.selenium.By;


public class LoginPage extends Actions{

    By INP_username = By.xpath("//input[ @data-test = 'username']");
    By INP_password = By.xpath("//input[ @data-test = 'password']");
    By BTN_login = By.xpath("//input[ @id=\"login-button\"]");

    public void login(String username, String password) {
        setText(INP_username,username);
        setText(INP_password, password);
        takeScreenshotAllure("Captura antes de hacer clic en el botón de inicio de sesión");
        click(BTN_login);
        takeScreenshotAllure("Captura después de hacer clic en el botón de inicio de sesión");
    }
}
