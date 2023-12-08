package northeastern.cs5520fa23.greenthumbs.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;

import northeastern.cs5520fa23.greenthumbs.R;

public class PostNotificationHelper {
    private final Context context;
    private final SharedPreferences sharedPreferences;

    public PostNotificationHelper(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("PostNotificationPrefs", Context.MODE_PRIVATE);
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
                                String timestampString = snapshot.child("timestamp").getValue(String.class) + "Z";
                                Instant instant = Instant.parse(timestampString);
                                long postTimestamp = instant.toEpochMilli();
                                if (postTimestamp > getLastNotifiedTimestamp()) {
                                    String postText = snapshot.child("post_text").getValue(String.class);
                                    String friendUsername = snapshot.child("username").getValue(String.class);
                                    String image_uri = snapshot.child("img_uri").getValue(String.class);

                                    // Trigger a notification for the new post
                                    NotificationHelper.showNotification(
                                            context,
                                            friendUsername + " made a new post",
                                            postText,
                                            R.drawable.baseline_notifications_24, image_uri
                                    );
                                    setLastNotifiedTimestamp(postTimestamp);
                                    break;
                                }
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

    private long getLastNotifiedTimestamp() {
        // Retrieve the last notified timestamp, default to 0 if not found
        return sharedPreferences.getLong("lastNotifiedTimestamp", 0);
    }

    private void setLastNotifiedTimestamp(long timestamp) {
        // Store the new timestamp
        sharedPreferences.edit().putLong("lastNotifiedTimestamp", timestamp).apply();
    }
}

