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
import page.KabinetPage;
import page.LoginPage;
import page.MainPage;

public class RedirectOutKabinetPageTest {

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

        MainPage mainPage = new MainPage(driver);
        mainPage.clickKabinetButton();
        new WebDriverWait(driver, Constants.TIMER).until(ExpectedConditions.urlToBe(Constants.KABINET_URL));
    }

    @Test
    @DisplayName("Переход из личного кабинета по кнопке конструктора")
    public void redirectOutClickConstructTest(){
        KabinetPage kabinetPage = new KabinetPage(driver);
        kabinetPage.clickLogonButton();
        new WebDriverWait(driver, Constants.TIMER).until(ExpectedConditions.urlToBe(Constants.MAIN_URL));
        Assert.assertEquals(driver.getCurrentUrl(), Constants.MAIN_URL);
    }

    @Test
    @DisplayName("Переход из личного кабинета по логотипу")
    public void redirectOutClickLogoTest(){
        KabinetPage kabinetPage = new KabinetPage(driver);
        kabinetPage.clickCunstructorButton();
        new WebDriverWait(driver, Constants.TIMER).until(ExpectedConditions.urlToBe(Constants.MAIN_URL));
        Assert.assertEquals(driver.getCurrentUrl(), Constants.MAIN_URL);

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
