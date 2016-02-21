package ichack16.getridofyoshit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import ichack16.getridofyoshit.api.QueryServer;

public class TakeMapView extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_map_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        LatLng currentLocation = getCurrentLocation().toLatLng();

        new ReadFromServer().execute(map);

        map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
    }

    private Location getCurrentLocation() {
        return new Location(52, 0);
    }

    public void goToDetailScreen(FreeStuff item) {
        Intent intent = new Intent(this, DetailScreen.class);
        intent.putExtra("Item", item);
        startActivity(intent);
    }
}

class ReadFromServer extends AsyncTask<GoogleMap, Void, List<FreeStuff>> {

    private GoogleMap map;

    @Override
    protected List<FreeStuff> doInBackground(GoogleMap... params) {
        QueryServer queryServer = new QueryServer("http://ec2-52-30-60-12.eu-west-1.compute.amazonaws.com");
        List<FreeStuff> freeStuff = queryServer.freeStuffNearTo(new Location(51, 0));
        map = params[0];

        return freeStuff;
    }

    public void onPostExecute(List<FreeStuff> freeStuff) {
        for (FreeStuff freeItem : freeStuff) {
            assert(freeItem != null);
            LatLng position = freeItem.getLocation().toLatLng();
            String description = freeItem.getDescription();
            assert(position != null);
            map.addMarker(new MarkerOptions().position(position).title(description));
        }
    }
}
