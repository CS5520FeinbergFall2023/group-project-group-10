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

    public SocialAdapter(List<ImgPost> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull SocialPostViewHolder holder, int position) {
        ImgPost post = posts.get(position);
        if (post != null) {
            String username = post.getUsername();
            String time = post.getTimestamp();
            Integer likes = post.getNum_likes();
            Integer replies = post.getNum_comments();
            String post_text = post.getPost_text();
            String postUri;
            if (username != null) {
                holder.getUsername().setText(username);
            }
            if (likes != null) {
                holder.getLikes().setText(likes.toString());
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

