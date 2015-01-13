package com.xplorer.hope.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
        TextView tv = new TextView(mContext,null,0);
        tv.setText(i+"");
        return tv;
    }
}
