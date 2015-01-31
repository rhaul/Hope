package com.xplorer.hope.activity;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.xplorer.hope.R;
import com.xplorer.hope.adapter.EmployerAdaptor;
import com.xplorer.hope.config.HopeApp;
import com.xplorer.hope.object.EWRelation;
import com.xplorer.hope.object.UserInfo;
import com.xplorer.hope.object.WorkAd;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PendingActivity extends Activity {
    @InjectView(R.id.lv_pending_ads)
    ListView lv_ads;

    @InjectView(R.id.tv_pending_result)
    TextView tv_result;

    public EmployerAdaptor empAdaptor;
    public boolean isWorker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);

        ButterKnife.inject(this);
        getActionBar().setTitle(HopeApp.getInstance().getHindiLanguage("Pending Requests", null, null));
        Integer colorVal = HopeApp.CategoryColor.get(HopeApp.TITLES[2]);
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(colorVal)));


        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
     protected void onResume() {
        super.onResume();
        HopeApp.getInstance().onPreExecute(PendingActivity.this);
        if (HopeApp.myWorksIds == null || HopeApp.myWorksIds.size() == 0) getMyWorkids();
        else fetchWorks();

    }

    private void fetchWorks() {

        ParseQuery<WorkAd> query = ParseQuery.getQuery("WorkAd");
        query.whereContainedIn("objectId", HopeApp.myPendingWorksIds.values());
        Log.d("HopeApp.myWorksIds.values().toString()", HopeApp.myWorksIds.values().toString());


        query.orderByDescending("category");
        query.findInBackground(new FindCallback<WorkAd>() {
            @Override
            public void done(List<WorkAd> workAds, ParseException e) {
                Log.d("workAds", String.valueOf(workAds.size()));
                HopeApp.pd.dismiss();
                if (e == null  && workAds.size()>0) {
                    empAdaptor = new EmployerAdaptor(PendingActivity.this, 0, workAds);

                    lv_ads.setAdapter(empAdaptor);
                    tv_result.setVisibility(View.GONE);
                    lv_ads.setVisibility(View.VISIBLE);

                } else {
                    tv_result.setVisibility(View.VISIBLE);
                    lv_ads.setVisibility(View.GONE);
                }

            }
        });
    }

    public void getMyWorkids() {

        String myWorkerId = ((UserInfo) ParseUser.getCurrentUser()).getObjectId();

        ParseQuery<EWRelation> query = ParseQuery.getQuery("EWRelation");
        query.whereEqualTo("userID", myWorkerId);

        Log.d("hope getMyWorkids", myWorkerId);

        query.findInBackground(new FindCallback<EWRelation>() {
            @Override
            public void done(List<EWRelation> parseObjects, ParseException e) {

                if (e == null) {
                    Log.d("hope getMyWorkids(done)", String.valueOf(parseObjects.size()));
                    for (int i = 0; i < parseObjects.size(); i++) {
                        if (parseObjects.get(i).getApprove() == true) {
                            HopeApp.myWorksIds.put(parseObjects.get(i).getWorkID(), parseObjects.get(i).getWorkID());
                            HopeApp.myEmployerIds.put(parseObjects.get(i).getEmployerID(), parseObjects.get(i).getEmployerID());
                        } else {
                            HopeApp.myPendingWorksIds.put(parseObjects.get(i).getWorkID(), parseObjects.get(i).getWorkID());
                            HopeApp.myPendingEmployerIds.put(parseObjects.get(i).getEmployerID(), parseObjects.get(i).getEmployerID());

                        }
                        Log.d("hope getMyWorkids(workId)", parseObjects.get(i).getWorkID());

                    }

                    fetchWorks();
                }
            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pending, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}
