package ir.ahmadandroid.mapproject.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

import ir.ahmadandroid.mapproject.utils.MyActivity;
import ir.ahmadandroid.mapproject.R;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.match;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAnchor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;


@SuppressWarnings({"MissingPermission"})
public class MapActivity extends MyActivity implements OnMapReadyCallback, PermissionsListener, MapboxMap.OnMapClickListener {

    private static final String GEOJSON_SOURCE_ID = "source_id";
    private MapView mpView;
    private MapboxMap mapboxMap;
    private static final int PERM_CODE = 121;
    private static final LatLng MY_LAT_LONG = new LatLng(35.179876, 48.380143);
    private static final LatLng LOCATION_1 = new LatLng(35.279876, 48.380143);
    private static final LatLng LOCATION_2 = new LatLng(35.379876, 48.380143);
    private static final LatLng LOCATION_3 = new LatLng(35.479876, 48.380143);
    private static final LatLng LOCATION_4 = new LatLng(35.579876, 48.380143);
    private static final LatLng LOCATION_5 = new LatLng(35.679876, 48.380143);
    private GeoJsonSource geoJsonSource;
    private PermissionsManager permissionsManager;
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String RED_ICON_ID = "RED_ICON_ID";
    private static final String YELLOW_ICON_ID = "YELLOW_ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";
    private static final String ICON_PROPERTY = "ICON_PROPERTY";
    private static final String ICON_ID = "ICON_ID";
    private static final int TIME_INTERVAL=5000;
    private static final int MAX_WAITE_TIME=TIME_INTERVAL*5;
    private LocationEngine locationEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getResources().getString(R.string.mapbox_access_token));

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_main);

        mpView = findViewById(R.id.map_view_main);
        mpView.onCreate(savedInstanceState);
        mpView.getMapAsync(this);

    }


    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        // one way
//        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
//            @Override
//            public void onStyleLoaded(@NonNull Style style) {
//                enableLocationComponent(style);
//            }
//        });

        // tow way
//        funcTow(mapboxMap);


        //three way
        showFavLocationOnMap(mapboxMap);
    }

    private void showFavLocationOnMap(@NonNull MapboxMap mapboxMap) {
        List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                Point.fromLngLat(48.380143, 34.181876)));
        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                Point.fromLngLat(48.380143, 34.182876)));
        symbolLayerIconFeatureList.add(Feature.fromGeometry(
                Point.fromLngLat(48.380143, 34.180876)));

        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")

                // Add the SymbolLayer icon image to the map style
                .withImage(ICON_ID, BitmapFactory.decodeResource(
                        MapActivity.this.getResources(), R.drawable.mapbox_marker_icon_default))

                // Adding a GeoJson source for the SymbolLayer icons.
                .withSource(new GeoJsonSource(SOURCE_ID,
                        FeatureCollection.fromFeatures(symbolLayerIconFeatureList)))

                // Adding the actual SymbolLayer to the map style. An offset is added that the bottom of the red
                // marker icon gets fixed to the coordinate, rather than the middle of the icon being fixed to
                // the coordinate point. This is offset is not always needed and is dependent on the image
                // that you use for the SymbolLayer icon.
                .withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                        .withProperties(
                                iconImage(ICON_ID),
                                iconAllowOverlap(true),
                                iconIgnorePlacement(true)
                        )
                ), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                // Map is set up and the style has loaded. Now you can add additional data or make other map adjustments.
                enableLocationComponent(style);
            }
        });
    }

    private void funcTow(@NonNull MapboxMap mapboxMap) {
        //        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cj44mfrt20f082snokim4ungi")
        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjerxnqt3cgvp2rmyuxbeqme7")

                // Add the SymbolLayer icon image to the map style
                .withImage(RED_ICON_ID, BitmapFactory.decodeResource(
                        getResources(), R.drawable.locaion))

                .withImage(YELLOW_ICON_ID, BitmapFactory.decodeResource(
                        getResources(), R.drawable.locaion))

                // Adding a GeoJson source for the SymbolLayer icons.
                .withSource(new GeoJsonSource(SOURCE_ID,
                        FeatureCollection.fromFeatures(initCoordinateData())))

                // Adding the actual SymbolLayer to the map style. The match expression will check the
                // ICON_PROPERTY property key and then use the partner value for the actual icon id.
                .withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                        .withProperties(iconImage(match(
                                get(ICON_PROPERTY), literal(RED_ICON_ID),
                                stop(YELLOW_ICON_ID, YELLOW_ICON_ID),
                                stop(RED_ICON_ID, RED_ICON_ID))),
                                iconAllowOverlap(true),
                                iconAnchor(Property.ICON_ANCHOR_BOTTOM))
                ), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                // Map is set up and the style has loaded. Now you can add additional data or make other map adjustments.

