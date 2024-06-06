package api.helpers;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;

public class StorageTokenClient {

    @Step("Download token from storage")
    public String downloadStorageAccessToken(WebDriver driver){
        LocalStorage localStorage = ((WebStorage) driver).getLocalStorage();
        System.out.println(localStorage);
        return localStorage.getItem("accessToken");
    }
}
