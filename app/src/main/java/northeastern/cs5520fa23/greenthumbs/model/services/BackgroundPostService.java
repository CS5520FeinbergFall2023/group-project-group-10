package northeastern.cs5520fa23.greenthumbs.model.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import northeastern.cs5520fa23.greenthumbs.model.PostNotificationHelper;

public class BackgroundPostService extends Service {

    private PostNotificationHelper postNotificationHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        postNotificationHelper = new PostNotificationHelper(getApplicationContext());
    }

    private String getCurrentUserIdFromIntent(Intent intent) {
        return "";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String currentUserId = getCurrentUserIdFromIntent(intent);
        postNotificationHelper.listenForFriendPosts(currentUserId);
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

