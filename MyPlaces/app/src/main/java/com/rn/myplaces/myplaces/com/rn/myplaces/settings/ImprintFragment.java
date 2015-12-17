package com.rn.myplaces.myplaces.com.rn.myplaces.settings;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rn.myplaces.myplaces.MainActivity;
import com.rn.myplaces.myplaces.R;
import com.rn.myplaces.myplaces.weather.JSONWeatherParser;
import com.rn.myplaces.myplaces.weather.Weather;
import com.rn.myplaces.myplaces.weather.WeatherHttpClient;

import org.json.JSONException;

/**
 * Created by katamarka on 04/11/15.
 */
public class ImprintFragment extends Fragment {

    private TextView weatherView;
    String city = "London,UK";
    Weather weather;

    public static ImprintFragment newInstance() {
        ImprintFragment fragment = new ImprintFragment();
        return fragment;
    }

    public ImprintFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.imprint, container, false);

        //TEST WEATHER STUFF
        final TextView weatherView = (TextView) rootView.findViewById(R.id.textView1);
        Button button = (Button)rootView.findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                weatherView.setText(weather.currentCondition.getDescr());
            }
        });

        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{city});

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(4);
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




