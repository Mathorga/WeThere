package keym.dev.rwethereyet.locations;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import keym.dev.rwethereyet.MainActivity;
import keym.dev.rwethereyet.R;
import keym.dev.rwethereyet.addlocation.AddLocationActivity;
import keym.dev.rwethereyet.keym.dev.rwethereyet.util.LocationParser;
import keym.dev.rwethereyet.keym.dev.rwethereyet.util.LocationItem;
import keym.dev.rwethereyet.settings.SettingsActivity;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by luka on 23/05/17.
 */

public class LocationsFragment extends Fragment {

    public static final String STATE_ARGUMENT = "state";

    private static final String TAG = "LocationsFragment";
    private static final int NEW_LOCATION_REQUEST = 1;

    private View rootView;
    private List<LocationItem> locations;
    private ListView locationsView;
    private FloatingActionButton addButton;

    private LocationListAdapter adapter;

    private LocationParser parser;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        this.parser = new LocationParser(this.getContext());
        return inflater.inflate(R.layout.fragment_locations, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.rootView = view;

        this.locations = new ArrayList<>();
        this.locationsView = (ListView) this.rootView.findViewById(R.id.locationsList);
        this.addButton = (FloatingActionButton) this.rootView.findViewById(R.id.addLocation);

        this.locations = this.parser.readAllItems();
//        this.locations.add(this.parser.readItem());
//
//        this.locations.add(new LocationItem("Pesaro",
//                5,
//                new LatLng(12.12345, 43.54326),
//                RingtoneManager.getRingtone(this.getActivity(),
//                        RingtoneManager.getValidRingtoneUri(this.getActivity()))));
//        this.locations.add(new LocationItem("Cesena",
//                5,
//                new LatLng(40.76543, 34.83624),
//                RingtoneManager.getRingtone(this.getActivity(),
//                        RingtoneManager.getValidRingtoneUri(this.getActivity()))));
//        this.locations.add(new LocationItem("Milano",
//                5,
//                new LatLng(40.76543, 34.83624),
//                RingtoneManager.getRingtone(this.getActivity(),
//                        RingtoneManager.getValidRingtoneUri(this.getActivity()))));
//        this.locations.add(new LocationItem("Roma",
//                5,
//                new LatLng(40.76543, 34.83624),
//                RingtoneManager.getRingtone(this.getActivity(),
//                        RingtoneManager.getValidRingtoneUri(this.getActivity()))));
//        this.locations.add(new LocationItem("Bari",
//                5,
//                new LatLng(40.76543, 34.83624),
//                RingtoneManager.getRingtone(this.getActivity(),
//                        RingtoneManager.getValidRingtoneUri(this.getActivity()))));
//        this.locations.add(new LocationItem("Torino",
//                5,
//                new LatLng(40.76543, 34.83624),
//                RingtoneManager.getRingtone(this.getActivity(),
//                        RingtoneManager.getValidRingtoneUri(this.getActivity()))));
//        this.locations.add(new LocationItem("Caserta",
//                5,
//                new LatLng(40.76543, 34.83624),
//                RingtoneManager.getRingtone(this.getActivity(),
//                        RingtoneManager.getValidRingtoneUri(this.getActivity()))));
//        this.locations.add(new LocationItem("Barrea",
//                5,
//                new LatLng(40.76543, 34.83624),
//                RingtoneManager.getRingtone(this.getActivity(),
//                        RingtoneManager.getValidRingtoneUri(this.getActivity()))));
//        this.locations.add(new LocationItem("San Giustino",
//                5,
//                new LatLng(40.76543, 34.83624),
//                RingtoneManager.getRingtone(this.getActivity(),
//                        RingtoneManager.getValidRingtoneUri(this.getActivity()))));

        adapter = new LocationListAdapter(this.getActivity(), R.layout.item_location, this.locations);
        this.locationsView.setAdapter(adapter);

        this.locationsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(view.getContext().getApplicationContext(),
                        locations.get(position).getLabel(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        this.locationsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(view.getContext().getApplicationContext(),
                        locations.get(i).getLocation().latitude + ", " + locations.get(i).getLocation().longitude,
                        Toast.LENGTH_SHORT).show();

                // ---------------------------------------------------------------------------------
                Intent openAppIntent = new Intent(getActivity(), MainActivity.class);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.drawable.ic_stat_external)
                        .setContentTitle(getActivity().getResources().getString(R.string.destination_reached))
                        .setContentText(locations.get(i).getLabel())
                        .setContentIntent(PendingIntent.getActivity(getActivity(), 1, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT));// Start alarm.
                Uri alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                Ringtone ringtone = RingtoneManager.getRingtone(getActivity().getApplicationContext(), alarm);
                ringtone.play();
                // ---------------------------------------------------------------------------------

                // Set an ID for the notification.
                int notificationId = i;
                // Get an instance of the NotificationManager service.
                NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                // Build the notification and issue it.
                manager.notify(notificationId, builder.build());
                return true;
            }
        });

        this.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the device is connected to the internet.
                if (checkInternetAccess()) {
                    Intent addLocationIntent = new Intent(getActivity(), AddLocationActivity.class);
                    startActivityForResult(addLocationIntent, NEW_LOCATION_REQUEST);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.no_internet_access), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == NEW_LOCATION_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Log.d(TAG, "Intent is NULL");
                } else {
                    LocationItem item = data.getParcelableExtra("result");

                    if (item.getId().equals(LocationItem.ID_UNDEFINED)) {
                        item.setId(this.parser.getNextIndex());
                    }
                    // Write new location on file.
                    try {
                        this.parser.writeItem(item);
                    } catch(JSONException exception) {
                        exception.printStackTrace();
                    }

                    this.locations.add(item);
                    adapter.notifyDataSetChanged();
                }
            }
        } else {
            Toast.makeText(this.getActivity(), "Unable to add a location", Toast.LENGTH_SHORT);
        }
    }

    /**
     * Checks the status of the internet connection, regardless of the technology used.
     * @return
     * whether the device is connected to the internet or not.
     */
    private boolean checkInternetAccess() {
        ConnectivityManager manager = (ConnectivityManager) this.getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }
}
