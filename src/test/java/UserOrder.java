import constants.StellarBurgerConst;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import order.OrderAssertion;
import order.OrderClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.*;

import static org.junit.Assert.assertEquals;

@Epic("Получение заказов пользователя")
public class UserOrder {

    UserClient userClient = new UserClient();
    OrderClient orderClient = new OrderClient();
    UserAssertion userAssertion = new UserAssertion();
    OrderAssertion orderAssertion = new OrderAssertion();
    private String token;
    Credentials credentials;
    String[] ingredients = {"\"61c0c5a71d1f82001bdaaa71\"", "\"61c0c5a71d1f82001bdaaa72\"", "\"61c0c5a71d1f82001bdaaa6e\""};
    String orderName;


    @Before
    public void createUserOrder() {
        User user = new UserGenerator().random();
        ValidatableResponse creationResponse = userClient.userCreate(user);
        token = userAssertion.createSuccess(creationResponse);
        credentials = new Credentials(user);
        credentials.setAccessToken(token);

        ValidatableResponse response = orderClient.orderCreate(ingredients, credentials);
        orderAssertion.createOrderSuccess(response);
        orderName = orderAssertion.getResponseCreateOrderName(response);
    }

    @After
    public void deleteCreatedUser() {
        if(token != null) {
            ValidatableResponse response = userClient.userDelete(credentials);
            String message = userAssertion.deleteSuccess(response);
            assertEquals(StellarBurgerConst.USER_REMOVED_MSG, message);
        }
    }

    @Test
    @DisplayName("Получение заказов пользователя")
    @Description("Получения заказов авторизованного пользователя")
    public void getAuthUserOrder() {
        ValidatableResponse getResponse = orderClient.getUserOrder(credentials);
        orderAssertion.getUserOrderSuccess(getResponse);

        String expectedOrderName = orderName;
        String actualOrderName = orderAssertion.getResponseGetOrderName(getResponse);
        System.out.println("Expected: " + expectedOrderName);
        System.out.println("Actual: " + actualOrderName);

        assertEquals(expectedOrderName,actualOrderName);
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    @Description("Получения заказов без токена авторизации, 401 ошибка")
    public void getUserOrder() {
        ValidatableResponse getResponse = orderClient.getUserOrderWithoutToken();
        String message = orderAssertion.getUserOrderWithoutAuth(getResponse);
        assertEquals(StellarBurgerConst.USER_UNAUTHORIZED_MSG, message);
    }
}
