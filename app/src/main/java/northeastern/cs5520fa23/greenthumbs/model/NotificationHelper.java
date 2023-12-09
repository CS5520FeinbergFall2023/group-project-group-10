package northeastern.cs5520fa23.greenthumbs.model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import androidx.core.app.NotificationCompat;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import northeastern.cs5520fa23.greenthumbs.MainActivity;

public class NotificationHelper {
    private static final String CHANNEL_ID = "NOTIFICATION_CHANNEL";

    public static void showNotification(Context context, String message, String description, int imageResourceId) {
        // Call the other method with null or empty string for imageUrl
        showNotification(context, message, description, imageResourceId, "");
    }

    public static void showNotification(Context context, String message, String description, int imageResourceId, String imageUrl) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager == null) {
            return;
        }

        createNotificationChannelIfNeeded(notificationManager);

        int notificationID = generateUniqueNotificationId();
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imageResourceId);

        Notification.Builder builder = new Notification.Builder(context, CHANNEL_ID)
                .setContentTitle(message)
                .setContentText(description)
                .setContentIntent(pendingIntent)
                .setSmallIcon(imageResourceId)
                .setLargeIcon(bitmap)
                .setAutoCancel(true);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            final Bitmap[] bitmaps = new Bitmap[1];
            Picasso.get().load(imageUrl).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bmp, Picasso.LoadedFrom from) {
                    builder.setLargeIcon(bmp);
                    builder.setChannelId(CHANNEL_ID);
                    Notification notification = builder.build();
                    notificationManager.notify(generateUniqueNotificationId(), notification);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    // Handle bitmap load failure
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });
        } else {
            // If no image URL, just show the notification as usual
            builder.setChannelId(CHANNEL_ID);
            Notification notification = builder.build();
            notificationManager.notify(generateUniqueNotificationId(), notification);
        }

        /*builder.setChannelId(CHANNEL_ID);
        Notification notification = builder.build();
        notificationManager.notify(notificationID, notification);*/
    }

    private static void createNotificationChannelIfNeeded(NotificationManager notificationManager) {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "General Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setDescription("Channel for general notifications");
        notificationManager.createNotificationChannel(channel);
    }

    private static int generateUniqueNotificationId() {
        return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    }
}

