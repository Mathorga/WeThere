package keym.dev.rwethereyet.distance;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import keym.dev.rwethereyet.R;
import keym.dev.rwethereyet.addlocation.ScrollableMapView;
import keym.dev.rwethereyet.util.LocationItem;
import keym.dev.rwethereyet.util.LocationParser;

/**
 * Created by luka on 17/11/17.
 * The Fragment shows a map representing every LocationItem and the current location.
 */

public class DistanceMapFragment extends Fragment {

    private static final int ZOOM = 16;

    private ScrollableMapView mapView;

    private GoogleMap map;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_distance_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mapView = (ScrollableMapView) view.findViewById(R.id.locationDistanceMap);
        this.mapView.onCreate(savedInstanceState);
        if (this.mapView != null) {
            this.mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    if (ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getContext(),
                                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        map.setMyLocationEnabled(true);
                        map.getUiSettings().setMyLocationButtonEnabled(true);
                    }
                    map.getUiSettings().setZoomControlsEnabled(true);

                    // Default position.
                    LatLng position = new LatLng(43.90921, 12.91640);

                    // Preferences markers.
                    List<LocationItem> tmpLocations = new LocationParser(getContext()).readAllItems();
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();

                    // Add a marker for every LocationItem.
                    for (LocationItem item : tmpLocations) {
                        if (item.isActive()) {
                            map.addMarker(new MarkerOptions().position(item.getLocation())
                                                             .title(item.getLabel())
                                                             .draggable(false)
                                                             .visible(true));
                            builder.include(item.getLocation());
                        }
                    }
                    builder.include(position);

                    LatLngBounds bounds;
                    try {
                        bounds = builder.build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 150);
                        map.animateCamera(cameraUpdate);
                    } catch (IllegalStateException e) {
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, ZOOM);
                        map.animateCamera(cameraUpdate);
                    }

                }
            });
        }
    }

    @Override
    public void onResume() {
        this.mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        this.mapView.onDestroy();
        super.onDestroy();
    }
}
