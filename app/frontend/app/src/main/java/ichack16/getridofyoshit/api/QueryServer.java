package ichack16.getridofyoshit.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONException;
import android.graphics.Bitmap;
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
      return ""; 
    }
  }

  public List<FreeStuff> getClose(Double longQuery, Double latQuery) throws JSONException{
        List<FreeStuff> listFS = new ArrayList<FreeStuff>();

        // JSONObject test = queryClose(longQuery, latQuery);
        String test  = "{ \"took\": 238, \"timed_out\" : false, \"_shards\" : { \"total\" : 5, \"successful\" : 5, \"failed\" : 0 }, \"hits\" : { \"total\" : 1, \"max_score\" : 1.0, \"hits\" : [ { \"_index\" : \"test4\", \"_type\" : \"stuff\", \"_id\" : \"AVL_tjbKoRpB4SBFfD2u\", \"_score\" : 1.0, \"_source\":{\"city\": \"Anchorage\", \"state\": \"AK\",\"location\": {\"lat\": 61.2180556, \"lon\": -149.9002778}} } ] } }";
        test = test.replaceAll("\n", "\\n");
        JSONObject jsonRes = new JSONObject(test);


        JSONObject res = jtest.get("_source");

        String resName = res.getString("name");
        String resDescr = res.getString("description");
        String resTel = res.getString("telephone");
        Image resImg = (Image)res.get("img");
        JSONObject resLoc = jtest.get("location");
        Double resLat = resLoc.getDouble("lat");
        Double resLong = resLoc.getDouble("lon");
        Location location = new  Location(resLat, resLong);

        FreeStuff fs = new FreeStuff(resImg, resName, resDescr, resTel, location);
        listFS.add(fs);

        return listFS;

    }

  public static void main(String[] args) {
    Location testLoc = new Location(61.2180556, -149.9002778);

    QueryServer qs = new QueryServer("http://localhost:8000");

    System.out.println(qs.queryClose(testLoc));
  }
}
