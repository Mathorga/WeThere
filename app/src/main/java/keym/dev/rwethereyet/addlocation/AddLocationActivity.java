package keym.dev.rwethereyet.addlocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import keym.dev.rwethereyet.R;

/**
 * Created by luka on 11/05/17.
 */

public class AddLocationActivity extends AppCompatActivity {

    private EditText label;
    private EditText radius;
    private SeekBar radiusBar;
    private MapView mapView;
    private FloatingActionButton done;

    private GoogleMap map;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_add_location);

        this.label = (EditText) this.findViewById(R.id.newLocationLabel);

        this.radius = (EditText) this.findViewById(R.id.newLocationRadius);
        this.radiusBar = (SeekBar) this.findViewById(R.id.newLocationRadiusBar);
        this.radius.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    radiusBar.setProgress(Integer.getInteger(((EditText) view).getText().toString()));
                }
            }
        });
        this.radiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                radius.setText(String.valueOf(i) + "km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        this.mapView = (MapView) this.findViewById(R.id.newLocationMap);
        this.mapView.onCreate(savedInstanceState);
        if (this.mapView != null) {
            this.mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    /*map.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_add_black_24dp))
                            .anchor(0.0f, 1.0f)
                            .position(new LatLng(55.854049, 13.661331)));
                    map.getUiSettings().setMyLocationButtonEnabled(true);
                    if (ActivityCompat.checkSelfPermission(AddLocationActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(AddLocationActivity.this,
                                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    map.setMyLocationEnabled(true);
                    map.getUiSettings().setZoomControlsEnabled(true);
                    MapsInitializer.initialize(AddLocationActivity.this);
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(new LatLng(55.854049, 13.661331));
                    LatLngBounds bounds = builder.build();
                    int padding = 0;
                    // Updates the location and zoom of the MapView
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    map.moveCamera(cameraUpdate);*/

                    LatLng position = new LatLng(37.77493, -122.41942);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 16);
                    map.animateCamera(cameraUpdate);
                }
            });

        }

        this.done = (FloatingActionButton) this.findViewById(R.id.newLocationDone);
    }

    @Override
    protected void onResume() {
        this.mapView.onResume();
        super.onResume();
    }
}