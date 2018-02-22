package keym.dev.rwethereyet.locations;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import keym.dev.rwethereyet.util.LocationParser;
import keym.dev.rwethereyet.util.LocationItem;
import keym.dev.rwethereyet.util.ParcelableUtil;

import static android.app.Activity.RESULT_OK;

/**
 * Created by luka on 23/05/17.
 * The Fragment shows a list of all saved LocationItems.
 */

public class LocationsFragment extends Fragment {

    private static final String TAG = "LocationsFragment";

    public static final String STATE_ARGUMENT = "state";

    public static final int NEW_LOCATION_REQUEST = 1;
    public static final int UPDATE_LOCATION_REQUEST = 2;
    public static final int ALARM_REQUEST = 3;

    private View rootView;
    private List<LocationItem> locations;
    private ListView locationsView;
    private FloatingActionButton addButton;

    private LocationListAdapter adapter;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_locations, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.rootView = view;

        this.locations = new ArrayList<>();
        this.locationsView = (ListView) this.rootView.findViewById(R.id.locationsList);
        this.addButton = (FloatingActionButton) this.rootView.findViewById(R.id.addLocation);

        this.locations = new LocationParser(this.getContext()).readAllItems();

        this.adapter = new LocationListAdapter(this.getActivity(), R.layout.item_location, this.locations);
        this.locationsView.setAdapter(this.adapter);

        this.locationsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Edit item.
                if (checkInternetAccess()) {
                    Intent addLocationIntent = new Intent(getActivity(), AddLocationActivity.class);
                    addLocationIntent.putExtra("location", ParcelableUtil.marshall(locations.get(position)));
                    startActivityForResult(addLocationIntent, UPDATE_LOCATION_REQUEST);
                }
            }
        });

        this.locationsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Delete item.
                Toast.makeText(getContext().getApplicationContext(),
                               "Deleted " + locations.get(i).getLabel(),
                               Toast.LENGTH_SHORT).show();
                adapter.remove(locations.get(i));
                new LocationParser(getContext()).deleteItem(i);

                // DEBUG Notify fake proximity.
//                Intent notificationIntent = new Intent(getContext(), NotificationService.class);
//                notificationIntent.putExtra("location", locations.get(i));
//                notificationIntent.putExtra(LocationManager.KEY_PROXIMITY_ENTERING, true);
//                getActivity().startService(notificationIntent);
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
            Log.wtf(TAG, "NEW RESULT");
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Log.d(TAG, "Intent is NULL");
                } else {
                    LocationItem item = ParcelableUtil.unmarshall(data.getByteArrayExtra("result"), LocationItem.CREATOR);

                    if (item.getId().equals(LocationItem.ID_UNDEFINED)) {
                        Log.d(TAG, "Id set to " + new LocationParser(this.getContext()).getNextIndex());
                        item.setId(new LocationParser(this.getContext()).getNextIndex());
                    }
                    // Write new location on file.
                    try {
                        new LocationParser(this.getContext()).writeItem(item);
                    } catch(JSONException exception) {
                        exception.printStackTrace();
                    }

                    this.locations.add(item);
                    adapter.notifyDataSetChanged();
                }
            }
        } else if (requestCode == UPDATE_LOCATION_REQUEST) {
            Log.wtf(TAG, "UPDATE RESULT");
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Log.d(TAG, "Intent is NULL");
                } else {
                    LocationItem item = ParcelableUtil.unmarshall(data.getByteArrayExtra("result"), LocationItem.CREATOR);

                    if (item.getId().equals(LocationItem.ID_UNDEFINED)) {
                        Log.d(TAG, "Id set to " + new LocationParser(this.getContext()).getNextIndex());
                        item.setId(new LocationParser(this.getContext()).getNextIndex());
                    }
                    // Write new location on file.
                    try {
                        new LocationParser(this.getContext()).writeItem(item);
                    } catch(JSONException exception) {
                        exception.printStackTrace();
                    }

                    int removeId = data.getIntExtra("removeId", 0);
                    Log.wtf(TAG, "RemoveId received: " + removeId);
                    this.adapter.remove(this.locations.get(removeId));
                    new LocationParser(this.getContext()).deleteItem(removeId);

                    this.locations.add(item);
                    adapter.notifyDataSetChanged();
                }
            }
        } else {
            Toast.makeText(this.getActivity(), "Unable to add a location", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks the status of the internet connection, regardless of the technology used.
     * @return
     *  whether the device is connected to the internet or not.
     */
    private boolean checkInternetAccess() {
        ConnectivityManager manager = (ConnectivityManager) this.getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            return info != null && info.isConnectedOrConnecting();
        } else {
            return false;
        }
    }
}
