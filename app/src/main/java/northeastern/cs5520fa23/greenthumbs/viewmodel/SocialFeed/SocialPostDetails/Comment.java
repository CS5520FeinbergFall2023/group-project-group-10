package northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.SocialPostDetails;

public class Comment {
    private String _id;
    private String post_id;
    private String user_id;
    private String username;
    private String comment_text;
    private int comment_likes;

    public Comment() {}

    public Comment(String _id, String post_id, String user_id, String username, String comment_text, int comment_likes) {
        this._id = _id;
        this.post_id = post_id;
        this.user_id = user_id;
        this.username = username;
        this.comment_text = comment_text;
        this.comment_likes = comment_likes;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
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

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public int getComment_likes() {
        return comment_likes;
    }

    public void setComment_likes(int comment_likes) {
        this.comment_likes = comment_likes;
    }
}
