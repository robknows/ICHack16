package ichack16.getridofyoshit;

import android.media.Image;

/**
 * Created by fangyi on 20/02/2016.
 */
public class FreeStuff {
    private final Image image;
    private final String name;
    private final String description;
    private final String telephoneNumber;
    private final Location location;

    public FreeStuff(Image image, String name, String description, String telephoneNumber, Location location) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.telephoneNumber = telephoneNumber;
        this.location = location;
    }
}
