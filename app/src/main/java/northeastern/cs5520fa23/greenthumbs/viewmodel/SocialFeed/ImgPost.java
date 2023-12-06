package northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed;

public class ImgPost {
    private String _id;
    private String uid;
    private String username;
    private String timestamp;
    private String tags;
    private String post_text;
    private boolean has_img;
    private String img_uri;
    private int num_likes;
    private int num_comments;
    private boolean isLiked;
    public ImgPost () {}


    /**
     * {
     *     "_id": 123,
     *     "avatarIcon": "spacex-icon.jpeg",
     *     "userName": "SpaceX",
     *     "verified": true,
     *     "handle": "nypost",
     *     "time": "23h",
     *     "tuit": "Dennis and Akiko Tito are the first two crewmembers on Starship's second commercial spaceflight around the moon",
     *     "tuitTextLink": "trib.al/nx2Gfaq",
     *     "hasImage": true,
     *     "image": "spacex-post.jpeg",
     *     "hasLink": false,
     *     "replies": "595",
     *     "retuits": "234",
     *     "likes": "456",
     *     "liked": true,
     *     "dislikes": 110
     *   },
     */

    public ImgPost (String _id, String uid, String username, boolean has_img, String img_uri, String timestamp, String tags, String post_text, int num_likes, int num_comments ) {
        this._id = _id;
        this.uid = uid;
        this.username = username;
        this.timestamp = timestamp;
        this.post_text = post_text;
        this.num_likes = num_likes;
        this.num_comments = num_comments;
        this.has_img = has_img;
        if (has_img) {
            this.img_uri = img_uri;
        } else {
            img_uri = null;
        }
        this.tags = tags;
        this.isLiked = false;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPost_text() {
        return post_text;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }

    public boolean isHas_img() {
        return has_img;
    }

    public void setHas_img(boolean has_img) {
        this.has_img = has_img;
    }

    public String getImg_uri() {
        return img_uri;
    }

    public void setImg_uri(String img_uri) {
        this.img_uri = img_uri;
    }

    public int getNum_likes() {
        return num_likes;
    }

    public void setNum_likes(int num_likes) {
        this.num_likes = num_likes;
    }

    public int getNum_comments() {
        return num_comments;
    }

    public void setNum_comments(int num_comments) {
        this.num_comments = num_comments;
    }

    @Override
    public String toString() {
        return "ImgPost{" +
                "_id=" + _id +
                ", uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", tags='" + tags + '\'' +
                ", post_text='" + post_text + '\'' +
                ", has_img=" + has_img +
                ", img_uri='" + img_uri + '\'' +
                ", num_likes=" + num_likes +
                ", num_comments=" + num_comments +
                '}';
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
