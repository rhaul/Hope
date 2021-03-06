package com.xplorer.hope.activity;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.xplorer.hope.R;
import com.xplorer.hope.config.HopeApp;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SelectUserTypeActivity extends Activity implements View.OnClickListener {

    @InjectView(R.id.b_selectUT_worker)Button b_worker;
    @InjectView(R.id.b_selectUT_employer)Button b_employer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);
        ButterKnife.inject(this);
        b_worker.setOnClickListener(this);
        b_employer.setOnClickListener(this);


        getActionBar().setTitle(HopeApp.getInstance().getHindiLanguage("Choose Profession Type", null, null));
        Integer colorVal =R.color.BlueViolet;

        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(colorVal)));

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        if (HopeApp.getSPString(HopeApp.SELECTED_LANGUAGE).equalsIgnoreCase("hindi")){
            b_worker.setText("कर्मचारी");
            b_employer.setText("नियोक्ता");

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_user_type, menu);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.b_selectUT_worker:{
                HopeApp.getInstance().setSPString(HopeApp.SELECTED_USER_TYPE,"worker");
            }
            break;

            case R.id.b_selectUT_employer:{
                HopeApp.getInstance().setSPString(HopeApp.SELECTED_USER_TYPE,"employer");
            }
            break;
        }
        finish();
        return;
    }
}
