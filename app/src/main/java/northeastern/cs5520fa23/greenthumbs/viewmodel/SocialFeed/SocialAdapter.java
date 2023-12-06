package northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

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
            // image getting
            if (posterId != null) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference profPicRef = storage.getReference("profile_pics/"+posterId);
                profPicRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (!task.isSuccessful()) {
                            Log.d("HAS_NO_PIC", profPicRef.toString());
                            Drawable d = context.getDrawable(R.drawable.baseline_person_24);
                            holder.getPostProfPic().setImageDrawable(d);
                        } else {
                            Uri uri = task.getResult();
                            //Picasso.get().load(uri).resize(40, 40).centerCrop().into(holder.getPostProfPic());
                            Picasso.get().load(uri).resize(40, 40).into(holder.getPostProfPic(), new Callback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap bitmap = ((BitmapDrawable) holder.getPostProfPic().getDrawable()).getBitmap();
                                    RoundedBitmapDrawable round = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
                                    round.setCircular(true);
                                    round.setCornerRadius(40/2.0f);
                                    holder.getPostProfPic().setImageDrawable(round);
                                }

                                @Override
                                public void onError(Exception e) {
                                    //holder.getPostProfPic().setImageResource(R.drawable.baseline_tag_faces_24);
                                }
                            });

                        }
                    }
                });
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
                        holder.getPostImg().setVisibility(View.VISIBLE);
                        try {
                            Picasso.get().load(uri).resize(holder.getPostImg().getWidth(), holder.getPostImg().getHeight()).centerCrop().into(holder.getPostImg());
                        } catch (IllegalArgumentException e) {
                            Log.d("ERROR IMG", holder.postText.getText() + " " + holder.getPostImg().getWidth() + " " + holder.getPostImg().getHeight());
                        }
                        //Glide.with(context).load(uri).into(holder.getPostImg());
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


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<ImgPost> getPosts() {
        return posts;
    }

    public void setPosts(List<ImgPost> posts) {
        this.posts.clear();
        this.posts.addAll(posts);
        notifyDataSetChanged();
    }
}

