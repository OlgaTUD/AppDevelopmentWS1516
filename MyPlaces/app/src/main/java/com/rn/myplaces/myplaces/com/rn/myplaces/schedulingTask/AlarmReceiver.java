package com.rn.myplaces.myplaces.com.rn.myplaces.schedulingTask;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;
import com.rn.myplaces.myplaces.R;
import com.rn.myplaces.myplaces.com.rn.myplaces.database.MySQLiteHelper;
import com.rn.myplaces.myplaces.com.rn.myplaces.database.Notification;
import com.rn.myplaces.myplaces.com.rn.myplaces.database.NotificationHelper;
import com.rn.myplaces.myplaces.com.rn.myplaces.database.Place;
import com.rn.myplaces.myplaces.com.rn.myplaces.placesAPI.GooglePlace;
import com.rn.myplaces.myplaces.com.rn.myplaces.placesAPI.JSONPlaceParser;
import com.rn.myplaces.myplaces.weather.JSONWeatherParser;
import com.rn.myplaces.myplaces.com.rn.myplaces.placesAPI.PlacesHttpClient;
import com.rn.myplaces.myplaces.weather.Weather;
import com.rn.myplaces.myplaces.weather.WeatherHttpClient;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class AlarmReceiver extends BroadcastReceiver {

    Weather weather;
    GooglePlace place;
    private Context mContext;
    private MySQLiteHelper db;
    private NotificationHelper db2;
    LocationManager lm;

    @Override
    public void onReceive(Context arg0, Intent arg1) {

        System.out.println("Alarm");

        mContext = arg0;
        db = MySQLiteHelper.getInstance(mContext);
        lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mContext, "No Permission",
                    Toast.LENGTH_LONG).show();
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location ==null){
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location == null){
                location = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }
        }
        if (checkCoonection() && location != null) {

            List<Place> places = getNearPlaces(location);
            if (!places.isEmpty()) {

                List<GooglePlace> gplaces = placeToGooglePlaceConverter(places);

                // Remove closed places
                for (GooglePlace p : gplaces){
                    if(p.getNow()!=null) {
                        if (p.getNow().equals("false")) {
                            gplaces.remove(p);
                        }
                    }
                }

                // GET PLACE TYPE AND WEATHER
                if(!gplaces.isEmpty()) {

                   String city =  getCityFromLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                   getWeather(city);

                   // cold , hot, normal
                   String temperature = getTemperatureDescription();
                   boolean noRain = weather.rain.getAmmount()==0.0;
                   boolean noSnow = weather.snow.getAmmount()==0.0;

                    //System.out.println(noRain);
                    for(GooglePlace p : gplaces) {
                        // Filter types and weather
                        if (!noRain){
                            //Remove places types not used for rain
                            if (p.getTypes().contains(" \"amusement_park\" " ) ||
                                    p.getTypes().contains("\"park\"") ||
                                    p.getTypes().contains("\"stadium\"")
                                    ){
                                gplaces.remove(p);
                            }
                        }

                        if (!noSnow){
                            //Remove places types not used for rain
                            if (p.getTypes().contains("")){
                                gplaces.remove(p);
                            }

                        }

                        if (temperature.equals("cold")){
                            //Remove places types not used for cold weather
                            if (p.getTypes().contains("")){
                                gplaces.remove(p);
                            }
                        }

                        if (temperature.equals("hot")){
                            //Remove places types not used for warm weather
                            if (p.getTypes().contains("")){
                                gplaces.remove(p);
                            }
                        }

                        if (temperature.equals("normal")){
                            if (p.getTypes().contains("")){
                                gplaces.remove(p);
                            }
                        }

                    }
                }

                pushNotification(gplaces);

            }
        }
    }

    private String getTemperatureDescription(){
        double temp = weather.temperature.getTemp()-273.15;
        if(temp <= 5) return "cold";
        if(temp >5 && temp < 20) return "normal";
        if(temp >20) return "hot";
        return "unknown";
    }

    private List<GooglePlace> placeToGooglePlaceConverter(List<Place> places){

        List<GooglePlace> gplaces = new ArrayList<>();

        for(Place p : places){
            GooglePlace newp = getGooglePlace(p.getIdentifikator());
            gplaces.add(newp);
        }
            return gplaces;
    }

    private List<Place> getNearPlaces(Location current) {

        Location l2 = new Location("Two");
        List<Place> places = new ArrayList<>();
        for (Place p : db.getAllPlaces()) {
            l2.setLatitude(Double.valueOf(p.getLat()));
            l2.setLongitude(Double.valueOf(p.getLong()));
            float distance_bw_one_and_two = current.distanceTo(l2);

            if (distance_bw_one_and_two <= 2000) {

                places.add(p);
            }

        }
        return places;
    }

    public boolean checkCoonection() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        } else return false;
    }

    public void getWeather(String city) {

        JSONWeatherTask task = new JSONWeatherTask();
        try {
            weather = task.execute(new String[]{city}).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public GooglePlace getGooglePlace(String id) {

        JSONOpenTask task = new JSONOpenTask();
        try {
            place = task.execute(new String[]{id}).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return place;
    }

    public void pushNotification(List<GooglePlace> places) {

        db2 = NotificationHelper.getInstance(mContext);

        String text ="Would you like to visit ";
        int num = places.size()-1;

            for (GooglePlace g : places){
                String gid = g.getPlaceId();
                for (Place p : db.getAllPlaces()) {
                    if (gid.equals(p.getIdentifikator())){

                        if(num == 0){
                            String nottext = p.getName();
                            text = text + nottext;
                            Notification n = new Notification(nottext);
                            db2.addNotification(n);
                            break;
                        }

                        else{
                            String nottext = p.getName();
                            text = text + nottext + ", ";
                            Notification n = new Notification(nottext);
                            db2.addNotification(n);
                            num = num - 1;
                            break;
                        }
                }
            }
        }
        text = text + ".";
        //System.out.println(text);

        NotificationManager notif = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.cast_ic_notification_0)
                        .setContentTitle("Your Places")
                        .setContentText(text);
        int mNotificationId = 001;
        notif.notify(mNotificationId, mBuilder.build());


    }

    public String getCityFromLatLng(LatLng coordinates) {

        Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(coordinates.latitude, coordinates.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0)
            return addresses.get(0).getLocality();

        else return "Uknown city";
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            weather = new Weather();
            String data = ( (new WeatherHttpClient()).getWeatherData(params[0]));

            try {
                weather = JSONWeatherParser.getWeather(data);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
        }

    }

    private class JSONOpenTask extends AsyncTask<String, Void, GooglePlace> {

        @Override
        protected GooglePlace doInBackground(String... params) {
            place = new GooglePlace();
            String data = ( (new PlacesHttpClient()).getPlaceData(params[0]));

            try {
                place = JSONPlaceParser.getPlace(data);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return place;

        }

        @Override
        protected void onPostExecute(GooglePlace place) {
            super.onPostExecute(place);
        }

    }

}

