package com.xplorer.hope.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.xplorer.hope.R;
import com.xplorer.hope.adapter.WorkerAdaptor;
import com.xplorer.hope.config.HopeApp;
import com.xplorer.hope.object.UserInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchActivity extends Activity {

    @InjectView(R.id.tv_seach_result)
    TextView tv_result;
    @InjectView(R.id.lv_search_items)
    ListView lv_items;

    MenuItem searchMenuItem;
    SearchView searchView;
    String searchVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.inject(this);

        searchVal="";
        handleIntent(getIntent());
    }
    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
        if(searchMenuItem!=null){
            Log.d("onNewIntent","searchMenuItem!=null");
            searchMenuItem.collapseActionView();
        }
    }
    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            searchVal = intent.getStringExtra(SearchManager.QUERY);
            Log.d("handleIntent", searchVal);
            getActionBar().setTitle(searchVal);
            fetchWorkerProfiles();
            //use the query to search your data somehow
        }
    }
    private void fetchWorkerProfiles() {
        HopeApp.getInstance().onPreExecute(SearchActivity.this);
        List<ParseQuery<ParseUser>> queries = new ArrayList<ParseQuery<ParseUser>>();

        //---search by name
        ParseQuery<ParseUser> q1 = ParseUser.getQuery();
        q1.whereContains("name", searchVal.toLowerCase());

        //---search by name
        ParseQuery<ParseUser> q2 = ParseUser.getQuery();
        q2.whereContains("address", searchVal.toLowerCase());



        queries.add(q1);
        queries.add(q2);

        ParseQuery<ParseUser> superQuery  = ParseQuery.or(queries);


        superQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                List <UserInfo> users_ = ( List <UserInfo>)(List<?>) parseUsers;
                HopeApp.pd.dismiss();
                if (e == null) {
                    if(users_.size()==0) {
                        tv_result.setVisibility(View.VISIBLE);
                        lv_items.setVisibility(View.GONE);
                        tv_result.setText("No results found for \""+searchVal+"\".");
                    }else{
                        lv_items.setVisibility(View.VISIBLE);
                        tv_result.setVisibility(View.GONE);
                        WorkerAdaptor clA = new WorkerAdaptor(SearchActivity.this ,users_, true );
                        lv_items.setAdapter(clA);

                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager =(SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchMenuItem = menu.findItem(R.id.search_searchActivity);
        searchView = (SearchView) searchMenuItem.getActionView();

        ComponentName cn = new ComponentName(this, SearchActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));


        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if(!queryTextFocused) {
                    searchMenuItem.collapseActionView();
                }
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_searchActivity) {

            searchMenuItem.expandActionView();
            searchView.setQuery(searchVal, false);
        }

        return super.onOptionsItemSelected(item);
    }
}
