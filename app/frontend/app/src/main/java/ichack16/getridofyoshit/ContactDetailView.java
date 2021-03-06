package ichack16.getridofyoshit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

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

import ichack16.getridofyoshit.api.QueryServer;

public class ContactDetailView extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private LatLng finalLocation;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initialiseGoogleApiClient();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_my_location);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
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

        finalLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        final Marker marker = map.addMarker(new MarkerOptions().position(finalLocation).title("Your location"));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(finalLocation, 15));

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                marker.setPosition(latLng);
                finalLocation = latLng;
            }
        });
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

    public void onButtonGivePressed(View view) {
        String description = getIntent().getStringExtra("description");
        Uri imageUri = getIntent().getParcelableExtra("image");
        Bitmap image = BitmapFactory.decodeFile(DescribeItem.removePrefixFromFilename(imageUri.toString()));
        String contact = ((EditText) findViewById(R.id.text_telephone)).getText().toString();
        FreeStuff freeStuff = new FreeStuff(image, "", description, contact, new Location(finalLocation.latitude, finalLocation.longitude));

        new AddToServer().execute(freeStuff);

        Intent intent = new Intent(this, GiveOrTake.class);
        startActivity(intent);
    }
}

class AddToServer extends AsyncTask<FreeStuff, Void, String> {
    @Override
    protected String doInBackground(FreeStuff... freeStuff) {
        QueryServer qs = new QueryServer("http://ec2-52-30-60-12.eu-west-1.compute.amazonaws.com");
        return qs.addStuff(freeStuff[0]);
    }
}