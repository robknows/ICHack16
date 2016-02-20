package ichack16.getridofyoshit.api;

import android.support.annotation.NonNull;

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

import ichack16.getridofyoshit.FreeStuff;
import ichack16.getridofyoshit.Location;

public class QueryServer {

    private final String serverURL;
    private static final String ADD_API = "/add/";
    private static final String SEARCH_API = "/search/";

    public QueryServer(String serverURL) {
        this.serverURL = serverURL;
    }

    public String writeToServer(FreeStuff dataToAdd) {
        try {
            URL addURL = new URL(serverURL + ADD_API);
            HttpURLConnection conn = (HttpURLConnection) addURL.openConnection();
            setUpRequestConditions(conn);

            BufferedWriter writer = createBufferedWriter(conn);
            writer.write(dataToAdd.toString());
            writer.flush();
            writer.close();

            conn.connect();

            return conn.getResponseMessage();
        } catch (Exception e) {
            return "FAIL";
        }
    }

    @NonNull
    private BufferedWriter createBufferedWriter(HttpURLConnection conn) throws IOException {
        OutputStream out = conn.getOutputStream();
        return new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
    }

    private String queryClose(Location location) {
        try {
            URL addURL = new URL(serverURL + SEARCH_API);
            HttpURLConnection conn = (HttpURLConnection) addURL.openConnection();

            setUpRequestConditions(conn);

            BufferedWriter writer = createBufferedWriter(conn);
            writer.write("{ \"location\": " + location + "}");
            writer.flush();
            writer.close();

            conn.connect();

            InputStream in = conn.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            return buildResponse(bufferedReader);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void setUpRequestConditions(HttpURLConnection conn) throws ProtocolException {
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
    }

    @NonNull
    private String buildResponse(BufferedReader bufferedReader) throws IOException {
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            response.append(line);
        }

        return response.toString();
    }

    public static void main(String[] args) {
        Location testLoc = new Location(61.2180556, -149.9002778);
        QueryServer qs = new QueryServer("http://localhost:8000");
        System.out.println(qs.queryClose(testLoc));
    }
}
