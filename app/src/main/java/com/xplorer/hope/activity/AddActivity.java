package com.xplorer.hope.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xplorer.hope.R;
import com.xplorer.hope.adapter.ClAdaptor;
import com.xplorer.hope.config.HopeApp;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AddActivity extends Activity implements View.OnClickListener {
    @InjectView(R.id.rl_add_button)RelativeLayout rl_button;
    @InjectView(R.id.iv_add_categoryImage)ImageView iv_categoryImage;

    @InjectView(R.id.et_add_description)EditText et_description;
    @InjectView(R.id.et_add_address)EditText et_address;
    @InjectView(R.id.et_add_phone)EditText et_phone;

    @InjectView(R.id.rb_add_jobTypeOneDay)RadioButton rb_jobTypeOneDay;
    @InjectView(R.id.rb_add_jobTypeMonthly)RadioButton rb_jobTypeMonthly;
    @InjectView(R.id.rb_add_jobTypeCustom)RadioButton rb_jobTypeCustom;

    @InjectView(R.id.tv_add_startingDate)TextView tv_startingDate;
    @InjectView(R.id.tv_add_endingDate)TextView tv_endingDate;

    @InjectView(R.id.rb_add_1day)RadioButton rb_1day;
    @InjectView(R.id.rb_add_2day)RadioButton rb_2day;

    @InjectView(R.id.tv_add_s1_startingTime)TextView tv_s1_startingTime;
    @InjectView(R.id.tv_add_s1_endingTime)TextView tv_s1_endingTime;

    @InjectView(R.id.tv_add_s2_startingTime)TextView tv_s2_startingTime;
    @InjectView(R.id.tv_add_s2_endingTime)TextView tv_s2_endingTime;

    @InjectView(R.id.et_add_wage)EditText et_wage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ButterKnife.inject(this);
        rl_button.setOnClickListener(this);

    }

    private void showCatDialog(){
        final Dialog dialog = new Dialog(this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_category_list);
        // Set dialog title
        dialog.setTitle("Select Category");
        ListView lvD = (ListView) dialog.findViewById(R.id.lv_category_list);
        ClAdaptor clA = new ClAdaptor(this);
        lvD.setAdapter(clA);
        lvD.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                iv_categoryImage.setVisibility(View.VISIBLE);
                Picasso.with(AddActivity.this).load(HopeApp.ImgUrl[0]).into(iv_categoryImage);
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
        switch (view.getId()){
            case R.id.rl_add_button: {
                showCatDialog();
                break;
            }

            default:
                break;
        }
    }
}
