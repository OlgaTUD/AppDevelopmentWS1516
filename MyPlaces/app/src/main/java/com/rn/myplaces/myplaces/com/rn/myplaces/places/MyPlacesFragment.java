package com.rn.myplaces.myplaces.com.rn.myplaces.places;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.rn.myplaces.myplaces.MainActivity;
import com.rn.myplaces.myplaces.R;
import com.rn.myplaces.myplaces.com.rn.myplaces.database.MySQLiteHelper;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.location.places.ui.PlacePicker;
import com.rn.myplaces.myplaces.com.rn.myplaces.database.Place;

public class MyPlacesFragment extends Fragment {

    ImageButton FAB;
    public static boolean isVisible = false;
    private MySQLiteHelper db;
    private static final int PLACE_PICKER_REQUEST = 1;

    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(51.0297723, 13.7247851), new LatLng(51.0650247, 13.7880782));

    public static MyPlacesFragment  newInstance() {
        MyPlacesFragment fragment = new MyPlacesFragment();
        return fragment;
    }

    public MyPlacesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.myplaces, container, false);
        ListView listview =(ListView) rootView.findViewById(R.id.list_view);
        isVisible = true;
        db = MySQLiteHelper.getInstance(getContext());

        ArrayList<String> places_name = new ArrayList<String>();
        ArrayList<Integer> places_count = new ArrayList<Integer>();

        for (Place p : db.getAllPlaces()){

            if(places_name.isEmpty() || !places_name.contains(p.getCity())){
                places_name.add(p.getCity());
                places_count.add(1);
            }

            else
            {
                int index = places_name.indexOf(p.getCity());
                places_count.set(index, places_count.get(index) + 1);
            }

        }

        ArrayAdapter adapter =
                new MyPlacesListAdapter(getActivity(), R.layout.myplaces_listitem, places_name, places_count);
        listview.setAdapter(adapter);

        //Round button

        FAB = (ImageButton) rootView.findViewById(R.id.imageButton);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateClick(FAB);

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
        ((MainActivity) activity).onSectionAttached(1);
    }

    public void animateClick(ImageButton img){
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.raise);
        img.startAnimation(shake);
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


