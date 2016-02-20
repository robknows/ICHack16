package ichack16.getridofyoshit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

public class DescribeItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_describe_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView imageView = (ImageView) findViewById(R.id.item_image);
        Uri imageUri = getIntent().getParcelableExtra("image");
        scaleDown(imageView, imageUri);
    }

    private void scaleDown(ImageView iv, Uri uri) {
        Bitmap decodedFile = BitmapFactory.decodeFile(removePrefixFromFilename(uri.toString()));
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int desiredWidth = screenHeight / 4;
        int newHeight = (decodedFile.getHeight() * desiredWidth / decodedFile.getWidth());
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(decodedFile, desiredWidth, newHeight, true);
        iv.setImageBitmap(scaledBitmap);
    }

    private String removePrefixFromFilename(String fileName) {
        int NUMBER_OF_CHARACTERS_TO_REMOVE_MINUS_ONE = 6;
        return fileName.substring(NUMBER_OF_CHARACTERS_TO_REMOVE_MINUS_ONE);
    }
}
