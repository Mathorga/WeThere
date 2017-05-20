package keym.dev.rwethereyet.addlocation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

import keym.dev.rwethereyet.R;
import keym.dev.rwethereyet.keym.dev.rwethereyet.utils.LocationItem;

/**
 * Created by luka on 11/05/17.
 */

public class AddLocationActivity extends AppCompatActivity {

    private static final String TAG = "AddLocationActivity";
    private static final int ZOOM = 16;

    private LocationItem locationItem;

    private ScrollView scroll;
    private EditText label;
    private TextView radius;
    private SeekBar radiusBar;
    private EditText searchPosition;
    private ScrollableMapView mapView;
    private FloatingActionButton done;

    private GoogleMap map;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_add_location);

        this.scroll = (ScrollView) this.findViewById(R.id.addLocationScroll);

        this.label = (EditText) this.findViewById(R.id.newLocationLabel);
        this.label.clearFocus();

        this.radius = (TextView) this.findViewById(R.id.newLocationRadius);

        this.radiusBar = (SeekBar) this.findViewById(R.id.newLocationRadiusBar);
        this.radiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                radius.setText(String.valueOf(i) + " km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        this.searchPosition = (EditText) findViewById(R.id.searchPosition);
        this.searchPosition.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_GO ||
                    event.getAction() == KeyEvent.ACTION_DOWN &&
                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    // hide virtual keyboard
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(searchPosition.getWindowToken(), 0);

                    new ResearchLocation(searchPosition.getText().toString(),
                                         AddLocationActivity.this).execute();
                    return true;
                }
                return false;
            }
        });

        this.mapView = (ScrollableMapView) this.findViewById(R.id.newLocationMap);
        this.mapView.onCreate(savedInstanceState);
        if (this.mapView != null) {
            this.mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    if (ActivityCompat.checkSelfPermission(AddLocationActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(AddLocationActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        map.setMyLocationEnabled(true);
                        map.getUiSettings().setMyLocationButtonEnabled(true);
                    }
                    map.getUiSettings().setZoomControlsEnabled(true);
                    LatLng position = new LatLng(43.90921, 12.91640);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, ZOOM);
                    map.animateCamera(cameraUpdate);
                }
            });
        }

        this.done = (FloatingActionButton) this.findViewById(R.id.newLocationDone);
        this.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Return locationItem to the LocationsActivity.
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", locationItem);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        this.locationItem = new LocationItem(this.label.getText().toString(),
                this.radiusBar.getProgress(),
                new LatLng(0.0, 0.0),
                RingtoneManager.getRingtone(this, RingtoneManager.getValidRingtoneUri(this)));
    }

    @Override
    protected void onResume() {
        this.mapView.onResume();
        super.onResume();
    }

    public GoogleMap getMap() {
        return this.map;
    }

    public LocationItem getLocationItem() {
        return this.locationItem;
    }

    public void moveMapTo(final LatLng position) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, ZOOM);
        map.animateCamera(cameraUpdate);
    }
}