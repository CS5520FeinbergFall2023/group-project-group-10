package northeastern.cs5520fa23.greenthumbs.model;

import android.Manifest;
import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import northeastern.cs5520fa23.greenthumbs.R;

public class MessageNotificationHelper {
    private final Context context;
    private final NotificationManagerCompat notificationManager;
    private SharedPreferences lastNotifiedPrefs;

    public MessageNotificationHelper(Context context) {
        this.context = context;
        this.notificationManager = NotificationManagerCompat.from(context);
        this.lastNotifiedPrefs = context.getSharedPreferences("LastNotifiedMessages", Context.MODE_PRIVATE);
    }

    public void startListeningForUserMessages(String userId) {
        DatabaseReference userChatsRef = FirebaseDatabase.getInstance().getReference("chats");

        // Listen for any chat nodes that include the user
        userChatsRef.orderByKey().startAt(userId + "_")//.endAt(userId + "_\uf8ff")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                        String chatNode = dataSnapshot.getKey();
                        if (chatNode != null && chatNode.contains(userId)) {
                            listenToChatMessages(chatNode);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void listenToChatMessages(String userId) {
        DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference("chats");

        chatsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String previousChildName) {
                // Check if the node starts with the user ID
                String chatNodeKey = dataSnapshot.getKey();
                if (chatNodeKey != null && chatNodeKey.contains(userId)) {
                    dataSnapshot.getRef().child("messages").orderByChild("timestamp").limitToLast(1)
                            .addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot messageSnapshot, String previousChildName) {
                                    String messageTimestamp = messageSnapshot.child("timestamp").getValue(String.class);
                                    String lastNotifiedTimestamp = lastNotifiedPrefs.getString(chatNodeKey, "");

                                    if (messageTimestamp != null && !messageTimestamp.equals(lastNotifiedTimestamp)) {
                                        String messageContent = messageSnapshot.child("messageContent").getValue(String.class);

                                        lastNotifiedPrefs.edit().putString(chatNodeKey, messageTimestamp).apply();

                                        String[] parts = chatNodeKey.split("_");
                                        String sender = parts[parts.length - 1];

                                        // Create and send a notification if the messageContent is not null
                                        if (messageContent != null) {
                                            showNotification(
                                                    "New Message from " + sender,
                                                    messageContent,
                                                    R.drawable.baseline_notifications_24
                                            );
                                        }
                                    }
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }

                                // ... Implement other ChildEventListener methods as necessary ...
                            });
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String chatNodeKey = snapshot.getKey();
                if (chatNodeKey != null && chatNodeKey.startsWith(userId)) {
                    snapshot.getRef().child("messages").orderByChild("timestamp").limitToLast(1)
                            .addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot messageSnapshot, String previousChildName) {
                                    String messageTimestamp = messageSnapshot.child("timestamp").getValue(String.class);
                                    String lastNotifiedTimestamp = lastNotifiedPrefs.getString(chatNodeKey, "");

                                    if (messageTimestamp != null && !messageTimestamp.equals(lastNotifiedTimestamp)) {
                                        String messageContent = messageSnapshot.child("messageContent").getValue(String.class);

                                        lastNotifiedPrefs.edit().putString(chatNodeKey, messageTimestamp).apply();

                                        // Create and send a notification if the messageContent is not null
                                        if (messageContent != null) {
                                            showNotification(
                                                    "New Message in " + chatNodeKey,
                                                    messageContent,
                                                    R.drawable.baseline_notifications_24
                                            );
                                        }
                                    }
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }

                                // ... Implement other ChildEventListener methods as necessary ...
                            });
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                System.out.println("Going here");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                System.out.println("Going here");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Going here");
            }

        });
    }


    private void showNotification(String title, String content, int iconResourceId) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "NOTIFICATION_CHANNEL")
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(iconResourceId)
                    .setAutoCancel(true);

            Notification notification = builder.build();
            notificationManager.notify(generateUniqueNotificationId(), notification);
        } else {
            // Permission is not granted, request the permission or handle accordingly
            // If this is an Activity, you can use ActivityCompat.requestPermissions to request the permission
        }
    }

    private int generateUniqueNotificationId() {
        // Implement a method to generate a unique notification ID
        return (int) (System.currentTimeMillis() & 0xfffffff);
    }
}
