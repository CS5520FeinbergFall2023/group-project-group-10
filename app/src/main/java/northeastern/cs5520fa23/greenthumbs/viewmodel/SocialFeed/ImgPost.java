package northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed;

public class ImgPost {
    private int _id;
    private int uid;
    private String username;
    private String time;
    private String tags;
    private String postText;
    private int likes;
    private int replies;
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

    public ImgPost (int _id, String username, String time, String postText, int likes, int replies ) {
        this._id = _id;
        this.username = username;
        this.time = time;
        this.postText = postText;
        this.likes = likes;
        this.replies = replies;
    }

    public int get_id() {
        return _id;
    }

    public String getUsername() {
        return username;
    }

    public String getTime() {
        return time;
    }

    public String getPostText() {
        return postText;
    }

    public int getLikes() {
        return likes;
    }

    public int getReplies() {
        return replies;
    }
}
