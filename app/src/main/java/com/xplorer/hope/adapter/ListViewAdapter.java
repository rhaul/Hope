package com.xplorer.hope.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
        return mAds.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
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
        final ViewHolder finalHolder = holder;
        if(mAds.get(i).imgURL == null){

            ParseQuery<ParseUser>  query = ParseUser.getQuery();

            query.getInBackground(mAds.get(pos).getUserId(), new GetCallback<ParseUser>() {
                public void done(ParseUser object, ParseException e) {
                    UserInfo usr = (UserInfo) object;

                    if (usr.getImageFile() != null){
                        Picasso.with(mContext).load(usr.getImageFile().getUrl()).error(R.drawable.ic_launcher).into(finalHolder.iv_employerPic);
                        if(mAds != null && mAds.size()>0) {
                            mAds.get(pos).imgURL = usr.getImageFile().getUrl();
                        }
                    }else{
                        Picasso.with(mContext).load(R.drawable.defaultuser).into(finalHolder.iv_employerPic);
                        if(mAds != null && mAds.size()>0) {
                            mAds.get(pos).imgURL = "default.jpg";
                        }//Log.d("hope i(null)",pos+"> "+mAds.get(pos).getCategory()+"> "+mAds.get(pos).getUserId()+"> "+ mAds.get(pos).imgURL);
                    }
                }
            });
        }else if(mAds.get(pos).imgURL.equalsIgnoreCase("default.jpg")){
            Picasso.with(mContext).load(R.drawable.defaultuser).into(holder.iv_employerPic);
        }else{
            Picasso.with(mContext).load(mAds.get(i).imgURL).into(holder.iv_employerPic);
        }

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


        holder.tv_description.setText(HopeApp.getInstance().getUpperCaseString(mAds.get(i).getDescription()));
        holder.tv_name.setText(HopeApp.getInstance().getUpperCaseString(mAds.get(i).getUserName()));

        holder.tv_jobType.setText(dateType);
        holder.tv_timeType.setText(timeType);
        holder.tv_wages.setText(mAds.get(i).getWageLowerLimit()+"-"+mAds.get(i).getWageHigherLimit());
        holder.tv_phoneNo.setText(mAds.get(i).getPhoneNo());
        holder.tv_address.setText(HopeApp.getInstance().getUpperCaseString(mAds.get(i).getAddress()));


        holder.ll_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String map;
                if(mAds.get(pos).getAddressGP()!=null){
                    String addr= mAds.get(pos).getAddressGP().getLatitude()+","+mAds.get(pos).getAddressGP().getLatitude();
                    map= "http://maps.google.com/maps?q="+addr;
                }else{
                    map = "http://maps.google.co.in/maps?q=" + mAds.get(pos).getAddress();

                }
                Intent int_ = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(map));
                mContext.startActivity(int_);
            }
        });
        holder.ll_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" +  mAds.get(pos).getPhoneNo()));
                mContext.startActivity(callIntent);
            }
        });
        String myWorkerId= ((UserInfo) ParseUser.getCurrentUser()).getObjectId();

        setIvColor(mAds.get(i).getCategory(), finalHolder);
        if(HopeApp.myWorksIds.containsKey(mAds.get(i).getObjectId()) || mAds.get(i).getUserId().equalsIgnoreCase(myWorkerId)){
            holder.b_apply.setVisibility(View.GONE);
            holder.ll_phone.setVisibility(View.VISIBLE);
        }else if(HopeApp.myPendingWorksIds.containsKey(mAds.get(i).getObjectId())){
            holder.b_apply.setText("Pending");
            holder.b_apply.setVisibility(View.VISIBLE);
            holder.ll_phone.setVisibility(View.GONE);
        }else{
            holder.ll_phone.setVisibility(View.GONE);
            holder.b_apply.setVisibility(View.VISIBLE);
            holder.b_apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    open(mAds.get(pos).getUserName(),pos, finalHolder);
                }
            });
        }
        return view;
    }

    public void setIvColor(String category, final ViewHolder holder){
        Integer colorVal =  HopeApp.CategoryColor.get(category);
        //Log.d("setIvColor",colorVal.toString());
        holder.iv_ad_date.setColorFilter(mContext.getResources().getColor(colorVal));
        holder.iv_ad_info.setColorFilter(mContext.getResources().getColor(colorVal));
        holder.iv_ad_addr.setColorFilter(mContext.getResources().getColor(colorVal));
        holder.iv_ad_time.setColorFilter(mContext.getResources().getColor(colorVal));
        holder.iv_ad_phone.setColorFilter(mContext.getResources().getColor(colorVal));
        holder.iv_ad_rupee.setColorFilter(mContext.getResources().getColor(colorVal));

        holder.b_apply.setBackgroundColor(mContext.getResources().getColor(colorVal));
        /*holder.ll_ad_card.setBackgroundColor(mContext.getResources().getColor(colorVal));


        Integer LightcolorVal =  HopeApp.CategoryLightColor.get(category);

        holder.ll_ad_bg.setBackgroundColor(mContext.getResources().getColor(LightcolorVal));*/

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

        @InjectView(R.id.iv_ad_date)ImageView iv_ad_date;
        @InjectView(R.id.iv_ad_time)ImageView iv_ad_time;
        @InjectView(R.id.iv_ad_info)ImageView iv_ad_info;
        @InjectView(R.id.iv_ad_addr)ImageView iv_ad_addr;
        @InjectView(R.id.iv_ad_phone)ImageView iv_ad_phone;
        @InjectView(R.id.iv_ad_rupee)ImageView iv_ad_rupee;

        @InjectView(R.id.ll_ad_bg)LinearLayout ll_ad_bg;
        @InjectView(R.id.ll_ad_card)LinearLayout ll_ad_card;
        @InjectView(R.id.ll_ad_addr)LinearLayout ll_addr;



        public ViewHolder(View view, final Context context ){
            ButterKnife.inject(this, view);

        }

    }

    public void open(String name, final int pos,final ViewHolder vh){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        alertDialogBuilder.setMessage("Are you sure you want to apply to work for "+name+".\nYour rating will depend upon your attendance at the following work if you are accepted.") ;
        alertDialogBuilder.setPositiveButton("Accept",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {

                        HopeApp.myPendingWorksIds.put(mAds.get(pos).getObjectId(), mAds.get(pos).getObjectId());
                        HopeApp.myPendingEmployerIds.put(mAds.get(pos).getUserId(), mAds.get(pos).getUserId());

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