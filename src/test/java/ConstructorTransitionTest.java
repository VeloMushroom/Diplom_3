import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import page.MainPage;

import java.util.Objects;

@RunWith(Parameterized.class)
public class ConstructorTransitionTest {

    @Rule
    public DriverFactory driverFactory = new DriverFactory();
    private WebDriver driver;
    private final By tab;
    private final String name;

    public ConstructorTransitionTest(By tab,String name) {
        this.tab = tab;
        this.name = name;
    }



    @Before
    public void before() {
        driver = driverFactory.getDriver();
        driver.get(Constants.MAIN_URL);
    }

    @Parameterized.Parameters
    public static Object[][] getOrderData() {
        return new Object[][] {
                {MainPage.bunTab, "Булки"},
                {MainPage.sauceTab, "Соусы"},
                {MainPage.fillingTab, "Начинки"},
        };
    }

    @Test
    @DisplayName("Переход на вкладку")
    public void transitionBunsTest() {
        MainPage mainPage = new MainPage(driver);

        if (Objects.equals(name, "Булки")){
        mainPage.clickTabButton("Начинки");
        mainPage.clickTabButton(name);
        } else {
            mainPage.clickTabButton(name);
        }

        new WebDriverWait(driver, Constants.TIMER).until(ExpectedConditions.attributeContains(tab, "class", "current"));
        Assert.assertThat(driver.findElement(tab).getAttribute("class"), CoreMatchers.containsString("current"));
    }
}
