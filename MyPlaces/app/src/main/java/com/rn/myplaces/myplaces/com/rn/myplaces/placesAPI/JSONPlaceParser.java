package com.rn.myplaces.myplaces.com.rn.myplaces.placesAPI;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;

public class JSONPlaceParser {

        public static GooglePlace getPlace(String data) throws JSONException {
            GooglePlace place = new GooglePlace();

            JSONObject jObj = new JSONObject(data);
            JSONObject coordObj = getObject("result", jObj);
            if(coordObj.has("opening_hours")){
                JSONObject opening = getObject("opening_hours", coordObj);
                place.setNow(getString("open_now", opening));
            }
            place.setPlaceId(getString("place_id", coordObj));

            String types = getString("types", coordObj);
            types = types.substring(1,types.length()-1);
            String[] typesarray = types.split(",");

            for(int i=0;i<typesarray.length;i++){
                typesarray[i] = typesarray[i].substring(1,typesarray[i].length()-1);
            }

            ArrayList typeslist = new ArrayList(Arrays.asList(typesarray));
            place.setTypes(typeslist);

            return place;
        }


        private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
            JSONObject subObj = jObj.getJSONObject(tagName);
            return subObj;
        }

        private static String getString(String tagName, JSONObject jObj) throws JSONException {
            return jObj.getString(tagName);
        }

        private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
            return (float) jObj.getDouble(tagName);
        }

        private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
            return jObj.getInt(tagName);
        }


    }

