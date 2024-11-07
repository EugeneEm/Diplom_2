import constants.StellarBurgerConst;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import order.Ingredient;
import order.Ingredients;
import order.OrderAssertion;
import order.OrderClient;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.net.HttpURLConnection.*;
import static org.junit.Assert.assertEquals;

@Epic("Создание заказа")
public class OrderCreateTest {

    OrderClient orderClient = new OrderClient();
    OrderAssertion orderAssertion = new OrderAssertion();

    String[] ingredientsIncorrect = {"\"123a\"", "\"456v\"", "\"789S\""};

    String[] ingredientsList;
    List<String> ids = new ArrayList<>();
    Ingredients ingredients;

    @Before
    public void getIngredientsList() {
        ValidatableResponse response = orderClient.getIngredients();
        orderAssertion.getIngredientsData(response);
        ingredients = response.extract().as(Ingredients.class);
        System.out.println("Success: " + ingredients.isSuccess());
        for (Ingredient ingredient : ingredients.getData()) {
            ids.add(ingredient.get_id());
        }
        ids.remove(0);
        System.out.println("Ingredients id list: " + ids);
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Создание заказа с ингредиентами, неавторизованный пользователь")
    public void createOrderWithIngredients() {
        ingredientsList = new String[]{"\"" + ids.get(0) + "\"", "\"" + ids.get(1) + "\""};
        ValidatableResponse response = orderClient.orderCreateWithoutToken(ingredientsList);
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
