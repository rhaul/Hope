package com.xplorer.hope.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.xplorer.hope.R;
import com.xplorer.hope.service.ConnectionDetector;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NoInternetActivity extends Activity {

    @InjectView(R.id.ll_nointernet_body)
    LinearLayout ll_body;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        ButterKnife.inject(this);
        ll_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

                Boolean isInternetPresent = cd.isConnectingToInternet();
                if(isInternetPresent){
                    finish();
                    return;
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_no_internet, menu);
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
