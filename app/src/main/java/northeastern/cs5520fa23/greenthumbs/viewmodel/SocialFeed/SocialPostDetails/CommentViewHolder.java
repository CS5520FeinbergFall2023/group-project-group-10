package northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.SocialPostDetails;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import northeastern.cs5520fa23.greenthumbs.R;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    private TextView commentUsername;
    private TextView commentContent;
    private TextView numLikes;
    private ImageButton likeButton;
    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        this.commentContent = itemView.findViewById(R.id.comment_content_tv);
        this.commentUsername = itemView.findViewById(R.id.comment_username_tv);
        //this.numLikes = itemView.findViewById(R.id.comment_like_count);
        //this.likeButton = itemView.findViewById(R.id.comment_like_button);
    }

    public TextView getCommentUsername() {
        return commentUsername;
    }

    public void setCommentUsername(TextView commentUsername) {
        this.commentUsername = commentUsername;
    }

    public TextView getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(TextView commentContent) {
        this.commentContent = commentContent;
    }

    /*
    public TextView getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(TextView numLikes) {
        this.numLikes = numLikes;
    }

    public ImageButton getLikeButton() {
        return likeButton;
    }

    public void setLikeButton(ImageButton likeButton) {
        this.likeButton = likeButton;
    }

     */
}
