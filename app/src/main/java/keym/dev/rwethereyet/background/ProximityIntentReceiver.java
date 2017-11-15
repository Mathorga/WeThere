package keym.dev.rwethereyet.background;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import keym.dev.rwethereyet.R;
import keym.dev.rwethereyet.keym.dev.rwethereyet.util.LocationItem;

/**
 * Created by luka on 15/11/17.
 */

public class ProximityIntentReceiver extends BroadcastReceiver {

    private static final String TAG = "ProximityIntentReceiver";
    private static final int NOTIFICATION_ID = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        LocationItem location = intent.getParcelableExtra("location");

        Boolean entering = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);

        if (entering) {
            Log.d(TAG, "entering");
        }
        else {
            Log.d(TAG, "exiting");
        }

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = createNotification(context, location);

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private Notification createNotification(Context context, LocationItem location) {


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                                             .setSmallIcon(R.drawable.ic_stat_external)
                                             .setContentTitle("Fixh Dich!")
                                             .setContentText("Fuck u");

        return builder.build();
    }
}
