package magikards.main;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "NOTICIAS";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String from = remoteMessage.getFrom();
        Log.d(TAG,"Mensagem recebida de: " + from);

        if(remoteMessage.getNotification() != null){
            Log.d(TAG, "Notificação: " + remoteMessage.getNotification().getBody());

            mostrarNotificação(remoteMessage.getNotification().getTitle(), remoteMessage. getNotification().getBody());

        }

        if(remoteMessage.getData().size() > 0){
            Log.d(TAG, "Data: " + remoteMessage.getData());
        }

    }

    private void mostrarNotificação(String title, String body) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.pokeball_notification44)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundURI)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

    }
}
