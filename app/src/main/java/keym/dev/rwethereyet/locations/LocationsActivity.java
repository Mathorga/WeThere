package keym.dev.rwethereyet.locations;

import android.content.Intent;
import android.location.Location;
import android.media.RingtoneManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import keym.dev.rwethereyet.R;
import keym.dev.rwethereyet.addlocation.AddLocationActivity;

public class LocationsActivity extends AppCompatActivity {

    private static final String TAG = "LocationsActivity";

    private List<SavedLocation> locations;
    private ListView locationsView;
    private FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_locations);

        this.locations = new ArrayList<>();
        this.locationsView = (ListView) this.findViewById(R.id.locationsList);
        this.addButton = (FloatingActionButton) this.findViewById(R.id.addLocation);

        this.locations.add(new SavedLocation("Pesaro",
                                             new Location("gps"),
                                             RingtoneManager.getRingtone(this, RingtoneManager.getValidRingtoneUri(this))));
        this.locations.add(new SavedLocation("Cesena",
                                             new Location("gps"),
                                             RingtoneManager.getRingtone(this, RingtoneManager.getValidRingtoneUri(this))));
        this.locations.add(new SavedLocation("Milano",
                                             new Location("gps"),
                                             RingtoneManager.getRingtone(this, RingtoneManager.getValidRingtoneUri(this))));
        this.locations.add(new SavedLocation("Roma",
                                             new Location("gps"),
                                             RingtoneManager.getRingtone(this, RingtoneManager.getValidRingtoneUri(this))));
        this.locations.add(new SavedLocation("Bari",
                                             new Location("gps"),
                                             RingtoneManager.getRingtone(this, RingtoneManager.getValidRingtoneUri(this))));
        this.locations.add(new SavedLocation("Torino",
                                             new Location("gps"),
                                             RingtoneManager.getRingtone(this, RingtoneManager.getValidRingtoneUri(this))));
        this.locations.add(new SavedLocation("Caserta",
                                             new Location("gps"),
                                             RingtoneManager.getRingtone(this, RingtoneManager.getValidRingtoneUri(this))));
        this.locations.add(new SavedLocation("Barrea",
                                             new Location("gps"),
                                             RingtoneManager.getRingtone(this, RingtoneManager.getValidRingtoneUri(this))));
        this.locations.add(new SavedLocation("San Giustino",
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
                Intent addLocationIntent = new Intent(LocationsActivity.this, AddLocationActivity.class);
                startActivity(addLocationIntent);
            }
        });

    }
}
