package com.neituiquan.work.widgets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.BarUtils;
import com.neituiquan.base.BaseActivity;
import com.neituiquan.view.SelectorCityView;
import com.neituiquan.work.R;


/**
 * Created by Augustine on 2018/6/27.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 地区选择
 *
 */

public class CitySelectorActivity extends BaseActivity implements SelectorCityView.OnSelectorCallBack {

    private static final int START_TO_SELECTOR_CITY = 323;

    private static final int SELECTOR_CITY_RESULT_CODE = 333;

    private View selectorCityUI_statusView;
    private ImageView selectorCityUI_backImg;
    private SelectorCityView selectorCityUI_selectorView;


    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_selector_city);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();
        selectorCityUI_selectorView.setOnSelectorCallBack(this);
//        finish();
        selectorCityUI_backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(String province, String city, int index) {
        Intent intent = new Intent();
        intent.putExtra("province",province);
        intent.putExtra("city",city);
        setResult(SELECTOR_CITY_RESULT_CODE,intent);
        finish();
    }

    private void initStatusBar(){
        int barHeight = BarUtils.getStatusBarHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,barHeight);
        selectorCityUI_statusView.setLayoutParams(params);
    }

    private void bindViews() {

        selectorCityUI_statusView = (View) findViewById(R.id.selectorCityUI_statusView);
        selectorCityUI_backImg = (ImageView) findViewById(R.id.selectorCityUI_backImg);
        selectorCityUI_selectorView = (com.neituiquan.view.SelectorCityView) findViewById(R.id.selectorCityUI_selectorView);
    }
}
