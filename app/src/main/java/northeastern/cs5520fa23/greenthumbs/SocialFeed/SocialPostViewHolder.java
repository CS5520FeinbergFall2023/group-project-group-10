package northeastern.cs5520fa23.greenthumbs.SocialFeed;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import northeastern.cs5520fa23.greenthumbs.R;

public class SocialPostViewHolder extends RecyclerView.ViewHolder {
    public TextView username;
    public TextView postText;
    public ImageView postImg;
    public TextView likes;
    public TextView replies;
    public TextView time;
    public SocialPostViewHolder(@NonNull View itemView) {
        super(itemView);
        this.username = itemView.findViewById(R.id.post_username);
        this.postText = itemView.findViewById(R.id.post_text);
        this.likes = itemView.findViewById(R.id.num_likes);
        this.replies = itemView.findViewById(R.id.num_comments);
    }

}
