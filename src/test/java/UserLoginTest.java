import constants.StellarBurgerConst;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import jdk.jfr.Description;
import org.junit.After;
import org.junit.Test;
import user.*;
import static java.net.HttpURLConnection.*;
import static org.junit.Assert.assertEquals;

@Epic("Авторизация пользователя")
public class UserLoginTest {
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
    @DisplayName("Логин пользователя")
    @Description("Успешный логин пользователя")
    public void userLogin() {
        User user = new UserGenerator().random();
        ValidatableResponse creationResponse = userClient.userCreate(user);
        token = userAssertion.createSuccess(creationResponse);
        credentials = new Credentials(user);
        credentials.setAccessToken(token);
        ValidatableResponse loginResponse = userClient.userLogin(credentials);
        userAssertion.loginSuccess(loginResponse);
    }

    @Test
    @DisplayName("Логин с пустым паролем")
    @Description("Логин пользователя с password = null, 401 ошибка")
    public void userLoginEmptyPass() {
        User user = new UserGenerator().random();
        user.setPassword(null);
        Credentials credentials = new Credentials(user);
        ValidatableResponse loginResponse = userClient.userLogin(credentials);
        String message = userAssertion.loginFail(loginResponse, HTTP_UNAUTHORIZED);
        assertEquals(StellarBurgerConst.USER_INCORRECT_MSG, message);
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    @Description("Логин пользователя с неверным логином и паролем, 401 ошибка")
    public void userLoginIncorrect() {
        User user = new UserGenerator().random();
        Credentials credentials = new Credentials(user);
        ValidatableResponse loginResponse = userClient.userLogin(credentials);
        String message = userAssertion.loginFail(loginResponse, HTTP_UNAUTHORIZED);
        assertEquals(StellarBurgerConst.USER_INCORRECT_MSG, message);
    }
}
