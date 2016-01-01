package com.rn.myplaces.myplaces.com.rn.myplaces.schedulingTask;

/**
 * Created by Uto4ko on 01.01.2016.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rn.myplaces.myplaces.R;
import com.rn.myplaces.myplaces.weather.JSONWeatherParser;
import com.rn.myplaces.myplaces.weather.Weather;
import com.rn.myplaces.myplaces.weather.WeatherHttpClient;

import org.json.JSONException;

public class AlarmReceiver extends BroadcastReceiver {

    String city = "London,UK";
    Weather weather;


    @Override
    public void onReceive(Context arg0, Intent arg1) {

        //weather.currentCondition.getDescr();
        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{city});

        // For our recurring task, we'll just display a message
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