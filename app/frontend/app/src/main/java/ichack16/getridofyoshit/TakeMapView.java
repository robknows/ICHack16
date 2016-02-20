package ichack16.getridofyoshit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressWarnings("deprecation")
public class TakeMapView extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    protected GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderApi fusedLocationApi;
    private LocationRequest locationRequest;

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            fusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
            getLastLocation();
        } catch (SecurityException e) {
            System.out.println("Permission denied!");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("failed");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_map_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getLocation();
    }

    private void getLocation() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000);
        fusedLocationApi = LocationServices.FusedLocationApi;
        buildGoogleApiClient();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
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
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        // Add a marker in Sydney and move the camera
        try {
            googleMap.setMyLocationEnabled(true);
            googleMap.getMyLocation();
        } catch (SecurityException ignored) {
        }
        googleMap.addMarker(new MarkerOptions().position(getLastLocation().toLatLng()).title("Me"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(getLastLocation().toLatLng()));
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                addApi(LocationServices.API).build();
    }

    private Location getLastLocation() {
        try {
            android.location.Location lastApiLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (lastApiLocation == null) {
                System.out.println("no location available");
                throw new RuntimeException();
            }
            return Location.fromGoogleLocation(lastApiLocation);
        } catch (SecurityException e) {
            System.out.println("Permission denied!");
            throw e;
        }
    }

    @Override
    public void onLocationChanged(android.location.Location location) {

    }
}
