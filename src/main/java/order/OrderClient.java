package order;

import constants.StellarBurgerConst;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import user.Credentials;

import java.util.Arrays;

import static io.restassured.RestAssured.given;

public class OrderClient {

    @Step("Создание нового заказа (без токена)")
    public ValidatableResponse orderCreateWithoutToken(String[] ingredients) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(StellarBurgerConst.BASE_URL)
                .body("{\"ingredients\": " + Arrays.toString(ingredients) + "}")
                .when()
                .post(StellarBurgerConst.ORDER)
                .then().log().all();
    }

    @Step("Создание нового заказа")
    public ValidatableResponse orderCreate(String[] ingredients, Credentials credentials) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(StellarBurgerConst.BASE_URL)
                .header("Authorization", credentials.getAccessToken())
                .body("{\"ingredients\": " + Arrays.toString(ingredients) + "}")
                .when()
                .post(StellarBurgerConst.ORDER)
                .then().log().all();
    }

    @Step("Получение заказов пользователя")
    public ValidatableResponse getUserOrder(Credentials credentials) {
        return  given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(StellarBurgerConst.BASE_URL)
                .header("Authorization", credentials.getAccessToken())
                .when()
                .get(StellarBurgerConst.ORDER)
                .then().log().all();
    }

    @Step("Получение заказов пользователя (без токена)")
    public ValidatableResponse getUserOrderWithoutToken() {
        return  given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(StellarBurgerConst.BASE_URL)
                .when()
                .get(StellarBurgerConst.ORDER)
                .then().log().all();
    }
}
