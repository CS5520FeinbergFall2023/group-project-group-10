package northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.SocialPostDetails;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import northeastern.cs5520fa23.greenthumbs.R;

public class SocialPostDetailsActivity extends AppCompatActivity {
    private String _id;
    private EditText commentText;
    private List<Comment> commentList;
    private CommentAdapter commentAdapter;
    private ImageView postImage;
    private DatabaseReference commentRef;
    FirebaseUser currUser;
    private Map<String, Object> newComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_post_detailed);
        this._id = getIntent().getStringExtra("_id");
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        newComment = new HashMap<>();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        //String postPath = "posts/" + this._id;
        DatabaseReference dbRef = db.getReference("posts").child(this._id);
        commentRef = dbRef.child("comments");
        commentList = new ArrayList<>();
        RecyclerView commentRV = findViewById(R.id.comment_rv);
        commentRV.setHasFixedSize(true);
        commentRV.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(commentList, this);
        commentRV.setAdapter(commentAdapter);
        commentText = findViewById(R.id.comment_edit_text);
        ImageButton addCommentButton = findViewById(R.id.add_comment_button);
        addCommentButton.setOnClickListener(v -> addComment());
        TextView postText = findViewById(R.id.post_detail_post_text);
        postText.setText(getIntent().getStringExtra("post_text"));
        TextView usernameText = findViewById(R.id.post_detail_username);
        usernameText.setText(getIntent().getStringExtra("post_username"));
        postImage = findViewById(R.id.post_detail_post_image);
        String storageUri = getIntent().getStringExtra("img_source");
        boolean hasImg = getIntent().getExtras().getBoolean("has_img");
        //Toast.makeText(this, "hasimg="+hasImg, Toast.LENGTH_SHORT).show();
        if (hasImg && storageUri != null) {
                // https://firebase.google.com/docs/storage/android/download-files
                //Uri imgUri = Uri.parse(storageUri);
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference imgRef = storage.getReferenceFromUrl(storageUri);
                imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(SocialPostDetailsActivity.this).load(uri).into(postImage);
                    getComments();
                });
            } else {
                Toast.makeText(this, "NO IMAGE", Toast.LENGTH_SHORT).show();
                postImage.setVisibility(View.GONE);
                getComments();
            }


    }

    private void addComment() {
        String commentContent = commentText.getText().toString();
        String commentId = commentRef.push().getKey();
        String currentTimestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(new Date());
        newComment.put("_id", commentId);
        newComment.put("post_id", _id);
        newComment.put("user_id", currUser.getUid());
        newComment.put("comment_text", commentContent);
        newComment.put("comment_likes", 0);

        FirebaseDatabase.getInstance().getReference().child("users").child(currUser.getUid()).child("username").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(SocialPostDetailsActivity.this, "Unable to fetch data", Toast.LENGTH_LONG).show();
            } else {
                newComment.put("username", String.valueOf(task.getResult().getValue()));
                DatabaseReference cRef = FirebaseDatabase.getInstance().getReference("posts").child(_id).child("comments");
                cRef.child(commentId).setValue(newComment).addOnSuccessListener(unused -> {
                    Toast.makeText(SocialPostDetailsActivity.this, "Comment Added", Toast.LENGTH_SHORT).show();
                    commentText.setText("");
                    hideKeyboard();
                    getComments();
                }).addOnFailureListener(e -> Toast.makeText(SocialPostDetailsActivity.this, "Comment Not Added", Toast.LENGTH_SHORT));
            }
        });

    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void getComments() {
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    commentList.clear();
                    for (DataSnapshot commentSnapshot : snapshot.getChildren()) {
                        Comment currComment = commentSnapshot.getValue(Comment.class);
                        commentList.add(currComment);
                    }
                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("CommentsError", "Failed to get comments.", error.toException());
            }
        });
    }
}