package northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;


public class SocialAdapter extends RecyclerView.Adapter<SocialPostViewHolder> {
    private List<ImgPost> posts;
    private Context context;
    public interface UsernameCallback {
        void openProfileCallback(String username, String posterId);
    }
    private UsernameCallback usernameCallback;

    public SocialAdapter(List<ImgPost> posts, Context context, UsernameCallback usernameCallback) {
        this.posts = posts;
        this.context = context;
        if (usernameCallback != null) {
            this.usernameCallback = usernameCallback;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SocialPostViewHolder holder, int position) {
        ImgPost post = posts.get(position);
        if (post != null) {

            holder.set_id(post.get_id());
            String username = post.getUsername();

            String time = post.getTimestamp();
            Integer num_likes = post.getNum_likes();
            Integer replies = post.getNum_comments();
            String post_text = post.getPost_text();
            String postUri;
            String posterId = post.getUid();
            if (username != null) {
                holder.getUsername().setText(username);
                holder.getUsername().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        usernameCallback.openProfileCallback(username, posterId);
                    }
                });
            }
            if (num_likes != null) {
                holder.getLikes().setText(num_likes.toString());
                //if (num_likes > 0) {
                //    holder.getLikesIcon().setImageResource(R.drawable.like_filled);
                //}
            }
            if (replies != null) {
                holder.getReplies().setText(replies.toString());
            }
            if (post_text != null) {
                holder.getPostText().setText(post_text);
            }
            if (post.isHas_img()) {
                // https://firebase.google.com/docs/storage/android/download-files
                postUri = post.getImg_uri();
                holder.setStorageUri(postUri);
                holder.setHasImg(true);
                Log.d(TAG, "onBindViewHolder: " + postUri);
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference imgRef = storage.getReferenceFromUrl(postUri);
                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context).load(uri).into(holder.getPostImg());
                    }
                });
            } else {
                postUri = null;
                holder.getPostImg().setVisibility(View.GONE);
                holder.setStorageUri(null);
                holder.setHasImg(false);
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

