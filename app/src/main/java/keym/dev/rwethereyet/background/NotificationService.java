package keym.dev.rwethereyet.background;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import keym.dev.rwethereyet.MainActivity;
import keym.dev.rwethereyet.R;
import keym.dev.rwethereyet.util.LocationItem;
import keym.dev.rwethereyet.util.ParcelableUtil;

/**
 * Created by luka on 27/06/17.
 */
public class NotificationService extends IntentService {

    private static final String TAG = "NotificationService";

    private static final int BACK_TO_MAIN_REQUEST = 1;

    public NotificationService() {
        super(TAG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        LocationItem location = ParcelableUtil.unmarshall(intent.getByteArrayExtra("location"), LocationItem.CREATOR);

        Log.wtf(TAG, intent.getExtras().toString());

        boolean entering = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);

        // Set an ID for the notification.
        int notificationId = location.getId();
        if (entering) {
            Intent openAppIntent = new Intent(this, MainActivity.class);
            openAppIntent.putExtra("location", location);

            // Start alarm.
//            Uri alarm = location.getTone();
//            MediaPlayer player = MediaPlayer.create(this, alarm);
//            player.setLooping(true);
//            player.start();
//            Ringtone ringtone = RingtoneManager.getRingtone(this.getApplicationContext(), alarm);
//            ringtone.play();
//            Log.wtf(TAG, player.toString());
//            openAppIntent.putExtra("player", player.toString());

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                                                                       .setSound(location.getTone())
                                                                       .setSmallIcon(R.drawable.ic_stat_external)
                                                                       .setContentTitle(this.getResources().getString(R.string.destination_reached))
                                                                       .setContentText(location.getLabel())
                                                                       .setContentIntent(PendingIntent.getActivity(this, BACK_TO_MAIN_REQUEST, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT));
            // Get an instance of the NotificationManager service.
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Build the notification and issue it.
            manager.notify(notificationId, builder.build());
        }
    }
}