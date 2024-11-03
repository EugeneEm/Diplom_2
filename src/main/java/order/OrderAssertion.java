package order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.hamcrest.Matchers.is;

public class OrderAssertion {

    @Step("Проверка создания нового заказа")
    public void createOrderSuccess(ValidatableResponse response) {
         response.assertThat()
                .statusCode(HTTP_OK)
                .body("success", is(true));
    }

    @Step("Проверка неуспешного создания заказа")
    public String createOrderFail(ValidatableResponse response, int expectedCode) {
        return response.assertThat()
                .statusCode(expectedCode)
                .body("success", is(false))
                .extract()
                .path("message");
    }

    @Step("Проверка ошибки при создании заказа")
    public void createOrderError(ValidatableResponse response, int expectedCode) {
         response.assertThat()
                .statusCode(expectedCode);
    }

    @Step("Проверка получения заказа пользователя")
    public void getUserOrderSuccess(ValidatableResponse response) {
         response.assertThat()
                .statusCode(HTTP_OK)
                .body("success", is(true));
    }

    @Step("Проверка получения заказа неавторизованного пользователя")
    public String getUserOrderWithoutAuth(ValidatableResponse response) {
        return response.assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .body("success", is(false))
                .extract()
                .path("message");
    }

    public String getResponseCreateOrderName(ValidatableResponse response) {
        return response.extract().path("order.name");
    }

    public String getResponseGetOrderName(ValidatableResponse response) {
        return response.extract().path("orders[0].name");
    }
}
