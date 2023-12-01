package northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.SocialPostDetails.SocialPostDetailsActivity;

public class SocialPostViewHolder extends RecyclerView.ViewHolder {
    public TextView username;
    public TextView postText;
    public ImageView postImg;
    public ImageView commentIcon;
    public TextView likes;
    public TextView replies;
    public TextView time;
    public SocialPostViewHolder(@NonNull View itemView) {
        super(itemView);
        this.username = itemView.findViewById(R.id.post_username);
        this.postImg = itemView.findViewById(R.id.social_post_image);
        this.postText = itemView.findViewById(R.id.post_text);
        this.likes = itemView.findViewById(R.id.num_likes);
        this.replies = itemView.findViewById(R.id.num_comments);
        this.commentIcon = itemView.findViewById(R.id.comment_icon);
        this.commentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(itemView.getContext(), SocialPostDetailsActivity.class);
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
}
