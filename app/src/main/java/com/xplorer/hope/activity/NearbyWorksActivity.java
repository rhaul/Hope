package com.xplorer.hope.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.edmodo.rangebar.RangeBar;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.xplorer.hope.R;
import com.xplorer.hope.config.HopeApp;
import com.xplorer.hope.object.EWRelation;
import com.xplorer.hope.object.UserInfo;
import com.xplorer.hope.object.WorkAd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NearbyWorksActivity extends FragmentActivity implements OnMapReadyCallback {

    // views
    @InjectView(R.id.ll_nbw_work)
    LinearLayout ll_work;
    @InjectView(R.id.ll_nbw_filter_dialog)
    LinearLayout ll_filter_dialog;

    @InjectView(R.id.ll_filter_buttons)
    LinearLayout ll_buttons;

    List<String> categories = new ArrayList<String>();
    private GoogleMap mMap;
    private Marker myMarker;
    private Geocoder geocoder;
    LocationManager manager;
    private Circle circle;
    LatLng latLng = new LatLng(28.63821, 77.2047405);
    String address = "";
    GetAddressTask getAddressTask;
    float currZoomLevel = 11;
    int [] filter = new int[]{1,1,1,1,1,0,40,0,10};
    private List<WorkAd> workAdItems = new ArrayList<WorkAd>();
    private List<Marker> workMarkers = new ArrayList<Marker>();
    ParseQuery<WorkAd> query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_works);
        ButterKnife.inject(this);
        categories.addAll(Arrays.asList(HopeApp.TITLES));
        categories.add(0, "All");

        getActionBar().setTitle("Search nearby jobs");
        Integer colorVal =  HopeApp.CategoryColor.get(HopeApp.TITLES[6]);
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(colorVal)));

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_nbw);

        if (Geocoder.isPresent()) {
            geocoder = new Geocoder(NearbyWorksActivity.this, Locale.getDefault());
        }
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (getIntent().getDoubleExtra("lat", 0) != 0) {
            latLng = new LatLng(getIntent().getDoubleExtra("lat", 0), getIntent().getDoubleExtra("lng", 0));
        } else if (manager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
            Location lastLoc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            latLng = new LatLng(lastLoc.getLatitude(), lastLoc.getLongitude());
        } else if (manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
            Location lastLoc = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            latLng = new LatLng(lastLoc.getLatitude(), lastLoc.getLongitude());
        } else if (manager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER) != null) {
            Location lastLoc = manager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            latLng = new LatLng(lastLoc.getLatitude(), lastLoc.getLongitude());
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                currZoomLevel = cameraPosition.zoom;
            }
        });
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(NearbyWorksActivity.this, "Please enable your GPS", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                latLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                if (getAddressTask != null) {
                    getAddressTask.cancel(true);
                }
                getAddressTask = new GetAddressTask(NearbyWorksActivity.this, false);
                getAddressTask.execute(latLng.latitude, latLng.longitude);
                removeWorkMarkers();
                showGeofence();
                fetchWorks(false);
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(myMarker.getId().equalsIgnoreCase(marker.getId())){
                    if(ll_work.getVisibility() == View.VISIBLE) {
                        hideWorkView();
                    }
                }else {
                    for (int i = 0; i < workMarkers.size(); i++) {
                        if (workMarkers.get(i).getId().equalsIgnoreCase(marker.getId())) {
                            loadFullWorkDetailsView(i);
                            break;
                        }
                    }
                }
                return false;
            }
        });
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                removeWorkMarkers();
                showMyMarkerOnMap();
                fetchWorks(false);
                // showWorksOnMap();
            }
        });
        showMyMarkerOnMap();
        fetchWorks(false);
        // showWorksOnMap();
    }

    private void hideWorkView() {

        Animation bottomDown = AnimationUtils.loadAnimation(this,
                R.anim.bottom_down);
        ll_work.startAnimation(bottomDown);
        ll_work.setVisibility(View.GONE);
    }

    private void showWorkView() {
        Animation bottomUp = AnimationUtils.loadAnimation(this,
                R.anim.bottom_up);
        ll_work.startAnimation(bottomUp);
        ll_work.setVisibility(View.VISIBLE);
    }

    private void loadFullWorkDetailsView(final int pos) {
        if (ll_work.getChildCount() > 0) {
            ll_work.removeViewAt(0);
        }
        View work = getLayoutInflater().inflate(R.layout.item_ad, null);
        work.setBackgroundColor(Color.TRANSPARENT);
        final ViewHolderK holder = new ViewHolderK(work, this);
        if (workAdItems.get(pos).imgURL == null) {

            ParseQuery<ParseUser> query = ParseUser.getQuery();

            query.getInBackground(workAdItems.get(pos).getUserId(), new GetCallback<ParseUser>() {
                public void done(ParseUser object, ParseException e) {
                    UserInfo usr = (UserInfo) object;

                    if (usr.getImageFile() != null) {
                        Picasso.with(NearbyWorksActivity.this).load(usr.getImageFile().getUrl()).error(R.drawable.ic_launcher).into(holder.iv_employerPic);
                        if (workAdItems != null && workAdItems.size() > 0) {
                            workAdItems.get(pos).imgURL = usr.getImageFile().getUrl();
                        }
                    } else {
                        Picasso.with(NearbyWorksActivity.this).load(R.drawable.defaultuser).into(holder.iv_employerPic);
                        if (workAdItems != null && workAdItems.size() > 0) {
                            workAdItems.get(pos).imgURL = "default.jpg";
                        }//Log.d("hope pos(null)",pos+"> "+workAdItems.get(pos).getCategory()+"> "+workAdItems.get(pos).getUserId()+"> "+ workAdItems.get(pos).imgURL);
                    }
                }
            });
        } else if (workAdItems.get(pos).imgURL.equalsIgnoreCase("default.jpg")) {
            Picasso.with(NearbyWorksActivity.this).load(R.drawable.defaultuser).into(holder.iv_employerPic);
        } else {
            Picasso.with(NearbyWorksActivity.this).load(workAdItems.get(pos).imgURL).into(holder.iv_employerPic);
        }

        String dateType = workAdItems.get(pos).getDateType() + " Job";
        if (workAdItems.get(pos).getDateType().equalsIgnoreCase("One Day")) {
            dateType += "\nOn: " + workAdItems.get(pos).getDateFrom();
        } else if (workAdItems.get(pos).getDateType().equalsIgnoreCase("Custom")) {
            dateType += "\nFrom: " + workAdItems.get(pos).getDateFrom() + "\nTo  : " + workAdItems.get(pos).getDateTo();
        }
        String timeType = workAdItems.get(pos).getTimeType();
        if (timeType.equalsIgnoreCase("Once a day")) {
            timeType += "\n" + workAdItems.get(pos).getS1StartingTime() + "-" + workAdItems.get(pos).getS1EndingTime();
        } else {
            timeType += "\n" + workAdItems.get(pos).getS1StartingTime() + "-" + workAdItems.get(pos).getS1EndingTime() + "\n" + workAdItems.get(pos).getS2StartingTime() + "-" + workAdItems.get(pos).getS2EndingTime();
        }


        holder.tv_description.setText(HopeApp.getInstance().getUpperCaseString(workAdItems.get(pos).getDescription()));
        holder.tv_name.setText(HopeApp.getInstance().getUpperCaseString(workAdItems.get(pos).getUserName()));

        holder.tv_jobType.setText(dateType);
        holder.tv_timeType.setText(timeType);
        holder.tv_wages.setText(workAdItems.get(pos).getWageLowerLimit() + "-" + workAdItems.get(pos).getWageHigherLimit());
        holder.tv_phoneNo.setText(workAdItems.get(pos).getPhoneNo());
        holder.tv_address.setText(HopeApp.getInstance().getUpperCaseString(workAdItems.get(pos).getAddress()));

        String myWorkerId = ParseUser.getCurrentUser().getObjectId();

        if (HopeApp.myWorksIds.containsKey(workAdItems.get(pos).getObjectId()) || workAdItems.get(pos).getUserId().equalsIgnoreCase(myWorkerId)) {
            holder.b_apply.setVisibility(View.GONE);
            holder.ll_phone.setVisibility(View.VISIBLE);
        } else if (HopeApp.myPendingWorksIds.containsKey(workAdItems.get(pos).getObjectId())) {
            holder.b_apply.setText("Pending");
            holder.b_apply.setVisibility(View.VISIBLE);
            holder.ll_phone.setVisibility(View.GONE);
        } else {
            holder.ll_phone.setVisibility(View.GONE);
            holder.b_apply.setVisibility(View.VISIBLE);
            holder.b_apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    open(workAdItems.get(pos).getUserName(), pos, holder);
                }
            });
        }
        ll_work.addView(work);
        showWorkView();

    }

    public void open(String name, final int pos, final ViewHolderK vh) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage("Are you sure you want to apply to work for " + name + ".\nYour rating will depend upon your attendance at the following work if you are accepted.");
        alertDialogBuilder.setPositiveButton("Accept",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        vh.b_apply.setOnClickListener(null);
                        vh.b_apply.setText("Pending");
                        setEWRelation(pos);
                        dialog.cancel();
                    }


                });
        alertDialogBuilder.setNegativeButton("Decline",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        if(ll_filter_dialog.getVisibility() == View.VISIBLE){
            ll_filter_dialog.setVisibility(View.GONE);
            ll_buttons.setVisibility(View.GONE);
        }
        else if (ll_work.getVisibility() == View.VISIBLE) {
            hideWorkView();
        }
        else {
            super.onBackPressed();
        }

    }

    private void setEWRelation(final int pos) {
        EWRelation rel = new EWRelation();
        rel.setApprove(false);
        rel.setEmployerID(workAdItems.get(pos).getUserId());
        rel.setUserID(ParseUser.getCurrentUser().getObjectId());
        rel.setWorkID(workAdItems.get(pos).getObjectId());
        ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(true);
        rel.setACL(acl);
        rel.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(NearbyWorksActivity.this, "Applied successfully", Toast.LENGTH_SHORT).show();
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    String message = currentUser.getString("name") + " has applied in response to your following Job Advertisement ";

                    HopeApp.sendPushMessage(workAdItems.get(pos).getUserId(), currentUser.getObjectId(), workAdItems.get(pos).getObjectId(), ((UserInfo) ParseUser.getCurrentUser()).getName() + " has made a Job Application.", message, "JARequest");
                } else {
                    Toast.makeText(NearbyWorksActivity.this, "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void removeWorkMarkers() {
        for (int i = 0; i < workMarkers.size(); i++) {
            workMarkers.get(i).remove();
        }
        circle.remove();
        workMarkers.clear();
        workAdItems.clear();
    }

    public static class ViewHolderK {
        @InjectView(R.id.iv_ad_employerPic)
        ImageView iv_employerPic;
        @InjectView(R.id.tv_ad_description)
        TextView tv_description;
        @InjectView(R.id.tv_ad_name)
        TextView tv_name;
        @InjectView(R.id.tv_ad_jobType)
        TextView tv_jobType;
        @InjectView(R.id.tv_ad_timeType)
        TextView tv_timeType;
        @InjectView(R.id.tv_ad_wages)
        TextView tv_wages;
        @InjectView(R.id.tv_ad_address)
        TextView tv_address;
        @InjectView(R.id.tv_ad_phoneNo)
        TextView tv_phoneNo;
        @InjectView(R.id.b_ad_apply)
        Button b_apply;
        @InjectView(R.id.ll_ad_phone)
        LinearLayout ll_phone;

        @InjectView(R.id.iv_ad_date)
        ImageView iv_ad_date;
        @InjectView(R.id.iv_ad_time)
        ImageView iv_ad_time;
        @InjectView(R.id.iv_ad_info)
        ImageView iv_ad_info;
        @InjectView(R.id.iv_ad_addr)
        ImageView iv_ad_addr;
        @InjectView(R.id.iv_ad_phone)
        ImageView iv_ad_phone;
        @InjectView(R.id.iv_ad_rupee)
        ImageView iv_ad_rupee;

        @InjectView(R.id.ll_ad_bg)
        LinearLayout ll_ad_bg;
        @InjectView(R.id.ll_ad_card)
        LinearLayout ll_ad_card;

        public ViewHolderK(View view, final Context context) {
            ButterKnife.inject(this, view);

        }
    }

    private void fetchWorks(boolean b) {

        query = ParseQuery.getQuery("WorkAd");
        if(filter[7] != 0){
            query.whereEqualTo("category", HopeApp.TITLES[filter[7]]);
        }
        query.whereWithinKilometers("addressGP", new ParseGeoPoint(latLng.latitude, latLng.longitude), filter[8]);
        if(b){
            //filter

            List<String> dateTypes= new ArrayList<String>();
            List<String> frequency= new ArrayList<String>();
            if(filter[0] == 1){
                dateTypes.add("One Day");
            }
            if(filter[1] == 1){
                dateTypes.add("Monthly");
            }
            if(filter[2] == 1){
                dateTypes.add("Custom");
            }
            if(filter[3] == 1){
                frequency.add("Once a day");
            }
            if(filter[4] == 1){
                frequency.add("Twice a day");
            }
            query.whereContainedIn("dateType", dateTypes);
            query.whereContainedIn("timeType", frequency);
            query.whereGreaterThanOrEqualTo("wageLowerLimit",filter[5]*500).whereLessThanOrEqualTo("wageHigherLimit",filter[6]*500);
            showGeofence();
        }
        query.addDescendingOrder("createdAt");
        // query.setLimit(2);
        query.setSkip(workAdItems.size());
        query.findInBackground(new FindCallback<WorkAd>() {
            @Override
            public void done(List<WorkAd> workAds, ParseException e) {
                if (workAds != null && workAds.size() > 0 && e == null) {
                    workAdItems.addAll(workAds);
                    showWorksOnMap();
                }
            }
        });
    }

    private void showWorksOnMap() {
        for (int i = 0; i < workAdItems.size(); i++) {
            addWorkMarker(i);
        }
        fitWorkAds();
    }

    private void fitWorkAds() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker m : workMarkers) {
            builder.include(m.getPosition());
        }
        builder.include(myMarker.getPosition());
        LatLngBounds bounds = builder.build();
        int padding = 200; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);

        myMarker.showInfoWindow();
    }

    private void addWorkMarker(int pos) {
        MarkerOptions markerOptions = new MarkerOptions().
                title(workAdItems.get(pos).getCategory()).
                snippet(workAdItems.get(pos).getDescription()).
                icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)).
                position(new LatLng(workAdItems.get(pos).getAddressGP().getLatitude(), workAdItems.get(pos).getAddressGP().getLongitude()));

        Marker marker = mMap.addMarker(markerOptions);
        workMarkers.add(marker);
    }

    public void showMyMarkerOnMap() {
        if (myMarker != null) {
            myMarker.remove();
            myMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Search Location")
                    .draggable(true));

            if (getAddressTask != null) {
                getAddressTask.cancel(true);
            }
            getAddressTask = new GetAddressTask(NearbyWorksActivity.this, false);
            getAddressTask.execute(latLng.latitude, latLng.longitude);

        } else {
            myMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Long press to drag me to your Search Location")
                    .draggable(true));
        }
        showGeofence();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, currZoomLevel);
        mMap.animateCamera(cameraUpdate);

    }


    private void showGeofence() {
        if(circle != null){
            circle.remove();
        }
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(myMarker.getPosition().latitude,myMarker.getPosition().longitude))
                .radius(filter[8]*1000)
                .fillColor(0x20ff0000)
                .strokeColor(Color.TRANSPARENT)
                .strokeWidth(2);

        circle = mMap.addCircle(circleOptions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nearby_works, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_mapFilter) {
            applyFilters();
            return true;
        }else if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void applyFilters() {
        final LinearLayout ll_category = (LinearLayout) findViewById(R.id.ll_filter_category);
        final LinearLayout ll_radius = (LinearLayout) findViewById(R.id.ll_filter_radius);
        final LinearLayout ll_workType = (LinearLayout) findViewById(R.id.ll_filter_workType);
        final LinearLayout ll_frequency = (LinearLayout) findViewById(R.id.ll_filter_frequency);
        final LinearLayout ll_wage = (LinearLayout) findViewById(R.id.ll_filter_wage);
        ll_category.setVisibility(View.VISIBLE);
        ll_radius.setVisibility(View.VISIBLE);
        final TextView tv_category = (TextView) findViewById(R.id.tv_filter_category);
        final Spinner sp_category = (Spinner) findViewById(R.id.sp_filter_category);
        final TextView tv_radius = (TextView) findViewById(R.id.tv_filter_radius);
        final SeekBar sb_radius = (SeekBar) findViewById(R.id.sb_filter_radius);
        final CheckBox cb_filter_wt_oneDay = (CheckBox) findViewById(R.id.cb_filter_wt_oneDay);
        final CheckBox cb_filter_wt_monthly = (CheckBox) findViewById(R.id.cb_filter_wt_monthly);
        final CheckBox cb_filter_wt_custom = (CheckBox) findViewById(R.id.cb_filter_wt_custom);
        final CheckBox cb_filter_f_once = (CheckBox) findViewById(R.id.cb_filter_f_once);
        final CheckBox cb_filter_f_twice = (CheckBox) findViewById(R.id.cb_filter_f_twice);
        final RangeBar rb_filter_wageLimit = (RangeBar) findViewById(R.id.rb_filter_wageLimit);
        final TextView tv_filter_wl_lower = (TextView) findViewById(R.id.tv_filter_wl_lower);
        final TextView tv_filter_wl_higher = (TextView) findViewById(R.id.tv_filter_wl_higher);
        Button b_cancel = (Button) findViewById(R.id.b_filter_cancel);
        Button b_apply = (Button) findViewById(R.id.b_filter_apply);

        b_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_filter_dialog.setVisibility(View.GONE);
                ll_buttons.setVisibility(View.GONE);
            }
        });

        b_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cb_filter_wt_oneDay.isChecked()) {
                    filter[0] = 1;
                } else {
                    filter[0] = 0;
                }
                if (cb_filter_wt_monthly.isChecked()) {
                    filter[1] = 1;
                } else {
                    filter[1] = 0;
                }
                if (cb_filter_wt_custom.isChecked()) {
                    filter[2] = 1;
                } else {
                    filter[2] = 0;
                }
                if (cb_filter_f_once.isChecked()) {
                    filter[3] = 1;
                } else {
                    filter[3] = 0;
                }
                if (cb_filter_f_twice.isChecked()) {
                    filter[4] = 1;
                } else {
                    filter[4] = 0;
                }
                filter[5] = rb_filter_wageLimit.getLeftIndex();
                filter[6] = rb_filter_wageLimit.getRightIndex();
                filter[7] = sp_category.getSelectedItemPosition();
                filter[8] = sb_radius.getProgress() + 1;
                //  pagerAdapter.getFragment(vp_pager.getCurrentItem()).checkIfFilterApplied();
                fetchWorks(true);
                ll_filter_dialog.setVisibility(View.GONE);
                ll_buttons.setVisibility(View.GONE);
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, categories.toArray(new String[categories.size()]));
        sp_category.setAdapter(adapter);
        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter[7] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        for (int i = 0; i < filter.length; i++) {
            switch (i) {
                case 0: {
                    if (filter[i] == 1) {
                        cb_filter_wt_oneDay.setChecked(true);
                    } else {
                        cb_filter_wt_oneDay.setChecked(false);
                    }
                }
                break;
                case 1: {
                    if (filter[i] == 1) {
                        cb_filter_wt_monthly.setChecked(true);
                    } else {
                        cb_filter_wt_monthly.setChecked(false);
                    }
                }
                break;
                case 2: {
                    if (filter[i] == 1) {
                        cb_filter_wt_custom.setChecked(true);
                    } else {
                        cb_filter_wt_custom.setChecked(false);
                    }
                }
                break;
                case 3: {
                    if (filter[i] == 1) {
                        cb_filter_f_once.setChecked(true);
                    } else {
                        cb_filter_f_once.setChecked(false);
                    }
                }
                break;
                case 4: {
                    if (filter[i] == 1) {
                        cb_filter_f_twice.setChecked(true);
                    } else {
                        cb_filter_f_twice.setChecked(false);
                    }
                }
                break;
                case 5: {
                    tv_filter_wl_lower.setText("Lower: ₹ " + filter[i] * 500);
                }
                break;
                case 6: {
                    tv_filter_wl_higher.setText("Higher: ₹ " + filter[i] * 500);
                    rb_filter_wageLimit.setThumbIndices(filter[5], filter[6]);
                }
                break;
                case 7: {
                    sp_category.setSelection(filter[7]);
                }
                break;
                case 8: {
                    tv_radius.setText("Radius: "+(filter[8])+" km");
                    sb_radius.setProgress(filter[8]-1);
                }
                break;
            }
        }
        sb_radius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_radius.setText("Radius: "+(progress+1)+" km");
                filter[8] = progress+1;
                showGeofence();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                ll_filter_dialog.setBackgroundColor(Color.TRANSPARENT);
                ll_radius.setBackgroundColor(getResources().getColor(R.color.ALPHA_Green_2));
                tv_category.setVisibility(View.GONE);
                sp_category.setVisibility(View.GONE);
                ll_workType.setVisibility(View.GONE);
                ll_frequency.setVisibility(View.GONE);
                ll_wage.setVisibility(View.GONE);
                ll_buttons.setVisibility(View.GONE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ll_filter_dialog.setBackgroundColor(Color.WHITE);
                ll_radius.setBackgroundColor(Color.WHITE);
                tv_category.setVisibility(View.VISIBLE);
                sp_category.setVisibility(View.VISIBLE);
                ll_workType.setVisibility(View.VISIBLE);
                ll_frequency.setVisibility(View.VISIBLE);
                ll_wage.setVisibility(View.VISIBLE);
                ll_buttons.setVisibility(View.VISIBLE);

            }
        });
        rb_filter_wageLimit.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int i, int i2) {
                tv_filter_wl_lower.setText("Lower: ₹ " + i * 500);
                tv_filter_wl_higher.setText("Higher: ₹ " + i2 * 500);
            }
        });
        ll_filter_dialog.setVisibility(View.VISIBLE);
        ll_buttons.setVisibility(View.VISIBLE);

    }

    private class GetAddressTask extends
            AsyncTask<Double, Void, Address> {
        Context mContext;
        boolean setFinalAddress = false;

        public GetAddressTask(Context context, boolean value) {
            super();
            mContext = context;
            setFinalAddress = value;
        }

        @Override
        protected Address doInBackground(Double... params) {
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(params[0], params[1], 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
                //loading.setVisibility(View.VISIBLE);
                return addresses.get(0);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Address addr) {
            //pb_loading.setVisibility(View.GONE);
            if (addr != null) {
                latLng = new LatLng(addr.getLatitude(), addr.getLongitude());
                address = String.format(
                        "%s, %s",
                        // If there's a street address, add it
                        addr.getMaxAddressLineIndex() > 0 ?
                                addr.getAddressLine(0) : "",
                        // Locality is usually a city
                        addr.getLocality());
                myMarker.setTitle(address);
                myMarker.showInfoWindow();
            } else {
                Toast.makeText(mContext, "No Address found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
