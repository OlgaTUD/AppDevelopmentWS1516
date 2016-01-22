package com.rn.myplaces.myplaces;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by katamarka on 23/11/15.
 */
public class SearchResultsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());

        Bundle appData = getIntent().getBundleExtra(SearchManager.APP_DATA);
        if (appData != null) {
            String hello = appData.getString("hello");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            showResults(query);
        }
    }
        private void showResults(String query) {
            // Query your data set and show results
            // ...
        }
}