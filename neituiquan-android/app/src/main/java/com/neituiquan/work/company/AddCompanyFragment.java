package com.neituiquan.work.company;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.google.gson.Gson;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.base.BaseFragment;
import com.neituiquan.dialog.DatePickerDialog;
import com.neituiquan.dialog.InputDialog;
import com.neituiquan.entity.CompanyEntity;
import com.neituiquan.gson.CompanyModel;
import com.neituiquan.httpEvent.AddCompanyEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.utils.PositionUtils;
import com.neituiquan.work.widgets.CitySelectorActivity;
import com.neituiquan.work.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Augustine on 2018/6/22.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 添加企业基本信息
 *
 */

public class AddCompanyFragment extends BaseFragment implements View.OnClickListener {

    public static AddCompanyFragment newInstance(CompanyEntity entity) {

        Bundle args = new Bundle();
        args.putSerializable("entity",entity);
        AddCompanyFragment fragment = new AddCompanyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView bindCompanyFG_backImg;
    private TextView bindCompanyFG_saveTv;
    private EditText bindCompanyFG_nameTv;
    private EditText bindCompanyFG_provinceTv;
    private EditText bindCompanyFG_cityTv;
    private EditText bindCompanyFG_addressTv;
    private TextView bindCompanyFG_creationTimeTv;
    private EditText bindCompanyFG_peopleNumTv;
    private EditText bindCompanyFG_linkUrlTv;
    private ImageView bindCompanyFG_addLabelImg;
    private FrameLayout bindCompanyFG_introduceLayout;
    private EditText bindCompanyFG_introduceTv;
    private View bindCompanyFG_emptyView;
    private ScrollView bindCompanyFG_scrollView;
    private LinearLayout bindCompanyFG_contentLayout;
    private TextView bindCompanyFG_nextTv;
    private LinearLayout bindCompanyFG_labelsLayout;
    private ImageView bindCompanyFG_toSelectorCity;
    private ImageView bindCompanyFG_toSelectorCity2;


    private CompanyEntity entity;

    private int keyboardHeight;

    private InputDialog inputDialog;

    private List<String> labelList = new ArrayList<>();

    private static final int START_TO_SELECTOR_CITY = 323;

    private static final int SELECTOR_CITY_RESULT_CODE = 333;

    private DatePickerDialog datePickerDialog;

    private PositionUtils.PositionCallBack locationListener = new PositionUtils.PositionCallBack() {
        @Override
        public void mapLocation(PositionUtils.LocationEntity locationEntity) {
            ((BindCompanyActivity)getContext()).getLoadingDialog().dismiss();
            //定位成功
            if(locationEntity.getErrorCode().equals("0")){
                bindCompanyFG_provinceTv.setText(locationEntity.getProvince());
                bindCompanyFG_cityTv.setText(locationEntity.getCity());
                bindCompanyFG_addressTv.setText(locationEntity.getAddress());
            }
        }
    };

    private PositionUtils positionUtils;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_add_company,null);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        entity = (CompanyEntity) getArguments().getSerializable("entity");
        changedSoft();
        inputDialog = new InputDialog(getContext());
        inputDialog.setInputDialogCallBack(inputDialogCallBack);
        if(entity == null){

        }else{
            initValues();
        }
        ((BindCompanyActivity)getContext()).getLoadingDialog().show();
        bindCompanyFG_nextTv.setVisibility(View.GONE);
        positionUtils = new PositionUtils();
        positionUtils.initGaoDeLocation(getContext(),locationListener);
        initDialog();
    }

    private void changedSoft(){
        bindCompanyFG_introduceTv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bindCompanyFG_introduceLayout.getLayoutParams();
                    params.height += SizeUtils.dp2px(500);
                    bindCompanyFG_introduceLayout.setLayoutParams(params);
                    LinearLayout.LayoutParams emptyParams = (LinearLayout.LayoutParams) bindCompanyFG_emptyView.getLayoutParams();
                    emptyParams.height = keyboardHeight;
                    bindCompanyFG_emptyView.setLayoutParams(emptyParams);
                    scrollToBottom(bindCompanyFG_scrollView,bindCompanyFG_contentLayout);
                }else{
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bindCompanyFG_introduceLayout.getLayoutParams();
                    params.height = SizeUtils.dp2px(100);
                    bindCompanyFG_introduceLayout.setLayoutParams(params);
                    LinearLayout.LayoutParams emptyParams = (LinearLayout.LayoutParams) bindCompanyFG_emptyView.getLayoutParams();
                    emptyParams.height = SizeUtils.dp2px(100);
                    bindCompanyFG_emptyView.setLayoutParams(emptyParams);
                }
            }
        });
        KeyboardUtils.registerSoftInputChangedListener(getActivity(), new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                if(height != 0){
                    keyboardHeight = height;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.bindCompanyFG_backImg:
                ((BindCompanyActivity)getContext()).finish();
                break;
            case R.id.bindCompanyFG_saveTv:
                if(entity == null){
                    save();
                }else{
                    saveChanged();
                }
                break;
            case R.id.bindCompanyFG_addLabelImg:
                inputDialog.show();
                break;
            case R.id.bindCompanyFG_nextTv:
                startToNextFragment();
                break;
            case R.id.bindCompanyFG_toSelectorCity:
            case R.id.bindCompanyFG_toSelectorCity2:
                startActivityForResult(new Intent(getContext(), CitySelectorActivity.class),START_TO_SELECTOR_CITY);
                break;
            case R.id.bindCompanyFG_creationTimeTv:
                datePickerDialog.show();
                break;
        }
    }

    private void initDialog(){
        datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setDialogCallBack(new DatePickerDialog.DatePickerDialogCallBack() {
            @Override
            public void onSelect(String date) {
                bindCompanyFG_creationTimeTv.setText(date);
            }
        });
        datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setDialogCallBack(new DatePickerDialog.DatePickerDialogCallBack() {
            @Override
            public void onSelect(String date) {
                bindCompanyFG_creationTimeTv.setText(date);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == SELECTOR_CITY_RESULT_CODE){
            if(requestCode == START_TO_SELECTOR_CITY){
                if(data != null){
                    String province = data.getStringExtra("province");
                    String city = data.getStringExtra("city");
                    bindCompanyFG_provinceTv.setText(province);
                    bindCompanyFG_cityTv.setText(city);
                }
            }
        }

    }

    private void save(){
        entity = new CompanyEntity();
        entity.setUserId(App.getAppInstance().getUserInfoUtils().getUserInfo().data.getId());
        entity.setCompanyName(bindCompanyFG_nameTv.getText().toString());
        entity.setProvince(bindCompanyFG_provinceTv.getText().toString());
        entity.setCity(bindCompanyFG_cityTv.getText().toString());
        entity.setAddress(bindCompanyFG_addressTv.getText().toString());
        entity.setCreationTime(bindCompanyFG_creationTimeTv.getText().toString());
        entity.setPeopleNum(bindCompanyFG_peopleNumTv.getText().toString());
        entity.setLinkUrl(bindCompanyFG_linkUrlTv.getText().toString());
        entity.setIntroduce(bindCompanyFG_introduceTv.getText().toString());
        if(labelList.size() != 0){
            entity.setLabels(new Gson().toJson(labelList));
        }
        String url = FinalData.BASE_URL + "/bindCompany";
        String json = new Gson().toJson(entity);
        HttpFactory.getHttpUtils().post(json,url,new AddCompanyEventModel(BindCompanyActivity.ADD_COMPANY));
    }

    private void saveChanged(){
        entity.setCompanyName(bindCompanyFG_nameTv.getText().toString());
        entity.setProvince(bindCompanyFG_provinceTv.getText().toString());
        entity.setCity(bindCompanyFG_cityTv.getText().toString());
        entity.setAddress(bindCompanyFG_addressTv.getText().toString());
        entity.setCreationTime(bindCompanyFG_creationTimeTv.getText().toString());
        entity.setPeopleNum(bindCompanyFG_peopleNumTv.getText().toString());
        entity.setLinkUrl(bindCompanyFG_linkUrlTv.getText().toString());
        entity.setIntroduce(bindCompanyFG_introduceTv.getText().toString());
        String url = FinalData.BASE_URL + "/updateCompany";
        String json = new Gson().toJson(entity);
        HttpFactory.getHttpUtils().post(json,url,new AddCompanyEventModel(BindCompanyActivity.UPDATE_COMPANY));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addResult(AddCompanyEventModel eventModel){
        switch (eventModel.eventId){
            case BindCompanyActivity.ADD_COMPANY:
                if(eventModel.isSuccess){
                    CompanyModel companyModel = new Gson().fromJson(eventModel.resultStr,CompanyModel.class);
                    entity = companyModel.data;
//                    Log.e("addResult",eventModel.resultStr);
                    startToNextFragment();
                }
                break;
            case BindCompanyActivity.UPDATE_COMPANY:
                if(eventModel.isSuccess){
                    ((BindCompanyActivity)getContext()).finish();
                }
                break;
        }
    }

    private void startToNextFragment(){
        final AddCompanyImgFragment addCompanyImgFragment = AddCompanyImgFragment.newInstance(entity);
        getFragmentManager()
                .beginTransaction()
                .add(R.id.bindCompanyUI_frameLayout,addCompanyImgFragment,"addCompanyImgFragment")
                .show(addCompanyImgFragment)
                .addToBackStack(null)
                .commit();
    }

    private void initValues(){
        bindCompanyFG_nameTv.setText(entity.getCompanyName());
        bindCompanyFG_provinceTv.setText(entity.getProvince());
        bindCompanyFG_cityTv.setText(entity.getCity());
        bindCompanyFG_addressTv.setText(entity.getAddress());
        bindCompanyFG_creationTimeTv.setText(entity.getCreationTime());
        bindCompanyFG_peopleNumTv.setText(entity.getPeopleNum());
        bindCompanyFG_linkUrlTv.setText(entity.getLinkUrl());
        bindCompanyFG_introduceTv.setText(entity.getIntroduce());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(positionUtils.getLocationClient() != null){
            positionUtils.getLocationClient().stopLocation();
        }
    }


    private InputDialog.InputDialogCallBack inputDialogCallBack = new InputDialog.InputDialogCallBack() {
        @Override
        public void executeTvClick(String inputMsg) {
            if(inputMsg == null){
                return;
            }
            if(inputMsg.length() == 0){
                return;
            }
            final TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_company_labels,bindCompanyFG_labelsLayout,false);
            textView.setText(inputMsg);
            bindCompanyFG_labelsLayout.addView(textView);
            labelList.add(inputMsg);
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    bindCompanyFG_labelsLayout.removeView(textView);
                    labelList.remove(textView.getText().toString());
                    return true;
                }
            });
        }
    };

    private void bindViews() {

        bindCompanyFG_backImg = (ImageView) findViewById(R.id.bindCompanyFG_backImg);
        bindCompanyFG_saveTv = (TextView) findViewById(R.id.bindCompanyFG_saveTv);
        bindCompanyFG_nameTv = (EditText) findViewById(R.id.bindCompanyFG_nameTv);
        bindCompanyFG_provinceTv = (EditText) findViewById(R.id.bindCompanyFG_provinceTv);
        bindCompanyFG_cityTv = (EditText) findViewById(R.id.bindCompanyFG_cityTv);
        bindCompanyFG_addressTv = (EditText) findViewById(R.id.bindCompanyFG_addressTv);
        bindCompanyFG_creationTimeTv = findViewById(R.id.bindCompanyFG_creationTimeTv);
        bindCompanyFG_peopleNumTv = (EditText) findViewById(R.id.bindCompanyFG_peopleNumTv);
        bindCompanyFG_linkUrlTv = (EditText) findViewById(R.id.bindCompanyFG_linkUrlTv);
        bindCompanyFG_labelsLayout = findViewById(R.id.bindCompanyFG_labelsLayout);
        bindCompanyFG_addLabelImg = (ImageView) findViewById(R.id.bindCompanyFG_addLabelImg);
        bindCompanyFG_introduceLayout = (FrameLayout) findViewById(R.id.bindCompanyFG_introduceLayout);
        bindCompanyFG_introduceTv = (EditText) findViewById(R.id.bindCompanyFG_introduceTv);
        bindCompanyFG_emptyView = findViewById(R.id.bindCompanyFG_emptyView);
        bindCompanyFG_scrollView = findViewById(R.id.bindCompanyFG_scrollView);
        bindCompanyFG_contentLayout = findViewById(R.id.bindCompanyFG_contentLayout);
        bindCompanyFG_nextTv = findViewById(R.id.bindCompanyFG_nextTv);
        bindCompanyFG_toSelectorCity = findViewById(R.id.bindCompanyFG_toSelectorCity);
        bindCompanyFG_toSelectorCity2 = findViewById(R.id.bindCompanyFG_toSelectorCity2);

        bindCompanyFG_backImg.setOnClickListener(this);
        bindCompanyFG_saveTv.setOnClickListener(this);
        bindCompanyFG_addLabelImg.setOnClickListener(this);
        bindCompanyFG_nextTv.setOnClickListener(this);
        bindCompanyFG_toSelectorCity.setOnClickListener(this);
        bindCompanyFG_toSelectorCity2.setOnClickListener(this);
        bindCompanyFG_creationTimeTv.setOnClickListener(this);

    }

    public static void scrollToBottom(final View scroll, final View inner) {

        Handler mHandler = new Handler();

        mHandler.post(new Runnable() {
            public void run() {
                if (scroll == null || inner == null) {
                    return;
                }
                int offset = inner.getMeasuredHeight() - scroll.getHeight();
                if (offset < 0) {
                    offset = 0;
                }

                scroll.scrollTo(0, offset);
            }
        });
    }

}
