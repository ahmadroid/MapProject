package ir.ahmadandroid.mapproject.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ir.ahmadandroid.mapproject.R;
import ir.ahmadandroid.mapproject.ui.fragments.UserHomeFragment;
import ir.ahmadandroid.mapproject.utils.MyActivity;

@SuppressWarnings({"MissingPermission"})
public class UserActivity extends MyActivity implements View.OnClickListener {

    private Button btn;
    private UserHomeFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        btn=findViewById(R.id.btn_show_list);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(btn)) {
//            Intent listIntent = new Intent(UserActivity.this, PersonListActivity.class);
//            startActivity(listIntent);
            Toast.makeText(this, "show fragment", Toast.LENGTH_SHORT).show();
            fragment=new UserHomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_user,fragment).commit();
        }
    }
//
    //
    //
    ///
//
    //
    //
    //
    //
    //
    //
    //













//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_location_map_frag);
//
//        // Mapbox access token is configured here. This needs to be called either in your application
//        // object or in the same activity which contains the mapview.
//        Mapbox.getInstance(this, getString(R.string.access_token));
//
//        // Create supportMapFragment
//        SupportMapFragment mapFragment;
//        if (savedInstanceState == null) {
//
//            // Create fragment
//            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//            // Build a Mapbox map
//            MapboxMapOptions options = MapboxMapOptions.createFromAttributes(this, null);
//            options.camera(new CameraPosition.Builder()
//                    .target(new LatLng(38.899895, -77.03401))
//                    .zoom(9)
//                    .build());
//
//            // Create map fragment
//            mapFragment = SupportMapFragment.newInstance(options);
//
//            // Add map fragment to parent container
//            transaction.add(R.id.location_frag_container, mapFragment, "com.mapbox.map");
//            transaction.commit();
//        } else {
//            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentByTag("com.mapbox.map");
//        }
//
//        if (mapFragment != null) {
//            mapFragment.getMapAsync(new OnMapReadyCallback() {
//                @Override
//                public void onMapReady(@NonNull MapboxMap mapboxMap) {
//                    LocationComponentFragmentActivity.this.mapboxMap = mapboxMap;
//                    mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
//                        @Override
//                        public void onStyleLoaded(@NonNull Style style) {
//                            enableLocationComponent(style);
//                        }
//                    });
//                }
//            });
//        }
//    }
//
//    /**
//     * Set up the LocationEngine and the parameters for querying the device's location
//     */
//    @SuppressLint("MissingPermission")
//    private void initLocationEngine() {
//        locationEngine = LocationEngineProvider.getBestLocationEngine(this);
//
//        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
//                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
//                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();
//
//        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
//        locationEngine.getLastLocation(callback);
//    }
//
//
//    private static class LocationComponentFragmentActivityLocationCallback
//            implements LocationEngineCallback<LocationEngineResult> {
//
//        private final WeakReference<LocationComponentFragmentActivity> activityWeakReference;
//
//        LocationComponentFragmentActivityLocationCallback(LocationComponentFragmentActivity activity) {
//            this.activityWeakReference = new WeakReference<>(activity);
//        }
//
//        /**
//         * The LocationEngineCallback interface's method which fires when the device's location has changed.
//         *
//         * @param result the LocationEngineResult object which has the last known location within it.
//         */
//        @Override
//        public void onSuccess(LocationEngineResult result) {
//            LocationComponentFragmentActivity activity = activityWeakReference.get();
//
//            if (activity != null) {
//                Location location = result.getLastLocation();
//
//                if (location == null) {
//                    return;
//                }
//
//                // Pass the new location to the Maps SDK's LocationComponent
//                if (activity.mapboxMap != null && result.getLastLocation() != null) {
//                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
//                }
//            }
//        }
//
//        /**
//         * The LocationEngineCallback interface's method which fires when the device's location can't be captured
//         *
//         * @param exception the exception message
//         */
//        @Override
//        public void onFailure(@NonNull Exception exception) {
//            LocationComponentFragmentActivity activity = activityWeakReference.get();
//            if (activity != null) {
//                Toast.makeText(activity, exception.getLocalizedMessage(),
//                        Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//
//    @SuppressWarnings( {"MissingPermission"})
//    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
//        // Check if permissions are enabled and if not request
//        if (PermissionsManager.areLocationPermissionsGranted(this)) {
//
//            // Get an instance of the LocationComponent.
//            LocationComponent locationComponent = mapboxMap.getLocationComponent();
//
//            // Activate the LocationComponent
//            locationComponent.activateLocationComponent(
//                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
//                            .useDefaultLocationEngine(false)
//                            .build());
//
//            // Enable the LocationComponent so that it's actually visible on the map
//            locationComponent.setLocationComponentEnabled(true);
//
//            // Set the LocationComponent's camera mode
//            locationComponent.setCameraMode(CameraMode.TRACKING);
//
//            // Set the LocationComponent's render mode
//            locationComponent.setRenderMode(RenderMode.NORMAL);
//
//            initLocationEngine();
//        } else {
//            permissionsManager = new PermissionsManager(this);
//            permissionsManager.requestLocationPermissions(this);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    @Override
//    public void onExplanationNeeded(List<String> permissionsToExplain) {
//        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (locationEngine != null) {
//            locationEngine.removeLocationUpdates(callback);
//        }
//    }
//
//    @Override
//    public void onPermissionResult(boolean granted) {
//        if (granted) {
//            mapboxMap.getStyle(new Style.OnStyleLoaded() {
//                @Override
//                public void onStyleLoaded(@NonNull Style style) {
//                    enableLocationComponent(style);
//                }
//            });
//        } else {
//            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
//            finish();
//        }
//    }




}