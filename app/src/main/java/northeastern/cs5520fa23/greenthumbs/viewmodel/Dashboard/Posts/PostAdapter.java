package northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.Posts;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.ImgPost;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.Like;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.SocialPostDetails.SocialPostDetailsActivity;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.SocialPostViewHolder;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private final List<ImgPost> postList;
    private FirebaseUser currUser;
    public interface DashboardPostCallback {
        void openProfileCallback(String username, String posterId);

    }
    private Context context;

    private DashboardPostCallback dashboardPostCallback;

    public PostAdapter(List<ImgPost> postList, Context context, DashboardPostCallback dashboardPostCallback) {
        this.postList = postList;
        this.dashboardPostCallback = dashboardPostCallback;
        this.context = context;
        this.currUser = FirebaseAuth.getInstance().getCurrentUser();
    }


    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.social_post_with_img, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        ImgPost post = postList.get(position);/*
        holder.postUsername.setText(post.getUsername());
        holder.postText.setText(post.getPost_text());
        holder.numLikes.setText(String.valueOf(post.getNum_likes()));
        holder.numComments.setText(String.valueOf(post.getNum_comments()));
        String uri = post.getImg_uri();
        if (uri != null && !uri.isEmpty()) {
            Picasso.get().load(uri).into(holder.postImage);
        }

         */
        if (post != null) {

            String _id = post.get_id();
            String username = post.getUsername();

            String time = post.getTimestamp();
            Integer num_likes = post.getNum_likes();
            Integer replies = post.getNum_comments();
            String post_text = post.getPost_text();
            String postUri;
            String posterId = post.getUid();
            String geolocation = post.getGeo_location();
            if (post.get_id() != null) {
                if (username != null) {
                    holder.set_id(_id);
                    holder.getUsername().setText(username);
                    holder.getUsername().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dashboardPostCallback.openProfileCallback(username, posterId);
                        }
                    });
                }
                if (num_likes != null) {
                    holder.getLikes().setText(num_likes.toString());
                    Log.d("CHECKING IF THIS IS NULL", "onBindViewHolder: " + post.get_id());
                    FirebaseDatabase.getInstance().getReference("posts").child(post.get_id()).child("likes").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //int num = (int) snapshot.getChildrenCount();
                            int num = 0;
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            for (DataSnapshot likeSnapshot : snapshot.getChildren()) {
                                if (likeSnapshot.getKey().equals(uid)) {
                                    post.setLiked(true);
                                    holder.getLikesIcon().setImageResource(R.drawable.like_filled);
                                }
                                num += 1;
                            }
                            post.setNum_likes(num);
                            String n = Integer.toString(num);
                            holder.getLikes().setText(n);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    holder.getLikesIcon().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //usernameCallback.addLikeCallback(post);
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("posts").child(post.get_id()).child("likes").child(currUser.getUid());
                            if (!post.isLiked()) {
                                Like newLike = new Like(currUser.getUid(), currUser.getUid());
                                ref.setValue(newLike).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(context, "Unable to add like", Toast.LENGTH_LONG).show();
                                        } else {
                                            post.setLiked(true);
                                            //post.setNum_likes(post.getNum_likes() + 1);
                                            holder.getLikes().setText(Integer.toString(post.getNum_likes()));
                                            holder.getLikesIcon().setImageResource(R.drawable.like_filled);
                                        }
                                    }
                                });
                            } else {
                                ref.removeValue();
                                post.setLiked(false);
                                post.setNum_likes(post.getNum_likes() - 1);
                                holder.getLikes().setText(Integer.toString(post.getNum_likes()));
                                holder.getLikesIcon().setImageResource(R.drawable.like_outline);
                            }
                        }
                    });
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
                    //holder.getPostImg().setVisibility(View.GONE);
                    holder.setStorageUri(null);
                    holder.setHasImg(false);
                }
                if (geolocation != null) {
                    holder.getGeolocationText().setText(geolocation);
                } else {
                    holder.getGeolocationText().setText("");
                }
                holder.getCommentIcon().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, SocialPostDetailsActivity.class);
                        i.putExtra("_id", post.get_id());
                        i.putExtra("post_text", post.getPost_text());
                        i.putExtra("post_username", post.getUsername());
                        i.putExtra("img_source", post.getImg_uri());
                        i.putExtra("has_img", post.isHas_img());
                        context.startActivity(i);
                    }
                });
            }




        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class PostViewHolder extends SocialPostViewHolder {

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

        public ImageView getPostImage() {
            return this.postImage;
        }

    }
}

