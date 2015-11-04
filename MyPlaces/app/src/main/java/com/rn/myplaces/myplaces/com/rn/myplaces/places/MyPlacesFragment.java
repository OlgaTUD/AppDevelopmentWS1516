package com.rn.myplaces.myplaces.com.rn.myplaces.places;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }
}


