package com.rn.myplaces.myplaces.com.rn.myplaces.settings;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.rn.myplaces.myplaces.MainActivity;
import com.rn.myplaces.myplaces.R;
import com.rn.myplaces.myplaces.com.rn.myplaces.database.Notification;
import com.rn.myplaces.myplaces.com.rn.myplaces.database.NotificationHelper;

public class ImprintFragment extends Fragment {

    public static ImprintFragment newInstance() {
        ImprintFragment fragment = new ImprintFragment();
        return fragment;
    }

    public ImprintFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.imprint, container, false);
       return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(4);
    }

}




