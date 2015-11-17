package com.rn.myplaces.myplaces.com.rn.myplaces.places;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rn.myplaces.myplaces.R;


/**
 * Created by katamarka on 07/11/15.
 */

    public class MyPlacesListViewAdapter extends ArrayAdapter {

        private final Activity context;
        private final String[] places;
        private final Integer[] place_distance;
        private final Integer[] place_marker;
        public  FragmentManager fragmentManager;




    public MyPlacesListViewAdapter(Activity context, int resource, String[] places, Integer[] place_distance, Integer[] place_marker) {
            super(context, R.layout.myplaces_listitem2, places);

            this.context=context;
            this.place_distance = place_distance;
            this.places = places;
            this.place_marker = place_marker;
    }

        @SuppressLint({ "ViewHolder", "InflateParams", "CutPasteId" })
        public View getView(final int position,View view,ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.myplaces_listitem2, null, true);

            TextView place_name = (TextView) rowView.findViewById(R.id.place_names);
            TextView place_count = (TextView) rowView.findViewById(R.id.place_distance);
            ImageView place_markers = (ImageView) rowView.findViewById(R.id.icon_marker);

            place_name.setText(places[position]);
            place_count.setText(place_distance[position] + " meter");
            place_markers.setImageResource(place_marker[position]);

            final Animation shake = AnimationUtils.loadAnimation(context, R.anim.raise);


            return rowView;
        };



    }



