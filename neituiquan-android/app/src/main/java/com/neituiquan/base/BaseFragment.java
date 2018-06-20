package com.neituiquan.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neituiquan.net.RequestEventModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by liang02.wang on 2018/3/14.
 */

public abstract class BaseFragment extends Fragment {

    private Context context;

    private View contentView;

    private boolean isInit = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return contentView = initView(inflater,container,savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initList(savedInstanceState);
    }

    public abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void initList(Bundle savedInstanceState);

    /**
     * 懒加载方法
     */
    public void onLazyInitList(){
        if(isInit){
            return;
        }
        isInit = true;
    }

    public <T extends View> T findViewById(int id){
        return getContentView().findViewById(id);
    }

    public View getContentView() {
        return contentView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpResponse(RequestEventModel eventModel){
//        if(eventModel.isSuccess){
//            ToastUtils.showShort(eventModel.successEventModel.response);
//        }else{
//            ToastUtils.showShort(eventModel.errorEventModel.msg);
//        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
