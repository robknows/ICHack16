package ichack16.getridofyoshit.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;

import ichack16.getridofyoshit.FreeStuff;
import ichack16.getridofyoshit.Location;

public class QueryServer {

  private final String serverURL;
  private static final String ADD_API = "/add/";
  private static final String SEARCH_API = "/search/";

  public QueryServer(String serverURL) {
    this.serverURL = serverURL;
  }

  public String addStuff(FreeStuff newStuff) {
    try {
      URL addURL = new URL(serverURL + ADD_API);
      HttpURLConnection connection = (HttpURLConnection) addURL.openConnection();

      setupConnection(connection);

      OutputStream out = connection.getOutputStream();
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

      writer.write(newStuff.toString());
      writer.flush();
      writer.close();

      connection.connect();

      return connection.getResponseMessage();
    } catch (Exception e) {
      return "FAIL";
    }
  }

  private void setupConnection(HttpURLConnection conn) throws ProtocolException {
    conn.setRequestMethod("POST");
    conn.setDoInput(true);
    conn.setDoOutput(true);
    conn.setReadTimeout(10000);
    conn.setConnectTimeout(15000);
  }

  private String queryClose(Location location) {
    try {
      URL addURL = new URL(serverURL + SEARCH_API);
      HttpURLConnection connection = (HttpURLConnection) addURL.openConnection();

      setupConnection(connection);

      OutputStream out = connection.getOutputStream();
      BufferedWriter writer = new BufferedWriter(
          new OutputStreamWriter(out, "UTF-8"));

      writer.write("{ \"location\": " + location + "}");
      writer.flush();
      writer.close();

      connection.connect();

      InputStream in = connection.getInputStream();
      BufferedReader read = new BufferedReader(new InputStreamReader(in, "UTF-8"));

      return buildResponse(read);
    } catch (Exception e) {
      e.printStackTrace();
      return ""; 
    }
  }

  @NonNull
  private String buildResponse(BufferedReader read) throws IOException {
    StringBuilder response = new StringBuilder();
    String line;
    while ( (line = read.readLine()) != null) {
      response.append(line);
    }
    return response.toString();
  }

  public List<FreeStuff> freeStuffNearTo(Location location) {
    List<FreeStuff> found = new ArrayList<FreeStuff>();

    try {
      JSONObject jsonObject = new JSONObject(queryClose(location));
      String KEY_FOR_RESULTS = "hits";
      JSONObject hits = jsonObject.getJSONObject(KEY_FOR_RESULTS);
      JSONArray actualHits = hits.getJSONArray(KEY_FOR_RESULTS);

      for (int i = 0; i < actualHits.length(); i++) {
        JSONObject obj = actualHits.getJSONObject(i);
        JSONObject data = obj.getJSONObject("_source");

        String description = data.getString("description");
        String name = data.getString("name");
        String telephoneNumber = data.getString("telephoneNumber");

        JSONObject jsonLocation = data.getJSONObject("location");

        Location thisLocation = new Location(jsonLocation.getDouble("lat"), jsonLocation.getDouble("lon"));

        byte[] decodedImage = Base64.decode(data.getString("image"), 0);

        Bitmap image = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);

        found.add(new FreeStuff(image, name, description, telephoneNumber, thisLocation));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return found;
  }
}
