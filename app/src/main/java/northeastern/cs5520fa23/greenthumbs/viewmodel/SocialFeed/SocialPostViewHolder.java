package northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.SocialPostDetails.SocialPostDetailsActivity;

public class SocialPostViewHolder extends RecyclerView.ViewHolder {
    private String _id;
    private int num_likes;
    private String storageUri;
    public TextView username;
    public TextView postText;
    public ImageView postImg;
    public ImageView commentIcon;
    private ImageView likesIcon;
    public TextView likes;
    public TextView replies;
    public TextView time;
    private boolean hasImg;
    private ImageView postProfPic;



    public SocialPostViewHolder(@NonNull View itemView) {
        super(itemView);
        this.username = itemView.findViewById(R.id.post_username);
        this.postImg = itemView.findViewById(R.id.social_post_image);
        this.postText = itemView.findViewById(R.id.post_text);
        this.likes = itemView.findViewById(R.id.num_likes);
        this.replies = itemView.findViewById(R.id.num_comments);
        this.commentIcon = itemView.findViewById(R.id.comment_icon);
        this.postProfPic = itemView.findViewById(R.id.post_prof_pic);
        this.storageUri = null;
        this.hasImg = false;
        this.commentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(itemView.getContext(), SocialPostDetailsActivity.class);
                i.putExtra("_id", get_id());
                i.putExtra("post_text", postText.getText().toString());
                i.putExtra("post_username", username.getText().toString());
                i.putExtra("img_source", storageUri);
                i.putExtra("has_img", hasImg);
                itemView.getContext().startActivity(i);
            }
        });
    }

    public TextView getUsername() {
        return username;
    }

    public TextView getPostText() {
        return postText;
    }

    public ImageView getPostImg() {
        return postImg;
    }

    public ImageView getCommentIcon() {
        return commentIcon;
    }

    public TextView getLikes() {
        return likes;
    }

    public TextView getReplies() {
        return replies;
    }

    public TextView getTime() {
        return time;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getNum_likes() {
        return num_likes;
    }

    public void setNum_likes(int num_likes) {
        this.num_likes = num_likes;
    }

    public ImageView getLikesIcon() {
        return likesIcon;
    }

    public void setLikesIcon(ImageView likesIcon) {
        this.likesIcon = likesIcon;
    }
    public String getStorageUri() {
        return storageUri;
    }

    public void setStorageUri(String storageUri) {
        this.storageUri = storageUri;
    }

    public boolean isHasImg() {
        return hasImg;
    }

    public void setHasImg(boolean hasImg) {
        this.hasImg = hasImg;
    }

    public ImageView getPostProfPic() {
        return postProfPic;
    }

    public void setPostProfPic(ImageView postProfPic) {
        this.postProfPic = postProfPic;
    }
}
