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

import java.util.List;

/**
 * Created by katamarka on 04/11/15.
 */
public class NotificationCenter extends Fragment {

    public static NotificationCenter newInstance() {
        NotificationCenter fragment = new NotificationCenter();
        return fragment;
    }

    public NotificationCenter() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings, container, false);

        TextView text1 =(TextView) rootView.findViewById(R.id.textView1);
        TextView text2 =(TextView) rootView.findViewById(R.id.textView2);
        TextView text3 =(TextView) rootView.findViewById(R.id.textView3);
        TextView text4 =(TextView) rootView.findViewById(R.id.textView4);
        TextView text5 =(TextView) rootView.findViewById(R.id.textView5);

        NotificationHelper db2 = NotificationHelper.getInstance(getContext());

        int num = db2.getAllNotifications().size();
        if (num>5){
            int minus = num -5;
            {
                for(int i=0;i<minus;i++){
                    db2.deleteNotification(db2.getAllNotifications().get(0));
                }
            }
        }
        
        List<Notification> list = db2.getAllNotifications();

        switch (list.size()) {
            case (5): {
                text1.setText(list.get(0).getText());
                text2.setText(list.get(1).getText());
                text3.setText(list.get(2).getText());
                text4.setText(list.get(3).getText());
                text5.setText(list.get(4).getText());
            }
            case (4): {
                text1.setText(list.get(0).getText());
                text2.setText(list.get(1).getText());
                text3.setText(list.get(2).getText());
                text4.setText(list.get(3).getText());
            }
            case (3): {
                text1.setText(list.get(0).getText());
                text2.setText(list.get(1).getText());
                text3.setText(list.get(2).getText());
            }
            case (2): {
                text1.setText(list.get(0).getText());
                text2.setText(list.get(1).getText());
            }
            case (1): {
                text1.setText(list.get(0).getText());
            }
            case (0): {}

        }
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(3);
    }
}


