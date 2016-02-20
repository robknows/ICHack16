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

  public static void main(String[] args) {
    Location testLoc = new Location(61.2180556, -149.9002778);

    QueryServer qs = new QueryServer("http://ec2-52-30-60-12.eu-west-1.compute.amazonaws.com");

    System.out.println(qs.queryClose(testLoc));
  }

}
