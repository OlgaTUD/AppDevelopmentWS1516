package com.rn.myplaces.myplaces.com.rn.myplaces.places;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.rn.myplaces.myplaces.R;
import com.rn.myplaces.myplaces.com.rn.myplaces.database.MySQLiteHelper;
import com.rn.myplaces.myplaces.com.rn.myplaces.database.Place;

/**
 * Created by helgafoxx on 17.11.15.
 */
public class NewPlaceActivity extends AppCompatActivity {

    private ActionBar toolbar;
    private String name;
    private String city;
    private String adress;
    private String lat;
    private String longt;

    private MySQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_place);
        db = MySQLiteHelper.getInstance(this);

        toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);
        toolbar.setTitle("New Place");
        toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.marker)));

        Intent myIntent = getIntent();
        name = myIntent.getStringExtra("name");
        city = myIntent.getStringExtra("city");
        adress = myIntent.getStringExtra("adress");
        lat = myIntent.getStringExtra("lat");
        longt = myIntent.getStringExtra("long");

        EditText namefield = (EditText) findViewById(R.id.name_field);
        namefield.setText(name);

        EditText locationfield = (EditText) findViewById(R.id.location_field);
        locationfield.setText(city);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.home) {

            return true;
        }

        if (id == R.id.ok) {

            db.addPlace(
                    new Place(
                            name,
                            city,
                            adress,
                            lat,
                            longt
                    ));

            Toast.makeText(this, "Place added!",
                    Toast.LENGTH_LONG).show();
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("My Places");
    }



    public void animateClick(ImageButton img){
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.raise);
        img.startAnimation(shake);
    }
}
