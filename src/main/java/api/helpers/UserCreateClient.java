package api.helpers;

import api.pojo.UserCreate;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class UserCreateClient extends Client {
    public static final String USER_REGISTER_PATH = "auth/register";

    @Step("Create user request")
    public ValidatableResponse userCreate (UserCreate userCreate) {
        return spec()
                .body(userCreate)
                .when()
                .post(USER_REGISTER_PATH)
                .then().log().all();
    }
}