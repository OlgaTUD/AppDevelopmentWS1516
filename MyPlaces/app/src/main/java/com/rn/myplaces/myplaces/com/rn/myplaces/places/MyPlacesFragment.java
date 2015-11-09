package com.rn.myplaces.myplaces.com.rn.myplaces.places;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.rn.myplaces.myplaces.MainActivity;
import com.rn.myplaces.myplaces.R;

/**
 * Created by katamarka on 04/11/15.
 */
public class MyPlacesFragment extends Fragment {

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

        String[] places_name = new String[] {"Dresden", "Rome", "Paris"};
        Integer[] places_count = new Integer[] {8, 12, 14};

        ArrayAdapter adapter =
                new MyPlacesListAdapter(getActivity(), R.layout.myplaces_listitem, places_name, places_count);
        listview.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }
}


