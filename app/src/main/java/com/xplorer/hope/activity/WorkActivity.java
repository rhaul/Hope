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
import com.xplorer.hope.adapter.WorkAdaptor;
import com.xplorer.hope.config.HopeApp;
import com.xplorer.hope.object.UserInfo;
import com.xplorer.hope.object.WorkAd;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WorkActivity extends Activity {
    @InjectView(R.id.lv_work_itemList)
    ListView lv_itemList;
@InjectView(R.id.tv_work_result)
    TextView tv_result;

    WorkAdaptor workAdptr;

    List<WorkAd> myWorkAds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        ButterKnife.inject(this);
        getActionBar().setTitle("My Job Ads");
        Integer colorVal = HopeApp.CategoryColor.get(HopeApp.TITLES[1]);
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(colorVal)));
        getMyWorkids();
        HopeApp.getInstance().onPreExecute(WorkActivity.this);
    }


    public void getMyWorkids(){
        String myWorkerId= ((UserInfo) ParseUser.getCurrentUser()).getObjectId();

        ParseQuery<WorkAd> query = ParseQuery.getQuery("WorkAd");
        query.whereEqualTo("userID", myWorkerId);

        Log.d("hope getMyWorkids", myWorkerId);

        query.findInBackground(new FindCallback<WorkAd>() {
            @Override
            public void done(List<WorkAd> parseObjects, ParseException e) {

                HopeApp.pd.dismiss();
                if (e == null) {
                    myWorkAds=parseObjects;
                    workAdptr = new WorkAdaptor(WorkActivity.this, myWorkAds);
                    lv_itemList.setAdapter(workAdptr);
                    Log.d("hope getMyWorkids(done)", String.valueOf(myWorkAds.size()));
                    tv_result.setVisibility(View.GONE);
                    lv_itemList.setVisibility(View.VISIBLE);
                }else{
                    tv_result.setVisibility(View.VISIBLE);
                    lv_itemList.setVisibility(View.GONE);
                }
            }
        });


    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_work, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
