package user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.*;

public class UserAssertion {

    @Step("Проверка создания нового пользователя")
    public String createSuccess(ValidatableResponse response) {
        return response.assertThat()
                .statusCode(HTTP_OK)
                .body("success", is(true))
                .extract()
                .path("accessToken");
    }

    @Step("Проверка создания существующего пользователя")
    public String createExisting(ValidatableResponse response) {
        return response.assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .body("success", is(false))
                .extract()
                .path("message");
    }

    @Step("Проверка неуспешного создания пользователя")
    public String createFail(ValidatableResponse response, int expectedCode) {
        return response.assertThat()
                .statusCode(expectedCode)
                .body("success", is(false))
                .extract()
                .path("message");
    }

    @Step("Проверка логина пользователя")
    public String loginSuccess(ValidatableResponse response) {
        return response.assertThat()
                .statusCode(HTTP_OK)
                .body("success", is(true))
                .extract()
                .path("accessToken");
    }

    @Step("Проверка неуспешного логина пользователя")
    public String loginFail(ValidatableResponse response, int expectedCode) {
        return response.assertThat()
                .statusCode(expectedCode)
                .body("success", is(false))
                .extract()
                .path("message");
    }

    @Step("Проверка удаления пользователя")
    public String deleteSuccess(ValidatableResponse response) {
        return response.assertThat()
                .statusCode(HTTP_ACCEPTED)
                .body("success", is(true))
                .extract()
                .path("message");
    }

    @Step("Проверка обновления пользователя")
    public void updateSuccess(ValidatableResponse response) {
         response.assertThat()
                .statusCode(HTTP_OK)
                .body("success", is(true));
    }

    @Step("Проверка неуспешного обновления пользователя")
    public String updateFail(ValidatableResponse response, int expectedCode) {
        return response.assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .body("success", is(false))
                .extract()
                .path("message");
    }

    public String getResponseEmail(ValidatableResponse response) {
        return response.extract().path("user.email");
    }

    public String getResponseName(ValidatableResponse response) {
        return response.extract().path("user.name");
    }
}
