package com.rn.myplaces.myplaces.com.rn.myplaces.places;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.rn.myplaces.myplaces.MainActivity;
import com.rn.myplaces.myplaces.R;
import com.rn.myplaces.myplaces.com.rn.myplaces.database.MySQLiteHelper;
import com.rn.myplaces.myplaces.com.rn.myplaces.database.Place;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by katamarka on 04/11/15.
 */
public class MyPlacesListViewFragment extends Fragment implements LocationListener {

    ImageButton FAB2;
    private MySQLiteHelper db;
    LocationManager lm;
    private static final int PLACE_PICKER_REQUEST = 1;

    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(51.0297723, 13.7247851), new LatLng(51.0650247, 13.7880782));


    public static MyPlacesListViewFragment newInstance() {
        MyPlacesListViewFragment fragment = new MyPlacesListViewFragment();
        return fragment;
    }

    public MyPlacesListViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.myplaces_listview, container, false);
        ListView listview = (ListView) rootView.findViewById(R.id.list_view_lv);

        lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "No Permission",
                    Toast.LENGTH_LONG).show();
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {
            //location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            // if (location == null){
            //     location = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            // }

        }
        if (location == null || location.getTime() < Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }


        Bundle bundle = this.getArguments();
        String place_name = bundle.getString("city");
        ArrayList<String> places_name = new ArrayList<String>();
        //get data from the fragment

        db = MySQLiteHelper.getInstance(getContext());

        for (Place p : db.getAllPlaces()) {
            if (p.getCity().equals(place_name)) {
                places_name.add(p.getName());
            }
        }

        //List of Places

        ArrayList<Integer> places_distance = new ArrayList<Integer>();
        for (Place p : db.getAllPlaces()) {
            if (p.getCity().equals(place_name)) {
                Location loc = new Location("loc");
                loc.setLatitude(Double.valueOf(p.getLat()));
                loc.setLongitude(Double.valueOf(p.getLong()));
                places_distance.add((int) getDistance(loc, location));
            }
        }


        ArrayList<Integer> places_marker = new ArrayList<Integer>();

        for (int i = 0; i < place_name.length(); i++) {
            places_marker.add(R.drawable.ic_loc_blue);
        }
        //places_marker.add(R.drawable.ic_loc_blue);
        // places_marker.add(R.drawable.ic_loc_green);
        // places_marker.add(R.drawable.ic_loc_orange);
        // places_marker.add(R.drawable.ic_loc_grey);


        ArrayAdapter adapter =
                new MyPlacesListViewAdapter(getActivity(), R.layout.myplaces_listitem2, places_name, places_distance, places_marker);
        listview.setAdapter(adapter);

        //Round button

        FAB2 = (ImageButton) rootView.findViewById(R.id.imageButton_lv);
        FAB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateClick(FAB2);

                ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (wifiNetwork != null && wifiNetwork.isConnected()){


                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
                    Intent intent = null;
                    try {
                        intent = intentBuilder.build(getActivity());
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);


                }
                else{
                    Toast.makeText(getActivity(), "No Wifi connection!",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        return rootView;
    }


    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {

            final com.google.android.gms.location.places.Place place = PlacePicker.getPlace(data, getActivity().getApplicationContext());

            Intent intent = new Intent(getContext(), NewPlaceActivity.class);
            intent.putExtra("name", place.getName().toString());
            intent.putExtra("city", getCityFromLatLng(place.getLatLng()));
            intent.putExtra("adress", place.getAddress().toString());
            intent.putExtra("lat",  String.valueOf(place.getLatLng().latitude));
            intent.putExtra("long",  String.valueOf(place.getLatLng().longitude));
            intent.putExtra("ident",  place.getId());
            startActivity(intent);

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(5);
    }

    public void animateClick(ImageButton img) {
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.raise);
        img.startAnimation(shake);
    }

    public float getDistance(Location loc, Location location) {
        return location.distanceTo(loc);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            lm.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    public String getCityFromLatLng(LatLng coordinates){

        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
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

}


