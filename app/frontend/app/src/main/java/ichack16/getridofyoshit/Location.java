package ichack16.getridofyoshit;

import com.google.android.gms.maps.model.LatLng;

public class Location {
    private final double latitude;
    private final double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public static Location fromGoogleLocation(android.location.Location location) {
        return new Location(location.getLatitude(), location.getLongitude());
    }

    public LatLng toLatLng() {
        return new LatLng(latitude, longitude);
    }

    @Override
    public String toString() {
        return "{ \"lon\": " + longitude + ", \"lat\": " + latitude + "}";
    }
}
