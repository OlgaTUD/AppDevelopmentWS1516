package com.rn.myplaces.myplaces.com.rn.myplaces.places;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rn.myplaces.myplaces.R;

/**
 * Created by katamarka on 07/11/15.
 */

    public class MyPlacesListAdapter extends ArrayAdapter {

        private final Activity context;
        private final String[] places;
        private final Integer[] place_number;



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
            ImageView mapView = (ImageView) rowView.findViewById(R.id.icon_mapview);
            ImageView listView = (ImageView) rowView.findViewById(R.id.icon_listview);


           place_name.setText(places[position]);
           place_count.setText(place_number[position]+" Places");

            mapView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mapViewClick();
                }
            });

            listView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    listViewClick();
                }
            });

            return rowView;
        };


        //		"MapView" button clicked
        public void mapViewClick() {
//			Perform action on click

        }

        //		"ListView" button clicked
        public void listViewClick() {
//			Perform action on click

        }

    }



