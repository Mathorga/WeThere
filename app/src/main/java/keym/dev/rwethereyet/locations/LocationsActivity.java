package keym.dev.rwethereyet.locations;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import keym.dev.rwethereyet.R;
import keym.dev.rwethereyet.addlocation.AddLocationActivity;
import keym.dev.rwethereyet.keym.dev.rwethereyet.utils.LocationItem;

public class LocationsActivity extends AppCompatActivity {

    private static final String TAG = "LocationsActivity";
    private static final int NEW_LOCATION_REQUEST = 1;

    private List<LocationItem> locations;
    private ListView locationsView;
    private FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_locations);

        // Ask for locations permissions.
        ActivityCompat.requestPermissions(this,
                                          new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                          1);
        ActivityCompat.requestPermissions(this,
                                          new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                          1);

        this.locations = new ArrayList<>();
        this.locationsView = (ListView) this.findViewById(R.id.locationsList);
        this.addButton = (FloatingActionButton) this.findViewById(R.id.addLocation);

        this.locations.add(new LocationItem("Pesaro",
                                             5,
                                             new Location("gps"),
                                             RingtoneManager.getRingtone(this, RingtoneManager.getValidRingtoneUri(this))));
        this.locations.add(new LocationItem("Cesena",
                                             5,
                                             new Location("gps"),
                                             RingtoneManager.getRingtone(this, RingtoneManager.getValidRingtoneUri(this))));
        this.locations.add(new LocationItem("Milano",
                                             5,
                                             new Location("gps"),
                                             RingtoneManager.getRingtone(this, RingtoneManager.getValidRingtoneUri(this))));
        this.locations.add(new LocationItem("Roma",
                                             5,
                                             new Location("gps"),
                                             RingtoneManager.getRingtone(this, RingtoneManager.getValidRingtoneUri(this))));
        this.locations.add(new LocationItem("Bari",
                                             5,
                                             new Location("gps"),
                                             RingtoneManager.getRingtone(this, RingtoneManager.getValidRingtoneUri(this))));
        this.locations.add(new LocationItem("Torino",
                                             5,
                                             new Location("gps"),
                                             RingtoneManager.getRingtone(this, RingtoneManager.getValidRingtoneUri(this))));
        this.locations.add(new LocationItem("Caserta",
                                             5,
                                             new Location("gps"),
                                             RingtoneManager.getRingtone(this, RingtoneManager.getValidRingtoneUri(this))));
        this.locations.add(new LocationItem("Barrea",
                                             5,
                                             new Location("gps"),
                                             RingtoneManager.getRingtone(this, RingtoneManager.getValidRingtoneUri(this))));
        this.locations.add(new LocationItem("San Giustino",
                                             5,
                                             new Location("gps"),
                                             RingtoneManager.getRingtone(this, RingtoneManager.getValidRingtoneUri(this))));
        LocationListAdapter adapter = new LocationListAdapter(this, R.layout.item_location, this.locations);
        this.locationsView.setAdapter(adapter);

        this.locationsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(LocationsActivity.this, "Item" + position, Toast.LENGTH_SHORT).show();
            }
        });

        this.locationsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(LocationsActivity.this, "Long" + i, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        this.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the device is connected to the internet.
                if (checkInternetAccess()) {
                    Intent addLocationIntent = new Intent(LocationsActivity.this, AddLocationActivity.class);
                    startActivityForResult(addLocationIntent, NEW_LOCATION_REQUEST);
                } else {
                    Toast.makeText(LocationsActivity.this, getString(R.string.no_internet_access), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == NEW_LOCATION_REQUEST) {
                Uri locationUri = data.getData();
            }
        } else {
            Toast.makeText(this, "Unable to add a location", Toast.LENGTH_SHORT);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Checks the status of the internet connection, regardless of the technology used.
     * @return
     * whether the device is connected to the internet or not.
     */
    private boolean checkInternetAccess() {
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }
}
