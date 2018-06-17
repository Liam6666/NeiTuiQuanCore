package com.neituiquan.work.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neituiquan.base.BaseFragment;
import com.neituiquan.work.R;

/**
 * Created by wangliang on 2018/6/17.
 */

public class UserFragment extends BaseFragment {

    public static UserFragment newInstance() {

        Bundle args = new Bundle();

        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_user,null);
    }

    @Override
    public void initList(Bundle savedInstanceState) {

    }
}
