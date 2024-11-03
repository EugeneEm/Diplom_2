package user;

import constants.StellarBurgerConst;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserClient {

    @Step("Создание нового пользователя")
    public ValidatableResponse userCreate(User user) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(StellarBurgerConst.BASE_URL)
                .body(user)
                .when()
                .post(StellarBurgerConst.USER_REGISTER)
                .then().log().all();
    }

    @Step("Создание пользователя без обязательного параметра")
    public ValidatableResponse userCreateWithoutParam(Map<String, String> userWithoutParam) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(StellarBurgerConst.BASE_URL)
                .body(userWithoutParam)
                .when()
                .post(StellarBurgerConst.USER_REGISTER)
                .then().log().all();
    }

    @Step("Логин пользователя")
    public ValidatableResponse userLogin(Credentials credentials) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(StellarBurgerConst.BASE_URL)
                .body(credentials)
                .when()
                .post(StellarBurgerConst.USER_LOGIN)
                .then().log().all();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse userDelete(Credentials credentials) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(StellarBurgerConst.BASE_URL)
                .header("Authorization", credentials.getAccessToken())
                .body(credentials)
                .when()
                .delete(StellarBurgerConst.USER)
                .then().log().all();
    }

    @Step("Изменение данных пользователя")
    public ValidatableResponse userUpdate(User user, Credentials credentials) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(StellarBurgerConst.BASE_URL)
                .header("Authorization", credentials.getAccessToken())
                .body(user)
                .when()
                .patch(StellarBurgerConst.USER)
                .then().log().all();
    }

    @Step("Изменение данных пользователя (без токена)")
    public ValidatableResponse userUpdateNoAuthHeader(User user) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(StellarBurgerConst.BASE_URL)
                .body(user)
                .when()
                .patch(StellarBurgerConst.USER)
                .then().log().all();
    }
}
