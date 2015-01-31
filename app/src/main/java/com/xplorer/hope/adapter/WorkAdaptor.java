package com.xplorer.hope.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import com.xplorer.hope.R;
import com.xplorer.hope.activity.AddActivity;
import com.xplorer.hope.config.HopeApp;
import com.xplorer.hope.object.EWRelation;
import com.xplorer.hope.object.UserInfo;
import com.xplorer.hope.object.WorkAd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Raghavendra on 26-01-2015.
 */
public class WorkAdaptor extends BaseAdapter {
    private Context mContext;
    public List<WorkAd> myWorkIds;
    public List<UserInfo> netWorkersForThisWork;
    public int runCount=0;
public String currentWork;
   public static Dialog dialog;

    public WorkAdaptor(Context mContext, List<WorkAd> myWorkIds) {
        netWorkersForThisWork=new ArrayList<UserInfo>();
        this.mContext = mContext;
        runCount=0;
        this.myWorkIds = myWorkIds;
    }

    @Override
    public int getCount() {
        return myWorkIds.size();
    }

    @Override
    public Object getItem(int i) {
        return myWorkIds.get(i);
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
        final ViewHolder finalHolder = holder;


        int index = Arrays.asList(HopeApp.TITLES).indexOf(myWorkIds.get(pos).getCategory());
        Picasso.with(mContext).load(HopeApp.ImgUrl[index]).into(finalHolder.iv_employerPic);

        holder.tv_name.setText(HopeApp.getInstance().getHindiLanguage(myWorkIds.get(i).getCategory(), this, "WA"));
        holder.tv_description.setText(HopeApp.getInstance().getHindiLanguage(myWorkIds.get(i).getDescription(), this, "WA"));
        holder.tv_address.setText(HopeApp.getInstance().getHindiLanguage(myWorkIds.get(i).getAddress(), this, "WA"));

        holder.ll_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String map;
                if(myWorkIds.get(pos).getAddressGP()!=null){
                    String addr= myWorkIds.get(pos).getAddressGP().getLatitude()+","+myWorkIds.get(pos).getAddressGP().getLongitude();
                    map= "http://maps.google.com/maps?q="+addr;
                }else{
                    map = "http://maps.google.co.in/maps?q=" + myWorkIds.get(pos).getAddress();

                }
                Intent int_ = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(map));
                mContext.startActivity(int_);
            }
        });
        holder.ll_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" +  myWorkIds.get(pos).getPhoneNo()));
                mContext.startActivity(callIntent);
            }
        });

        String dateType=HopeApp.getInstance().getHindiLanguage(myWorkIds.get(i).getDateType()+" Job", this, "WA");
        if(myWorkIds.get(i).getDateType().equalsIgnoreCase("One Day")){
            dateType+="\nOn: "+myWorkIds.get(i).getDateFrom();
        }else if(myWorkIds.get(i).getDateType().equalsIgnoreCase("Custom")){
            dateType+="\nFrom: "+myWorkIds.get(i).getDateFrom()+"\nTo  : "+myWorkIds.get(i).getDateTo();
        }
        String timeType=HopeApp.getInstance().getHindiLanguage(myWorkIds.get(i).getTimeType(), this, "WA");
        if(myWorkIds.get(i).getTimeType().equalsIgnoreCase("Once a day")){
            timeType+="\n"+myWorkIds.get(i).getS1StartingTime()+"-"+myWorkIds.get(i).getS1EndingTime();
        }else {
            timeType+="\n"+myWorkIds.get(i).getS1StartingTime()+"-"+myWorkIds.get(i).getS1EndingTime()+"\n"+myWorkIds.get(i).getS2StartingTime()+"-"+myWorkIds.get(i).getS2EndingTime();
        }


        holder.tv_jobType.setText(dateType);
        holder.tv_timeType.setText(timeType);
        holder.tv_wages.setText(myWorkIds.get(i).getWageLowerLimit()+"-"+myWorkIds.get(i).getWageHigherLimit());
        holder.tv_phoneNo.setText(myWorkIds.get(i).getPhoneNo());

        holder.b_apply.setText(HopeApp.getInstance().getHindiLanguage("Workers", null, null));
        holder.ll_btns.setVisibility(View.VISIBLE);
        holder.b_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HopeApp.getInstance().onPreExecute(mContext);
                currentWork=myWorkIds.get(pos).getObjectId();
                runCount=0;
                netWorkersForThisWork=new ArrayList<UserInfo>();
                fetchWorkerForThisWork(myWorkIds.get(pos).getObjectId(), true);
                fetchWorkerForThisWork(myWorkIds.get(pos).getObjectId(), false);

            }
        });


        holder.b_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, AddActivity.class).putExtra("title", "save").putExtra("workId", myWorkIds.get(pos).getObjectId()));
                return;
            }
        });
        holder.b_Del.setText(myWorkIds.get(pos).getActive()?"Disable":"Enable");
        holder.b_Del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableThisWork(myWorkIds.get(pos).getObjectId(), pos, finalHolder);
            }
        });
        //b_Edit
        //b_Del
        holder.ll_phone.setVisibility(View.GONE);
        return view;
    }

    public void disableThisWork(final String workObjId, final int pos, final ViewHolder finalHolder){

        final boolean setVal = finalHolder.b_Del.getText().toString().equalsIgnoreCase("Disable")?false:true;

        ParseQuery<WorkAd> query = ParseQuery.getQuery("WorkAd");
        query.whereEqualTo("objectId", workObjId);
        HopeApp.getInstance().onPreExecute(mContext);
        Log.d("hope deleteDialog", workObjId);

        query.findInBackground(new FindCallback<WorkAd>() {
            @Override
            public void done(List<WorkAd> parseObjects, ParseException e) {

                HopeApp.pd.dismiss();
                if (e == null && parseObjects.size()==1) {

                    WorkAd workAdSave = parseObjects.get(0);
                    workAdSave.setActive(setVal);
                    workAdSave.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                String val= setVal?"Enable":"Disable";
                                Toast.makeText(mContext, val+" Successful", Toast.LENGTH_LONG).show();
                                finalHolder.b_Del.setText(val);
                                notifyDataSetChanged();
                            } else {
                                Log.e("Disable (done)",e.toString());
                                Toast.makeText(mContext, "Could not disable, please try again later.", Toast.LENGTH_LONG).show();

                            }
                        }
                    });


                }else{
                    Log.e("fetchWorkFromWorkId(size)",parseObjects.size()+"" );
                }
            }
        });



    }


    public void fetchWorkerForThisWork(String workId,final boolean approveStatus){
        String myWorkerId= ((UserInfo) ParseUser.getCurrentUser()).getObjectId();

        ParseQuery<EWRelation> query = ParseQuery.getQuery("EWRelation");
        query.whereEqualTo("employerID", myWorkerId);
        query.whereEqualTo("Approve", approveStatus);
        query.whereEqualTo("workID", workId);

        Log.d("hope fetchWorkerForThisWork", myWorkerId);

        query.findInBackground(new FindCallback<EWRelation>() {
            @Override
            public void done(List<EWRelation> parseObjects, ParseException e) {
                List<String> workerIds= new ArrayList<String>();
                if (e == null) {
                    for (int i = 0; i < parseObjects.size(); i++) {
                        workerIds.add(parseObjects.get(i).getUserID());
                    }
                    fetchWorkerProfiles(workerIds, approveStatus);
                    Log.d("hope fetchWorkerForThisWork(done)", String.valueOf(parseObjects.size()));

                }
            }
        });
    }

    private void fetchWorkerProfiles(List<String> workerIds,final boolean approveStatus) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContainedIn("objectId",workerIds );

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                List <UserInfo> users_ = ( List <UserInfo>)(List<?>) parseUsers;

                if (e == null) {
                    for(int i=0; i<users_.size(); i++) {
                        users_.get(i).isApproved = approveStatus;

                        netWorkersForThisWork.add(users_.get(i));
                    }

                }
                runCount++;
                Log.d("hope runCount", runCount+"");
                if(runCount==2){
                    HopeApp.pd.dismiss();
                    if(netWorkersForThisWork.size()==0) {
                        Toast.makeText(mContext, "No worker has been assigned for this work.", Toast.LENGTH_SHORT).show();

                    }else showWorkerDialog(netWorkersForThisWork);
                }
            }
        });

    }
    private void showWorkerDialog(List<UserInfo> parseUsers) {
        dialog = new Dialog(mContext);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_category_list);
        // Set dialog title
        dialog.setTitle("Workers");
        ListView lvD = (ListView) dialog.findViewById(R.id.lv_category_list);
        WorkerAdaptor adapter = new WorkerAdaptor(mContext,parseUsers, "workersAcceptReject",currentWork  );
        lvD.setAdapter(adapter);
        dialog.show();

    }

    public static  void cancelDialog(){
        if(dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
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
        @InjectView(R.id.ll_ad_btns)LinearLayout ll_btns;
        @InjectView(R.id.b_ad_Edit)Button b_Edit;
        @InjectView(R.id.b_ad_Del)Button b_Del;

        @InjectView(R.id.ll_ad_addr)LinearLayout ll_addr;

        public ViewHolder(View view, final Context context ){
            ButterKnife.inject(this, view);

        }

    }
}
