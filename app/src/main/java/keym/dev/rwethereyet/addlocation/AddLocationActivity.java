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
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import keym.dev.rwethereyet.BaseActivity;
import keym.dev.rwethereyet.R;
import keym.dev.rwethereyet.util.LocationItem;
import keym.dev.rwethereyet.util.LocationParser;
import keym.dev.rwethereyet.util.ParcelableUtil;

/**
 * Created by luka on 11/05/17.
 * The class implements the activity used to add or modify a LocationItem.
 */

public class AddLocationActivity extends BaseActivity {

    private static final String TAG = "AddLocationActivity";
    private static final int ZOOM = 16;
    private static final int TONE_PICKER = 1;

    private LocationItem locationItem;

    private LatLng coords;

    private ScrollView scroll;
    private EditText label;
    private TextView radius;
    private SeekBar radiusBar;
    private LinearLayout ringtoneItem;
    private TextView ringtoneName;
    private EditText searchPosition;
    private ScrollableMapView mapView;
    private FloatingActionButton done;

    private GoogleMap map;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_add_location);

        this.coords = new LatLng(43.90921, 12.91640);

        this.scroll = (ScrollView) this.findViewById(R.id.addLocationScroll);
        this.label = (EditText) this.findViewById(R.id.newLocationLabel);
        this.radius = (TextView) this.findViewById(R.id.newLocationRadius);
        this.ringtoneName = (TextView) this.findViewById(R.id.newLocationRingtone);
        this.ringtoneItem = (LinearLayout) this.findViewById(R.id.newLocationRingtoneSelector);
        this.searchPosition = (EditText) findViewById(R.id.searchPosition);
        this.mapView = (ScrollableMapView) this.findViewById(R.id.newLocationMap);
        this.done = (FloatingActionButton) this.findViewById(R.id.newLocationDone);


        this.label.clearFocus();
        this.label.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                if (i == EditorInfo.IME_ACTION_SEARCH ||
                    i == EditorInfo.IME_ACTION_DONE ||
                    i == EditorInfo.IME_ACTION_GO) {

                    // Hide virtual keyboard.
                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(searchPosition.getWindowToken(), 0);

                    label.clearFocus();
                    return true;
                }
                return false;
            }
        });
        this.label.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    // If focus is lost.
                    locationItem.setLabel(label.getText().toString());
                }
            }
        });


        this.radiusBar = (SeekBar) this.findViewById(R.id.newLocationRadiusBar);
        this.radiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                radius.setText(String.valueOf(i) + " km");
                locationItem.setRadius(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Hide virtual keyboard.
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(searchPosition.getWindowToken(), 0);
                // Clear focus on the label field.
                label.clearFocus();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




        final String ringtonePreferenceKey = this.getResources().getString(R.string.preference_ringtone_key);
        final Uri defaultRingtoneUri = Uri.parse(PreferenceManager.getDefaultSharedPreferences(this).getString(ringtonePreferenceKey, "DEF"));

        Uri ringtoneUri;
        if (!defaultRingtoneUri.equals(Uri.parse("DEF"))) {
            Log.wtf(TAG, "Default ringtone URI: " + defaultRingtoneUri);
            ringtoneUri = defaultRingtoneUri;
        } else {
            ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        }
        Ringtone defaultRingtone = RingtoneManager.getRingtone(this, ringtoneUri);
        this.ringtoneName.setText(defaultRingtone.getTitle(this));

        this.ringtoneItem.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Uri ringtoneUri;
                if (!defaultRingtoneUri.equals((Uri.parse("DEF")))) {
                    ringtoneUri = defaultRingtoneUri;
                } else {
                    ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                }

                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, ringtoneUri);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                startActivityForResult(intent, TONE_PICKER);
//                startActivity(intent);
            }
        });




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


        final byte[] bytes = getIntent().getByteArrayExtra("location");
        if (bytes != null) {
            this.locationItem = ParcelableUtil.unmarshall(bytes, LocationItem.CREATOR);
            this.label.setText(this.locationItem.getLabel());
            this.radiusBar.setProgress(this.locationItem.getRadius());
            this.ringtoneName.setText(new RingtoneManager(this).getRingtone(this, this.locationItem.getTone()).getTitle(this));
            this.coords = this.locationItem.getLocation();
            Log.wtf(TAG, this.locationItem.toString());
        } else {
            this.locationItem = new LocationItem(LocationItem.ID_UNDEFINED,
                    this.label.getText().toString(),
                    this.radiusBar.getProgress(),
                    new LatLng(0.0, 0.0),
                    null,
                    false);
        }

        this.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, locationItem.toString());
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", ParcelableUtil.marshall(locationItem));
                if (bytes != null) {
                    returnIntent.putExtra("removeId", (int) ParcelableUtil.unmarshall(bytes, LocationItem.CREATOR).getId());
                }
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

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
                    map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            locationItem.setLocation(latLng);
                            map.clear();
                            map.addMarker(new MarkerOptions().position(latLng)
                                                             .draggable(false)
                                                             .visible(true));
                        }
                    });
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(coords, ZOOM);
                    map.clear();
                    map.addMarker(new MarkerOptions().position(coords)
                                                     .draggable(false)
                                                     .visible(true));
                    map.animateCamera(cameraUpdate);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        this.mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        this.mapView.onDestroy();
        super.onDestroy();
    }

    /**
     * Getter.
     * @return
     *  the GoogleMap used to display the location.
     */
    public GoogleMap getMap() {
        return this.map;
    }

    /**
     * Getter.
     * @return
     *  the LocationItem that's being created.
     */
    public LocationItem getLocationItem() {
        return this.locationItem;
    }

    /**
     * Update the map to the given position.
     * @param position
     *  The destination position.
     */
    public void moveMapTo(final LatLng position) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, ZOOM);
        this.map.animateCamera(cameraUpdate);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == TONE_PICKER && resultCode == RESULT_OK) {
            // Update the LocationItem's ringtone.
            Uri selectedUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            this.locationItem.setTone(selectedUri);
            this.ringtoneName.setText(RingtoneManager.getRingtone(this, selectedUri).getTitle(this));
        }
    }
}