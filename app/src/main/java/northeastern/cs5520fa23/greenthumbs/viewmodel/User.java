package northeastern.cs5520fa23.greenthumbs.viewmodel;

public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String user_id;
    private String username;

    public User() {
    }

    public User(String email, String firstName, String lastName, String user_id, String username) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.user_id = user_id;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
