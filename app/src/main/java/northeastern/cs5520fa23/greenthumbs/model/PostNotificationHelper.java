package northeastern.cs5520fa23.greenthumbs.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.util.HashSet;

import northeastern.cs5520fa23.greenthumbs.R;

public class PostNotificationHelper {
    private final Context context;
    private final SharedPreferences sharedPreferences;
    private HashSet<String> notifiedPostIds = new HashSet<>();

    public PostNotificationHelper(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("PostNotificationPrefs", Context.MODE_PRIVATE);
    }

    public void listenForFriendPosts(String currentUserId) {
        DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId).child("friends");
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("posts");

        friendsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot friendSnapshot : dataSnapshot.getChildren()) {
                    String friendUserId = friendSnapshot.child("friend_id").getValue(String.class);

                    postsRef.orderByChild("uid").equalTo(friendUserId).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                            // New post added, handle notification here
                            String timestampString = snapshot.child("timestamp").getValue(String.class) + "Z";
                            Instant instant = Instant.parse(timestampString);
                            long postTimestamp = instant.toEpochMilli();
                            String postId = snapshot.getKey();

                            if (notifiedPostIds.contains(postId)) {
                                return;
                            }
                            fetchLastActiveTimestamp(currentUserId, new OnTimestampFetchedListener() {
                                @Override
                                public void onTimestampFetched(long timestamp) {
                                    if (postTimestamp > timestamp && postTimestamp > getLastNotifiedTimestamp()) {
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
                                        notifiedPostIds.add(postId);
                                    }
                                }
                            });
                        }

                        // Implement other methods of ChildEventListener as needed, possibly empty
                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {}
                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {}
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
        return sharedPreferences.getLong("lastNotifiedTimestamp", 0);
    }

    private void setLastNotifiedTimestamp(long timestamp) {
        // Store the new timestamp
        sharedPreferences.edit().putLong("lastNotifiedTimestamp", timestamp).apply();
    }

    private void fetchLastActiveTimestamp(String userId, final OnTimestampFetchedListener listener) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.child("lastActiveTime").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long lastActiveTime = dataSnapshot.getValue(Long.class);
                    if (lastActiveTime != null) {
                        listener.onTimestampFetched(lastActiveTime);
                    } else {
                        listener.onTimestampFetched(0L);
                    }
                } else {
                    listener.onTimestampFetched(0L);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
                listener.onTimestampFetched(0L);
            }
        });
    }

    public interface OnTimestampFetchedListener {
        void onTimestampFetched(long timestamp);
    }

}

