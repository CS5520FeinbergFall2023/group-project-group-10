package northeastern.cs5520fa23.greenthumbs.viewmodel.Profile;

import northeastern.cs5520fa23.greenthumbs.viewmodel.User;

public class Friend extends User {
    private String friend_id;
    private String status;
    private String friend_username;

    public Friend() {
    }

    public Friend(String friend_id, String status) {
        this.friend_id = friend_id;
        this.status = status;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getFriend_username() {
        return friend_username;
    }

    public void setFriend_username(String friend_username) {
        this.friend_username = friend_username;
    }
}
