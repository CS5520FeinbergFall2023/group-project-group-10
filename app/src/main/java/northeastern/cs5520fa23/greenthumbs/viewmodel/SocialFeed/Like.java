package northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed;

public class Like {
    private String user_id;
    private String id_value;

    public Like() {
    }

    public Like(String user_id, String id_value) {
        this.user_id = user_id;
        this.id_value = id_value;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId_value() {
        return id_value;
    }

    public void setId_value(String id_value) {
        this.id_value = id_value;
    }
}
