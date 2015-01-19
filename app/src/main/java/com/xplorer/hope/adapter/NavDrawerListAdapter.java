package com.xplorer.hope.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xplorer.hope.R;
import com.xplorer.hope.config.HopeApp;

/**
 * Created by Raghavendra on 14-01-2015.
 */
public class NavDrawerListAdapter extends BaseAdapter {
    private Context context;

    public NavDrawerListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return HopeApp.drawerTitlesWorker.length;
    }

    @Override
    public Object getItem(int position) {
        return HopeApp.drawerTitlesWorker[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, parent, false);
        }

        TextView itemTxt = (TextView) convertView.findViewById(R.id.tv_drawer_itemTxt);
        itemTxt.setText(getItem(position).toString());

        return convertView;
    }
}
