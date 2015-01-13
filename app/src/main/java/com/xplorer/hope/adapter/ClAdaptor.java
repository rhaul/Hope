package com.xplorer.hope.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xplorer.hope.R;
import com.xplorer.hope.config.HopeApp;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Raghavendra on 13-01-2015.
 */
public class ClAdaptor extends BaseAdapter {
    private Context mContext;

    public ClAdaptor(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return 10;
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
            view = inflater.inflate(R.layout.item_cl, viewGroup, false);

            holder = new ViewHolder(view);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        Picasso.with(mContext).load(HopeApp.ImgUrl[0]).into(holder.iv_img);
        Log.d("pos",i+"");
        return view;
    }
    public static class ViewHolder {
        @InjectView(R.id.iv_cl_img)ImageView iv_img;
        @InjectView(R.id.tv_cl_name)TextView tv_name;

        public ViewHolder(View view){
            ButterKnife.inject(this, view);
        }

    }
}
