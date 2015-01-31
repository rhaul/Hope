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

public class EmpolyerActivity extends Activity {
    @InjectView(R.id.lv_employer_ads)
    ListView lv_ads;

    @InjectView(R.id.tv_employer_result)
    TextView tv_result;



    public EmployerAdaptor empAdaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empolyer);
        ButterKnife.inject(this);


        getActionBar().setTitle(HopeApp.getInstance().getHindiLanguage("My Employers", null, null));
        Integer colorVal =  HopeApp.CategoryColor.get(HopeApp.TITLES[4]);
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(colorVal)));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        /*lv_ads.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                iv_categoryImage.setVisibility(View.VISIBLE);
                tv_categoryName.setVisibility(View.VISIBLE);
                Picasso.with(AddActivity.this).load(HopeApp.ImgUrl[i]).into(iv_categoryImage);
                tv_categoryName.setText(HopeApp.TITLES[i]);
                dialog.dismiss();
            }
        });*/
    }


    @Override
    protected void onResume() {
        super.onResume();
        HopeApp.getInstance().onPreExecute(EmpolyerActivity.this);
        if(HopeApp.myWorksIds==null || HopeApp.myWorksIds.size()==0) getMyWorkids();
        else fetchWorks();

    }

    private void fetchWorks() {

        ParseQuery<WorkAd> query = ParseQuery.getQuery("WorkAd");
        query.whereContainedIn("objectId", HopeApp.myWorksIds.values());
        Log.d("HopeApp.myWorksIds.values().toString()", HopeApp.myWorksIds.values().toString());


        query.orderByDescending("category");
        query.findInBackground(new FindCallback<WorkAd>() {
            @Override
            public void done(List<WorkAd> workAds, ParseException e) {
                Log.d("workAds", String.valueOf(workAds.size()));
                HopeApp.pd.dismiss();
                if (e == null && workAds.size()>0) {
                    empAdaptor= new EmployerAdaptor(EmpolyerActivity.this, 0, workAds);
                    //sectionAdapter= new SimpleSectionAdapter<WorkAd>
                     //      (EmpolyerActivity.this,empAdaptor, R.layout.category_header, R.id.tv_catHeader_catname, new CategorySectionizer());
                    lv_ads.setAdapter(empAdaptor);
                    tv_result.setVisibility(View.GONE);
                    lv_ads.setVisibility(View.VISIBLE);

                }else{
                    tv_result.setVisibility(View.VISIBLE);
                    lv_ads.setVisibility(View.GONE);
                }

            }
        });
    }
    public void getMyWorkids(){

        String myWorkerId= ((UserInfo) ParseUser.getCurrentUser()).getObjectId();

        ParseQuery<EWRelation> query = ParseQuery.getQuery("EWRelation");
        query.whereEqualTo("userID", myWorkerId);

        Log.d("hope getMyWorkids", myWorkerId);

        query.findInBackground(new FindCallback<EWRelation>() {
            @Override
            public void done(List<EWRelation> parseObjects, ParseException e) {

                if (e == null && parseObjects.size()>0) {
                    Log.d("hope getMyWorkids(done)", String.valueOf(parseObjects.size()));
                    for (int i = 0; i < parseObjects.size(); i++) {
                        if(parseObjects.get(i).getApprove()==true) {
                            HopeApp.myWorksIds.put(parseObjects.get(i).getWorkID(), parseObjects.get(i).getWorkID());
                            HopeApp.myEmployerIds.put(parseObjects.get(i).getEmployerID(), parseObjects.get(i).getEmployerID());
                        }else{
                            HopeApp.myPendingWorksIds.put(parseObjects.get(i).getWorkID(), parseObjects.get(i).getWorkID());
                            HopeApp.myPendingEmployerIds.put(parseObjects.get(i).getEmployerID(), parseObjects.get(i).getEmployerID());

                        }
                        Log.d("hope getMyWorkids(workId)", parseObjects.get(i).getWorkID());

                    }

                    fetchWorks();
                }else{
                    tv_result.setVisibility(View.VISIBLE);
                    lv_ads.setVisibility(View.GONE);
                    HopeApp.pd.dismiss();
                }
            }
        });


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_empolyer, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
