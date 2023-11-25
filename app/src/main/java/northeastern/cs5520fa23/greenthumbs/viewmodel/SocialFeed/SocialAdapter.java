package northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;


public class SocialAdapter extends RecyclerView.Adapter<SocialPostViewHolder> {
    private List<ImgPost> posts;
    private Context context;

    public SocialAdapter(List<ImgPost> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull SocialPostViewHolder holder, int position) {
        ImgPost post = posts.get(position);
        if (post != null) {
            String username = post.getUsername();
            String time = post.getTime();
            Integer likes = post.getLikes();
            Integer replies = post.getReplies();
            String postText = post.getPostText();

            if (username != null) {
                holder.username.setText(username);
            }
            if (likes != null) {
                holder.likes.setText(likes.toString());
            }
            if (replies != null) {
                holder.replies.setText(replies.toString());
            }
            if (postText != null) {
                holder.postText.setText(postText);
            }

        }
    }

    @NonNull
    @Override
    public SocialPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {
        return new SocialPostViewHolder(LayoutInflater.from(context).inflate(R.layout.social_post_with_img, null));
    }

    @Override
    public int getItemCount() {return posts.size();}
}

