package ichack16.getridofyoshit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class DetailScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);

        String itemjson = this.getIntent().getStringExtra("Item");

        try {
            JSONObject data = new JSONObject(itemjson);

            String description = data.getString("description");
            String name = data.getString("name");
            String telephoneNumber = data.getString("telephoneNumber");

            JSONObject jsonLocation = data.getJSONObject("location");

            Location thisLocation = new Location(jsonLocation.getDouble("lat"), jsonLocation.getDouble("lon"));

            byte[] decodedImage = Base64.decode(data.getString("image"), 0);

            Bitmap image = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);

            FreeStuff item = new FreeStuff(image, name, description, telephoneNumber, thisLocation);

            ImageView imageview = (ImageView) findViewById(R.id.image);
            imageview.setImageBitmap(item.getImage());
            imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);

            TextView descriptionview = (TextView) findViewById(R.id.description);
            descriptionview.setText(item.getDescription());

            TextView contactDetails = (TextView) findViewById(R.id.contactDetails);
            contactDetails.setText(item.getTelephoneNumber());
        } catch (JSONException e) {
            finish();
        }
    }

    public void onNoButtonPressed(View view) {
        finish();
    }
}
