package ichack16.getridofyoshit.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
      HttpURLConnection conn = (HttpURLConnection) addURL.openConnection();

      conn.setRequestMethod("POST");
      conn.setDoInput(true);
      conn.setDoOutput(true);
      conn.setReadTimeout(10000);
      conn.setConnectTimeout(15000);

      OutputStream out = conn.getOutputStream();
      BufferedWriter writer = new BufferedWriter(
          new OutputStreamWriter(out, "UTF-8"));

      writer.write(newStuff.toString());
      writer.flush();
      writer.close();

      conn.connect();

      return conn.getResponseMessage();
    } catch (Exception e) {
      return "FAIL";
    }
  }

  private String queryClose(Location location) {
    try {
      URL addURL = new URL(serverURL + SEARCH_API);
      HttpURLConnection conn = (HttpURLConnection) addURL.openConnection();

      conn.setRequestMethod("POST");
      conn.setDoInput(true);
      conn.setDoOutput(true);
      conn.setReadTimeout(10000);
      conn.setConnectTimeout(15000);

      OutputStream out = conn.getOutputStream();
      BufferedWriter writer = new BufferedWriter(
          new OutputStreamWriter(out, "UTF-8"));

      writer.write("{ \"location\": " + location + "}");
      writer.flush();
      writer.close();

      conn.connect();

      InputStream in = conn.getInputStream();
      BufferedReader read = new BufferedReader(new InputStreamReader(in, "UTF-8"));

      StringBuilder response = new StringBuilder();

      String line;

      while ( (line = read.readLine()) != null) {
        response.append(line);
      }

      return response.toString();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public List<FreeStuff> getClose(Location location) {
    List<FreeStuff> found = new ArrayList<FreeStuff>();

    String res = queryClose(location);

    try {
      JSONObject jsonObject = new JSONObject(res);
      JSONObject hits = jsonObject.getJSONObject("hits");
      JSONArray actualhits = hits.getJSONArray("hits");

      for (int i = 0; i < actualhits.length(); i++) {
        JSONObject obj = actualhits.getJSONObject(i);
        JSONObject data = obj.getJSONObject("_source");

        String description = data.getString("description");
        String name = data.getString("name");
        String telephoneNumber = data.getString("telephoneNumber");

        JSONObject jsonlocation = data.getJSONObject("location");

        Location thisLocation = new Location(
            jsonlocation.getDouble("lat"), jsonlocation.getDouble("lon"));

        byte[] decodedimage = Base64.decode(data.getString("image"), 0);

        Bitmap image = BitmapFactory.decodeByteArray(
            decodedimage, 0, decodedimage.length);

        found.add(new FreeStuff(
              image, name, description, telephoneNumber, thisLocation));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
    
  }
}
