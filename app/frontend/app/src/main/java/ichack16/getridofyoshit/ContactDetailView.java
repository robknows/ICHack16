package ichack16.getridofyoshit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import ichack16.getridofyoshit.api.QueryServer;

public class ContactDetailView extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onButtonGivePressed(View view) {
        String description = getIntent().getStringExtra("description");
        Uri imageUri = getIntent().getParcelableExtra("image");
        Bitmap image = BitmapFactory.decodeFile(DescribeItem.removePrefixFromFilename(imageUri.toString()));
        String contact = ((EditText) findViewById(R.id.text_telephone)).getText().toString();
        double latitude = 51;
        double longitude = 0;
        FreeStuff freeStuff = new FreeStuff(image, "", description, contact, new Location(latitude, longitude));

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