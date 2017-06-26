package keym.dev.rwethereyet.addlocation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import keym.dev.rwethereyet.BaseActivity;
import keym.dev.rwethereyet.R;
import keym.dev.rwethereyet.keym.dev.rwethereyet.util.LocationItem;
import keym.dev.rwethereyet.keym.dev.rwethereyet.util.LocationParser;
import keym.dev.rwethereyet.settings.SettingsActivity;

/**
 * Created by luka on 11/05/17.
 */

public class AddLocationActivity extends BaseActivity {

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
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_add_location);

        this.scroll = (ScrollView) this.findViewById(R.id.addLocationScroll);

        this.label = (EditText) this.findViewById(R.id.newLocationLabel);
        this.label.clearFocus();
        this.label.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                if (i == EditorInfo.IME_ACTION_SEARCH ||
                    i == EditorInfo.IME_ACTION_DONE ||
                    i == EditorInfo.IME_ACTION_GO) {

                    // hide virtual keyboard
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(searchPosition.getWindowToken(), 0);

                    locationItem.setLabel(label.getText().toString());
                    Log.d(TAG, locationItem.getLabel() + "\n" + locationItem.getId());
                    Toast.makeText(AddLocationActivity.this, locationItem.getLabel(), Toast.LENGTH_SHORT).show();

                    return true;
                }
                return false;
            }
        });

        this.radius = (TextView) this.findViewById(R.id.newLocationRadius);

        this.radiusBar = (SeekBar) this.findViewById(R.id.newLocationRadiusBar);
        this.radiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                radius.setText(String.valueOf(i) + " km");
                locationItem.setRadius(i);
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

                    ResearchLocation research = new ResearchLocation(searchPosition.getText().toString(),
                                         AddLocationActivity.this);

                    research.execute();

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

        this.locationItem = new LocationItem(LocationItem.ID_UNDEFINED,
                                             this.label.getText().toString(),
                                             this.radiusBar.getProgress(),
                                             new LatLng(0.0, 0.0),
                                             null,
                                             false);

        this.done = (FloatingActionButton) this.findViewById(R.id.newLocationDone);
        this.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, locationItem.toString());
                Intent returnIntent = new Intent();

                returnIntent.putExtra("result", locationItem);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
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