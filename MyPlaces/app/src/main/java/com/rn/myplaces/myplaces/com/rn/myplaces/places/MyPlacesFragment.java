package com.rn.myplaces.myplaces.com.rn.myplaces.places;

import android.app.Activity;
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


        //List of Places

        String[] places_name = new String[] {"Dresden", "Rome", "Paris"};
        Integer[] places_count = new Integer[] {8, 12, 14};

        ArrayAdapter adapter =
                new MyPlacesListAdapter(getActivity(), R.layout.myplaces_listitem, places_name, places_count);
        listview.setAdapter(adapter);

        //Round button

        FAB = (ImageButton) rootView.findViewById(R.id.imageButton);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateClick(FAB);
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


