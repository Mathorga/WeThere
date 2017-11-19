package keym.dev.rwethereyet.locations;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import keym.dev.rwethereyet.MainActivity;
import keym.dev.rwethereyet.R;
import keym.dev.rwethereyet.background.NotificationService;
import keym.dev.rwethereyet.keym.dev.rwethereyet.util.LocationItem;
import keym.dev.rwethereyet.keym.dev.rwethereyet.util.LocationParser;

/**
 * Created by Luka on 09/05/2017.
 */
public class LocationListAdapter extends ArrayAdapter<LocationItem> {

    private static final String TAG = "LocationListAdapter";

    private static final NumberFormat coordFormat = new DecimalFormat("##.########");

    private final Context context;
    private int layoutResource;
    
    public LocationListAdapter(final Context context, final int layoutResource, List<LocationItem> data) {
        super(context, layoutResource, data);
        this.context = context;
        this.layoutResource = layoutResource;
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
            TextView name = (TextView) view.findViewById(R.id.locationName);
            TextView coordinates = (TextView) view.findViewById(R.id.locationCoord);
            Switch active = (Switch) view.findViewById(R.id.locationActive);

            if (name != null) {
                name.setText(item.getLabel());
            }

            if (coordinates != null) {
                coordinates.setText(coordFormat.format(item.getLocation().latitude) + " : " + coordFormat.format(item.getLocation().longitude));
            }

            if (active != null) {
                active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        item.setActive(b);

                        // Notify activation/disactivation.
                        if (context instanceof MainActivity) {
                            ((MainActivity) context).refreshDistances(item, b);
                        }

                        // Register the alarm for the location.
                        LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                        int permission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                        if (permission == PackageManager.PERMISSION_GRANTED) {
                            Intent alarmIntent = new Intent(getContext(), NotificationService.class);
                            alarmIntent.putExtra("location", item);
                            Log.d(TAG, "Put LocationItem extra");
                            PendingIntent pendingAlarm = PendingIntent.getService(getContext(),
                                                                                  LocationsFragment.ALARM_REQUEST,
                                                                                  alarmIntent,
                                                                                  PendingIntent.FLAG_UPDATE_CURRENT);
                            if (item.isActive()) {
                                // Set alarm.
                                manager.addProximityAlert(item.getLocation().latitude,
                                                          item.getLocation().longitude,
                                                          item.getRadius() * LocationItem.M_TO_KM,
                                                          -1,
                                                          pendingAlarm);
                                Log.d(TAG, "Set proximity alert for " + item.getLabel());
                                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
                                    @Override
                                    public void onLocationChanged(Location location) {
                                        Log.d(TAG, "Location changed");
                                    }

                                    @Override
                                    public void onStatusChanged(String provider, int status, Bundle extras) {

                                    }

                                    @Override
                                    public void onProviderEnabled(String provider) {

                                    }

                                    @Override
                                    public void onProviderDisabled(String provider) {

                                    }
                                });
                            } else {
                                // Remove alarm.
                                manager.removeProximityAlert(pendingAlarm);
                            }
                        }

                        // Save on file.
                        LocationParser parser = new LocationParser(LocationListAdapter.this.getContext());
                        try {
                            parser.updateItem(item);
                        } catch (JSONException exception) {
                            exception.printStackTrace();
                        }
                    }
                });
                active.setChecked(item.isActive());
            }
        }
        return view;
    }
}
