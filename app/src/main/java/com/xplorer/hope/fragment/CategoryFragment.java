package com.xplorer.hope.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.dobmob.doblist.DobList;
import com.dobmob.doblist.events.OnLoadMoreListener;
import com.dobmob.doblist.exceptions.NoEmptyViewException;
import com.dobmob.doblist.exceptions.NoListViewException;
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
    @InjectView(R.id.lv_frag_category)
    ListView lv_category;
    private List<WorkAd> categoryItems;
    int cat = 0;
    DobList dobList;

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
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.inject(this, view);
        cat = getArguments().getInt("category", 1);
        setUpFragment(inflater, view);
        return view;
    }

    private void setUpFragment(LayoutInflater inflater, View view) {
        View headerView = inflater.inflate(R.layout.item_category_header, null, false);
        ImageView iv = (ImageView) headerView.findViewById(R.id.iv_category_header);
        Picasso.with(getActivity()).load(HopeApp.ImgUrl[cat]).into(iv);

        lv_category.addHeaderView(headerView);
        categoryItems = new ArrayList<WorkAd>();
        lva = new ListViewAdapter(getActivity(), categoryItems);
        lv_category.setAdapter(lva);
        initList(view, lv_category);
    }

    private void fetchWorks() {

        ParseQuery<WorkAd> query = ParseQuery.getQuery("WorkAd");
        query.whereEqualTo("category", HopeApp.TITLES[cat]);
        query.setLimit(1);
        query.addDescendingOrder("createdAt");
        query.setSkip(categoryItems.size());
        query.findInBackground(new FindCallback<WorkAd>() {
            @Override
            public void done(List<WorkAd> workAds, ParseException e) {
                if (e == null) {
                    categoryItems.addAll(workAds);
                    lva.notifyDataSetChanged();
                }
                dobList.finishLoading();
            }
        });
    }

    private void initList(View rootView, ListView listView) {

        // DobList initializing
        dobList = new DobList();
        try {

            // Register ListView
            //
            // NoListViewException will be thrown when
            // there is no ListView
            dobList.register(listView);
            // Add ProgressBar to footers of ListView
            // to be shown in loading more
            dobList.addDefaultLoadingFooterView();

            // Sets the view to show if the adapter is empty
            // see startCentralLoading() method
            //  View noItems = rootView.findViewById(R.id.tv_interest_name);
            // dobList.setEmptyView(noItems);

            // Callback called when reaching last item in ListView
            dobList.setOnLoadMoreListener(new OnLoadMoreListener() {

                @Override
                public void onLoadMore(final int totalItemCount) {
                    fetchWorks();
                }
            });

        } catch (NoListViewException e) {
            e.printStackTrace();
        }

        try {
            // Show ProgressBar at the center of ListView
            // this can be used while loading data from
            // server at the first time
            //
            // setEmptyView() must be called before
            //
            // NoEmptyViewException will be thrown when
            // there is no EmptyView
            dobList.startCentralLoading();

        } catch (NoEmptyViewException e) {
            e.printStackTrace();
        }
    }

}
