package com.rn.myplaces.myplaces.com.rn.myplaces.placesAPI;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlacesHttpClient {

        private static String BASE_URL = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
        private static String KEY ="&key=AIzaSyC04KXB3TMSus2gCkZLBLrckaKW4UbnsGM" ;

        public String getPlaceData(String PLACE_ID) {
            HttpURLConnection con = null ;
            InputStream is = null;

            try {
                con = (HttpURLConnection) ( new URL(BASE_URL + PLACE_ID + KEY)).openConnection();
                con.setRequestMethod("GET");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                StringBuffer buffer = new StringBuffer();
                is = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = null;
                while (  (line = br.readLine()) != null ){
                    buffer.append(line + "\r\n");
                }

                is.close();
                con.disconnect();
                return buffer.toString();
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            finally {
                try { is.close(); } catch(Throwable t) {}
                try { con.disconnect(); } catch(Throwable t) {}
            }

            return null;

        }

    }
