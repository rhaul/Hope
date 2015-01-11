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

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SelectLangActivity extends Activity implements View.OnClickListener {

    @InjectView(R.id.b_selectLang_hindi)Button b_hindi;
    @InjectView(R.id.b_selectLang_english)Button b_english;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lang);
        ButterKnife.inject(this);
        b_hindi.setOnClickListener(this);
        b_english.setOnClickListener(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_lang, menu);
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
            case R.id.b_selectLang_hindi:{
                HopeApp.getInstance().setSPString(HopeApp.SELECTED_LANGUAGE,"hindi");
            }
            break;

            case R.id.b_selectLang_english:{
                HopeApp.getInstance().setSPString(HopeApp.SELECTED_LANGUAGE,"english");
            }
            break;
        }
        startActivity(new Intent(this,SelectUserTypeActivity.class));
        finish();
    }
}
