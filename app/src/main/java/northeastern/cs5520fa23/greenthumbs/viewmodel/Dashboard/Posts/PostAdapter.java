package northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.Posts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.ImgPost;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private final List<ImgPost> postList;

    public PostAdapter(List<ImgPost> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.social_post_with_img, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        ImgPost post = postList.get(position);
        holder.postUsername.setText(post.getUsername());
        holder.postText.setText(post.getPost_text());
        holder.numLikes.setText(String.valueOf(post.getNum_likes()));
        holder.numComments.setText(String.valueOf(post.getNum_comments()));
        // Set post image if you have an image URL or resource
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView postUsername, postText, numLikes, numComments;
        ImageView postImage;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postUsername = itemView.findViewById(R.id.post_username);
            postText = itemView.findViewById(R.id.post_text);
            numLikes = itemView.findViewById(R.id.num_likes);
            numComments = itemView.findViewById(R.id.num_comments);
            postImage = itemView.findViewById(R.id.social_post_image);
        }
    }
}

