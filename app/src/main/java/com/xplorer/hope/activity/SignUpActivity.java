package com.xplorer.hope.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.squareup.picasso.Picasso;
import com.xplorer.hope.R;
import com.xplorer.hope.config.HopeApp;
import com.xplorer.hope.object.UserInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignUpActivity extends Activity implements View.OnClickListener {
    private static final int SELECT_LOCATION = 1;
    private static final int SELECT_FILE = 2;

    // Activity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener
    @InjectView(R.id.et_sign_name)
    EditText et_name;
    @InjectView(R.id.tv_sign_dob)
    TextView tv_dob;
    @InjectView(R.id.et_sign_addr)
    EditText et_addr;
    @InjectView(R.id.et_sign_num)
    EditText et_num;

    @InjectView(R.id.rg_sign_gender)
    RadioGroup rg_gender;
    @InjectView(R.id.rb_sign_male)
    RadioButton rb_male;
    @InjectView(R.id.rb_sign_female)
    RadioButton rb_female;

    @InjectView(R.id.cb_sign_DishWashing)
    CheckBox cb_dishWashing;
    @InjectView(R.id.et_sign_DishWashingExpWage)
    EditText et_DishWashingExpWage;

    @InjectView(R.id.cb_sign_HouseCleaning)
    CheckBox cb_HouseCleaning;
    @InjectView(R.id.et_sign_HouseCleaningExpWage)
    EditText et_HouseCleaningExpWage;

    @InjectView(R.id.cb_sign_ClothWashing)
    CheckBox cb_ClothWashing;
    @InjectView(R.id.et_sign_ClothWashingExpWage)
    EditText et_ClothWashingExpWage;

    @InjectView(R.id.cb_sign_Cooking)
    CheckBox cb_Cooking;
    @InjectView(R.id.et_sign_CookingExpWage)
    EditText et_CookingExpWage;

    @InjectView(R.id.cb_sign_Construction)
    CheckBox cb_Construction;
    @InjectView(R.id.et_sign_ConstructionExpWage)
    EditText et_ConstructionExpWage;

    @InjectView(R.id.cb_sign_Wallpaint)
    CheckBox cb_Wallpaint;
    @InjectView(R.id.et_sign_WallpaintExpWage)
    EditText et_WallpaintExpWage;

    @InjectView(R.id.cb_sign_Driver)
    CheckBox cb_Driver;
    @InjectView(R.id.et_sign_DriverExpWage)
    EditText et_DriverExpWage;

    @InjectView(R.id.ll_signUp_license)
    LinearLayout ll_license;
    @InjectView(R.id.cb_sign_licenseFourwheeler)
    CheckBox cb_licenseFourwheeler;
    @InjectView(R.id.cb_sign_licenseHeavy)
    CheckBox cb_licenseHeavy;

    @InjectView(R.id.cb_sign_Guard)
    CheckBox cb_Guard;
    @InjectView(R.id.et_sign_GuardExpWage)
    EditText et_GuardExpWage;

    @InjectView(R.id.cb_sign_ShopWorker)
    CheckBox cb_ShopWorker;
    @InjectView(R.id.et_sign_ShopWorkerExpWage)
    EditText et_ShopWorkerExpWage;

    @InjectView(R.id.ll_signUp_language)
    LinearLayout ll_language;
    @InjectView(R.id.cb_sign_LanguageEnglish)
    CheckBox cb_LanguageEnglish;
    @InjectView(R.id.cb_sign_LanguageHindi)
    CheckBox cb_LanguageHindi;

    @InjectView(R.id.cb_sign_Gardening)
    CheckBox cb_Gardening;
    @InjectView(R.id.et_sign_GardeningExpWage)
    EditText et_GardeningExpWage;

    @InjectView(R.id.cb_sign_Miscellaneous)
    CheckBox cb_Miscellaneous;
    @InjectView(R.id.et_sign_MiscellaneousExpWage)
    EditText et_MiscellaneousExpWage;

    @InjectView(R.id.iv_sign_profilePhoto)
    ImageView iv_profilePhoto;

    @InjectView(R.id.ll_signUp_qualDialogue)
    LinearLayout ll_qualDialogue;

    @InjectView(R.id.b_sign_map)
    Button b_map;

    Menu menu;
    MenuItem saveBtn;
    Context mcontex;

    int dobYear;
    int dobMonth;
    int dobDay;
    String PhoneNumber;
    boolean isSaveClicked = false;
    byte[] dataImage = null;
    UserInfo usr;
    Boolean isNull = false;
    ParseGeoPoint gp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dataImage = null;
        ButterKnife.inject(this);
        isSaveClicked = false;
        mcontex = this;
        et_addr.setOnClickListener(this);
        b_map.setOnClickListener(this);
        tv_dob.setOnClickListener(this);
        cb_ShopWorker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    ll_language.setVisibility(View.VISIBLE);
                }else{

                    ll_language.setVisibility(View.GONE);
                }
            }
        });
        cb_Driver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    ll_license.setVisibility(View.VISIBLE);
                }else{

                    ll_license.setVisibility(View.GONE);
                }
            }
        });
        iv_profilePhoto.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        this.menu = menu;
        MenuItem saveBtn = menu.findItem(R.id.action_signup);


        if (HopeApp.getSPString(HopeApp.SELECTED_USER_TYPE).equalsIgnoreCase("employer")) {
            ll_qualDialogue.setVisibility(View.GONE);
        }else{
            ll_qualDialogue.setVisibility(View.VISIBLE);
        }

        et_addr.setOnClickListener(this);


        getActionBar().setTitle("Profile");
        Integer colorVal = HopeApp.CategoryColor.get(HopeApp.TITLES[0]);
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(colorVal)));

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getStringExtra("from").equalsIgnoreCase("singup")) {
            saveBtn.setTitle("SIGN UP");
            showMobileDialog();
        } else {
            saveBtn.setTitle("SAVE");

            HopeApp.getInstance().onPreExecute(SignUpActivity.this);
            usr = (UserInfo) ParseUser.getCurrentUser();


            gp = usr.getAddressGP();
            //Log.d();
            et_name.setText(usr.getName());
            et_addr.setText(usr.getAddress());
            et_num.setText(usr.getPhoneNo());
            tv_dob.setText(usr.getDob());
            cb_Cooking.setChecked(usr.getCooking());
            if(usr.getCooking()){
                et_CookingExpWage.setText(String.valueOf(usr.getCookingExpWage()));
            }else{
                et_CookingExpWage.setText("");
            }
            cb_ClothWashing.setChecked(usr.getClothWashing());
            if(usr.getClothWashing()){
                et_ClothWashingExpWage.setText(String.valueOf(usr.getClothWashingExpWage()));
            }else{
                et_ClothWashingExpWage.setText("");
            }
            cb_HouseCleaning.setChecked(usr.getHouseCleaning());
            if(usr.getHouseCleaning()){
                et_HouseCleaningExpWage.setText(String.valueOf(usr.getHouseCleaningExpWage()));
            }else{
                et_HouseCleaningExpWage.setText("");
            }
            cb_dishWashing.setChecked(usr.getDishWashing());
            if(usr.getDishWashing()){
                et_DishWashingExpWage.setText(String.valueOf(usr.getDishWashingExpWage()));
            }else{
                et_DishWashingExpWage.setText("");
            }
            cb_Construction.setChecked(usr.getConstruction());
            if(usr.getConstruction()){
                et_ConstructionExpWage.setText(String.valueOf(usr.getConstructionExpWage()));
            }else{
                et_ConstructionExpWage.setText("");
            }
            cb_Wallpaint.setChecked(usr.getWallpaint());
            if(usr.getWallpaint()){
                et_WallpaintExpWage.setText(String.valueOf(usr.getWallpaintExpWage()));
            }else{
                et_WallpaintExpWage.setText("");
            }
            cb_Driver.setChecked(usr.getDriver());
            if(usr.getDriver()){
                et_DriverExpWage.setText(String.valueOf(usr.getDriverExpWage()));
                ll_license.setVisibility(View.VISIBLE);
                cb_licenseFourwheeler.setChecked(usr.getlicenseFour());
                cb_licenseHeavy.setChecked(usr.getlicenseHeavy());
            }else{
                et_DriverExpWage.setText("");
                ll_license.setVisibility(View.GONE);
            }
            cb_Guard.setChecked(usr.getGuard());
            if(usr.getGuard()){
                et_GuardExpWage.setText(String.valueOf(usr.getGuardExpWage()));
            }else{
                et_GuardExpWage.setText("");
            }
            cb_ShopWorker.setChecked(usr.getShopWorker());
            if(usr.getShopWorker()){
                et_ShopWorkerExpWage.setText(String.valueOf(usr.getShopWorkerExpWage()));
                ll_language.setVisibility(View.VISIBLE);
                cb_LanguageEnglish.setChecked(usr.getLangEnglish());
                cb_LanguageHindi.setChecked(usr.getLangHindi());
            }else{
                et_ShopWorkerExpWage.setText("");
                ll_language.setVisibility(View.GONE);
            }
            cb_Gardening.setChecked(usr.getGardening());
            if(usr.getGardening()){
                et_GardeningExpWage.setText(String.valueOf(usr.getGardeningExpWage()));
            }else{
                et_GardeningExpWage.setText("");
            }
            cb_Miscellaneous.setChecked(usr.getMiscellaneous());
            if(usr.getMiscellaneous()){
                et_MiscellaneousExpWage.setText(String.valueOf(usr.getMiscellaneousExpWage()));
            }else{
                et_MiscellaneousExpWage.setText("");
            }

            if (usr.getImageFile() != null)
                Picasso.with(this).load(usr.getImageFile().getUrl()).into(iv_profilePhoto);


            HopeApp.pd.dismiss();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signup) {
            if (checkForm() == true && !isSaveClicked) saveWorkAd();

            return true;
        }else if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveWorkAd() {
        isSaveClicked = true;
        usr = (UserInfo) ParseUser.getCurrentUser();


        if (usr == null) {
            usr = new UserInfo();
            isNull = true;
        }
        usr.setPassword("");
        usr.setUsername(et_num.getText().toString());
        usr.setType(HopeApp.getSPString(HopeApp.SELECTED_USER_TYPE));
        usr.setName(et_name.getText().toString().toLowerCase());
        usr.setDob(tv_dob.getText().toString());
        usr.setAddress(et_addr.getText().toString().toLowerCase());
        usr.setPhoneNo(et_num.getText().toString());
        usr.setGender(getGenderFromRG(rg_gender.getCheckedRadioButtonId()));

        usr.setDishWashing(cb_dishWashing.isChecked());
        usr.setHouseCleaning(cb_HouseCleaning.isChecked());
        usr.setClothWashing(cb_ClothWashing.isChecked());
        usr.setCooking(cb_Cooking.isChecked());
        usr.setConstruction(cb_Construction.isChecked());
        usr.setDriver(cb_Driver.isChecked());
        usr.setGardening(cb_Gardening.isChecked());
        usr.setGuard(cb_Guard.isChecked());
        usr.setShopWorker(cb_ShopWorker.isChecked());
        usr.setMiscellaneous(cb_Miscellaneous.isChecked());
        usr.setWallpaint(cb_Wallpaint.isChecked());
        usr.setAddressGP(gp);

        if(!et_DishWashingExpWage.getText().toString().equalsIgnoreCase("")) usr.setDishWashingExpWage(Long.parseLong(et_DishWashingExpWage.getText().toString()));
        if(!et_HouseCleaningExpWage.getText().toString().equalsIgnoreCase("")) usr.setHouseCleaningExpWage(Long.parseLong(et_HouseCleaningExpWage.getText().toString()));
        if(!et_ClothWashingExpWage.getText().toString().equalsIgnoreCase("")) usr.setClothWashingExpWage(Long.parseLong(et_ClothWashingExpWage.getText().toString()));
        if(!et_CookingExpWage.getText().toString().equalsIgnoreCase("")) usr.setCookingExpWage(Long.parseLong(et_CookingExpWage.getText().toString()));
        if(!et_ConstructionExpWage.getText().toString().equalsIgnoreCase("")) usr.setConstructionExpWage(Long.parseLong(et_ConstructionExpWage.getText().toString()));
        if(!et_DriverExpWage.getText().toString().equalsIgnoreCase("")) usr.setDriverExpWage(Long.parseLong(et_DriverExpWage.getText().toString()));
        if(!et_GardeningExpWage.getText().toString().equalsIgnoreCase("")) usr.setGardeningExpWage(Long.parseLong(et_GardeningExpWage.getText().toString()));
        if(!et_GuardExpWage.getText().toString().equalsIgnoreCase("")) usr.setGuardExpWage(Long.parseLong(et_GuardExpWage.getText().toString()));
        if(!et_ShopWorkerExpWage.getText().toString().equalsIgnoreCase("")) usr.setShopWorkerExpWage(Long.parseLong(et_ShopWorkerExpWage.getText().toString()));
        if(!et_MiscellaneousExpWage.getText().toString().equalsIgnoreCase("")) usr.setMiscellaneousExpWage(Long.parseLong(et_MiscellaneousExpWage.getText().toString()));
        if(!et_WallpaintExpWage.getText().toString().equalsIgnoreCase("")) usr.setWallpaintExpWage(Long.parseLong(et_WallpaintExpWage.getText().toString()));

        usr.setlicenseFour(cb_licenseFourwheeler.isChecked());
        usr.setlicenseHeavy(cb_licenseHeavy.isChecked());
        usr.setLangEnglish(cb_LanguageEnglish.isChecked());
        usr.setLangHindi(cb_LanguageHindi.isChecked());

        if (dataImage != null) {
            final ParseFile file = new ParseFile(et_num.getText().toString() + ".jpg", dataImage);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        usr.setImageFile(file);
                    }
                    sendDataToServer();
                }
            });

        }else{
            sendDataToServer();
        }

        HopeApp.getInstance().onPreExecute(SignUpActivity.this);
    }

    public void sendDataToServer() {
        if (isNull == true) {
            usr.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    isSaveClicked = false;
                    HopeApp.pd.dismiss();
                    if (e == null) {
                        Toast.makeText(SignUpActivity.this, "User signed up successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(mcontex, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            usr.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    isSaveClicked = false;
                    HopeApp.pd.dismiss();
                    if (e == null) {
                        Toast.makeText(SignUpActivity.this, "Profile saved successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }



    private void showMobileDialog() {
        TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        PhoneNumber = tMgr.getLine1Number();
        et_num.setText(PhoneNumber);

        if(PhoneNumber==null || PhoneNumber.equalsIgnoreCase("")){
            final Dialog dialog = new Dialog(this);
            // Include dialog.xml file
            dialog.setContentView(R.layout.dialog_phone_no);
            // Set dialog title
            dialog.setTitle("Enter your mobile number.");
            final EditText etPhoneNo = (EditText) dialog.findViewById(R.id.et_pdialog_mobile);
            final Button btnPhoneConfirm = (Button) dialog.findViewById(R.id.b_pdialog_confirm);

            btnPhoneConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String username = etPhoneNo.getText().toString();
                    dialog.dismiss();
                    HopeApp.getInstance().onPreExecute(SignUpActivity.this);
                    ParseUser.logInInBackground(username, "", new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            HopeApp.pd.dismiss();
                            if (user != null) {
                                finish();
                                return;
                            } else {
                                et_num.setText(username);
                                Log.e("hope showMobileDialog", e.toString());
                                et_num.setText(username);
                            }
                        }
                    });



                }
            });

            dialog.show();
        }else{
            ParseUser.logInInBackground(PhoneNumber, "", new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        finish();
                        return;
                    } else {
                        Log.e("hope showMobileDialog(else)", e.toString());
                    }
                }
            });
        }


    }
    private String getGenderFromRG(int checkedRadioButtonId) {
        switch (checkedRadioButtonId) {
            case R.id.rb_sign_male: {
                return "Male";
            }
            case R.id.rb_sign_female: {
                return "Female";
            }
        }
        return null;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sign_dob: {
                showDatePickerDialog();
                break;
            }

            case R.id.iv_sign_profilePhoto: {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, "Select File"),
                        SELECT_FILE);
                break;
            }

            case R.id.et_sign_addr: {
                if(gp == null){
                    openMapForAddress();
                }
                break;
            }
            case R.id.b_sign_map: {
                openMapForAddress();
                break;
            }



        }
    }

    private void openMapForAddress() {
        Intent intent = new Intent(this,MapActivity.class);
        if(usr!=null && usr.getAddressGP()!= null) {
            intent.putExtra("lat", usr.getAddressGP().getLatitude());
            intent.putExtra("lng", usr.getAddressGP().getLongitude());
        }

        startActivityForResult(intent, 1); // 1 = get address from map
    }
    public Bitmap getResizedBitmap(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = 400;
            height = (int) (width / bitmapRatio);
        } else {
            height = 480;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_LOCATION && data!=null) {

            String address = data.getStringExtra("address");
            double lat = data.getDoubleExtra("lat", 0);
            double lng = data.getDoubleExtra("lng",0);
            gp = new ParseGeoPoint(lat,lng);

            et_addr.setText(address);

        }else if (requestCode == SELECT_FILE) {
            if (resultCode == RESULT_OK) {
                File f = null;
                Bitmap bm = null;

                Uri selectedImageUri = data.getData();

                String tempPath = getPath(selectedImageUri, this);
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
                bm = getResizedBitmap(bm);
                iv_profilePhoto.setImageBitmap(bm);


                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                dataImage = stream.toByteArray();
            }
        }
    }


    public String getPath(Uri uri, Activity activity) {

        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        if (dobDay != 0) {
            year = dobYear;
            month = dobMonth;
            day = dobDay;
        }

        DatePickerDialog dialog = new DatePickerDialog(SignUpActivity.this,
                new SEDatePicker(), year, month, day);
        dialog.show();
    }

    public class SEDatePicker implements DatePickerDialog.OnDateSetListener {

        public SEDatePicker() {
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            dobYear = year;
            dobMonth = monthOfYear;
            dobDay = dayOfMonth;
            updateSEDateDisplay();
        }
    }

    private void updateSEDateDisplay() {
        tv_dob.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(dobMonth + 1).append("/").append(dobDay).append("/")
                .append(dobYear).append(" "));

    }

    public boolean checkForm() {
        if (et_name.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(SignUpActivity.this, "Your Name cannot be empty.", Toast.LENGTH_LONG).show();
            return false;
        } else if (tv_dob.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(SignUpActivity.this, "Your date of birth cannot be empty.", Toast.LENGTH_LONG).show();
            return false;
        } else if (et_addr.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(SignUpActivity.this, "Your Address cannot be empty.", Toast.LENGTH_LONG).show();
            return false;
        } else if (et_num.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(SignUpActivity.this, "Your Mobile Number cannot be empty.", Toast.LENGTH_LONG).show();
            return false;
        }else if (et_num.getText().toString().length()!=10) {
            Toast.makeText(SignUpActivity.this, "Please enter 10 digit valid mobile number", Toast.LENGTH_LONG).show();
            return false;
        }else if (et_num.getText().toString().length()!=10) {
            Toast.makeText(SignUpActivity.this, "Please enter 10 digit valid mobile number", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


}
