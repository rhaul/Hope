package com.xplorer.hope.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xplorer.hope.R;

/**
 * Created by Raghavendra on 14-01-2015.
 */
public class NavDrawerListAdapter extends BaseAdapter {
    private Context context;
    String[] drawerCandidate = {};

    public NavDrawerListAdapter(Context context, String[] drawerCandidate ) {

        this.context = context;
        this.drawerCandidate=drawerCandidate;
    }

    @Override
    public int getCount() {
        return drawerCandidate.length;
    }

    @Override
    public Object getItem(int position) {
        return drawerCandidate[position];
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

        final TextView FinalitemTxt = itemTxt;
/*
        FinalitemTxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){

                    case ACTION_DOWN:
                        Integer colorVal =  HopeApp.CategoryColor.get(HopeApp.TITLES[MainActivity.currentFragment]);
                        FinalitemTxt.setBackgroundColor(context.getResources().getColor(colorVal));
                        break;

                    case ACTION_UP:
                        Log.d("FinalitemTxt","ACTION_UP");
                        FinalitemTxt.setBackgroundColor(context.getResources().getColor(R.color.white));
                        break;
                    default:
                        Log.d("FinalitemTxt","default");
                        FinalitemTxt.setBackgroundColor(context.getResources().getColor(R.color.white));
                        break;

                }
                return true;
            }
        });*/
        return convertView;
    }
}
