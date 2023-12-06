package northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.FriendRequests;

public class FriendRequest {
    String approved;
    String from_uid;
    String from_username;

    public FriendRequest() {
    }

    public FriendRequest(String approved, String from_uid, String from_username) {
        this.approved = approved;
        this.from_uid = from_uid;
        this.from_username = from_username;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getFrom_uid() {
        return from_uid;
    }

    public void setFrom_uid(String from_uid) {
        this.from_uid = from_uid;
    }

    public String getFrom_username() {
        return from_username;
    }

    public void setFrom_username(String from_username) {
        this.from_username = from_username;
    }
}
