import api.helpers.DeleteUserClient;
import api.helpers.StorageTokenClient;
import api.helpers.UserChecks;
import api.helpers.UserCreateClient;
import api.pojo.UserCreate;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import page.LoginPage;
import page.MainPage;
import page.RecoveryPage;
import page.RegistrationPage;

public class LoginTest {

    @Rule
    public DriverFactory driverFactory = new DriverFactory();
    private StorageTokenClient storageTokenClient = new StorageTokenClient();
    private WebDriver driver;
    private LoginPage loginPage;
    private final UserCreateClient client = new UserCreateClient();
    private final UserChecks check = new UserChecks();
    private String email;
    private String password;
    private String storageToken;
    private String token;

    @Before
    public void before() {
        driver = driverFactory.getDriver();
        loginPage = new LoginPage(driver);

        UserCreate user = UserCreate.random();
        email = user.getEmail();
        password = user.getPassword();
        ValidatableResponse createUserResponse = client.userCreate(user);
        token = check.createdUserSuccessfully(createUserResponse);
    }

    @Test
    @DisplayName("Логин через главную страницу")
    public void loginMainPageTest() {
        MainPage mainPage = new MainPage(driver);
        driver.get(Constants.MAIN_URL);
        mainPage.clickLoginButton();

        loginPage.inputEmail(email);
        loginPage.inputPassword(password);
        loginPage.clickLoginButton();

        MainPage.waitOrderButtonClickable(driver);
        storageToken = storageTokenClient.downloadStorageAccessToken(driver);
        Assert.assertNotNull(storageToken);
    }

    @Test
    @DisplayName("Логин через личный кабинет")
    public void loginKabinetPageTest() {
        MainPage mainPage = new MainPage(driver);
        driver.get(Constants.MAIN_URL);
        mainPage.clickKabinetButton();

        loginPage.inputEmail(email);
        loginPage.inputPassword(password);
        loginPage.clickLoginButton();

        MainPage.waitOrderButtonClickable(driver);
        storageToken = storageTokenClient.downloadStorageAccessToken(driver);
        Assert.assertNotNull(storageToken);

    }

    @Test
    @DisplayName("Логин через форму регистрации")
    public void loginRegistrationPageTest() {
        RegistrationPage registrationPage = new RegistrationPage(driver);
        driver.get(Constants.RESISTRATION_URL);
        registrationPage.clickLoginButton();

        new WebDriverWait(driver, Constants.TIMER).until(ExpectedConditions.urlToBe(Constants.LOGIN_URL));

        loginPage.inputEmail(email);
        loginPage.inputPassword(password);
        loginPage.clickLoginButton();

        MainPage.waitOrderButtonClickable(driver);
        storageToken = storageTokenClient.downloadStorageAccessToken(driver);
        Assert.assertNotNull(storageToken);
    }

    @Test
    @DisplayName("Логин через форму восстановления пароля")
    public void loginRecoveryPageTest() {
        RecoveryPage recoveryPage = new RecoveryPage(driver);
        driver.get(Constants.RECOVERY_URL);
        recoveryPage.clickLoginButton();

        new WebDriverWait(driver, Constants.TIMER).until(ExpectedConditions.urlToBe(Constants.LOGIN_URL));

        loginPage.inputEmail(email);
        loginPage.inputPassword(password);
        loginPage.clickLoginButton();

        MainPage.waitOrderButtonClickable(driver);
        storageToken = storageTokenClient.downloadStorageAccessToken(driver);
        Assert.assertNotNull(storageToken);
    }

    @After
    public void after() {
        if (storageToken != null) {
            DeleteUserClient deleteClient = new DeleteUserClient();
            deleteClient.userDelete(storageToken);
        } else {
            DeleteUserClient deleteClient = new DeleteUserClient();
            deleteClient.userDelete(token);
        }
    }
}