package com.rn.myplaces.myplaces.com.rn.myplaces.places;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rn.myplaces.myplaces.R;
import com.rn.myplaces.myplaces.com.rn.myplaces.mapview.MapViewFragment;


/**
 * Created by katamarka on 07/11/15.
 */

    public class MyPlacesListAdapter extends ArrayAdapter {

        private final Activity context;
        private final String[] places;
        private final Integer[] place_number;
        public  FragmentManager fragmentManager;




    public MyPlacesListAdapter(Activity context,int resource, String[] places,  Integer[] place_number) {
            super(context, R.layout.myplaces_listitem, places);

            this.context=context;
            this.place_number = place_number;
            this.places = places;
        }

        @SuppressLint({ "ViewHolder", "InflateParams", "CutPasteId" })
        public View getView(final int position,View view,ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.myplaces_listitem, null, true);

            TextView place_name = (TextView) rowView.findViewById(R.id.place_name);
            TextView place_count = (TextView) rowView.findViewById(R.id.place_count);
            final ImageButton mapView = (ImageButton) rowView.findViewById(R.id.icon_mapview);
            final ImageButton listView = (ImageButton) rowView.findViewById(R.id.icon_listview);


           place_name.setText(places[position]);
           place_count.setText(place_number[position] + " Places");

           final Animation shake = AnimationUtils.loadAnimation(context, R.anim.raise);


            mapView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        mapView.startAnimation(shake);

                        return true;
                    }

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        mapViewClick();

                        return true;
                    }

                    return false;
                }
            });

            listView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        listView.startAnimation(shake);

                        return true;
                    }

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        listViewClick();


                        return true;
                    }

                    return false;
                }
            });


            return rowView;
        };


        //		"MapView" button clicked
        public void mapViewClick() {
//			Perform action on click

            Fragment tf = new MapViewFragment();
            FragmentTransaction ft = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.replace(R.id.container, tf);
            ft.addToBackStack(null);
            ft.commit();

        }

        //		"ListView" button clicked
        public void listViewClick() {
//			Perform action on click

            Fragment tf = new MyPlacesListViewFragment();
            FragmentTransaction ft = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.replace(R.id.container, tf);
            ft.addToBackStack(null);
            ft.commit();


        }


    }



