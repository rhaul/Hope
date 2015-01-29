package com.xplorer.hope.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.xplorer.hope.R;
import com.xplorer.hope.adapter.WorkerAdaptor;
import com.xplorer.hope.config.HopeApp;
import com.xplorer.hope.object.EWRelation;
import com.xplorer.hope.object.UserInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AttendanceActivity extends Activity {
    @InjectView(R.id.lv_attendance_ads)
    ListView lv_ads;

    @InjectView(R.id.tv_attendance_result)
    TextView tv_result;



WorkerAdaptor wAdaptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        ButterKnife.inject(this);


        getActionBar().setTitle("Attendance");
        Integer colorVal =  HopeApp.CategoryColor.get(HopeApp.TITLES[4]);
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(colorVal)));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        lv_ads.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

    }

    public void fetchWorkerAll(){
        String myWorkerId= ((UserInfo) ParseUser.getCurrentUser()).getObjectId();

        ParseQuery<EWRelation> query = ParseQuery.getQuery("EWRelation");
        query.whereEqualTo("employerID", myWorkerId);
        query.whereEqualTo("Approve", true);

        Log.d("hope fetchWorkerForThisWork", myWorkerId);

        query.findInBackground(new FindCallback<EWRelation>() {
            @Override
            public void done(List<EWRelation> parseObjects, ParseException e) {
                List<String> workerIds= new ArrayList<String>();
                if (e == null && parseObjects.size()>0) {
                    for (int i = 0; i < parseObjects.size(); i++) {
                        workerIds.add(parseObjects.get(i).getUserID());
                    }
                    fetchWorkerProfiles(workerIds);
                    Log.d("hope fetchWorkerForThisWork(done)", String.valueOf(parseObjects.size()));
                }
            }
        });
    }
    private void fetchWorkerProfiles(List<String> workerIds) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContainedIn("objectId",workerIds );

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                List <UserInfo> users_ = ( List <UserInfo>)(List<?>) parseUsers;

                if (e == null) {

                    wAdaptor= new WorkerAdaptor(AttendanceActivity.this,users_,"NoBehavour", "NoWorkId");
                    lv_ads.setAdapter(wAdaptor);
                    tv_result.setVisibility(View.GONE);
                    lv_ads.setVisibility(View.VISIBLE);

                }else{
                    tv_result.setVisibility(View.VISIBLE);
                    lv_ads.setVisibility(View.GONE);
                }
            }
        });

    }

    private void showCalenderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Attendance");
        View CalView = getLayoutInflater().inflate(R.layout.dialog_calender, null);
        builder.setView(CalView);

        final CalendarView cv=  (CalendarView) CalView.findViewById(R.id.cv_calender);

        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_attendance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
