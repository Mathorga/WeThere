package keym.dev.rwethereyet.background;

import android.Manifest;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import keym.dev.rwethereyet.keym.dev.rwethereyet.util.LocationItem;
import keym.dev.rwethereyet.keym.dev.rwethereyet.util.LocationParser;

/**
 * The class manages alarm triggering when a location is reached.
 * Created by luka on 10/06/17.
 */

public class Triggerer extends IntentService {

    private static final String TAG = "Triggerer";

    private static final int ALARM_REQUEST = 1;

    private static final int M_TO_KM = 1000;

    private LocationManager manager;

    public Triggerer() {
        super(TAG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        this.manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            for (LocationItem location : new LocationParser(this).readAllItems()) {
                if (location.isActive()) {
                    Intent alarmIntent = new Intent(this, NotificationService.class);
                    alarmIntent.putExtra("location", location);
                    PendingIntent pendingAlarm = PendingIntent.getService(this,
                                                                          ALARM_REQUEST,
                                                                          alarmIntent,
                                                                          PendingIntent.FLAG_UPDATE_CURRENT);
                    this.manager.addProximityAlert(location.getLocation().latitude,
                                                   location.getLocation().longitude,
                                                   location.getRadius() * M_TO_KM,
                                                   -1,
                                                   pendingAlarm);
                }
            }
        }
    }
}