package com.xplorer.hope.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xplorer.hope.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AddActivity extends Activity {
    @InjectView(R.id.rl_add_button)RelativeLayout rl_button;

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


    }

    private void showCatDialog(){
        final Dialog dialog = new Dialog(this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_category_list);
        // Set dialog title
        dialog.setTitle("Select Category");

        // set values for custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.textDialog);
        text.setText("Custom dialog Android example.");
        ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
        image.setImageResource(R.drawable.image0);

        dialog.show();

        Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
        // if decline button is clicked, close the custom dialog
        declineButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });
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
}
