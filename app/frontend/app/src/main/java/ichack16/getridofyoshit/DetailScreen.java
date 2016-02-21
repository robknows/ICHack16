package ichack16.getridofyoshit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DetailScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);

        FreeStuff item = (FreeStuff) this.getIntent().getSerializableExtra("Item");

        ImageView image = (ImageView) findViewById(R.id.image);
        image.setImageBitmap(item.getImage());

        TextView description = (TextView) findViewById(R.id.description);
        description.setText(item.getDescription());

        TextView contactDetails = (TextView) findViewById(R.id.contactDetails);
        contactDetails.setText(item.getTelephoneNumber());
    }

    public void onNoButtonPressed(View view) {
        finish();
    }

    public void onTakeButtonPressed(View view) {
        Intent call = new Intent(Intent.ACTION_CALL);
        call.setData(Uri.parse("tel:" + ((TextView) findViewById(R.id.contactDetails)).getText()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(call);
        Intent start = new Intent(this, GiveOrTake.class);
        startActivity(start);
    }
}
