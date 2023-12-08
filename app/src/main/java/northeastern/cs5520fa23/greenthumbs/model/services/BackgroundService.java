package northeastern.cs5520fa23.greenthumbs.model.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import northeastern.cs5520fa23.greenthumbs.model.MessageNotificationHelper;
import northeastern.cs5520fa23.greenthumbs.model.PostNotificationHelper;

public class BackgroundService extends Service {
    private PostNotificationHelper postNotificationHelper;
    private MessageNotificationHelper messageNotificationHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        postNotificationHelper = new PostNotificationHelper(getApplicationContext());
        messageNotificationHelper = new MessageNotificationHelper(getApplicationContext());
    }

    private String getCurrentUserIdFromIntent(Intent intent) {
        if (intent != null && intent.hasExtra("userIdKey")) {
            return intent.getStringExtra("userIdKey");
        }
        return "";
    }

    private String getCurrentUsernameFromIntent(Intent intent) {
        if (intent != null && intent.hasExtra("username")) {
            return intent.getStringExtra("username");
        }
        return "";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String currentUserId = getCurrentUserIdFromIntent(intent);
        postNotificationHelper.listenForFriendPosts(currentUserId);
        String currentUsername = getCurrentUsernameFromIntent(intent);
        messageNotificationHelper.startListeningForUserMessages(currentUsername);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // For bound services only, otherwise return null
        return null;
    }

    // Add other service lifecycle methods if necessary

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Clean up resources, if any, and stop listening for posts
    }
}

