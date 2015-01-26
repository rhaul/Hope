package com.xplorer.hope.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.costum.android.widget.LoadMoreListView;
import com.etiennelawlor.quickreturn.library.enums.QuickReturnType;
import com.etiennelawlor.quickreturn.library.listeners.QuickReturnListViewOnScrollListener;
import com.etiennelawlor.quickreturn.library.utils.QuickReturnUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;
import com.xplorer.hope.R;
import com.xplorer.hope.activity.QuickReturnInterface;
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

    private QuickReturnInterface mCoordinator;
    // private QuickReturnFooter ll_footer;
    public ListViewAdapter lva;
    @InjectView(R.id.lv_frag_category)
	LoadMoreListView lv_category;

    @InjectView(R.id.fl_frag_bg)
    FrameLayout fl_bg;

    

    private List<WorkAd> categoryItems;
    int cat = 0;
    int locallySortedBy = 3;
    int locallyFilteredBy = 0;
    ParseQuery<WorkAd> query;
    boolean loadMore = true;

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

        /*Integer colorVal =  HopeApp.CategoryLightColor.get(HopeApp.TITLES[cat]);
        fl_bg.setBackgroundColor(getResources().getColor(colorVal));
        lv_category.setBackgroundColor(getResources().getColor(colorVal));*/
        return view;
    }

    // region Lifecycle Methods
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof QuickReturnInterface) {
            mCoordinator = (QuickReturnInterface) activity;
        } else
            throw new ClassCastException("Parent container must implement the QuickReturnInterface");
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int footerHeight = getResources().getDimensionPixelSize(R.dimen.twitter_footer_height);
        int indicatorHeight = QuickReturnUtils.dp2px(getActivity(), 4);
        int footerTranslation = -footerHeight + indicatorHeight;
        QuickReturnListViewOnScrollListener scrollListener =new QuickReturnListViewOnScrollListener(QuickReturnType.FOOTER, null, 0, mCoordinator.getFooter(), -footerTranslation);
        scrollListener.setCanSlideInIdleScrollState(true);
        lv_category.setOnScrollListener(scrollListener);
        View headerView = getActivity().getLayoutInflater().inflate(R.layout.item_category_header, null, false);
        ImageView iv = (ImageView) headerView.findViewById(R.id.iv_category_header);
        Picasso.with(getActivity()).load(HopeApp.ImgUrl[cat]).into(iv);

        lv_category.addHeaderView(headerView);
        categoryItems = new ArrayList<WorkAd>();
        lva = new ListViewAdapter(getActivity(), categoryItems);
        lv_category.setAdapter(lva);


        lv_category.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                fetchWorks(HopeApp.getInstance().applyFilteredQuery());
            }
        });
        //initList(view, lv_category);
        fetchWorks(HopeApp.getInstance().applyFilteredQuery());

    }

    private void fetchWorks(ParseQuery<WorkAd> adParseQuery) {

        query = adParseQuery;
        query.whereEqualTo("category", HopeApp.TITLES[cat]);


        query.addDescendingOrder("createdAt");


        query.setLimit(2);

        query.setSkip(categoryItems.size());
        query.findInBackground(new FindCallback<WorkAd>() {
            @Override
            public void done(List<WorkAd> workAds, ParseException e) {
                if (workAds != null && workAds.size() > 0 && e == null) {
                    categoryItems.addAll(workAds);
                    lva.notifyDataSetChanged();
                }


                locallySortedBy = HopeApp.sortedBy;
                locallyFilteredBy = HopeApp.filteredBy;
                query = null;
                loadMore = true;
                lv_category.onLoadMoreComplete();

            }
        });
    }


    public void checkIfFilterApplied() {
        if (query == null && categoryItems != null) {
            if (HopeApp.sortedBy != locallySortedBy) {
                categoryItems.clear();
                lva.notifyDataSetChanged();
                fetchWorks(HopeApp.getInstance().applyFilteredQuery());
            } else if (HopeApp.filteredBy != locallyFilteredBy) {
                categoryItems.clear();
                lva.notifyDataSetChanged();
                fetchWorks(HopeApp.getInstance().applyFilteredQuery());
            }
        }
    }

}
