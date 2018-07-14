package com.neituiquan.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.ResourceUtils;
import com.neituiquan.adapter.SelectorCityAdapter;
import com.neituiquan.entity.CitysEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Augustine on 2018/6/27.
 * <p>
 * email:nice_ohoh@163.com
 */

public class SelectorCityView extends FrameLayout implements RightSelectorView.OnSelectorListener {

    private ExpandableListView expandableListView;

    private RightSelectorView rightSelectorView;

    private String assets;

    private static final String FILE_NAME = "provinces.txt";

    private static final int READ_ASSETS = 111;

    private InnerHandler innerHandler = new InnerHandler(this);

    private SelectorCityAdapter selectorCityAdapter;

    private Vibrator vibrator;

    private OnSelectorCallBack onSelectorCallBack;

    public SelectorCityView(Context context) {
        super(context);
        init();
    }

    public SelectorCityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnSelectorCallBack(OnSelectorCallBack onSelectorCallBack) {
        this.onSelectorCallBack = onSelectorCallBack;
    }

    private void init(){
        expandableListView = new ExpandableListView(getContext());
        expandableListView.setChildDivider(null);
        expandableListView.setDivider(null);
        expandableListView.setGroupIndicator(null);
        rightSelectorView = new RightSelectorView(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(70,-1);
        params.gravity = Gravity.RIGHT;
        rightSelectorView.setLayoutParams(params);
        addView(expandableListView);
        addView(rightSelectorView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = Message.obtain();
                message.obj = ResourceUtils.readAssets2String(FILE_NAME);
                message.what = READ_ASSETS;
                innerHandler.sendMessage(message);
            }
        }).start();

        vibrator = (Vibrator)getContext().getSystemService(getContext().VIBRATOR_SERVICE);
    }


    private void initRightView(){
        List<String> provinceNameList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(assets);
            JSONArray provinceArray = jsonObject.getJSONArray("provinces");
            for(int i = 0 ; i < provinceArray.length() ; i ++){
                JSONObject cityObj = provinceArray.getJSONObject(i);
                String provinceName = cityObj.getString("provinceName");
                provinceNameList.add(provinceName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        rightSelectorView.init(provinceNameList);
        rightSelectorView.invalidate();
        rightSelectorView.setOnSelectorListener(this);
    }

    private void initListView(){
        final List<CitysEntity> entityList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(assets);
            JSONArray provinceArray = jsonObject.getJSONArray("provinces");

            for(int i = 0 ; i < provinceArray.length() ; i ++){
                CitysEntity entity = new CitysEntity();
                JSONObject object = provinceArray.getJSONObject(i);
                entity.setProvince(object.getString("provinceName"));
                JSONArray array = object.getJSONArray("citys");
                List<String> citys = new ArrayList<>();
                for(int j = 0 ; j < array.length() ; j ++){
                    JSONObject obj = array.getJSONObject(j);
                    citys.add(obj.getString("citysName"));
                }
                entity.setCityList(citys);
                entityList.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        selectorCityAdapter = new SelectorCityAdapter(entityList,getContext());
        expandableListView.setAdapter(selectorCityAdapter);
        for(int i = 0 ; i < selectorCityAdapter.getGroupCount() ; i ++){
            expandableListView.expandGroup(i,false);
        }
        //禁止折叠
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        selectorCityAdapter.setChildItemCallBack(new SelectorCityAdapter.ChildItemCallBack() {
            @Override
            public void onChildItemClickListener(int groupIndex, int childIndex) {
                if(onSelectorCallBack != null){
                    onSelectorCallBack.onItemClick(entityList.get(groupIndex).getProvince(),entityList.get(groupIndex).getCityList().get(childIndex),childIndex);
                }
            }
        });
    }

    private int oldIndex = -1;

    @Override
    public void onSelectorIndex(int index, String city) {
        if(oldIndex != index){
            vibrator.cancel();
            vibrator.vibrate(15);
            expandableListView.setSelectedGroup(index);
            oldIndex = index;
        }
    }

    public ExpandableListView getExpandableListView() {
        return expandableListView;
    }


    public interface OnSelectorCallBack {

        public void onItemClick(String province,String city,int index);

    }
    static class InnerHandler extends Handler{

        private SelectorCityView selectorCityView;

        public InnerHandler(SelectorCityView selectorCityView) {
            this.selectorCityView = selectorCityView;
        }

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if(selectorCityView == null){
                return;
            }
            switch (msg.what){
                case READ_ASSETS:
                    selectorCityView.assets = (String) msg.obj;
                    selectorCityView.initRightView();
                    selectorCityView.initListView();
                    break;
            }
        }
    }
}
