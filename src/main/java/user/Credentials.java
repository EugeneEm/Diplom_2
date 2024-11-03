package user;

public class Credentials {
    private String email;
    private String password;
    private String accessToken;

    public Credentials() {
    }

    public Credentials(String email, String password, String accessToken) {
        this.email = email;
        this.password = password;
        this.accessToken = accessToken;
    }

    public Credentials(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
