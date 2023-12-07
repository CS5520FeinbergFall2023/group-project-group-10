package northeastern.cs5520fa23.greenthumbs.model;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import northeastern.cs5520fa23.greenthumbs.R;

public class PostNotificationHelper {

    private final Context context;

    public PostNotificationHelper(Context context) {
        this.context = context;
    }

    public void listenForFriendPosts(String currentUserId) {
        DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId).child("friends");
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("posts");

        friendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                    String friendUserId = friendSnapshot.child("friend_id").getValue(String.class);

                    // Listen for posts made by this friend
                    postsRef.orderByChild("uid").equalTo(friendUserId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot postSnapshot) {
                            for (DataSnapshot snapshot : postSnapshot.getChildren()) {
                                String postText = snapshot.child("post_text").getValue(String.class);
                                String friendUsername = snapshot.child("username").getValue(String.class);

                                // Trigger a notification for the new post
                                NotificationHelper.showNotification(
                                        context,
                                        friendUsername + " made a new post",
                                        postText,
                                        R.drawable.baseline_notifications_24
                                );
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle error
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}

