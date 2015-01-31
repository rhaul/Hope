package com.xplorer.hope.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.xplorer.hope.R;
import com.xplorer.hope.adapter.WorkerAdaptor;
import com.xplorer.hope.config.HopeApp;
import com.xplorer.hope.object.Attendance;
import com.xplorer.hope.object.EWRelation;
import com.xplorer.hope.object.UserInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AttendanceActivity extends FragmentActivity {
    @InjectView(R.id.lv_attendance_ads)
    ListView lv_ads;

    @InjectView(R.id.tv_attendance_result)
    TextView tv_result;


    WorkerAdaptor wAdaptor;
    SimpleDateFormat formatter;
    CaldroidFragment dialogCaldroidFragment;
    List<UserInfo> users_;
    HashMap<String, String> attnd = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        ButterKnife.inject(this);

        getActionBar().setTitle(HopeApp.getInstance().getHindiLanguage("Worker's Attendance", null, null));
        Integer colorVal = HopeApp.CategoryColor.get(HopeApp.TITLES[4]);
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(colorVal)));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        formatter = new SimpleDateFormat("dd MMM yyyy");
        lv_ads.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showCalenderDialog(i);
            }
        });

        HopeApp.getInstance().onPreExecute(AttendanceActivity.this);
        fetchWorkerAll();
    }

    public void fetchWorkerAll() {


        String myWorkerId = ((UserInfo) ParseUser.getCurrentUser()).getObjectId();

        ParseQuery<EWRelation> query = ParseQuery.getQuery("EWRelation");
        query.whereEqualTo("employerID", myWorkerId);
        query.whereEqualTo("Approve", true);

        Log.d("hope fetchWorkerForThisWork", myWorkerId);

        query.findInBackground(new FindCallback<EWRelation>() {
            @Override
            public void done(List<EWRelation> parseObjects, ParseException e) {
                List<String> workerIds = new ArrayList<String>();
                if (e == null && parseObjects.size() > 0) {
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
        query.whereContainedIn("objectId", workerIds);

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                users_ = (List<UserInfo>) (List<?>) parseUsers;

                if (e == null) {

                    wAdaptor = new WorkerAdaptor(AttendanceActivity.this, users_, "NoBehavour", "NoWorkId");
                    lv_ads.setAdapter(wAdaptor);
                    tv_result.setVisibility(View.GONE);
                    lv_ads.setVisibility(View.VISIBLE);

                } else {
                    tv_result.setVisibility(View.VISIBLE);
                    lv_ads.setVisibility(View.GONE);
                }

                HopeApp.pd.dismiss();
            }
        });

    }

    private void showCalenderDialog(final int i) {


        HopeApp.getInstance().onPreExecute(AttendanceActivity.this);
        ParseQuery<Attendance> query = ParseQuery.getQuery("Attendance");
        query.whereEqualTo("workerID", users_.get(i).getObjectId());
        query.whereEqualTo("employerID", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<Attendance>() {
            @Override
            public void done(List<Attendance> parseObjects, ParseException e) {
                if (e == null && parseObjects.size() > 0) {
                    // Setup caldroid to use as dialog
                    dialogCaldroidFragment = new CaldroidFragment();
                    Bundle args = new Bundle();
                    args.putString("dialogTitle", users_.get(i).getName()+"'s Attendance");
                    dialogCaldroidFragment.setArguments(args);
                    dialogCaldroidFragment.setCaldroidListener(listener);
                    attnd = new HashMap<String, String>();
                    for (int j = 0; j < parseObjects.size(); j++) {
                        if (attnd.containsKey(parseObjects.get(j).getDate())) {
                            dialogCaldroidFragment.setBackgroundResourceForDate(R.color.MediumAquamarine,convertToDate(parseObjects.get(j).getDate()));
                            attnd.put(parseObjects.get(j).getDate(), attnd.get(parseObjects.get(j).getDate()) + "\nSlot 2: "+parseObjects.get(j).getTime() );
                        } else {
                            dialogCaldroidFragment.setBackgroundResourceForDate(R.color.green,convertToDate(parseObjects.get(j).getDate()));
                            attnd.put(parseObjects.get(j).getDate(), "Slot 1: "+parseObjects.get(j).getTime());
                        }
                    }

                    final String dialogTag = "CALDROID_DIALOG_FRAGMENT";

                    dialogCaldroidFragment.show(getSupportFragmentManager(), dialogTag);
                }
                HopeApp.pd.dismiss();
            }
        });

    }

    private Date convertToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    // Setup listener
    CaldroidListener listener = new CaldroidListener() {

        @Override
        public void onSelectDate(Date date, View view) {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            String key =df.format(date);
            Log.d("onSelectDate", key);
            if(attnd.containsKey(key)){
                Toast.makeText(getApplicationContext(), attnd.get(key), Toast.LENGTH_SHORT).show();

            }


        }

        @Override
        public void onChangeMonth(int month, int year) {
            /*String text = "month: " + month + " year: " + year;
            Toast.makeText(getApplicationContext(), text,
                    Toast.LENGTH_SHORT).show();*/
        }

        @Override
        public void onLongClickDate(Date date, View view) {/*
            Toast.makeText(getApplicationContext(),
                    "Long click " + formatter.format(date),
                    Toast.LENGTH_SHORT).show();*/
        }

        @Override
        public void onCaldroidViewCreated() {
            if (dialogCaldroidFragment.getLeftArrowButton() != null) {
                /*Toast.makeText(getApplicationContext(),
                        "Caldroid view is created", Toast.LENGTH_SHORT)
                        .show();*/
            }
        }

    };

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
