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
import com.rn.myplaces.myplaces.com.rn.myplaces.database.Place;
import com.rn.myplaces.myplaces.weather.JSONWeatherParser;
import com.rn.myplaces.myplaces.weather.Weather;
import com.rn.myplaces.myplaces.weather.WeatherHttpClient;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class AlarmReceiver extends BroadcastReceiver {

    String city = "London,UK";
    Weather weather;
    private Context mContext;
    private MySQLiteHelper db;

    LocationManager lm;


    @Override
    public void onReceive(Context arg0, Intent arg1) {

        mContext = arg0;
        db = MySQLiteHelper.getInstance(mContext);
        lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(mContext,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mContext, "No Permission",
                    Toast.LENGTH_LONG).show();
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        // Toast.makeText(mContext, location.toString(), Toast.LENGTH_LONG).show();

        if(checkCoonection()){
            List<Place> places = getNearPlaces(location);
            if (!places.isEmpty()){
                // System.out.println(p.getIdentifikator());

                //getOpeningTimes()
                //getWeather();
                //pushNotification();
            }
        }
    }

    private void getOpeningTimes(){
        
    }

    private List<Place> getNearPlaces(Location current) {

        Location l2=new Location("Two");
        List<Place> places = new ArrayList<>();
        for (Place p : db.getAllPlaces()) {
            l2.setLatitude(Double.valueOf(p.getLat()));
            l2.setLongitude(Double.valueOf(p.getLong()));
            float distance_bw_one_and_two=current.distanceTo(l2);

            if(distance_bw_one_and_two<=2000){

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

    public String getWeather() {

        JSONWeatherTask task = new JSONWeatherTask();
        try {
            weather = task.execute(new String[]{city}).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return weather.currentCondition.getCondition();
    }

    public void pushNotification() {

        NotificationManager notif = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setSmallIcon(R.drawable.cast_ic_notification_0)
                        .setContentTitle("Your Places")
                        .setContentText("Hello World!");
        int mNotificationId = 001;
        notif.notify(mNotificationId, mBuilder.build());

    }

    public String getCityFromLatLng(LatLng coordinates){

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

}

