package page;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RecoveryPage {
    private WebDriver driver;

    public RecoveryPage(WebDriver driver) {
        this.driver = driver;
    }

    private final By loginButton = By.xpath(".//a[@class='Auth_link__1fOlj']");

    @Step("Click login button")
    public void clickLoginButton() {
        driver.findElement(loginButton).click();
    }
}
