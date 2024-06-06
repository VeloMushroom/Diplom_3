package api.helpers;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import java.net.HttpURLConnection;

public class UserChecks {

    @Step("Check user created successfully")
    public String createdUserSuccessfully(ValidatableResponse createUserResponse) {
        return createUserResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .extract()
                .path("accessToken");
    }
}
