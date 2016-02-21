package ichack16.getridofyoshit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ichack16.getridofyoshit.api.QueryServer;

public class TakeMapView extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleMap map;
    private Map<Marker, FreeStuff> markers = new HashMap<>();
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialiseGoogleApiClient();
        setContentView(R.layout.activity_take_map_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initialiseGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnInfoWindowClickListener(new InfoWindowClickListener());
    }

    public void goToDetailScreen(FreeStuff item) {
        Intent intent = new Intent(this, DetailScreen.class);
        intent.putExtra("Item", item.toString());
        startActivity(intent);
    }

    private void assertLocationPermissions() {
        boolean fineLocationPermissionDenied = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        boolean coarseLocationPermissionDenied = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;

        if (fineLocationPermissionDenied && coarseLocationPermissionDenied) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        assertLocationPermissions();
        @SuppressWarnings("ResourceType")
        android.location.Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation == null) {
            System.out.println("Error shite");
            finish();
        }

        LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        new ReadFromServer(markers, new Location(mLastLocation.getLatitude(), mLastLocation.getLongitude())).execute(map);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private class InfoWindowClickListener implements GoogleMap.OnInfoWindowClickListener {
        @Override
        public void onInfoWindowClick(Marker marker) {
            goToDetailScreen(markers.get(marker));
        }
    }
}

class ReadFromServer extends AsyncTask<GoogleMap, Void, List<FreeStuff>> {
    private GoogleMap map;
    private Map<Marker, FreeStuff> markers;
    private Location location;

    public ReadFromServer(Map<Marker, FreeStuff> markers, Location location) {
        this.markers = markers;
        this.location = location;
    }

    @Override
    protected List<FreeStuff> doInBackground(GoogleMap... params) {
        QueryServer queryServer = new QueryServer("http://ec2-52-30-60-12.eu-west-1.compute.amazonaws.com");
        List<FreeStuff> freeStuff = queryServer.freeStuffNearTo(location);
        map = params[0];
        return freeStuff;
    }

    public void onPostExecute(List<FreeStuff> freeStuff) {
        for (FreeStuff freeItem : freeStuff) {
            assert (freeItem != null);
            LatLng position = freeItem.getLocation().toLatLng();
            String description = freeItem.getDescription();
            assert (position != null);
            Marker marker = map.addMarker(new MarkerOptions().position(position).title(description));
            markers.put(marker, freeItem);
        }
    }
}
