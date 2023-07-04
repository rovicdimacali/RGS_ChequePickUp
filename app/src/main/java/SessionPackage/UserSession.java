package SessionPackage;

public class UserSession {
    final String email;
    final String password;

    public UserSession(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
