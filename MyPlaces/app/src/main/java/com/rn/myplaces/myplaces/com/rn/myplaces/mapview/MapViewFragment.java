package com.rn.myplaces.myplaces.com.rn.myplaces.mapview;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rn.myplaces.myplaces.MainActivity;
import com.rn.myplaces.myplaces.R;
import com.rn.myplaces.myplaces.com.rn.myplaces.database.MySQLiteHelper;
import com.rn.myplaces.myplaces.com.rn.myplaces.database.Place;
import com.rn.myplaces.myplaces.com.rn.myplaces.places.MyPlacesFragment;

import java.io.IOException;
import java.util.List;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private MapFragment mMapFragment;
    private ImageButton button;
    private ImageButton button2;
    private ImageButton button3;
    boolean markerset;
    private MarkerOptions currentMarker = null ;

    private MySQLiteHelper db;

    public static MapViewFragment newInstance() {
        MapViewFragment fragment = new MapViewFragment();
        return fragment;
    }

    public MapViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db = MySQLiteHelper.getInstance(getContext());
        View rootView = inflater.inflate(R.layout.mapview, container, false);
        MyPlacesFragment.isVisible = false;
        markerset = false;
        map = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.location_map))
                .getMap();
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);

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
                System.out.println("123");
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

                            //TODO GET CITY !!!!
                            currentMarker.title(bestMatch.getAddressLine(0));
                            String[] city = bestMatch.getAddressLine(1).split(" ");

                            db.addPlace(new Place(bestMatch.getAddressLine(0), city[1]));
                        }
                    }


                    // GET STREET
                    //bestMatch.getAddressLine(0)

                    //GET PLZ + CITY
                    //bestMatch.getAddressLine(1)

                }

            }
        });

        button3 = (ImageButton) rootView.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                for (Place p : db.getAllPlaces()){
                    db.deletePlace(p);
                }


            }
        });

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        jumpToCurLocation(map.getMyLocation());
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

    //animation for buttons and icons
    public void animateClick(ImageButton img){
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.raise);
        img.startAnimation(shake);
    }

}


