package keym.dev.rwethereyet.background;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;

import org.json.JSONObject;

/**
 * The class manages alarm triggering when a location is reached.
 * Created by luka on 10/06/17.
 */

public class AlarmManager extends Service {

    private LocationManager manager;

    @Override
    public IBinder onBind(Intent intent) {
        this.manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//        this.manager.addProximityAlert();
        return null;
    }
}
