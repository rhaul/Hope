package com.xplorer.hope.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.squareup.picasso.Picasso;
import com.xplorer.hope.R;
import com.xplorer.hope.config.HopeApp;
import com.xplorer.hope.object.Attendance;
import com.xplorer.hope.object.UserInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Raghavendra on 26-01-2015.
 */
public class WorkerAdaptor extends BaseAdapter {

    private static Context mContext;
    public List<UserInfo> usrs;
    public String behaviour;
    public String workId;

    public WorkerAdaptor(Context mContext, List<UserInfo> usrs, String behaviour, String workId) {
        this.mContext = mContext;
        this.usrs = usrs;
        this.behaviour = behaviour;
        this.workId = workId;

    }

    @Override
    public int getCount() {
        return usrs.size();
    }

    @Override
    public Object getItem(int i) {
        return usrs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        final UserInfo workerObject = usrs.get(i);
        final int pos = i;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_worker_profile, viewGroup, false);

            holder = new ViewHolder(view, mContext);

            view.setTag(holder);
            holder.ll_interestTitle.setVisibility(View.GONE);


            if (behaviour == "attendance") {


                holder.ll_interestTitle.setVisibility(View.VISIBLE);
                holder.tv_wprofile_btn1.setText("View Attendance");
                holder.tv_wprofile_btn2.setText("Mark Attendance");
                holder.tv_wprofile_btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showCalenderDialog(pos);
                        return;
                    }
                });
                holder.tv_wprofile_btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        markAttendance(pos);
                        return;
                    }
                });
            }

            if (behaviour == "workersAcceptReject" && workerObject.isApproved == false) {


                holder.ll_interestTitle.setVisibility(View.VISIBLE);
                holder.tv_wprofile_btn1.setText("Accept");
                holder.tv_wprofile_btn2.setText("Decline");
                holder.tv_wprofile_btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HopeApp.sendPushMessage(workerObject.getObjectId(), ParseUser.getCurrentUser().getObjectId(), workId, "Job Application Accepted", "Your application for the following job has been accepted.", "JAReply");
                        HopeApp.setEWRelationTrue(workId, workerObject.getObjectId(), ParseUser.getCurrentUser().getObjectId());

                        usrs.remove(pos);
                        notifyDataSetChanged();
                        return;
                    }
                });
                holder.tv_wprofile_btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HopeApp.sendPushMessage(workerObject.getObjectId(), ParseUser.getCurrentUser().getObjectId(), workId, "Job Application Declined", "Your application for the following job has been declined.", "JAReply");
                        HopeApp.removeEWRelation(workId, workerObject.getObjectId(), ParseUser.getCurrentUser().getObjectId());
                        usrs.remove(pos);
                        notifyDataSetChanged();
                        return;
                    }
                });
            }

            if (behaviour == "Interest") {
                final ViewHolder finalHolder = holder;

                if (workerObject.getCooking()) {
                    addInterest(HopeApp.getInstance().getHindiLanguage("Cooking", null,null), "₹ " + String.valueOf(workerObject.getCookingExpWage()), finalHolder);
                    holder.ll_interestTitle.setVisibility(View.VISIBLE);
                }
                if (workerObject.getClothWashing()) {
                    addInterest(HopeApp.getInstance().getHindiLanguage("Cloth Washing", null,null), "₹ " + String.valueOf(workerObject.getClothWashingExpWage()), finalHolder);
                    holder.ll_interestTitle.setVisibility(View.VISIBLE);
                }
                if (workerObject.getHouseCleaning()) {
                    addInterest(HopeApp.getInstance().getHindiLanguage("House Cleaning", null,null), "₹ " + String.valueOf(workerObject.getHouseCleaningExpWage()), finalHolder);
                    holder.ll_interestTitle.setVisibility(View.VISIBLE);
                }
                if (workerObject.getDishWashing()) {
                    addInterest(HopeApp.getInstance().getHindiLanguage("Dish Washing", null,null), "₹ " + String.valueOf(workerObject.getDishWashingExpWage()), finalHolder);
                    holder.ll_interestTitle.setVisibility(View.VISIBLE);
                }
                if (workerObject.getConstruction()) {
                    addInterest(HopeApp.getInstance().getHindiLanguage("Construction", null,null), "₹ " + String.valueOf(workerObject.getConstructionExpWage()), finalHolder);
                    holder.ll_interestTitle.setVisibility(View.VISIBLE);
                }
                if (workerObject.getWallpaint()) {
                    addInterest(HopeApp.getInstance().getHindiLanguage("Wall Paint", null,null), "₹ " + String.valueOf(workerObject.getWallpaintExpWage()), finalHolder);
                    holder.ll_interestTitle.setVisibility(View.VISIBLE);
                }
                if (workerObject.getDriver()) {
                    addInterest(HopeApp.getInstance().getHindiLanguage("Driver", null,null), "₹ " + String.valueOf(workerObject.getDriverExpWage()), finalHolder);
                    holder.ll_interestTitle.setVisibility(View.VISIBLE);
                }
                if (workerObject.getGuard()) {
                    addInterest(HopeApp.getInstance().getHindiLanguage("Guard", null,null), "₹ " + String.valueOf(workerObject.getGuardExpWage()), finalHolder);
                    holder.ll_interestTitle.setVisibility(View.VISIBLE);
                }
                if (workerObject.getShopWorker()) {
                    addInterest(HopeApp.getInstance().getHindiLanguage("Shop Worker", null,null), "₹ " + String.valueOf(workerObject.getShopWorkerExpWage()), finalHolder);
                    holder.ll_interestTitle.setVisibility(View.VISIBLE);
                }
                if (workerObject.getGardening()) {
                    addInterest(HopeApp.getInstance().getHindiLanguage("Gardening", null,null), "₹ " + String.valueOf(workerObject.getGardeningExpWage()), finalHolder);
                    holder.ll_interestTitle.setVisibility(View.VISIBLE);
                }
                if (workerObject.getMiscellaneous()) {
                    addInterest(HopeApp.getInstance().getHindiLanguage("Miscellaneous", null,null), "₹ " + String.valueOf(workerObject.getMiscellaneousExpWage()), finalHolder);
                    holder.ll_interestTitle.setVisibility(View.VISIBLE);
                }
            }
        } else {
            holder = (ViewHolder) view.getTag();
        }




        holder.tv_worker_name.setText(HopeApp.getInstance().getHindiLanguage(workerObject.getName(), this, "WRA"));
        holder.tv_gender.setText(HopeApp.getInstance().getHindiLanguage(workerObject.getGender(), this, "WRA"));


        if (workerObject.getImageFile() != null)
            Picasso.with(mContext).load(workerObject.getImageFile().getUrl()).into(holder.iv_workerPic);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        holder.tv_age.setText("");
        try {
            Date bdate = dateFormat.parse(workerObject.getDob());

            holder.tv_age.setText(HopeApp.getInstance().getHindiLanguage("Age", null, null)+": "+getAge(bdate.getYear()+1900, bdate.getMonth(), bdate.getDate()));
           //Log.d("hope Age", bdate.getYear() + 1900 + ":" + bdate.getMonth() + ":" + bdate.getDate());


        } catch (java.text.ParseException e1) {
            e1.printStackTrace();
        }
        holder.tv_addr.setText(HopeApp.getInstance().getHindiLanguage(workerObject.getAddress(), this, "WRA"));
        holder.tv_wprofile_phoneno.setText(workerObject.getPhoneNo());

        holder.ll_wprofile_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String map;
                if (workerObject.getAddressGP() != null) {
                    String addr = workerObject.getAddressGP().getLatitude() + "," + workerObject.getAddressGP().getLongitude();
                    map = "http://maps.google.com/maps?q=" + addr;
                } else {
                    map = "http://maps.google.co.in/maps?q=" + workerObject.getAddress();

                }
                Intent int_ = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(map));
                mContext.startActivity(int_);
            }
        });
        holder.ll_wprofile_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + workerObject.getPhoneNo()));
                mContext.startActivity(callIntent);
            }
        });


        String LicenseStr = "License: ", LangStr = "Language: ";
        if (workerObject.getlicenseFour()) {
            LicenseStr += "Four Wheeler";
        }
        if (workerObject.getlicenseHeavy()) {
            if (LicenseStr.equalsIgnoreCase("License: ")) LicenseStr += "Heavy";
            else LicenseStr += " | Heavy";
        }
        if (LicenseStr.equalsIgnoreCase("License: ")) {
            holder.ll_license.setVisibility(View.GONE);
        } else {
            holder.ll_license.setVisibility(View.VISIBLE);
            holder.tv_license.setText(LicenseStr);
        }
        if (workerObject.getLangEnglish()) {
            LangStr += "English";
        }
        if (workerObject.getLangHindi()) {
            if (LangStr.equalsIgnoreCase("Language: ")) LangStr += "Hindi";
            else LangStr += " | Hindi";
        }
        if (LangStr.equalsIgnoreCase("Language: ")) {
            holder.ll_language.setVisibility(View.GONE);
        } else {
            holder.ll_language.setVisibility(View.VISIBLE);
            holder.tv_lang.setText(LangStr);
        }


        return view;
    }

    public static String getCurrentDateStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("MM/dd/yyyy");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public static String getCurrentTimeStamp() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    private void markAttendance(int pos) {
        HopeApp.getInstance().onPreExecute(mContext);
        Attendance rel = new Attendance();
        rel.setWorkerID(usrs.get(pos).getObjectId());
        rel.setDate(getCurrentDateStamp());
        rel.setTime(getCurrentTimeStamp());
        rel.setEmployerID(ParseUser.getCurrentUser().getObjectId());
        ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(true);
        rel.setACL(acl);
        rel.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(mContext, "Attendance marked successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
                }

                HopeApp.pd.cancel();
            }
        });
    }

    public void addInterest(String Interested, String expectedWage, final ViewHolder finalHolder) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewItemInterest = inflater.inflate(R.layout.item_interest, null);
        TextView tvInterested = (TextView) viewItemInterest.findViewById(R.id.tv_interest_name);
        TextView tvWage = (TextView) viewItemInterest.findViewById(R.id.tv_interest_wage);
        tvInterested.setText(Interested);
        tvWage.setText(expectedWage);

        finalHolder.ll_interest.addView(viewItemInterest);
    }


    public static class ViewHolder {

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
        LinearLayout ll_license;
        LinearLayout ll_language;


        TextView tv_wprofile_btn1;
        TextView tv_wprofile_btn2;


        LinearLayout ll_wprofile_addr;
        LinearLayout ll_wprofile_phone;


        public ViewHolder(View view, final Context context) {
            tv_worker_name = (TextView) view.findViewById(R.id.tv_wprofile_name);
            tv_gender = (TextView) view.findViewById(R.id.tv_wprofile_gender);
            tv_age = (TextView) view.findViewById(R.id.tv_wprofile_age);
            tv_addr = (TextView) view.findViewById(R.id.tv_wprofile_addr);
            tv_wprofile_phoneno = (TextView) view.findViewById(R.id.tv_wprofile_phoneno);
            ll_interest = (LinearLayout) view.findViewById(R.id.ll_wprofile_interest);
            tv_license = (TextView) view.findViewById(R.id.tv_wprofile_license);
            tv_lang = (TextView) view.findViewById(R.id.tv_wprofile_lang);
            iv_workerPic = (ImageView) view.findViewById(R.id.iv_wprofile_img);
            ll_interestTitle = (LinearLayout) view.findViewById(R.id.ll_wprofile_interestTitle);
            ll_language = (LinearLayout) view.findViewById(R.id.ll_wprofile_language);
            ll_license = (LinearLayout) view.findViewById(R.id.ll_wprofile_license);

            tv_wprofile_btn1 = (TextView) view.findViewById(R.id.tv_wprofile_btn1);
            tv_wprofile_btn2 = (TextView) view.findViewById(R.id.tv_wprofile_btn2);

            ll_wprofile_addr = (LinearLayout) view.findViewById(R.id.ll_wprofile_addr);
            ll_wprofile_phone = (LinearLayout) view.findViewById(R.id.ll_wprofile_phone);

        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (usrs != null && usrs.size() == 0) {
            WorkAdaptor.cancelDialog();
        }
    }

    public static String getAge(int _year, int _month, int _day) {

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
        if (a < 0) a = 0;
        return a + "";
    }


    private void showCalenderDialog(final int i) {
        HopeApp.getInstance().onPreExecute(mContext);
        ParseQuery<Attendance> query = ParseQuery.getQuery("Attendance");
        query.whereEqualTo("workerID", usrs.get(i).getObjectId());
        query.whereEqualTo("employerID", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<Attendance>() {
            @Override
            public void done(List<Attendance> parseObjects, ParseException e) {
                if (e == null && parseObjects.size() > 0) {
                    // Setup caldroid to use as dialog
                    final HashMap<String, String> attnd = new HashMap<String, String>();

                    CaldroidFragment dialogCaldroidFragment = new CaldroidFragment();
                    // Setup listener
                    CaldroidListener listener = new CaldroidListener() {

                        @Override
                        public void onSelectDate(Date date, View view) {
                            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                            String key = df.format(date);
                            Log.d("onSelectDate", key);
                            if (attnd.containsKey(key)) {
                                Toast.makeText(mContext, attnd.get(key), Toast.LENGTH_SHORT).show();

                            }


                        }

                    };
                    Bundle args = new Bundle();
                    args.putString("dialogTitle", usrs.get(i).getName() + "'s Attendance");
                    dialogCaldroidFragment.setArguments(args);
                    dialogCaldroidFragment.setCaldroidListener(listener);
                    for (int j = 0; j < parseObjects.size(); j++) {
                        if (attnd.containsKey(parseObjects.get(j).getDate())) {
                            dialogCaldroidFragment.setBackgroundResourceForDate(R.color.MediumAquamarine, convertToDate(parseObjects.get(j).getDate()));
                            attnd.put(parseObjects.get(j).getDate(), attnd.get(parseObjects.get(j).getDate()) + "\nSlot 2: " + parseObjects.get(j).getTime());
                        } else {
                            dialogCaldroidFragment.setBackgroundResourceForDate(R.color.green, convertToDate(parseObjects.get(j).getDate()));
                            attnd.put(parseObjects.get(j).getDate(), "Slot 1: " + parseObjects.get(j).getTime());
                        }
                    }

                    final String dialogTag = "CALDROID_DIALOG_FRAGMENT";
                    FragmentActivity activity = (FragmentActivity) mContext;
                    dialogCaldroidFragment.show(activity.getSupportFragmentManager(), dialogTag);
                }
                HopeApp.pd.dismiss();
            }
        });

    }


    private Date convertToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }
}
