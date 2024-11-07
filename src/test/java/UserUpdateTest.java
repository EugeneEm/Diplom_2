import constants.StellarBurgerConst;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.*;
import static java.net.HttpURLConnection.*;

import static org.junit.Assert.assertEquals;

@Epic("Изменение данных пользователя")
public class UserUpdateTest {
    UserClient userClient = new UserClient();
    UserAssertion userAssertion = new UserAssertion();
    private String token;
    Credentials credentials;
    User user;

    @Before
    public void preregisterUser() {
        user = new UserGenerator().random();
        ValidatableResponse creationResponse = userClient.userCreate(user);
        token = userAssertion.createSuccess(creationResponse);
        credentials = new Credentials(user);
        credentials.setAccessToken(token);
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
    @DisplayName("Изменение email пользователя")
    @Description("Изменекние электронной почты авторизованного пользователя")
    public void emailUpdate() {
        User updatedUser = new UserGenerator().random();
        user.setEmail(updatedUser.getEmail());
        ValidatableResponse updateResponse = userClient.userUpdate(user, credentials);
        userAssertion.updateSuccess(updateResponse);
        assertEquals(user.getEmail().toLowerCase(), userAssertion.getResponseEmail(updateResponse).toLowerCase());
    }

    @Test
    @DisplayName("Изменение name пользователя")
    @Description("Изменение имени авторизованного пользователя")
    public void nameUpdate() {
        User updatedUser = new UserGenerator().random();
        user.setName(updatedUser.getName());
        ValidatableResponse updateResponse = userClient.userUpdate(user, credentials);
        userAssertion.updateSuccess(updateResponse);
        assertEquals(user.getName().toLowerCase(), userAssertion.getResponseName(updateResponse).toLowerCase());
    }

    @Test
    @DisplayName("Изменение password пользователя")
    @Description("Изменение пароля авторизованного пользователя")
    public void passwordUpdate() {
        User updatedUser = new UserGenerator().generic();
        user.setPassword(updatedUser.getPassword());
        ValidatableResponse updateResponse = userClient.userUpdate(user, credentials);
        userAssertion.updateSuccess(updateResponse);
    }

    @Test
    @DisplayName("Изменение email пользователя без авторизации")
    @Description("Изменение электронной почты неавторизованного пользователя, 401 ошибка")
    public void emailUpdateNoAuth() {
        User updatedUser = new UserGenerator().random();
        user.setEmail(updatedUser.getEmail());
        ValidatableResponse updateResponse = userClient.userUpdateNoAuthHeader(user);
        String message = userAssertion.updateFail(updateResponse,HTTP_UNAUTHORIZED);
        assertEquals(StellarBurgerConst.USER_UNAUTHORIZED_MSG, message);
    }
}
