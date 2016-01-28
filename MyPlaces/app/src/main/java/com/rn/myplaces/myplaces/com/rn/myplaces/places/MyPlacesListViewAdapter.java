package com.rn.myplaces.myplaces.com.rn.myplaces.places;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rn.myplaces.myplaces.R;
import com.rn.myplaces.myplaces.com.rn.myplaces.database.MySQLiteHelper;
import com.rn.myplaces.myplaces.com.rn.myplaces.database.Place;

import java.util.ArrayList;

    public class MyPlacesListViewAdapter extends ArrayAdapter {

        private final Activity context;
        private final ArrayList<String> places;
        private final ArrayList<String> adress;
        private final ArrayList<Integer> place_distance;
        private final ArrayList<Integer> place_marker;
        public  FragmentManager fragmentManager;

        private MySQLiteHelper db;


    public MyPlacesListViewAdapter(Activity context, int resource, ArrayList<String> places,  ArrayList<String> adress, ArrayList<Integer> place_distance, ArrayList<Integer> place_marker) {
            super(context, R.layout.myplaces_listitem2, places);

            this.context=context;
            this.place_distance = place_distance;
            this.places = places;
            this.place_marker = place_marker;
            this.adress = adress;
    }

        @SuppressLint({ "ViewHolder", "InflateParams", "CutPasteId" })
        public View getView(final int position,View view,ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.myplaces_listitem2, null, true);
            db = MySQLiteHelper.getInstance(getContext());

            final TextView place_name = (TextView) rowView.findViewById(R.id.place_names);
            final TextView place_adress = (TextView) rowView.findViewById(R.id.place_adress);
            TextView place_count = (TextView) rowView.findViewById(R.id.place_distance);
            ImageView place_markers = (ImageView) rowView.findViewById(R.id.icon_marker);
            final TextView place_delete = (TextView) rowView.findViewById(R.id.place_delete);

            place_name.setText(places.get(position));
            place_adress.setText(adress.get(position));
            place_delete.setText("x");
            place_count.setText(place_distance.get(position) + " meter");
            place_markers.setImageResource(place_marker.get(position));

            final Animation shake = AnimationUtils.loadAnimation(context, R.anim.raise);

            place_delete.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    place_delete.startAnimation(shake);
                    for(Place p : db.getAllPlaces()){
                        if(p.getName().equals(places.get(position))){
                            db.deletePlace(p);
                            Toast.makeText(getContext(), "Place removed",
                                    Toast.LENGTH_LONG).show();

                            break;
                        }
                    }

                    return false;
                }
            });

            return rowView;
        };
    }



