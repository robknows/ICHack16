package ichack16.getridofyoshit;

import android.media.Image;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONException;

/**
 * Created by fs on 20/02/2016.
 */


public class QueryServer {


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

}
