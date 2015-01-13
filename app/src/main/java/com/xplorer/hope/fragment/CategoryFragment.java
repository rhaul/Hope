package com.xplorer.hope.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;
import com.xplorer.hope.R;
import com.xplorer.hope.adapter.ListViewAdapter;
import com.xplorer.hope.config.HopeApp;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    private ListViewAdapter lva;
    @InjectView(R.id.lv_frag_category) ListView lv_category;

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
        setUpFragment();
        return view;
    }

    private void setUpFragment() {
        int cat = getArguments().getInt("category", 1);
        View view = getActivity().getLayoutInflater().inflate(R.layout.item_category_header, null, false);
        ImageView iv= (ImageView) view.findViewById(R.id.iv_category_header);
        Picasso.with(getActivity()).load(HopeApp.ImgUrl[0]).into(iv);
        lv_category.addHeaderView(view);
        lva = new ListViewAdapter(getActivity());
        lv_category.setAdapter(lva);
    }


}
