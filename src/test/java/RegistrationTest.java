import api.helpers.DeleteUserClient;
import api.helpers.StorageTokenClient;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import io.qameta.allure.junit4.DisplayName;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import page.LoginPage;
import page.MainPage;
import page.RegistrationPage;
import com.github.javafaker.Faker;
import java.time.LocalDateTime;


public class RegistrationTest {

    @Rule
    public DriverFactory driverFactory = new DriverFactory();
    private StorageTokenClient storageTokenClient = new StorageTokenClient();
    private WebDriver driver;
    private RegistrationPage registrationPage;
    private LoginPage loginPage;
    private String token;
    Faker faker = new Faker();

    @Before
    public void before(){
        driver = driverFactory.getDriver();
        registrationPage = new RegistrationPage(driver);
        driver.get(Constants.RESISTRATION_URL);
    }

    @Test
    @DisplayName("Регистрация пользователя")
    public void registrationUserTest() {
        loginPage = new LoginPage(driver);
        String email = "Mushroom" + faker.name().firstName() + "@yandex.ru";
        String password = String.valueOf(LocalDateTime.now());

        registrationPage.inputName("Ivan" + faker.name().lastName());
        registrationPage.inputEmail(email);
        registrationPage.inputPassword(password);
        registrationPage.clickregistrationButton();

        new WebDriverWait(driver, Constants.TIMER).until(ExpectedConditions.urlToBe(Constants.LOGIN_URL));
        loginPage.inputEmail(email);
        loginPage.inputPassword(password);
        loginPage.clickLoginButton();

        MainPage.waitOrderButtonClickable(driver);
        token = storageTokenClient.downloadStorageAccessToken(driver);
        Assert.assertNotNull(token);
    }

    @Test
    @DisplayName("Ошибка для некорректного пароля")
    public void errorIncorrectPasswordTest() {
        registrationPage.inputName("Ivan" + faker.name().lastName());
        registrationPage.inputEmail("Mushroom" + faker.name().firstName() + "@yandex.ru");
        registrationPage.inputPassword("null");
        registrationPage.clickregistrationButton();

        new WebDriverWait(driver, Constants.TIMER).until(ExpectedConditions.visibilityOfElementLocated(registrationPage.incorrectPassword));
        Assert.assertEquals(registrationPage.getIncorrectPasswordMessage(), "Некорректный пароль");
    }

    @After
    public void after() {
        if (token != null) {
            DeleteUserClient deleteClient = new DeleteUserClient();
            deleteClient.userDelete(token);
        }
    }
}
