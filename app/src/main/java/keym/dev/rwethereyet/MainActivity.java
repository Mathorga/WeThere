package keym.dev.rwethereyet;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import keym.dev.rwethereyet.background.NotificationService;
import keym.dev.rwethereyet.keym.dev.rwethereyet.util.FragmentTabAdapter;
import keym.dev.rwethereyet.keym.dev.rwethereyet.util.LocationItem;
import keym.dev.rwethereyet.settings.SettingsActivity;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private static final Long LOCATION_INTERVAL = 1000L;
    private static final Long LOCATION_FASTEST_INTERVAL = 5000L;

    private ViewPager pager;
    private FragmentTabAdapter adapter;
    private TabLayout tabs;
    private ImageButton menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        Intent starter = this.getIntent();
        LocationItem location = starter.getParcelableExtra("location");
        // Stop alarm.
        Uri alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(this.getApplicationContext(), alarm);
        ringtone.stop();

        // Ask for locations permissions.
        ActivityCompat.requestPermissions(this,
                                          new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                          1);
        ActivityCompat.requestPermissions(this,
                                          new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                          1);

        this.pager = (ViewPager) this.findViewById(R.id.mainPager);
        this.adapter = new FragmentTabAdapter(this.getSupportFragmentManager());
        this.pager.setAdapter(adapter);

        this.tabs = (TabLayout) this.findViewById(R.id.mainTabs);
        this.tabs.setupWithViewPager(this.pager);
        for (int i = 0; i < this.tabs.getTabCount(); i++) {
            this.tabs.getTabAt(i).setIcon(this.adapter.getTabs().get(i).getIconResId());
            this.tabs.getTabAt(i).setText(this.adapter.getTabs().get(i).getTitleResId());
        }

        this.menu = (ImageButton) this.findViewById(R.id.mainMenu);
        this.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this, menu);
                popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            // Settings.
                            case R.id.mainSettings:
                                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                                startActivity(intent);
                                return true;
                            // TODO Credits.
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(LOCATION_INTERVAL);
        locationRequest.setFastestInterval(LOCATION_FASTEST_INTERVAL);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
//                        try {
//                            // Show the dialog by calling startResolutionForResult(),
//                            // and check the result in onActivityResult().
//                            ResolvableApiException resolvable = (ResolvableApiException) e;
//                            resolvable.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
//                        } catch (IntentSender.SendIntentException sendEx) {
//                            // Ignore the error.
//                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });

        FusedLocationProviderClient fusedClient = new FusedLocationProviderClient(this);
    }
}