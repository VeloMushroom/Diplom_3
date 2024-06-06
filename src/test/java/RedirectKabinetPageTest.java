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

import static page.MainPage.orderButton;

public class RedirectKabinetPageTest {

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

        driver.get(Constants.LOGIN_URL);
        loginPage.inputEmail(email);
        loginPage.inputPassword(password);
        loginPage.clickLoginButton();

        MainPage.waitOrderButtonClickable(driver);
        storageToken = storageTokenClient.downloadStorageAccessToken(driver);
    }

    @Test
    @DisplayName("Переход в личный кабинет с главной страницы")
    public void redirectMainPageTest() {
        MainPage mainPage = new MainPage(driver);
        mainPage.clickKabinetButton();
        new WebDriverWait(driver, Constants.TIMER).until(ExpectedConditions.urlToBe(Constants.KABINET_URL));
        Assert.assertEquals(driver.getCurrentUrl(), Constants.KABINET_URL);
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
