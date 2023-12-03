package northeastern.cs5520fa23.greenthumbs.viewmodel;

public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String user_id;
    private String username;
    private String profile_pic;
    private String header_image;
    private String user_bio;

    public User() {
    }

    public User(String email, String firstName, String lastName, String user_id, String username, String profile_pic, String header_image, String user_bio) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.user_id = user_id;
        this.username = username;
        this.header_image = header_image;
        this.profile_pic = profile_pic;
        this.user_bio = user_bio;
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

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getHeader_image() {
        return header_image;
    }

    public void setHeader_image(String header_image) {
        this.header_image = header_image;
    }

    public String getUser_bio() {
        return user_bio;
    }

    public void setUser_bio(String user_bio) {
        this.user_bio = user_bio;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", profile_pic='" + profile_pic + '\'' +
                ", header_image='" + header_image + '\'' +
                ", user_bio='" + user_bio + '\'' +
                '}';
    }
}
