import constants.StellarBurgerConst;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import order.OrderAssertion;
import order.OrderClient;
import org.junit.Test;
import static java.net.HttpURLConnection.*;
import static org.junit.Assert.assertEquals;

@Epic("Создание заказа")
public class OrderCreateTest {

    OrderClient orderClient = new OrderClient();
    OrderAssertion orderAssertion = new OrderAssertion();
    String[] ingredients = {"\"61c0c5a71d1f82001bdaaa71\"", "\"61c0c5a71d1f82001bdaaa72\"", "\"61c0c5a71d1f82001bdaaa6e\""};
    String[] ingredientsIncorrect = {"\"123a\"", "\"456v\"", "\"789S\""};

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Создание заказа с ингредиентами, неавторизованный пользователь")
    public void createOrderWithIngredients() {
        ValidatableResponse response = orderClient.orderCreateWithoutToken(ingredients);
        orderAssertion.createOrderSuccess(response);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Создание заказа без ингредиентов, неавторизованный пользователь, 400 ошибка")
    public void createOrderWithoutIngredients() {
        ValidatableResponse response = orderClient.orderCreateWithoutToken(null);
        String message = orderAssertion.createOrderFail(response, HTTP_BAD_REQUEST);
        assertEquals(StellarBurgerConst.ORDER_NO_INGREDIENTS_MSG, message);

    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Создание заказа с неверным хешем ингредиентов, неавторизованный пользователь, 500 ошибка")
    public void createOrderWithIncorrectIngredients() {
        ValidatableResponse response = orderClient.orderCreateWithoutToken(ingredientsIncorrect);
        orderAssertion.createOrderError(response, HTTP_INTERNAL_ERROR);
    }

}
