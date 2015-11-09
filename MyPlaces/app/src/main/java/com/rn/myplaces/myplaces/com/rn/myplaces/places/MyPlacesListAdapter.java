package com.rn.myplaces.myplaces.com.rn.myplaces.places;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

    public class MyPlacesListAdapter extends ArrayAdapter<String> {

        private final Activity context;
        private final ImageView icon;
        private final ImageView mapview;
        private final ImageView listview;
        private final TextView place_name;
        private final TextView place_count;



    public MyPlacesListAdapter(Activity context, ImageView icon, ImageView mapview, ImageView listview, TextView place_name,  TextView place_count) {
            super(context, R.layout.myplaces_listitem);
            // TODO Auto-generated constructor stub

            this.context=context;
            this.icon=icon;
            this.mapview=mapview;
            this.listview = listview;
            this.place_count = place_count;
            this.place_name = place_name;
        }

        @SuppressLint({ "ViewHolder", "InflateParams", "CutPasteId" })
        public View getView(final int position,View view,ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView=inflater.inflate(R.layout.myplaces_listitem, null, true);

            TextView place_name = (TextView) rowView.findViewById(R.id.place_name);
            TextView place_count = (TextView) rowView.findViewById(R.id.place_count);
            ImageView icon = (ImageView) rowView.findViewById(R.id.icon);
            ImageView mapView = (ImageView) rowView.findViewById(R.id.icon_mapview);
            ImageView listView = (ImageView) rowView.findViewById(R.id.icon_listview);


            txtTitle.setText(itemname[position]);
            imageView.setImageResource(imgid[position%8]);
            txtUnten.setText(rating_count[position] + " 'Gefällt mir'-Angaben " + comment_count[position] + " Kommentar(e)");
            comment.setText("Kommentieren");
            like.setText("Gefällt mir");
            name.setText(messageOwners[position]);
            timestamp.setText(date[position]);
            share.setText("Teilen");





//			Define text view for the comment function and set OnClickListener
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCommentClick(messageID[position]);
                }
            });

//			Define text view for the comment function and set OnClickListener
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLikeClick();
                    like.setTextColor(context.getResources().getColor(R.color.green3));

                }
            });

//			Define text view for the share function and set OnClickListener
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShareClick();
                }
            });


            return rowView;
        };


        //		"Kommentieren" button clicked
        public void onCommentClick(String messageID) {
//			Perform action on click
            SocialMediaCommentsDialog.newInstance(messageID).show(context.getFragmentManager(), null);
        }

        //		"Like" button clicked
        public void onLikeClick() {

//			Perform action on click

        }

        //		"Share" button clicked
        @SuppressWarnings("deprecation")
        public void onShareClick() {
//			Perform action on click
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Share");
            alertDialog.setMessage("Hier kann man TEILEN");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }

    }


}
