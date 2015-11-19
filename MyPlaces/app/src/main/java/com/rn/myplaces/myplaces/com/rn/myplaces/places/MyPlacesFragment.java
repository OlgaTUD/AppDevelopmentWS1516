package com.rn.myplaces.myplaces.com.rn.myplaces.places;

import android.app.Activity;
import android.content.Intent;
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

import com.rn.myplaces.myplaces.MainActivity;
import com.rn.myplaces.myplaces.R;
import com.rn.myplaces.myplaces.com.rn.myplaces.database.MySQLiteHelper;
import com.rn.myplaces.myplaces.com.rn.myplaces.database.Place;

import java.util.ArrayList;


/**
 * Created by katamarka on 04/11/15.
 */
public class MyPlacesFragment extends Fragment {

    ImageButton FAB;

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

        MySQLiteHelper db = new MySQLiteHelper(getActivity());
        if (db.getAllPlaces().isEmpty()){
            db.addPlace(new Place("Museum","Dresden"));
        }
       //

        ArrayList<String> places_name = new ArrayList<String>();
        ArrayList<Integer> places_count = new ArrayList<Integer>();
        for (Place p : db.getAllPlaces()){
            db.deletePlace(p);
            places_name.add(p.getCity());
            places_count.add(5);
            System.out.println(p.getPlace());
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
//                Fragment tf = NewPlaceFragment.newInstance();
//                FragmentTransaction ft = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                ft.setTransitionStyle(FragmentTransaction.TRANSIT_NONE);
//                ft.replace(R.id.container, tf);
//                ft.addToBackStack(null);
//                ft.commit();

                Intent intent = new Intent(getContext(), NewPlaceActivity.class);
                startActivity(intent);
            }
        });


        return rootView;
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


}


