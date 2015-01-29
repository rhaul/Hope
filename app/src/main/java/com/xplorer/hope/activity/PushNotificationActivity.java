package com.xplorer.hope.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.xplorer.hope.R;
import com.xplorer.hope.config.HopeApp;
import com.xplorer.hope.object.UserInfo;
import com.xplorer.hope.object.WorkAd;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PushNotificationActivity extends Activity {

    @InjectView(R.id.tv_push_message)
    TextView tv_message;
    @InjectView(R.id.ll_push_profile)
    LinearLayout ll_profile;
    @InjectView(R.id.b_push_accept)
    Button b_accept;
    @InjectView(R.id.b_push_decline)
    Button b_decline;
    @InjectView(R.id.ll_push_workad)
    LinearLayout ll_workad;
    @InjectView(R.id.ll_push_divider)
    LinearLayout ll_divider;



    String workerId;
    String workId;
    String msg;
    String type;
    UserInfo workerObject;
    WorkAd workAdObject;

    //  viewProfile
    View viewProfile;
    TextView tv_worker_name;
    TextView tv_gender;
    TextView tv_age;
    TextView tv_addr;
    TextView tv_wprofile_phoneno;
    TextView tv_license;
    TextView tv_lang;
    ImageView iv_workerPic;
    LinearLayout ll_interestTitle;

    LinearLayout ll_interest;
    LinearLayout ll_license;
    LinearLayout ll_language;
    LinearLayout ll_wprofile_addr;
    LinearLayout ll_wprofile_phone;


    // workAd
    View viewWorkAd;
    ImageView iv_employerPic;
    TextView tv_description;
    TextView tv_name;
    TextView tv_jobType;
    TextView tv_timeType;
    TextView tv_wages;
    TextView tv_address;
    TextView tv_phoneNo;
    Button b_apply;
    LinearLayout ll_phone;
    LinearLayout ll_addr;

    int infoObtained =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notification);

        ButterKnife.inject(this);

        viewProfile = getLayoutInflater().inflate(R.layout.item_worker_profile, null);
        tv_worker_name = (TextView) viewProfile.findViewById(R.id.tv_wprofile_name);
        tv_gender = (TextView) viewProfile.findViewById(R.id.tv_wprofile_gender);
        tv_age = (TextView) viewProfile.findViewById(R.id.tv_wprofile_age);
        tv_addr = (TextView) viewProfile.findViewById(R.id.tv_wprofile_addr);
        tv_wprofile_phoneno = (TextView) viewProfile.findViewById(R.id.tv_wprofile_phoneno);
        ll_interest = (LinearLayout) viewProfile.findViewById(R.id.ll_wprofile_interest);
        tv_license = (TextView) viewProfile.findViewById(R.id.tv_wprofile_license);
        tv_lang = (TextView) viewProfile.findViewById(R.id.tv_wprofile_lang);
        iv_workerPic = (ImageView) viewProfile.findViewById(R.id.iv_wprofile_img);
        ll_interestTitle = (LinearLayout)  viewProfile.findViewById(R.id.ll_wprofile_interestTitle);
        ll_language = (LinearLayout) viewProfile.findViewById(R.id.ll_wprofile_language);
        ll_license = (LinearLayout) viewProfile.findViewById(R.id.ll_wprofile_license);
        ll_wprofile_addr = (LinearLayout) viewProfile.findViewById(R.id.ll_wprofile_addr);
        ll_wprofile_phone = (LinearLayout) viewProfile.findViewById(R.id.ll_wprofile_phone);

        ll_profile.addView(viewProfile);

        viewWorkAd = getLayoutInflater().inflate(R.layout.item_ad, null);
        iv_employerPic = (ImageView) viewWorkAd.findViewById(R.id.iv_ad_employerPic);
        tv_description = (TextView) viewWorkAd.findViewById(R.id.tv_ad_description);
        tv_name = (TextView) viewWorkAd.findViewById(R.id.tv_ad_name);
        tv_jobType = (TextView) viewWorkAd.findViewById(R.id.tv_ad_jobType);
        tv_timeType = (TextView) viewWorkAd.findViewById(R.id.tv_ad_timeType);
        ll_phone = (LinearLayout) viewWorkAd.findViewById(R.id.ll_ad_phone);
        tv_wages = (TextView) viewWorkAd.findViewById(R.id.tv_ad_wages);
        tv_address = (TextView) viewWorkAd.findViewById(R.id.tv_ad_address);
        tv_phoneNo = (TextView) viewWorkAd.findViewById(R.id.tv_ad_phoneNo);
        b_apply = (Button) viewWorkAd.findViewById(R.id.b_ad_apply);
        ll_addr = (LinearLayout) viewWorkAd.findViewById(R.id.ll_ad_addr);

        ll_workad.addView(viewWorkAd);
        Bundle b = getIntent().getExtras();


        try {
            JSONObject jsonObject = new JSONObject(getIntent().getExtras().getString("com.parse.Data"));
            workerId = jsonObject.getString("senderId");
            workId = jsonObject.getString("workId");
            msg = jsonObject.getString("message");
            type = jsonObject.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }



        tv_message.setText(msg);
        if(type.equalsIgnoreCase("JAReply")){
            b_accept.setText("My Schedule");
            ll_divider.setVisibility(View.GONE);
            b_decline.setVisibility(View.GONE);

            //--- My Schedule Activity
            b_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(PushNotificationActivity.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                    return;
                }
            });
        }else{
            b_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HopeApp.sendPushMessage(workerId,ParseUser.getCurrentUser().getObjectId(),workId,"Job Application Accepted","Your application for the following job has been accepted.","JAReply");
                    HopeApp.setEWRelationTrue(workId, workerId, ParseUser.getCurrentUser().getObjectId());
                    Intent i = new Intent(PushNotificationActivity.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                    return;
                }
            });
            b_decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HopeApp.sendPushMessage(workerId,ParseUser.getCurrentUser().getObjectId(),workId,"Job Application Declined","Your application for the following job has been declined.","JAReply");
                    HopeApp.removeEWRelation(workId,workerId,ParseUser.getCurrentUser().getObjectId());
                    Intent i = new Intent(PushNotificationActivity.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                    return;
                }
            });
        }


        HopeApp.getInstance().onPreExecute(PushNotificationActivity.this);
        fetchWorker();
        fetchWork();

    }

    public void fetchWorker(){
        ParseQuery<ParseUser> fetchUser;
        fetchUser = ParseUser.getQuery();
        fetchUser.getInBackground(workerId, new GetCallback<ParseUser>() {
            public void done(ParseUser object, ParseException e) {

                if (e == null) {
                    infoObtained++;
                    if(infoObtained==2) HopeApp.pd.dismiss();

                    workerObject = (UserInfo) object;
                    setWorkerProfile();
                }else{
                    fetchWorker();
                }
            }
        });
    }
    public void fetchWork(){
        ParseQuery<WorkAd> fetchWorkAd = ParseQuery.getQuery("WorkAd");
        fetchWorkAd.getInBackground(workId, new GetCallback<WorkAd>() {
            @Override
            public void done(WorkAd workAd, ParseException e) {
                if (e == null) {
                    infoObtained++;
                    if(infoObtained==2) HopeApp.pd.dismiss();
                    workAdObject = workAd;
                    setWorkAd();
                }else{
                    fetchWork();
                }
            }
        });
    }

    private void setWorkAd() {

        if(((UserInfo) ParseUser.getCurrentUser()).getImageFile() != null ) Picasso.with(this).load(((UserInfo) ParseUser.getCurrentUser()).getImageFile().getUrl()).into(iv_employerPic);

        ParseQuery<ParseUser>  query = ParseUser.getQuery();

        query.getInBackground(workAdObject.getUserId(), new GetCallback<ParseUser>() {
            public void done(ParseUser object, ParseException e) {
                UserInfo usr = (UserInfo) object;

                if (usr.getImageFile() != null) {
                    Picasso.with(PushNotificationActivity.this).load(usr.getImageFile().getUrl()).error(R.drawable.ic_launcher).into(iv_employerPic);
                } else {
                    Picasso.with(PushNotificationActivity.this).load(R.drawable.defaultuser).into(iv_employerPic);
                }
            }
        });


        String dateType = workAdObject.getDateType() + " Job";
        if (workAdObject.getDateType().equalsIgnoreCase("One Day")) {
            dateType += "\nOn: " + workAdObject.getDateFrom();
        } else if (workAdObject.getDateType().equalsIgnoreCase("Custom")) {
            dateType += "\nFrom: " + workAdObject.getDateFrom() + "\nTo  : " + workAdObject.getDateTo();
        }
        String timeType = workAdObject.getTimeType();
        if (timeType.equalsIgnoreCase("Once a day")) {
            timeType += "\n" + workAdObject.getS1StartingTime() + "-" + workAdObject.getS1EndingTime() + "";
        } else {
            timeType += "\n" + workAdObject.getS1StartingTime() + "-" + workAdObject.getS1EndingTime() + "\n" + workAdObject.getS2StartingTime() + "-" + workAdObject.getS2EndingTime() + "";
        }

        ll_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String map;
                if(workAdObject.getAddressGP()!=null){
                    String addr= workAdObject.getAddressGP().getLatitude()+","+workAdObject.getAddressGP().getLatitude();
                    map= "http://maps.google.com/maps?q="+addr;
                }else{
                    map = "http://maps.google.co.in/maps?q=" + workAdObject.getAddress();

                }
                Intent int_ = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(map));
                startActivity(int_);
            }
        });
        ll_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" +  workAdObject.getPhoneNo()));
                startActivity(callIntent);
            }
        });
        tv_name.setText(HopeApp.getInstance().getUpperCaseString(workAdObject.getCategory()));
        tv_description.setText(HopeApp.getInstance().getUpperCaseString(workAdObject.getDescription()));
        tv_address.setText(HopeApp.getInstance().getUpperCaseString(workAdObject.getAddress()));

        tv_jobType.setText(dateType);
        tv_timeType.setText(timeType);
        tv_wages.setText(workAdObject.getWageLowerLimit() + "-" + workAdObject.getWageHigherLimit());
        tv_phoneNo.setText(workAdObject.getPhoneNo());
        ll_phone.setVisibility(View.VISIBLE);
        b_apply.setVisibility(View.GONE);
    }


    public void setWorkerProfile() {
        tv_worker_name.setText(HopeApp.getInstance().getUpperCaseString(workerObject.getName()));
        tv_gender.setText(workerObject.getGender());

        if(workerObject.getImageFile() != null ) Picasso.with(this).load(workerObject.getImageFile().getUrl()).into(iv_workerPic);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        tv_age.setText("");
        try {
            Date bdate = dateFormat.parse(workerObject.getDob());
            tv_age.setText("Age: "+getAge(bdate.getYear()+1900, bdate.getMonth(), bdate.getDate()));

        } catch (java.text.ParseException e1) {
            e1.printStackTrace();
        }
        tv_addr.setText(HopeApp.getInstance().getUpperCaseString(workerObject.getAddress()));
        tv_wprofile_phoneno.setText(workerObject.getPhoneNo());

        ll_wprofile_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String map;
                if(workerObject.getAddressGP()!=null){
                    String addr= workerObject.getAddressGP().getLatitude()+","+workerObject.getAddressGP().getLatitude();
                    map= "http://maps.google.com/maps?q="+addr;
                }else{
                    map = "http://maps.google.co.in/maps?q=" + workerObject.getAddress();

                }
                Intent int_ = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(map));
                startActivity(int_);
            }
        });
        ll_wprofile_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" +  workerObject.getPhoneNo()));
                startActivity(callIntent);
            }
        });

        String LicenseStr = "License: ", LangStr = "Language: ";
        if (workerObject.getlicenseFour()) {
            LicenseStr += "Four Wheeler";
        }
        if (workerObject.getlicenseHeavy()) {
            if(LicenseStr.equalsIgnoreCase("License: ")) LicenseStr +="Heavy";
            else LicenseStr += " | Heavy";
        }
        if (LicenseStr.equalsIgnoreCase("License: ")) {
            ll_license.setVisibility(View.GONE);
        } else {
            ll_license.setVisibility(View.VISIBLE);
            tv_license.setText(LicenseStr);
        }
        if (workerObject.getLangEnglish()) {
            LangStr += "English";
        }
        if (workerObject.getLangHindi()) {
            if (LangStr.equalsIgnoreCase("Language: "))LangStr += "Hindi";
            else LangStr += " | Hindi";
        }
        if (LangStr.equalsIgnoreCase("Language: ")) {
            ll_language.setVisibility(View.GONE);
        } else {
            ll_language.setVisibility(View.VISIBLE);
            tv_lang.setText(LangStr);
        }

        if (workerObject.getCooking()) {
            addInterest("Cooking", "₹ "+String.valueOf(workerObject.getCookingExpWage()));
            ll_interestTitle.setVisibility(View.VISIBLE);
        }
        if (workerObject.getClothWashing()) {
            addInterest("Washing", "₹ "+String.valueOf(workerObject.getClothWashingExpWage()));
            ll_interestTitle.setVisibility(View.VISIBLE);
        }
        if (workerObject.getHouseCleaning()) {
            addInterest("House Cleaning", "₹ "+String.valueOf(workerObject.getHouseCleaningExpWage()));
            ll_interestTitle.setVisibility(View.VISIBLE);
        }
        if (workerObject.getDishWashing()) {
            addInterest("Dish Washing", "₹ "+String.valueOf(workerObject.getDishWashingExpWage()));
            ll_interestTitle.setVisibility(View.VISIBLE);
        }
        if (workerObject.getConstruction()) {
            addInterest("Construction", "₹ "+String.valueOf(workerObject.getConstructionExpWage()));
            ll_interestTitle.setVisibility(View.VISIBLE);
        }
        if (workerObject.getWallpaint()) {
            addInterest("Wall Paint", "₹ "+String.valueOf(workerObject.getWallpaintExpWage()));
            ll_interestTitle.setVisibility(View.VISIBLE);
        }
        if (workerObject.getDriver()) {
            addInterest("Driver", "₹ "+String.valueOf(workerObject.getDriverExpWage()));
            ll_interestTitle.setVisibility(View.VISIBLE);
        }
        if (workerObject.getGuard()) {
            addInterest("Guard", "₹ "+String.valueOf(workerObject.getGuardExpWage()));
            ll_interestTitle.setVisibility(View.VISIBLE);
        }
        if (workerObject.getShopWorker()) {
            addInterest("Shop work", "₹ "+String.valueOf(workerObject.getShopWorkerExpWage()));
            ll_interestTitle.setVisibility(View.VISIBLE);
        }
        if (workerObject.getGardening()) {
            addInterest("Gardening", "₹ "+String.valueOf(workerObject.getGardeningExpWage()));
            ll_interestTitle.setVisibility(View.VISIBLE);
        }
        if (workerObject.getMiscellaneous()) {
            addInterest("Cooking", "₹ "+String.valueOf(workerObject.getMiscellaneousExpWage()));
            ll_interestTitle.setVisibility(View.VISIBLE);
        }


    }

    public void addInterest(String Interested, String expectedWage) {
        View viewItemInterest = getLayoutInflater().inflate(R.layout.item_interest, null);
        TextView tvInterested = (TextView) viewItemInterest.findViewById(R.id.tv_interest_name);
        TextView tvWage = (TextView) viewItemInterest.findViewById(R.id.tv_interest_wage);
        tvInterested.setText(Interested);
        tvWage.setText(expectedWage);

        ll_interest.addView(viewItemInterest);
    }

    public String getAge(int _year, int _month, int _day) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(_year, _month, _day);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if (a < 0) a=0;
        return a + "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_push_notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}