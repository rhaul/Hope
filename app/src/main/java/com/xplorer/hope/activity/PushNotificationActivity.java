package com.xplorer.hope.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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

        ll_profile.addView(viewWorkAd);
        Bundle b = getIntent().getExtras();


        /*try {
            JSONObject jsonObject = new JSONObject(getIntent().getExtras().getString("com.parse.Data"));
            workerId = jsonObject.getString("senderId");
            workId = jsonObject.getString("workId");
            msg = jsonObject.getString("message");
            type = jsonObject.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();


        }*/
                workerId = "fxgEQj3UdP";
            workId = "4RTVJMVU64";
            msg = "Rahul Kumar has applied in response to your Job Advertisement ";
            type = "JARequest";


        tv_message.setText(msg);
        if(type.equalsIgnoreCase("JAReply")){
            b_accept.setVisibility(View.GONE);
            b_decline.setVisibility(View.GONE);
        }
        ParseQuery<ParseUser> fetchUser;
        fetchUser = ParseUser.getQuery();

        fetchUser.getInBackground(workerId, new GetCallback<ParseUser>() {
            public void done(ParseUser object, ParseException e) {
                if (e == null) {
                    workerObject = (UserInfo) object;
                    setWorkerProfile();
                }
            }
        });

        ParseQuery<WorkAd> fetchWorkAd = ParseQuery.getQuery("WorkAd");
        fetchWorkAd.getInBackground(workId, new GetCallback<WorkAd>() {
            @Override
            public void done(WorkAd workAd, ParseException e) {
                if (e == null) {
                    workAdObject = workAd;
                    setWorkAd();
                }
            }
        });
        b_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HopeApp.sendPushMessage(workerId,ParseUser.getCurrentUser().getObjectId(),workId,"Job Application Accepted","Your application for the following job has been accepted.","JAReply");
            }
        });
        b_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HopeApp.sendPushMessage(workerId,ParseUser.getCurrentUser().getObjectId(),workId,"Job Application Declined","Your application for the following job has been declined.","JAReply");
                HopeApp.removeEWRelation(workId,workerId,ParseUser.getCurrentUser().getObjectId());
            }
        });
    }

    private void setWorkAd() {

        if(((UserInfo) ParseUser.getCurrentUser()).getImageFile() != null ) Picasso.with(this).load(((UserInfo) ParseUser.getCurrentUser()).getImageFile().getUrl()).into(iv_employerPic);
        tv_description.setText(workAdObject.getDescription());
        tv_name.setText(workAdObject.getUserName());
        String dateType = workAdObject.getDateType() + " Job";
        if (workAdObject.getDateType().equalsIgnoreCase("One Day")) {
            dateType += "\n(" + workAdObject.getDateFrom() + ")";
        } else if (workAdObject.getDateType().equalsIgnoreCase("Custom")) {
            dateType += "\n(" + workAdObject.getDateFrom() + "-" + workAdObject.getDateTo() + ")";
        }
        String timeType = workAdObject.getTimeType();
        if (timeType.equalsIgnoreCase("Once a day")) {
            timeType += "\n(" + workAdObject.getS1StartingTime() + "-" + workAdObject.getS1EndingTime() + ")";
        } else {
            timeType += "\n(" + workAdObject.getS1StartingTime() + "-" + workAdObject.getS1EndingTime() + ")\n(" + workAdObject.getS2StartingTime() + "-" + workAdObject.getS2EndingTime() + ")";
        }
        tv_name.setText(workAdObject.getUserName());
        tv_jobType.setText(dateType);
        tv_timeType.setText(timeType);
        tv_wages.setText("₹" + workAdObject.getWageLowerLimit() + "-" + workAdObject.getWageHigherLimit());
        tv_phoneNo.setText(workAdObject.getPhoneNo());
        ll_phone.setVisibility(View.VISIBLE);
        tv_address.setText(workAdObject.getAddress());
        b_apply.setVisibility(View.GONE);
    }

    public void setWorkerProfile() {
        tv_worker_name.setText(workerObject.getName());
        tv_gender.setText(workerObject.getGender());

        if(workerObject.getImageFile() != null ) Picasso.with(this).load(workerObject.getImageFile().getUrl()).into(iv_workerPic);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        tv_age.setText("");
        try {
            Date bdate = dateFormat.parse(workerObject.getDob());
            tv_age.setText("Age: "+getAge(bdate.getYear()+1900, bdate.getMonth(), bdate.getDate()));
            Log.d("hope Age", bdate.getYear()+1900+":"+bdate.getMonth()+":"+bdate.getDate());

        } catch (java.text.ParseException e1) {
            e1.printStackTrace();
        }
        tv_addr.setText("Address: "+workerObject.getAddress());
        tv_wprofile_phoneno.setText(workerObject.getPhoneNo());


        String LicenseStr = "License: ", LangStr = "Language: ";
        if (workerObject.getlicenseFour()) {
            LicenseStr += "FourWheeler";
        }
        if (workerObject.getlicenseHeavy()) {
            LicenseStr += "| Heavy";
        }
        if (LicenseStr.equalsIgnoreCase("License: ")) {
            tv_license.setVisibility(View.GONE);
        } else {
            tv_license.setText(LicenseStr);
        }
        if (workerObject.getLangEnglish()) {
            LangStr += "English";
        }
        if (workerObject.getLangHindi()) {
            LangStr += "| Hindi";
        }
        if (LangStr.equalsIgnoreCase("Language: ")) {
            tv_lang.setVisibility(View.GONE);
        } else {
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
