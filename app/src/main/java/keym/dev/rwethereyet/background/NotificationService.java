package keym.dev.rwethereyet.background;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import keym.dev.rwethereyet.MainActivity;
import keym.dev.rwethereyet.R;
import keym.dev.rwethereyet.keym.dev.rwethereyet.util.LocationItem;

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
        LocationItem location = intent.getParcelableExtra("location");
        boolean entering = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);

        Intent openAppIntent = new Intent(this, MainActivity.class);
        openAppIntent.putExtra("location", location);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                                             .setSmallIcon(R.drawable.ic_stat_external)
                                             .setContentTitle(this.getResources().getString(R.string.destination_reached))
//                                             .setContentText(location.getLabel())
                                             .setContentIntent(PendingIntent.getService(this, BACK_TO_MAIN_REQUEST, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        // Set an ID for the notification.
//        int notificationId = location.getId();
        int notificationId = 0;
        if (entering) {
            // Get an instance of the NotificationManager service.
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Build the notification and issue it.
            manager.notify(notificationId, builder.build());
            // Start alarm.
            Uri alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            Ringtone ringtone = RingtoneManager.getRingtone(this.getApplicationContext(), alarm);
            ringtone.play();
        }
    }
}