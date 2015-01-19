package com.xplorer.hope.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.xplorer.hope.R;
import com.xplorer.hope.config.HopeApp;
import com.xplorer.hope.object.UserInfo;

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
        if (id == R.id.action_settings) {
            return true;
        }

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
        UserInfo user = (UserInfo) UserInfo.getCurrentUser();
        if(user==null){
            startActivity(new Intent(this,SignUpActivity.class).putExtra("from", "singup"));
            finish();
        }else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
