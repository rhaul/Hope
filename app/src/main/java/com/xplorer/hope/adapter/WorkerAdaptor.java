package com.xplorer.hope.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xplorer.hope.R;
import com.xplorer.hope.object.UserInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Raghavendra on 26-01-2015.
 */
public class WorkerAdaptor  extends BaseAdapter {

    private static Context mContext;
    public List<UserInfo> usrs;

    public WorkerAdaptor(Context mContext, List<UserInfo> usrs) {
        this.mContext = mContext;
        this.usrs = usrs;
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

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_worker_profile, viewGroup, false);

            holder = new ViewHolder(view, mContext);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }
        UserInfo workerObject=usrs.get(i);

        holder.tv_worker_name.setText(workerObject.getName());
        holder.tv_gender.setText(workerObject.getGender());

        if(workerObject.getImageFile() != null ) Picasso.with(mContext).load(workerObject.getImageFile().getUrl()).into(holder.iv_workerPic);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        holder.tv_age.setText("");
        try {
            Date bdate = dateFormat.parse(workerObject.getDob());
            holder.tv_age.setText("Age: "+getAge(bdate.getYear()+1900, bdate.getMonth(), bdate.getDate()));
            Log.d("hope Age", bdate.getYear() + 1900 + ":" + bdate.getMonth() + ":" + bdate.getDate());

        } catch (java.text.ParseException e1) {
            e1.printStackTrace();
        }
        holder.tv_addr.setText(workerObject.getAddress());
        holder.tv_wprofile_phoneno.setText(workerObject.getPhoneNo());

        String LicenseStr = "License: ", LangStr = "Language: ";
        if (workerObject.getlicenseFour()) {
            LicenseStr += "FourWheeler";
        }
        if (workerObject.getlicenseHeavy()) {
            if(LicenseStr.equalsIgnoreCase("License: ")) LicenseStr +="Heavy";
            else LicenseStr += "| Heavy";
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
            if (LangStr.equalsIgnoreCase("Language: "))LangStr += "Hindi";
            else LangStr += "| Hindi";
        }
        if (LangStr.equalsIgnoreCase("Language: ")) {
            holder.ll_language.setVisibility(View.GONE);
        } else {
            holder.ll_language.setVisibility(View.VISIBLE);
            holder.tv_lang.setText(LangStr);
        }

        holder.ll_interestTitle.setVisibility(View.GONE);
        return view;
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

        }
    }



    public  static String getAge(int _year, int _month, int _day) {

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
        if (a < 0) a=0;
        return a + "";
    }

}
