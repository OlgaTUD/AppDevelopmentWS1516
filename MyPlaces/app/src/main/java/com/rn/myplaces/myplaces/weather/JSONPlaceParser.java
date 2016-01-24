package com.rn.myplaces.myplaces.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Uto4ko on 23.01.2016.
 */
public class JSONPlaceParser {

        public static GooglePlace getPlace(String data) throws JSONException {
            GooglePlace place = new GooglePlace();

            // We create out JSONObject from the data
            JSONObject jObj = new JSONObject(data);
            JSONObject coordObj = getObject("result", jObj);
            if(coordObj.has("opening_hours")){
                JSONObject opening = getObject("opening_hours", coordObj);
                place.setNow(getString("open_now", opening));
            }

           // place.setPlaceId(getString("place_id",coordObj));
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

