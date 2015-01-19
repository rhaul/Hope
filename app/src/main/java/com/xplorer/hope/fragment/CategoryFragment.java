package com.xplorer.hope.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;
import com.xplorer.hope.R;
import com.xplorer.hope.adapter.ListViewAdapter;
import com.xplorer.hope.config.HopeApp;
import com.xplorer.hope.object.WorkAd;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    private ListViewAdapter lva;
    @InjectView(R.id.lv_frag_category) ListView lv_category;
    private List<WorkAd> categoryItems = new ArrayList<WorkAd>();

    public static CategoryFragment newInstance(int someInt) {
        CategoryFragment myFragment = new CategoryFragment();

        Bundle args = new Bundle();
        args.putInt("category", someInt);
        myFragment.setArguments(args);

        return myFragment;
    }

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container,false);
        ButterKnife.inject(this, view);
        setUpFragment(inflater);
        return view;
    }

    private void setUpFragment(LayoutInflater inflater) {
        int cat = getArguments().getInt("category", 1);
        View view = inflater.inflate(R.layout.item_category_header, null, false);
        ImageView iv= (ImageView) view.findViewById(R.id.iv_category_header);
        //Log.d("raghav", String.valueOf(HopeApp.ImgUrl[cat]));
        Picasso.with(getActivity()).load(HopeApp.ImgUrl[cat]).into(iv);

        //iv.setImageResource(R.drawable.washing);
        lv_category.addHeaderView(view);
        lva = new ListViewAdapter(getActivity(), categoryItems);
        lv_category.setAdapter(lva);


        ParseQuery<WorkAd> query = ParseQuery.getQuery("WorkAd");
        query.whereEqualTo("category",HopeApp.TITLES[cat]);
        query.setLimit(10);
        query.findInBackground(new FindCallback<WorkAd>() {
            @Override
            public void done(List<WorkAd> workAds, ParseException e) {
                if(e == null) {
                    categoryItems = workAds;
                    lva.notifyDataSetChanged();

                }


            }
        });
    }


}
