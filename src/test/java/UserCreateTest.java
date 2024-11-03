import constants.StellarBurgerConst;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import user.*;

import java.util.Map;

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static org.junit.Assert.assertEquals;

@Epic("Регистрация пользователя")
public class UserCreateTest {
    UserClient userClient = new UserClient();
    UserAssertion userAssertion = new UserAssertion();
    private String token;
    Credentials credentials;

    @After
    public void deleteCreatedUser() {
        if(token != null) {
            ValidatableResponse response = userClient.userDelete(credentials);
            String message = userAssertion.deleteSuccess(response);
            assertEquals(StellarBurgerConst.USER_REMOVED_MSG, message);
        }
    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("Создание нового уникального пользователя")
    public void registerUser() {
        User user = new UserGenerator().random();
        ValidatableResponse creationResponse = userClient.userCreate(user);
        token = userAssertion.createSuccess(creationResponse);
        credentials = new Credentials(user);
        credentials.setAccessToken(token);
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Создание существующего пользователя, 403 ошибка")
    public void registerExisting() {
        User user = new UserGenerator().random();
        ValidatableResponse creationResponse = userClient.userCreate(user);
        token = userAssertion.createSuccess(creationResponse);
        credentials = new Credentials(user);
        credentials.setAccessToken(token);

        ValidatableResponse createExistingResponse = userClient.userCreate(user);
        String message = userAssertion.createFail(createExistingResponse, HTTP_FORBIDDEN);
        assertEquals(StellarBurgerConst.USER_EXIST_MSG, message);
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Создание пользователя без параметра password, 403 ошибка")
    public void  registerWithoutPass() {
        User user = new UserGenerator().random();
        var userWithoutPass = Map.of("name", user.getName(), "email", user.getEmail());
        ValidatableResponse creationResponse = userClient.userCreateWithoutParam(userWithoutPass);
        String message = userAssertion.createFail(creationResponse, HTTP_FORBIDDEN);
        assertEquals(StellarBurgerConst.USER_WITHOUT_REQ_FIELD_MSG, message);
    }

    @Test
    @DisplayName("Создание пользователя с пустым паролем")
    @Description("Создание пользователя с параметром password = null, 403 ошибка")
    public void  registerWithEmptyPass() {
        User user = new UserGenerator().random();
        user.setPassword(null);
        ValidatableResponse creationResponse = userClient.userCreate(user);
        String message = userAssertion.createFail(creationResponse, HTTP_FORBIDDEN);
        assertEquals(StellarBurgerConst.USER_WITHOUT_REQ_FIELD_MSG, message);
    }

}
