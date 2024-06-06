package page;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegistrationPage {
    private WebDriver driver;

    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
    }

    private final By nameInput = By.xpath(".//label[text()='Имя']/../input");
    private final By emailInput = By.xpath(".//label[text()='Email']/../input");
    private final By passwordInput = By.xpath(".//input[@name='Пароль']");
    private final By registrationButton = By.xpath(".//button[@class='button_button__33qZ0 button_button_type_primary__1O7Bx button_button_size_medium__3zxIa']");
    public By incorrectPassword = By.xpath(".//p[@class='input__error text_type_main-default']");
    private final By loginButton = By.xpath(".//a[@class='Auth_link__1fOlj']");

    public void clickregistrationButton() {
        driver.findElement(registrationButton).click();
    }

    public void inputName(String name) {
        driver.findElement(nameInput).sendKeys(name);
    }

    public void inputEmail(String email) {
        driver.findElement(emailInput).sendKeys(email);
    }

    public void inputPassword(String password) {
        driver.findElement(passwordInput).sendKeys(password);
    }

    @Step("Get incorrect password message")
    public String getIncorrectPasswordMessage() {
        return driver.findElement(incorrectPassword).getText();
    }

    @Step("Click login button")
    public void clickLoginButton() {
        driver.findElement(loginButton).click();
    }
}
