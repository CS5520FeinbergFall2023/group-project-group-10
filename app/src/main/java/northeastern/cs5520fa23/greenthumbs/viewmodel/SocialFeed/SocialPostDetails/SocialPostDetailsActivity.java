package northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.SocialPostDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView usernameText;
    private TextView postText;
    private ImageButton addCommentButton;
    private EditText commentText;
    private RecyclerView commentRV;
    private List<Comment> commentList;
    private CommentAdapter commentAdapter;
    private ImageView postImage;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
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
        db = FirebaseDatabase.getInstance();
        String postPath = "posts/" + this._id;
        dbRef = db.getReference(postPath);
        commentRef = dbRef.child("comments");
        commentList = new ArrayList<>();
        commentRV = findViewById(R.id.comment_rv);
        commentRV.setHasFixedSize(true);
        commentRV.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter();
        commentRV.setAdapter(commentAdapter);
        commentText = findViewById(R.id.comment_edit_text);
        addCommentButton = findViewById(R.id.add_comment_button);
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });
        postText = findViewById(R.id.post_detail_post_text);
        postText.setText(getIntent().getStringExtra("post_text"));
        usernameText = findViewById(R.id.post_detail_username);
        usernameText.setText(getIntent().getStringExtra("post_username"));
        postImage = findViewById(R.id.post_detail_post_image);
        getComments();
    }

    private void addComment() {
        String commentContent = commentText.getText().toString();
        String commentId = commentRef.push().getKey();
        String currentTimestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(new Date());
        /*
        this._id = _id;
        this.post_id = post_id;
        this.user_id = user_id;
        this.username = username;
        this.comment_text = comment_text;
        this.comment_likes = comment_likes;
         */
        newComment.put("_id", commentId);
        newComment.put("post_id", _id);
        newComment.put("user_id", currUser.getUid().toString());
        newComment.put("comment_text", commentContent);
        newComment.put("comment_likes", 0);

        dbRef.child("users").child(currUser.getUid()).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(SocialPostDetailsActivity.this, "Unable to fetch data", Toast.LENGTH_LONG);
                } else {
                    newComment.put("username", String.valueOf(task.getResult().getValue()));
                    commentRef.child(commentId).setValue(newComment).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(SocialPostDetailsActivity.this, "Comment Added", Toast.LENGTH_SHORT);
                            getComments();
                        }
                    });
                }
            }
        });

    }

    private void getComments() {
        commentRef.addListenerForSingleValueEvent(new ValueEventListener() {
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