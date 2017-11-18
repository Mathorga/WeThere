package keym.dev.rwethereyet.Distance;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import keym.dev.rwethereyet.R;
import keym.dev.rwethereyet.background.LocationUpdateService;
import keym.dev.rwethereyet.keym.dev.rwethereyet.util.LocationItem;

/**
 * Created by luka on 18/11/17.
 */

class DistanceListAdapter extends ArrayAdapter<LocationItem> {

    private static final String TAG = "DistanceListAdapter";

    private int layoutResource;
    private LocationManager manager;

    public DistanceListAdapter(final Context context, final int layoutResource, List<LocationItem> data) {
        super(context, layoutResource, data);
        this.layoutResource = layoutResource;
        this.manager = (LocationManager) this.getContext().getSystemService(Context.LOCATION_SERVICE);
//        try {
//            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LocationUpdateService.LOCATION_INTERVAL, LocationUpdateService.LOCATION_DISTANCE, this);
//            Log.d(TAG, "Requested location updates");
//        } catch (java.lang.SecurityException ex) {
//            Log.i(TAG, "fail to request location update, ignore", ex);
//        } catch (IllegalArgumentException ex) {
//            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
//        }
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(this.getContext());
            view = inflater.inflate(this.layoutResource, null);
        }

        final LocationItem item = this.getItem(position);

        if (item != null) {
            TextView name = (TextView) view.findViewById(R.id.locationDistanceName);
            TextView distance = (TextView) view.findViewById(R.id.locationDistance);

            if (name != null) {
                name.setText(item.getLabel());
            }

            if (distance != null) {
                if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Location lastLocation = this.manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    float[] result = new float[1];
                    if (lastLocation != null) {
                        Location.distanceBetween(item.getLocation().latitude,
                                item.getLocation().longitude,
                                lastLocation.getLatitude(),
                                lastLocation.getLongitude(),
                                result);
                        distance.setText(String.valueOf(result[0]));
                    }
                }
            }
        }
        return view;
    }

//    @Override
//    public void onLocationChanged(Location location) {
//
//    }
//
//    @Override
//    public void onStatusChanged(String s, int i, Bundle bundle) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String s) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String s) {
}
