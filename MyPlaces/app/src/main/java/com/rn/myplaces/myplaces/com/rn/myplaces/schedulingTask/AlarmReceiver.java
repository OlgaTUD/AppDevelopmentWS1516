package com.rn.myplaces.myplaces.com.rn.myplaces.schedulingTask;

/**
 * Created by Uto4ko on 01.01.2016.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rn.myplaces.myplaces.MainActivity;
import com.rn.myplaces.myplaces.R;
import com.rn.myplaces.myplaces.weather.JSONWeatherParser;
import com.rn.myplaces.myplaces.weather.Weather;
import com.rn.myplaces.myplaces.weather.WeatherHttpClient;

import org.json.JSONException;

public class AlarmReceiver extends BroadcastReceiver {

    String city = "London,UK";
    Weather weather;
    private Context mContext;

    @Override
    public void onReceive(Context arg0, Intent arg1) {

        //weather.currentCondition.getDescr();
        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{city});

        NotificationManager notif=(NotificationManager) arg0.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(arg0)
                        .setSmallIcon(R.drawable.cast_ic_notification_0)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        int mNotificationId = 001;
        // notif.notify(mNotificationId, mBuilder.build());

        //  Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();

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
            //weatherView.setText(weather.toString());
            // condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
            // temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "C");
            // hum.setText("" + weather.currentCondition.getHumidity() + "%");
            // press.setText("" + weather.currentCondition.getPressure() + " hPa");
            // windSpeed.setText("" + weather.wind.getSpeed() + " mps");
            // windDeg.setText("" + weather.wind.getDeg() + "");

        }

    }

}