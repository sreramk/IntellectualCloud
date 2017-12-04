package com.intellectualcloud.intellectualcloud;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by root on 2/12/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    NotificationCompat.Builder builder;

String title,body;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        super.onMessageReceived(remoteMessage);

        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> map = remoteMessage.getData();

        title=notification.getTitle();
        body=notification.getBody();

        sendNotification(title,body, map);


    }


    private void sendNotification(String title, String body, Map<String, String> map) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
       // String TrueOrFalse = map.get("TrueorFalse");
        String picture_url = map.get("picture_url");
        Intent intent = new Intent(this, Home.class);
        intent.putExtra("title",title);
        intent.putExtra("picture_url",picture_url);

        intent.putExtra("body",body);

        //intent.putExtra("AnotherActivity", TrueOrFalse);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "100")
                .setContentTitle(title)
                .setContentText(body)

                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setContentInfo(title)
                .setLargeIcon(icon)
                .setSmallIcon(R.mipmap.ic_launcher);

        try {

            if (picture_url != null && !"".equals(picture_url)) {
                URL url = new URL(picture_url);
                Uri uri = Uri.parse(url.toString());
                Bitmap bitmap = getBitmapfromUrl(uri.toString());

                //Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).setSummaryText(body));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        //  notificationBuilder.setLights(Color.YELLOW, 1000, 300);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

}