//                MapActivity.this.mapboxMap = mapboxMap;
//
//                mapboxMap.addOnMapClickListener(MapActivity.this);
//
//                Toast.makeText(MapActivity.this, R.string.tap_on_marker_instruction,
//                        Toast.LENGTH_SHORT).show();
                enableLocationComponent(style);
            }
        });
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
//            favLocation(loadedMapStyle);

            currentLocation(loadedMapStyle);

//            mapboxMap.addOnMapClickListener(MapActivity.this);
//
//            Toast.makeText(MapActivity.this, R.string.tap_on_marker_instruction,
//                    Toast.LENGTH_SHORT).show();

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    /**
     * This method handles click events for SymbolLayer symbols.
     *
     * @param screenPoint the point on screen clicked
     */
    private boolean handleClickIcon(PointF screenPoint) {
        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint, LAYER_ID);
        if (!features.isEmpty()) {
// Show the Feature in the TextView to show that the icon is based on the ICON_PROPERTY key/value
            TextView featureInfoTextView = findViewById(R.id.feature_info);
            featureInfoTextView.setText(features.get(0).toJson());
            return true;
        } else {
            return false;
        }
    }

    private List<Feature> initCoordinateData() {
        Feature singleFeatureOne = Feature.fromGeometry(
                Point.fromLngLat(LOCATION_1.getLongitude(),LOCATION_1.getLatitude()));
        singleFeatureOne.addStringProperty(ICON_PROPERTY, RED_ICON_ID);

        Feature singleFeatureTwo = Feature.fromGeometry(
                Point.fromLngLat(LOCATION_2.getLongitude(),LOCATION_2.getLatitude()));

        singleFeatureTwo.addStringProperty(ICON_PROPERTY, YELLOW_ICON_ID);

        Feature singleFeatureThree =Feature.fromGeometry(
                Point.fromLngLat(LOCATION_3.getLongitude(),LOCATION_3.getLatitude()));

        singleFeatureThree.addStringProperty(ICON_PROPERTY, RED_ICON_ID);

        // Not adding a ICON_PROPERTY property to fourth and fifth features in order to show off the default
        // nature of the match expression used in the example up above
        Feature singleFeatureFour = Feature.fromGeometry(
                Point.fromLngLat(LOCATION_4.getLongitude(),LOCATION_4.getLatitude()));

        Feature singleFeatureFive = Feature.fromGeometry(
                Point.fromLngLat(LOCATION_5.getLongitude(),LOCATION_5.getLatitude()));

        List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
        symbolLayerIconFeatureList.add(singleFeatureOne);
        symbolLayerIconFeatureList.add(singleFeatureTwo);
        symbolLayerIconFeatureList.add(singleFeatureThree);
        symbolLayerIconFeatureList.add(singleFeatureFour);
        symbolLayerIconFeatureList.add(singleFeatureFive);
        return symbolLayerIconFeatureList;
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
         return handleClickIcon(mapboxMap.getProjection().toScreenLocation(point));
    }

    private void currentLocation(@NonNull Style loadedMapStyle) {
        //add current location

        // Get an instance of the component
        LocationComponent locationComponent = mapboxMap.getLocationComponent();

        // Activate with options
        locationComponent.activateLocationComponent(
                LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

        // Enable to make component visible
        locationComponent.setLocationComponentEnabled(true);

        // Set the component's camera mode
        locationComponent.setCameraMode(CameraMode.TRACKING);

        // Set the component's render mode
        locationComponent.setRenderMode(RenderMode.COMPASS);

    }

    private void favLocation(@NonNull Style loadedMapStyle) {
        //add one location both icon and point contains of latitude and longitude
        geoJsonSource = new GeoJsonSource("source_id",
                Feature.fromGeometry(Point.fromLngLat(MY_LAT_LONG.getLongitude(), MY_LAT_LONG.getLatitude())));

        // Map is set up and the style has loaded. Now you can add data or make other map adjustments
        loadedMapStyle.addImage("marker_icon", BitmapFactory.decodeResource(getResources(), R.drawable.locaion));
        loadedMapStyle.addSource(geoJsonSource);
        loadedMapStyle.addLayer(new SymbolLayer("layer_id", "source_id")
                .withProperties(
                        iconImage("marker_icon"),
                        iconAllowOverlap(true),
                        PropertyFactory.iconIgnorePlacement(true)
                ));
    }

    private void getLocationOne(LatLng location, @Nullable Style style) {
        GeoJsonSource geoJsonSource = new GeoJsonSource("source_id",
                Feature.fromGeometry(Point.fromLngLat(location.getLongitude(), location.getLatitude())));
        style.addImage("marker_icon", BitmapFactory.decodeResource(getResources(), R.drawable.locaion));
        style.addSource(geoJsonSource);
        style.addLayer(new SymbolLayer("layer_id", "source_id")
                .withProperties(
                        iconImage("marker_icon"),
                        PropertyFactory.iconIgnorePlacement(true),
                        iconAllowOverlap(true)
                )
        );
    }

    //permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //permission
    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    //permission
    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mpView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mpView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mpView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mpView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mpView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mpView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mpView.onDestroy();
    }


}