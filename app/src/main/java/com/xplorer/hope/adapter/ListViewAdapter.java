package com.xplorer.hope.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.xplorer.hope.R;
import com.xplorer.hope.config.HopeApp;
import com.xplorer.hope.object.EWRelation;
import com.xplorer.hope.object.UserInfo;
import com.xplorer.hope.object.WorkAd;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Raghavendra on 13-01-2015.
 */
public class ListViewAdapter extends BaseAdapter{

    private Context mContext;
    private List<WorkAd> mAds;
    public ListViewAdapter(Context context, List<WorkAd> ads){
        mContext = context;
        mAds = ads;
    }

    @Override
    public int getCount() {
        return mAds.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_ad, viewGroup, false);

            holder = new ViewHolder(view, mContext);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        final int pos = i;
        if(mAds.get(i).imgURL == null){
            final ViewHolder finalHolder = holder;

            ParseQuery<ParseUser>  query = ParseUser.getQuery();

            query.getInBackground(mAds.get(pos).getUserId(), new GetCallback<ParseUser>() {
                public void done(ParseUser object, ParseException e) {
                    UserInfo usr = (UserInfo) object;

                    if (usr.getImageFile() != null){
                        //Log.d("hope usr", pos+"> "+usr.getName()+">"+usr.getObjectId()+">"+usr.getImageFile().getUrl());
                        Picasso.with(mContext).load(usr.getImageFile().getUrl()).error(R.drawable.ic_launcher).into(finalHolder.iv_employerPic);
                        if(mAds != null && mAds.size()>0) {
                            mAds.get(pos).imgURL = usr.getImageFile().getUrl();
                        }
                        //Log.d("hope i",pos+"> "+mAds.get(pos).getCategory()+"> "+mAds.get(pos).getUserId()+"> "+ mAds.get(pos).imgURL);
                    }else{
                        //Log.d("hope usr (null)", pos+"> "+usr.getName()+">"+usr.getObjectId());
                        Picasso.with(mContext).load(R.drawable.defaultuser).into(finalHolder.iv_employerPic);
                        if(mAds != null && mAds.size()>0) {
                            mAds.get(pos).imgURL = "default.jpg";
                        }
                        //Log.d("hope i(null)",pos+"> "+mAds.get(pos).getCategory()+"> "+mAds.get(pos).getUserId()+"> "+ mAds.get(pos).imgURL);
                    }
                }
            });
        }else if(mAds.get(pos).imgURL.equalsIgnoreCase("default.jpg")){
            Picasso.with(mContext).load(R.drawable.defaultuser).into(holder.iv_employerPic);
        }else{
            Picasso.with(mContext).load(mAds.get(i).imgURL).into(holder.iv_employerPic);
        }
        holder.tv_description.setText(mAds.get(i).getDescription());
        holder.tv_name.setText(mAds.get(i).getUserName());
        String dateType=mAds.get(i).getDateType()+" Job";
        if(mAds.get(i).getDateType().equalsIgnoreCase("One Day")){
            dateType+="\nOn: "+mAds.get(i).getDateFrom();
        }else if(mAds.get(i).getDateType().equalsIgnoreCase("Custom")){
            dateType+="\nFrom: "+mAds.get(i).getDateFrom()+"\nTo  : "+mAds.get(i).getDateTo();
        }
        String timeType=mAds.get(i).getTimeType();
        if(timeType.equalsIgnoreCase("Once a day")){
            timeType+="\n"+mAds.get(i).getS1StartingTime()+"-"+mAds.get(i).getS1EndingTime();
        }else {
            timeType+="\n"+mAds.get(i).getS1StartingTime()+"-"+mAds.get(i).getS1EndingTime()+"\n"+mAds.get(i).getS2StartingTime()+"-"+mAds.get(i).getS2EndingTime();
        }


        holder.tv_name.setText(mAds.get(i).getUserName());
        holder.tv_jobType.setText(dateType);
        holder.tv_timeType.setText(timeType);
        holder.tv_wages.setText("₹ "+mAds.get(i).getWageLowerLimit()+"-"+mAds.get(i).getWageHigherLimit());
        holder.tv_phoneNo.setText(mAds.get(i).getPhoneNo());
        holder.ll_phone.setVisibility(View.GONE);
        holder.tv_address.setText(mAds.get(i).getAddress());

        holder.b_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open(mAds.get(pos).getUserName(),pos);
            }
        });
        return view;
    }
    public static class ViewHolder {
        @InjectView(R.id.iv_ad_employerPic)ImageView iv_employerPic;
        @InjectView(R.id.tv_ad_description)TextView tv_description;
        @InjectView(R.id.tv_ad_name)TextView tv_name;
        @InjectView(R.id.tv_ad_jobType)TextView tv_jobType;
        @InjectView(R.id.tv_ad_timeType)TextView tv_timeType;
        @InjectView(R.id.tv_ad_wages)TextView tv_wages;
        @InjectView(R.id.tv_ad_address)TextView tv_address;
        @InjectView(R.id.tv_ad_phoneNo)TextView tv_phoneNo;
        @InjectView(R.id.b_ad_apply)Button b_apply;
        @InjectView(R.id.ll_ad_phone)LinearLayout ll_phone;

        public ViewHolder(View view, final Context context ){
            ButterKnife.inject(this, view);

        }

    }

    public void open(String name, final int pos){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        alertDialogBuilder.setMessage("Are you sure you want to apply to work for "+name+".\nYour rating will depend upon your attendance at the following work if you are accepted.") ;
        alertDialogBuilder.setPositiveButton("Accept",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {

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
    private void setEWRelation(final int pos) {
        EWRelation rel = new EWRelation();
        rel.setApprove(false);
        rel.setEmployerID(mAds.get(pos).getUserId());
        rel.setUserID(ParseUser.getCurrentUser().getObjectId());
        rel.setWorkID(mAds.get(pos).getObjectId());
        ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(true);
        rel.setACL(acl);
        rel.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(mContext, "Applied successfully", Toast.LENGTH_SHORT).show();
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    String message = currentUser.getString("name") + " has applied in response to your following Job Advertisement ";

                    HopeApp.sendPushMessage(mAds.get(pos).getUserId(), currentUser.getObjectId(), mAds.get(pos).getObjectId(), ((UserInfo)ParseUser.getCurrentUser()).getName()+ " has made a Job Application.", message,"JARequest");
                } else {
                    Toast.makeText(mContext, "Please check your Internet Connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}