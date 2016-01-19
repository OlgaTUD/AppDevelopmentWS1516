package com.rn.myplaces.myplaces.com.rn.myplaces.mapview;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rn.myplaces.myplaces.MainActivity;
import com.rn.myplaces.myplaces.R;
import com.rn.myplaces.myplaces.com.rn.myplaces.database.MySQLiteHelper;
import com.rn.myplaces.myplaces.com.rn.myplaces.database.Place;
import com.rn.myplaces.myplaces.com.rn.myplaces.places.MyPlacesFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private MapFragment mMapFragment;
    private ImageButton button;
    private ImageButton button2;
    private ImageButton button3;
    boolean markerset;
    private MarkerOptions currentMarker = null ;
    private ArrayList<MarkerOptions> markers;

    private Activity act;

    private MySQLiteHelper db;

    public static MapViewFragment newInstance() {
        MapViewFragment fragment = new MapViewFragment();
        return fragment;
    }

    public MapViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        act = getActivity();
        db = MySQLiteHelper.getInstance(getContext());

        View rootView = inflater.inflate(R.layout.mapview, container, false);
        MyPlacesFragment.isVisible = false;
        markerset = false;

        map = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.location_map))
                .getMap();
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        markers = new ArrayList<MarkerOptions>();
        addMarkers();
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);
                currentMarker = markerOptions;
                markerset = true;
                // Clears the previously touched position
                map.clear();

                // Animating to the touched position
                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                map.addMarker(markerOptions);
            }
        });

        button = (ImageButton) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                animateClick(button);
                jumpToCurLocation(map.getMyLocation());
            }
        });

        button2 = (ImageButton) rootView.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (wifiNetwork != null && wifiNetwork.isConnected())
                {

                    if (markerset) {
                        Geocoder geoCoder = new Geocoder(getContext());
                        List<Address> matches = null;
                        try {
                            matches = geoCoder.getFromLocation(currentMarker.getPosition().latitude, currentMarker.getPosition().longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Address bestMatch;
                        if (matches != null) {
                            bestMatch = (matches.isEmpty() ? null : matches.get(0));
                            if (bestMatch != null) {

                                currentMarker.title(bestMatch.getAddressLine(0));
                                String[] city = bestMatch.getAddressLine(1).split(" ");

                                db.addPlace(
                                        new Place(
                                                bestMatch.getAddressLine(0),
                                                city[1],
                                                String.valueOf(currentMarker.getPosition().latitude),
                                                String.valueOf(currentMarker.getPosition().longitude)
                                        ));

                                Toast.makeText(getActivity(), "Place added",
                                        Toast.LENGTH_LONG).show();
                            }
                        }


                        // GET STREET
                        //bestMatch.getAddressLine(0)

                        //GET PLZ + CITY
                        //bestMatch.getAddressLine(1)

                    }

                    else {
                        Toast.makeText(getActivity(), "Please set Marker!",
                                Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    Toast.makeText(getActivity(), "No Wifi connection!",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        button3 = (ImageButton) rootView.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                for (Place p : db.getAllPlaces()){
                    db.deletePlace(p);
                }

                Toast.makeText(getActivity(), "All places removed",
                        Toast.LENGTH_LONG).show();


            }
        });

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // jumpToCurLocation(map.getMyLocation());

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(2);
    }

    public void jumpToCurLocation(Location location){

        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(11);
        map.moveCamera(center);
        map.animateCamera(zoom);
    }

    public void addMarkers(){

        for (Place p : db.getAllPlaces()){

            LatLng coordianates = new LatLng(Double.parseDouble(p.getLat()),Double.parseDouble(p.getLong()));

            Marker marker = map.addMarker(new MarkerOptions().position(coordianates)
                    .title(p.getName()));

        }

    }

    //animation for buttons and icons
    public void animateClick(ImageButton img){
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.raise);
        img.startAnimation(shake);
    }

}

