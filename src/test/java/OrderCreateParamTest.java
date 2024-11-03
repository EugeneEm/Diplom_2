import constants.StellarBurgerConst;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import order.OrderAssertion;
import order.OrderClient;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import user.*;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;


@RunWith(Parameterized.class)
@Epic("Создание заказа (параметризованные тесты)")
public class OrderCreateParamTest {

    private final String[] ingredients;
    OrderClient orderClient = new OrderClient();
    UserClient userClient = new UserClient();
    OrderAssertion orderAssertion = new OrderAssertion();
    UserAssertion userAssertion = new UserAssertion();
    private String token;
    Credentials credentials;

    public OrderCreateParamTest(String[] ingredients) {
        this.ingredients = ingredients;
    }

    @After
    public void deleteCreatedUser() {
        if(token != null) {
            ValidatableResponse response = userClient.userDelete(credentials);
            String message = userAssertion.deleteSuccess(response);
            assertEquals(StellarBurgerConst.USER_REMOVED_MSG, message);
        }
    }

    @Parameterized.Parameters
    public static Collection<Object[]> ingredientData() {
        return Arrays.asList(new Object[][]{
                {new String[]{"\"61c0c5a71d1f82001bdaaa6d\"", "\"61c0c5a71d1f82001bdaaa6f\""}},
                {new String[]{"\"61c0c5a71d1f82001bdaaa70\""}},
                {new String[]{"\"61c0c5a71d1f82001bdaaa71\"", "\"61c0c5a71d1f82001bdaaa72\"", "\"61c0c5a71d1f82001bdaaa6e\""}}
        });
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Создание заказа с ингредиентами для неавторизованного пользователя")
    public void createOrderWithoutAuth() {
        ValidatableResponse response = orderClient.orderCreateWithoutToken(ingredients);
        orderAssertion.createOrderSuccess(response);
    }


    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Создание заказа с ингредиентами для авторизованного пользователя")
    public void createOrderWithAuth() {
        User user = new UserGenerator().random();
        ValidatableResponse creationResponse = userClient.userCreate(user);
        token = userAssertion.createSuccess(creationResponse);
        credentials = new Credentials(user);
        credentials.setAccessToken(token);

        ValidatableResponse response = orderClient.orderCreate(ingredients, credentials);
        orderAssertion.createOrderSuccess(response);
    }

}
