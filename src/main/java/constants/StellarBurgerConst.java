package constants;

public class StellarBurgerConst {
    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    //User endpoints
    public static final String USER = "/api/auth/user";
    public static final String USER_REGISTER = "/api/auth/register";
    public static final String USER_LOGIN = "/api/auth/login";

    //User response messages
    public static final String USER_EXIST_MSG = "User already exists";
    public static final String USER_WITHOUT_REQ_FIELD_MSG = "Email, password and name are required fields";
    public static final String USER_INCORRECT_MSG = "email or password are incorrect";
    public static final String USER_REMOVED_MSG = "User successfully removed";
    public static final String USER_UNAUTHORIZED_MSG = "You should be authorised";

    //Order endpoints
    public static final String ORDER = "/api/orders";

    //Order messages
    public static final String ORDER_NO_INGREDIENTS_MSG = "Ingredient ids must be provided";


}
