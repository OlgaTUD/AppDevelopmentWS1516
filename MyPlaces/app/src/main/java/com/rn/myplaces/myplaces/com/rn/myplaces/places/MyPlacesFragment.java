package com.rn.myplaces.myplaces.com.rn.myplaces.places;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.rn.myplaces.myplaces.MainActivity;
import com.rn.myplaces.myplaces.R;

/**
 * Created by katamarka on 04/11/15.
 */
public class MyPlacesFragment extends Fragment {

    Toolbar toolbar;
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

                Toast.makeText(getActivity(), "Hello World", Toast.LENGTH_SHORT).show();

            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }
}


