package com.neituiquan.work.resume;

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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.base.BaseFragment;
import com.neituiquan.dialog.DatePickerDialog;
import com.neituiquan.dialog.SinglePickerDialog;
import com.neituiquan.gson.UserModel;
import com.neituiquan.gson.UserResumeModel;
import com.neituiquan.httpEvent.UpdateResumeEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.utils.PositionUtils;
import com.neituiquan.work.CitySelectorActivity;
import com.neituiquan.work.R;
import com.weigan.loopview.OnItemSelectedListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Augustine on 2018/6/19.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 简历基本信息
 */

public class BaseInfoFragment extends BaseFragment implements View.OnClickListener {

    public static BaseInfoFragment newInstance() {

        Bundle args = new Bundle();

        BaseInfoFragment fragment = new BaseInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private TextView baseInfoFG_saveTv;
    private EditText baseInfoFG_nameTv;
    private TextView baseInfoFG_sexTv;
    private TextView baseInfoFG_birthdayTv;
    private TextView baseInfoFG_educationTv;
    private EditText baseInfoFG_workAgeTv;
    private EditText baseInfoFG_phoneTv;
    private EditText baseInfoFG_emailTv;
    private TextView baseInfoFG_locationTv;
    private EditText baseInfoFG_targetWorkTv;
    private EditText baseInfoFG_targetSalaryTv;
    private FrameLayout baseInfoFG_introductionLayout;
    private EditText baseInfoFG_introductionTv;
    private View baseInfoFG_emptyView;
    private ScrollView baseInfoFG_scrollView;
    private LinearLayout baseInfoFG_linearLayout;
    private ImageView baseInfoFG_backImg;
    private EditText baseInfoFG_mottoTv;
    private LinearLayout baseInfoFG_birthdayLayout;
    private LinearLayout baseInfoFG_sexLayout;
    private LinearLayout baseInfoFG_educationLayout;

    private int keyboardHeight = 0;

    private UserResumeModel resumeModel;

    private PositionUtils positionUtils;

    private DatePickerDialog datePickerDialog;

    private SinglePickerDialog sexPickerDialog;

    private SinglePickerDialog educationPickerDialog;//学历选择

    private List<String> sexSelectorList = new ArrayList<>();

    private List<String> educationSelectorList = new ArrayList<>();

    private static final int START_TO_SELECTOR_CITY = 323;

    private static final int SELECTOR_CITY_RESULT_CODE = 333;

    private AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            ((EditResumeActivity)getContext()).getLoadingDialog().dismiss();
            if(aMapLocation.getErrorCode() == 0){
                baseInfoFG_locationTv.setText(aMapLocation.getCity());
            }else{
                baseInfoFG_locationTv.setText("未知位置");
                ToastUtils.showShort("定位失败，请手动选择");
            }
        }
    };

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_base_info,null);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        changedSoft();
        resumeModel = ((EditResumeActivity)getContext()).getResumeModel();
        initValues();
        ((EditResumeActivity)getContext()).getLoadingDialog().show();
        positionUtils = new PositionUtils();
        positionUtils.initGaoDeLocation(getContext(),locationListener);
        initTimePicker();
        initSexPickerDialog();
        initEducationPicker();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.baseInfoFG_backImg:
                ((EditResumeActivity)getContext()).finish();
                break;
            case R.id.baseInfoFG_saveTv:
                saveChanged();
                break;
            case R.id.baseInfoFG_birthdayLayout:
                datePickerDialog.show();
                break;
            case R.id.baseInfoFG_sexLayout:
                sexPickerDialog.show(baseInfoFG_sexTv.getText().toString());
                break;
            case R.id.baseInfoFG_educationLayout:
                educationPickerDialog.show(baseInfoFG_educationTv.getText().toString());
                break;
            case R.id.baseInfoFG_locationTv:
                startActivityForResult(new Intent(getContext(), CitySelectorActivity.class),START_TO_SELECTOR_CITY);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == SELECTOR_CITY_RESULT_CODE){
            if(requestCode == START_TO_SELECTOR_CITY){
                if(data != null){
                    String province = data.getStringExtra("province");
                    String city = data.getStringExtra("city");
                    baseInfoFG_locationTv.setText(city);
                }
            }
        }
    }

    private void initSexPickerDialog(){
        sexPickerDialog = new SinglePickerDialog(getContext());
        sexSelectorList.add("男");
        sexSelectorList.add("女");
        sexSelectorList.add("不方便透露");
        sexPickerDialog.setContentData(sexSelectorList);
        sexPickerDialog.setDialogCallBack(new SinglePickerDialog.SinglePickerDialogCallBack() {
            @Override
            public void onSelect(String info) {
                baseInfoFG_sexTv.setText(info);
            }
        });
    }

    private void initEducationPicker(){
        educationSelectorList.add("高中");
        educationSelectorList.add("大专/专科");
        educationSelectorList.add("本科");
        educationSelectorList.add("硕士");
        educationSelectorList.add("博士");
        educationSelectorList.add("其他");
        educationPickerDialog = new SinglePickerDialog(getContext());
        educationPickerDialog.setContentData(educationSelectorList);
        educationPickerDialog.setDialogCallBack(new SinglePickerDialog.SinglePickerDialogCallBack() {
            @Override
            public void onSelect(String info) {
                baseInfoFG_educationTv.setText(info);
            }
        });
    }

    private void initTimePicker(){
        datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setDialogCallBack(new DatePickerDialog.DatePickerDialogCallBack() {
            @Override
            public void onSelect(String date) {
                baseInfoFG_birthdayTv.setText(date);
            }
        });
    }

    /**
     * 保存修改的信息
     */
    private void saveChanged(){
        ((EditResumeActivity)getContext()).getLoadingDialog().show();

        resumeModel.data.setTargetCity(baseInfoFG_locationTv.getText().toString());
        resumeModel.data.setTargetWork(baseInfoFG_targetWorkTv.getText().toString());
        resumeModel.data.setTargetSalary(baseInfoFG_targetSalaryTv.getText().toString());
        resumeModel.data.setIntroduction(baseInfoFG_introductionTv.getText().toString());
        resumeModel.data.setEducation(baseInfoFG_educationTv.getText().toString());
        resumeModel.data.setBirthday(baseInfoFG_birthdayTv.getText().toString());
        resumeModel.data.setWorkAge(baseInfoFG_workAgeTv.getText().toString());
        UserModel userModel = App.getAppInstance().getUserInfoUtils().getUserInfo();
        userModel.data.setNickName(baseInfoFG_nameTv.getText().toString());
        userModel.data.setSex(baseInfoFG_sexTv.getText().toString());
        userModel.data.setEmail(baseInfoFG_emailTv.getText().toString());
        userModel.data.setMotto(baseInfoFG_mottoTv.getText().toString());
        String json = new Gson().toJson(resumeModel.data);
        String url = FinalData.BASE_URL + "/updateUserResume";
        HttpFactory.getHttpUtils().post(json,url,new UpdateResumeEventModel(EditResumeActivity.UPDATE_RESUME));
        String json2 = new Gson().toJson(userModel.data);
        String url2 = FinalData.BASE_URL + "/updateUser";
        HttpFactory.getHttpUtils().post(json2,url2,new UpdateResumeEventModel(EditResumeActivity.UPDATE_USER_INFO));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateResult(UpdateResumeEventModel eventModel){
        ((EditResumeActivity)getContext()).getLoadingDialog().dismiss();
        switch (eventModel.eventId){
            case EditResumeActivity.UPDATE_RESUME:
                if(eventModel.isSuccess){
//                    ToastUtils.showShort("保存成功");
                }
                break;
            case EditResumeActivity.UPDATE_USER_INFO:
                if(eventModel.isSuccess){
                    //更新用户信息
                    App.getAppInstance().getUserInfoUtils().saveUserInfo(eventModel.resultStr);

                    UserModel userModel = new Gson().fromJson(eventModel.resultStr,UserModel.class);

                    //发送给UserFragment, 更新信息
                    EventBus.getDefault().post(userModel);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((EditResumeActivity)getContext()).finish();
                        }
                    },100);
                }
                break;
        }
    }


    private void initValues(){
        UserModel userModel = App.getAppInstance().getUserInfoUtils().getUserInfo();
        baseInfoFG_nameTv.setText(userModel.data.getNickName());
        baseInfoFG_sexTv.setText(userModel.data.getSex());
        baseInfoFG_birthdayTv.setText(resumeModel.data.getBirthday());
        baseInfoFG_educationTv.setText(resumeModel.data.getEducation());
        baseInfoFG_workAgeTv.setText(resumeModel.data.getWorkAge());
        baseInfoFG_emailTv.setText(userModel.data.getEmail());
        baseInfoFG_targetWorkTv.setText(resumeModel.data.getTargetWork());
        baseInfoFG_targetSalaryTv.setText(resumeModel.data.getTargetSalary());
        baseInfoFG_phoneTv.setText(userModel.data.getAccount());
//        baseInfoFG_locationTv
        baseInfoFG_introductionTv.setText(resumeModel.data.getIntroduction());
        baseInfoFG_mottoTv.setText(userModel.data.getMotto());
    }

    private void changedSoft(){
        baseInfoFG_introductionTv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) baseInfoFG_introductionLayout.getLayoutParams();
                    params.height += SizeUtils.dp2px(500);
                    baseInfoFG_introductionLayout.setLayoutParams(params);
                    LinearLayout.LayoutParams emptyParams = (LinearLayout.LayoutParams) baseInfoFG_emptyView.getLayoutParams();
                    emptyParams.height = keyboardHeight;
                    baseInfoFG_emptyView.setLayoutParams(emptyParams);
                    scrollToBottom(baseInfoFG_scrollView,baseInfoFG_linearLayout);
                }else{
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) baseInfoFG_introductionLayout.getLayoutParams();
                    params.height = SizeUtils.dp2px(100);
                    baseInfoFG_introductionLayout.setLayoutParams(params);
                    LinearLayout.LayoutParams emptyParams = (LinearLayout.LayoutParams) baseInfoFG_emptyView.getLayoutParams();
                    emptyParams.height = SizeUtils.dp2px(100);
                    baseInfoFG_emptyView.setLayoutParams(emptyParams);
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
    public void onDetach() {
        super.onDetach();
        positionUtils.getLocationClient().stopLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        positionUtils.getLocationClient().onDestroy();
    }

    private void bindViews() {

        baseInfoFG_saveTv = (TextView) findViewById(R.id.baseInfoFG_saveTv);
        baseInfoFG_nameTv = (EditText) findViewById(R.id.baseInfoFG_nameTv);
        baseInfoFG_sexTv = (TextView) findViewById(R.id.baseInfoFG_sexTv);
        baseInfoFG_birthdayTv = (TextView) findViewById(R.id.baseInfoFG_birthdayTv);
        baseInfoFG_educationTv = (TextView) findViewById(R.id.baseInfoFG_educationTv);
        baseInfoFG_workAgeTv = (EditText) findViewById(R.id.baseInfoFG_workAgeTv);
        baseInfoFG_phoneTv = (EditText) findViewById(R.id.baseInfoFG_phoneTv);
        baseInfoFG_emailTv = (EditText) findViewById(R.id.baseInfoFG_emailTv);
        baseInfoFG_locationTv = (TextView) findViewById(R.id.baseInfoFG_locationTv);
        baseInfoFG_targetWorkTv = (EditText) findViewById(R.id.baseInfoFG_targetWorkTv);
        baseInfoFG_targetSalaryTv = (EditText) findViewById(R.id.baseInfoFG_targetSalaryTv);
        baseInfoFG_introductionLayout = (FrameLayout) findViewById(R.id.baseInfoFG_introductionLayout);
        baseInfoFG_introductionTv = (EditText) findViewById(R.id.baseInfoFG_introductionTv);
        baseInfoFG_emptyView = (View) findViewById(R.id.baseInfoFG_emptyView);
        baseInfoFG_scrollView = findViewById(R.id.baseInfoFG_scrollView);
        baseInfoFG_linearLayout = findViewById(R.id.baseInfoFG_linearLayout);
        baseInfoFG_backImg = findViewById(R.id.baseInfoFG_backImg);
        baseInfoFG_mottoTv = findViewById(R.id.baseInfoFG_mottoTv);
        baseInfoFG_birthdayLayout = findViewById(R.id.baseInfoFG_birthdayLayout);
        baseInfoFG_sexLayout = findViewById(R.id.baseInfoFG_sexLayout);
        baseInfoFG_educationLayout = findViewById(R.id.baseInfoFG_educationLayout);
        baseInfoFG_backImg.setOnClickListener(this);
        baseInfoFG_saveTv.setOnClickListener(this);
        baseInfoFG_birthdayLayout.setOnClickListener(this);
        baseInfoFG_sexLayout.setOnClickListener(this);
        baseInfoFG_educationLayout.setOnClickListener(this);
        baseInfoFG_locationTv.setOnClickListener(this);
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
