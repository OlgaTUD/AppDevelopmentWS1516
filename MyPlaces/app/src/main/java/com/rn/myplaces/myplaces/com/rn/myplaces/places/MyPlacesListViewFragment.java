package com.rn.myplaces.myplaces.com.rn.myplaces.places;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
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

import com.rn.myplaces.myplaces.MainActivity;
import com.rn.myplaces.myplaces.R;
import com.rn.myplaces.myplaces.com.rn.myplaces.database.MySQLiteHelper;
import com.rn.myplaces.myplaces.com.rn.myplaces.database.Place;

import java.util.ArrayList;


/**
 * Created by katamarka on 04/11/15.
 */
public class MyPlacesListViewFragment extends Fragment {

    ImageButton FAB2;
    private MySQLiteHelper db;
    LocationManager lm;

    public static MyPlacesListViewFragment newInstance() {
        MyPlacesListViewFragment fragment = new MyPlacesListViewFragment();
        return fragment;
    }

    public MyPlacesListViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.myplaces_listview, container, false);
        ListView listview =(ListView) rootView.findViewById(R.id.list_view_lv);

        lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "No Permission",
                    Toast.LENGTH_LONG).show();
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location ==null){
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location == null){
                location = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            }
        }



        Bundle bundle = this.getArguments();
        String place_name = bundle.getString("city");
        ArrayList<String> places_name =  new ArrayList<String>();
        //get data from the fragment

        db = MySQLiteHelper.getInstance(getContext());

        for (Place p : db.getAllPlaces()){
            if(p.getCity().equals(place_name)){
                places_name.add(p.getName());
            }
        }

        //List of Places

        ArrayList<Integer>  places_distance = new ArrayList<Integer>();
        for (Place p : db.getAllPlaces()){
            if(p.getCity().equals(place_name)){
                Location loc = new Location("loc");
                loc.setLatitude(Double.valueOf(p.getLat()));
                loc.setLongitude(Double.valueOf(p.getLong()));
                places_distance.add((int) getDistance(loc,location));
            }
        }


        ArrayList<Integer>  places_marker = new ArrayList<Integer>();

        for (int i = 0; i< place_name.length();i++){
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
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(5);
    }

    public void animateClick(ImageButton img){
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.raise);
        img.startAnimation(shake);
    }

    public float getDistance(Location loc,Location location){
        return location.distanceTo(loc);
    }
}


