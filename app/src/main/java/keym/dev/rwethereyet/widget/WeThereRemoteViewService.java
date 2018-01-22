package keym.dev.rwethereyet.widget;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import keym.dev.rwethereyet.R;
import keym.dev.rwethereyet.background.LocationUpdateService;
import keym.dev.rwethereyet.keym.dev.rwethereyet.util.LocationItem;
import keym.dev.rwethereyet.keym.dev.rwethereyet.util.LocationParser;

/**
 * Created by luka on 14/01/18.
 */

public class WeThereRemoteViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WeThereRemoteViewsFactory(this.getApplicationContext(),
                                             new LocationParser(this.getApplicationContext()).readAllItems());
    }
}

class WeThereRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = "Factory";

    private Context context;
    private LocationManager manager;

    private final List<LocationItem> items;

    public WeThereRemoteViewsFactory(final Context applicationContext, final List<LocationItem> items) {
        this.context = applicationContext;
        this.items = items;
        this.manager = (LocationManager) applicationContext.getSystemService(Context.LOCATION_SERVICE);
        Log.wtf(TAG, items.toString());
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        final RemoteViews views = new RemoteViews(this.context.getPackageName(), R.layout.item_distance_widget);


        final LocationItem item = this.items.get(position);
        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            try {
                this.manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LocationUpdateService.LOCATION_INTERVAL, LocationUpdateService.LOCATION_DISTANCE, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        float[] result = new float[1];
                        Location.distanceBetween(item.getLocation().latitude,
                                item.getLocation().longitude,
                                location.getLatitude(),
                                location.getLongitude(),
                                result);
                        views.setTextViewText(R.id.wLocationDistance, String.valueOf(result[0]) + "m");
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                });
                Log.d(TAG, "Requested location updates");
            } catch (java.lang.SecurityException ex) {
                Log.i(TAG, "Failed to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "GPS provider does not exist " + ex.getMessage());
            }

            // Retrieve the last cached location to avoid the latency for the first location.
            float[] result = new float[1];
            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                Location.distanceBetween(item.getLocation().latitude,
                        item.getLocation().longitude,
                        lastLocation.getLatitude(),
                        lastLocation.getLongitude(),
                        result);
                views.setTextViewText(R.id.wLocationDistance, String.valueOf(result[0]));
            }
        }


//        views.setTextViewText(R.id.wLocationDistance, items.get(position).getRadius().toString() + " km");
        views.setTextViewText(R.id.wLocationDistanceName, items.get(position).getLabel());

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}