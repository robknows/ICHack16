package ichack16.getridofyoshit;

import android.graphics.Bitmap;
import android.util.Base64;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

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
    this.image = resizeImage(image);
    this.name = name;
    this.description = description;
    this.telephoneNumber = telephoneNumber;
    this.location = location;
  }

    private Bitmap resizeImage(Bitmap image) {
        int maxSide = Math.max(image.getWidth(), image.getHeight());

        double scale = 200 / (double)maxSide;

        int newWidth = (int)((double)image.getWidth() * scale);
        int newHeight = (int)((double)image.getHeight() * scale);

        return Bitmap.createScaledBitmap(image, newWidth, newHeight, true);
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
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    image.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);

    String encodedImage = Base64.encodeToString(
        byteArrayOutputStream.toByteArray(), Base64.NO_WRAP);

    sb.append("{\n");
    sb.append("\"image\": \"").append(StringEscapeUtils.escapeJson(encodedImage)).append("\",\n");
    sb.append("\"description\": \"").append(StringEscapeUtils.escapeJson(description)).append("\",\n");
    sb.append("\"location\": ").append(location).append(",\n");
    sb.append("\"name\": \"").append(name).append("\",\n");
    sb.append("\"telephoneNumber\": \"").append(telephoneNumber).append("\"\n}");

    return sb.toString();
  }
}
