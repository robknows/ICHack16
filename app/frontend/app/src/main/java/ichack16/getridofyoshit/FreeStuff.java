package ichack16.getridofyoshit;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.util.Base64;

/**
 * Created by fangyi on 20/02/2016.
 */
public class FreeStuff {
  private final Bitmap image;
  private final String name;
  private final String description;
  private final String telephoneNumber;
  private final Location location;

  public FreeStuff(Bitmap image, String name, String description, String telephoneNumber, Location location) {
    this.image = image;
    this.name = name;
    this.description = description;
    this.telephoneNumber = telephoneNumber;
    this.location = location;
  }

  /**
   * @return the image
   */
  public Bitmap getImage() {
    return image;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @return the telephoneNumber
   */
  public String getTelephoneNumber() {
    return telephoneNumber;
  }

  /**
   * @return the location
   */
  public Location getLocation() {
    return location;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);

    String encodedImage = Base64.encodeToString(
        baos.toByteArray(), Base64.DEFAULT);

    sb.append("{\n");
    sb.append("'image': '").append(encodedImage).append("',\n");
    sb.append("'description': '").append(description).append("',\n");
    sb.append("'location': ").append(location).append(",\n");
    sb.append("'name': '").append(name).append("'\n");
    sb.append("'telephoneNumber': '").append(telephoneNumber).append("'\n}");

    return sb.toString();
  }
}
