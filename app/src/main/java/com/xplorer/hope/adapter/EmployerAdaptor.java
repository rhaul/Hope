package com.xplorer.hope.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.xplorer.hope.R;
import com.xplorer.hope.object.UserInfo;
import com.xplorer.hope.object.WorkAd;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Raghavendra on 25-01-2015.
 */

public class EmployerAdaptor extends ArrayAdapter<WorkAd> {

    private Context mContext;
    public List<WorkAd> myWorkIds;
    public int noHeader=0;
    public List<String>  HeaderTitle;
    public String prevCat= "";

    public EmployerAdaptor(Context context, int resource, List<WorkAd> objects) {
        super(context, resource, objects);
        String PrevCatStr="";
        myWorkIds=new ArrayList<WorkAd>();
        HeaderTitle=new ArrayList<String>();
        if(objects.size()!=0){
            myWorkIds.add(null);
            PrevCatStr = objects.get(0).getCategory();
            HeaderTitle.add(PrevCatStr);
        }else{
            myWorkIds=objects;
        }

        for(int i=0; i<objects.size(); i++){
            if(!PrevCatStr.equalsIgnoreCase(objects.get(i).getCategory())){
                myWorkIds.add(null);
                PrevCatStr = objects.get(i).getCategory();
                HeaderTitle.add(PrevCatStr);

            }
            myWorkIds.add(objects.get(i));
        }

        mContext=context;
    }


    @Override
    public int getViewTypeCount() {
        return 2;

    }

    @Override
    public int getItemViewType(int position) {
        if(myWorkIds.get(position)==null){
            return 0;
        }else{
            return 1;
        }

        /*if(position-noHeader==0) {
            prevCat=myWorkIds.get(position-noHeader).getCategory();
            noHeader++;
            return 0;
        }else if(myWorkIds.get(position-noHeader).getCategory().equalsIgnoreCase(prevCat) ){
            return 1;
        }
        prevCat=myWorkIds.get(position-noHeader).getCategory();
        noHeader++;*/


        //return 0;
    }

    @Override
    public int getCount() {


        return myWorkIds.size();
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        ViewHolder holder = null;
        ViewHolderH holderH = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            switch (getItemViewType(i)){
                case 0:
                    view = inflater.inflate(R.layout.category_header, viewGroup, false);
                    holderH=new ViewHolderH(view, mContext);
                    view.setTag(holderH);
                    break;
                case 1:
                    view = inflater.inflate(R.layout.item_ad, viewGroup, false);
                    holder = new ViewHolder(view, mContext);
                    view.setTag(holder);
                    break;

            }

        }
        else {
            if(getItemViewType(i)==0) {
                holderH = (ViewHolderH) view.getTag();
            }else{
                holder = (ViewHolder) view.getTag();
            }
        }


        if(getItemViewType(i)==0){
            holderH.tv_catname.setText(myWorkIds.get(i+1).getCategory());
            noHeader++;
        }else {
            final int pos = i;
            final ViewHolder finalHolder = holder;
            if (myWorkIds.get(i).imgURL == null) {

                ParseQuery<ParseUser> query = ParseUser.getQuery();

                query.getInBackground(myWorkIds.get(pos).getUserId(), new GetCallback<ParseUser>() {
                    public void done(ParseUser object, ParseException e) {
                        UserInfo usr = (UserInfo) object;

                        if (usr.getImageFile() != null) {
                            Picasso.with(mContext).load(usr.getImageFile().getUrl()).error(R.drawable.ic_launcher).into(finalHolder.iv_employerPic);
                            myWorkIds.get(pos).imgURL = usr.getImageFile().getUrl();
                        } else {
                            Picasso.with(mContext).load(R.drawable.defaultuser).into(finalHolder.iv_employerPic);
                            myWorkIds.get(pos).imgURL = "default.jpg";
                        }
                    }
                });
            } else if (myWorkIds.get(pos).imgURL.equalsIgnoreCase("default.jpg")) {
                Picasso.with(mContext).load(R.drawable.defaultuser).into(holder.iv_employerPic);
            } else {
                Picasso.with(mContext).load(myWorkIds.get(i).imgURL).into(holder.iv_employerPic);
            }

            holder.tv_description.setText(myWorkIds.get(i).getDescription());
            holder.tv_name.setText(myWorkIds.get(i).getUserName());
            String dateType = myWorkIds.get(i).getDateType() + " Job";
            if (myWorkIds.get(i).getDateType().equalsIgnoreCase("One Day")) {
                dateType += "\nOn: " + myWorkIds.get(i).getDateFrom();
            } else if (myWorkIds.get(i).getDateType().equalsIgnoreCase("Custom")) {
                dateType += "\nFrom: " + myWorkIds.get(i).getDateFrom() + "\nTo  : " + myWorkIds.get(i).getDateTo();
            }
            String timeType = myWorkIds.get(i).getTimeType();
            if (timeType.equalsIgnoreCase("Once a day")) {
                timeType += "\n" + myWorkIds.get(i).getS1StartingTime() + "-" + myWorkIds.get(i).getS1EndingTime();
            } else {
                timeType += "\n" + myWorkIds.get(i).getS1StartingTime() + "-" + myWorkIds.get(i).getS1EndingTime() + "\n" + myWorkIds.get(i).getS2StartingTime() + "-" + myWorkIds.get(i).getS2EndingTime();
            }


            holder.tv_name.setText(myWorkIds.get(i).getUserName());
            holder.tv_jobType.setText(dateType);
            holder.tv_timeType.setText(timeType);
            holder.tv_wages.setText("₹ " + myWorkIds.get(i).getWageLowerLimit() + "-" + myWorkIds.get(i).getWageHigherLimit());
            holder.tv_phoneNo.setText(myWorkIds.get(i).getPhoneNo());
            holder.tv_address.setText(myWorkIds.get(i).getAddress());


            holder.b_apply.setVisibility(View.GONE);
            holder.ll_phone.setVisibility(View.VISIBLE);
        }
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
    public static class ViewHolderH {
        @InjectView(R.id.tv_catHeader_catname)TextView tv_catname;

        public ViewHolderH(View view, final Context context ){
            ButterKnife.inject(this, view);

        }
    }

}
