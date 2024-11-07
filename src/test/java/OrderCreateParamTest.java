import constants.StellarBurgerConst;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import order.Ingredient;
import order.Ingredients;
import order.OrderAssertion;
import order.OrderClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import user.*;

import java.util.*;


import static org.junit.Assert.assertEquals;


@RunWith(Parameterized.class)
@Epic("Создание заказа с авторизованного или неавторизованного пользователя")
public class OrderCreateParamTest {

    boolean userAuth;

    String[] ingredientsList;
    List<String> ids = new ArrayList<>();
    Ingredients ingredients;

    OrderClient orderClient = new OrderClient();
    UserClient userClient = new UserClient();
    OrderAssertion orderAssertion = new OrderAssertion();
    UserAssertion userAssertion = new UserAssertion();

    private String token;
    Credentials credentials;


    public OrderCreateParamTest(boolean userAuth) {
        this.userAuth = userAuth;
    }


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

    @After
    public void deleteCreatedUser() {
        if(token != null) {
            ValidatableResponse response = userClient.userDelete(credentials);
            String message = userAssertion.deleteSuccess(response);
            assertEquals(StellarBurgerConst.USER_REMOVED_MSG, message);
        }
    }

    @Parameterized.Parameters(name = "Тестовые данные: авторизован = {0}")
    public static Object[][] ingredientData() {
        return new Object[][]{
                {false},
                {true}
        };
    }


    @Test
    @DisplayName("Создание заказа")
    @Description("Параметризованный тест создания заказа с ингредиентами для авторизованного пользователя и для неавторизованного пользователя")
    public void createOrderWithAuth() {
        ingredientsList = new String[]{"\"" + ids.get(0) + "\"", "\"" + ids.get(1) + "\""};
        ValidatableResponse response;
        if(userAuth) {
            User user = new UserGenerator().random();
            ValidatableResponse creationResponse = userClient.userCreate(user);
            token = userAssertion.createSuccess(creationResponse);
            credentials = new Credentials(user);
            credentials.setAccessToken(token);
             response = orderClient.orderCreate(ingredientsList, credentials);

        } else {
             response = orderClient.orderCreateWithoutToken(ingredientsList);
        }
        orderAssertion.createOrderSuccess(response);
    }
}
