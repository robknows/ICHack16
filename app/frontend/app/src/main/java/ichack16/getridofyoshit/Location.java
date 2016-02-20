package ichack16.getridofyoshit;

/**
 * Created by fangyi on 20/02/2016.
 */
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

  @Override
  public String toString() {
    return "{ \"lon\": " + longitude + ", \"lat\": " + latitude + "}";
  }
}
