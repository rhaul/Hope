package com.xplorer.hope.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.squareup.picasso.Picasso;
import com.xplorer.hope.R;
import com.xplorer.hope.adapter.CLAdapter;
import com.xplorer.hope.config.HopeApp;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AddActivity extends Activity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener {

    //views
    @InjectView(R.id.rl_add_button)
    RelativeLayout rl_button;
    @InjectView(R.id.iv_add_categoryImage)
    ImageView iv_categoryImage;
    @InjectView(R.id.tv_add_categoryName)
    TextView tv_categoryName;

    @InjectView(R.id.et_add_description)
    EditText et_description;
    @InjectView(R.id.et_add_address)
    EditText et_address;
    @InjectView(R.id.et_add_phone)
    EditText et_phone;

    @InjectView(R.id.rg_add_jobType)
    RadioGroup rg_jobType;
    @InjectView(R.id.rb_add_jobTypeOneDay)
    RadioButton rb_jobTypeOneDay;
    @InjectView(R.id.rb_add_jobTypeMonthly)
    RadioButton rb_jobTypeMonthly;
    @InjectView(R.id.rb_add_jobTypeCustom)
    RadioButton rb_jobTypeCustom;

    @InjectView(R.id.ll_add_customView)
    LinearLayout ll_customView;
    @InjectView(R.id.tv_add_startingDate)
    TextView tv_startingDate;
    @InjectView(R.id.tv_add_endingDate)
    TextView tv_endingDate;

    @InjectView(R.id.rg_add_timingType)
    RadioGroup rg_timingType;
    @InjectView(R.id.rb_add_1day)
    RadioButton rb_1day;
    @InjectView(R.id.rb_add_2day)
    RadioButton rb_2day;

    @InjectView(R.id.ll_add_s1_timings)
    LinearLayout ll_s1_timings;
    @InjectView(R.id.tv_add_s1_startingTime)
    TextView tv_s1_startingTime;
    @InjectView(R.id.tv_add_s1_endingTime)
    TextView tv_s1_endingTime;

    @InjectView(R.id.ll_add_s2_timings)
    LinearLayout ll_s2_timings;
    @InjectView(R.id.tv_add_s2_startingTime)
    TextView tv_s2_startingTime;
    @InjectView(R.id.tv_add_s2_endingTime)
    TextView tv_s2_endingTime;

    @InjectView(R.id.et_add_wage)
    EditText et_wage;

    // variables
    int startYear;
    int startMonth;
    int startDay;
    int endYear;
    int endMonth;
    int endDay;
    int s1StartingHour;
    int s1StartingMinute;
    String s1StartingTimeType = "";
    int s1EndingHour;
    int s1EndingMinute;
    String s1EndingTimeType = "";
    int s2StartingHour;
    int s2StartingMinute;
    String s2StartingTimeType = "";
    int s2EndingHour;
    int s2EndingMinute;
    String s2EndingTimeType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.inject(this);

        rl_button.setOnClickListener(this);
        rg_jobType.setOnCheckedChangeListener(this);
        tv_startingDate.setOnClickListener(this);
        tv_endingDate.setOnClickListener(this);
        rg_timingType.setOnCheckedChangeListener(this);
        tv_s1_startingTime.setOnClickListener(this);
        tv_s1_endingTime.setOnClickListener(this);
        tv_s2_startingTime.setOnClickListener(this);
        tv_s2_endingTime.setOnClickListener(this);
    }

    private void showDatePickerDialog(int i) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        if(i == 0){
            if(startDay != 0){
                year = startYear;
                month = startMonth;
                day = startDay;
            }
        }else{
            if(endDay != 0){
                year = endYear;
                month = endMonth;
                day = endDay;
            }
        }

        DatePickerDialog dialog = new DatePickerDialog(AddActivity.this,
                new SEDatePicker(i), year, month, day);
        dialog.show();
    }
    private void showTimePickerDialog(int i) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        if(i == 0){
            if(!s1StartingTimeType.equalsIgnoreCase("")){
                hour = s1StartingHour;
                minute = s1StartingMinute;
            }
        }else if(i == 1){
            if(!s1EndingTimeType.equalsIgnoreCase("")){
                hour = s1EndingHour;
                minute = s1EndingMinute;
            }
        }else if(i == 2){
            if(!s2StartingTimeType.equalsIgnoreCase("")){
                hour = s2StartingHour;
                minute = s2StartingMinute;
            }
        }else if(i == 3){
            if(!s2EndingTimeType.equalsIgnoreCase("")){
                hour = s2EndingHour;
                minute = s2EndingMinute;
            }
        }
        TimePickerDialog mTimePicker = new TimePickerDialog(AddActivity.this,new SETimePicker(i),hour,minute,false);
        mTimePicker.show();
    }

    private void showCatDialog() {
        final Dialog dialog = new Dialog(this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_category_list);
        // Set dialog title
        dialog.setTitle("Select Category");
        ListView lvD = (ListView) dialog.findViewById(R.id.lv_category_list);
        CLAdapter clA = new CLAdapter(this);
        lvD.setAdapter(clA);
        lvD.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                iv_categoryImage.setVisibility(View.VISIBLE);
                tv_categoryName.setVisibility(View.VISIBLE);
                Picasso.with(AddActivity.this).load(HopeApp.ImgUrl[i]).into(iv_categoryImage);
                tv_categoryName.setText(HopeApp.TITLES[i]);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_add_button: {
                showCatDialog();
                break;
            }
            case R.id.tv_add_startingDate: {
                showDatePickerDialog(0);
            }
            break;
            case R.id.tv_add_endingDate: {
                showDatePickerDialog(1);
            }
            break;
            case R.id.tv_add_s1_startingTime: {
                showTimePickerDialog(0);
            }
            break;
            case R.id.tv_add_s1_endingTime: {
                showTimePickerDialog(1);
            }
            break;
            case R.id.tv_add_s2_startingTime: {
                showTimePickerDialog(2);
            }
            break;
            case R.id.tv_add_s2_endingTime: {
                showTimePickerDialog(3);
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()){
            case R.id.rg_add_jobType:{
                switch (checkedId){
                    case R.id.rb_add_jobTypeOneDay:{
                        tv_endingDate.setVisibility(View.GONE);
                        ll_customView.setVisibility(View.VISIBLE);

                    }
                    break;
                    case R.id.rb_add_jobTypeMonthly:{
                        ll_customView.setVisibility(View.GONE);
                    }
                    break;
                    case R.id.rb_add_jobTypeCustom:{
                        tv_endingDate.setVisibility(View.VISIBLE);
                        ll_customView.setVisibility(View.VISIBLE);
                    }
                    break;
                }
            }
            break;
            case R.id.rg_add_timingType:{
                switch (checkedId){
                    case R.id.rb_add_1day:{
                        ll_s2_timings.setVisibility(View.GONE);
                    }
                    break;
                    case R.id.rb_add_2day:{
                        ll_s2_timings.setVisibility(View.VISIBLE);
                    }
                    break;
                }
            }
            break;


        }
    }

    public class SEDatePicker implements DatePickerDialog.OnDateSetListener {

        private int mType = 0;

        public SEDatePicker(int type) {
            mType = type;
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            if (mType == 0) {
                startYear = year;
                startMonth = monthOfYear;
                startDay = dayOfMonth;
            } else {
                endYear = year;
                endMonth = monthOfYear;
                endDay = dayOfMonth;
            }
            updateSEDateDisplay(mType);
        }
    }


    private class SETimePicker implements TimePickerDialog.OnTimeSetListener {
        private int mType = 0;

        public SETimePicker(int type) {
            mType = type;
        }
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String type = (hourOfDay < 12) ? "AM" : "PM";
            hourOfDay = (hourOfDay <= 12) ? hourOfDay : hourOfDay-12;
            hourOfDay = (hourOfDay == 0)? 12:hourOfDay;
            if (mType == 0) {
                s1StartingHour= hourOfDay;
                s1StartingMinute = minute;
                s1StartingTimeType = type;
            } else if(mType == 1) {
                s1EndingHour= hourOfDay;
                s1EndingMinute = minute;
                s1EndingTimeType = type;
            } else if(mType == 2) {
                s2StartingHour= hourOfDay;
                s2StartingMinute = minute;
                s2StartingTimeType = type;
            } else if(mType == 3) {
                s2EndingHour= hourOfDay;
                s2EndingMinute = minute;
                s2EndingTimeType = type;
            }
            updateSESlotTimeDisplay(mType);
        }
    }

    private void updateSESlotTimeDisplay(int mType) {
        if (mType == 0) {
            tv_s1_startingTime.setText( s1StartingHour + ":" + s1StartingMinute +" "+s1StartingTimeType);
        } else if(mType == 1) {
            tv_s1_endingTime.setText( s1EndingHour + ":" + s1EndingMinute+" "+s1EndingTimeType);
        } else if(mType == 2) {
            tv_s2_startingTime.setText( s2StartingHour + ":" + s2StartingMinute+" "+s2StartingTimeType);
        } else if(mType == 3) {
            tv_s2_endingTime.setText( s2EndingHour + ":" + s2EndingMinute+" "+s2EndingTimeType);
        }
    }

    private void updateSEDateDisplay(int mType) {
        if (mType == 0) {
            tv_startingDate.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(startMonth + 1).append("/").append(startDay).append("/")
                    .append(startYear).append(" "));
        } else {
            tv_endingDate.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(endMonth + 1).append("/").append(endDay).append("/")
                    .append(endYear).append(" "));
        }
    }
}
