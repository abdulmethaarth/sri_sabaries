package affinity.com.srisabaries.fcm;

/**
 * Created by akash on 17/10/16.
 */


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import affinity.com.srisabaries.R;
import affinity.com.srisabaries.ui.activities.NotificationActivity;
import affinity.com.srisabaries.utils.AppConstants;

/**
 * Created by Belal on 5/27/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    int maximum=10000;
    int minimum=1;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String message="";
        if(!remoteMessage.getData().isEmpty()) {
            try {
                JSONObject js = new JSONObject(remoteMessage.getData().get("payload"));
                message = (new JSONObject(js.get("aps").toString()).get("alert")).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Calling method to generate notification
            sendNotification(message);
        }
        else
        {
            Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());

        }
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtra(AppConstants.TAG_FROM_WHERE,AppConstants.FROM_NOTIFICATION_ON_STATUS_BAR);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.txt_fitstreet))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(Notification.PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentIntent(pendingIntent);

        notificationBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getApplication().getString(R.string.default_notification_channel_id);
            NotificationChannel channel = new NotificationChannel(channelId,   getString(R.string.txt_fitstreet), NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(messageBody);
            notificationManager.createNotificationChannel(channel);
            notificationBuilder.setChannelId(channelId);
        }

        notificationManager.notify(generateRandom(), notificationBuilder.build());

    }


    public int generateRandom()
    {
        Random rn = new Random();
        int n = maximum - minimum + 1;
        int i = rn.nextInt() % n;
        return  minimum + i;

    }
}
