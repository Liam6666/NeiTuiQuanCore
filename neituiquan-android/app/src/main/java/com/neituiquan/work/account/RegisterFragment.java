package com.neituiquan.work.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.constant.RegexConstants;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.base.BaseFragment;
import com.neituiquan.httpEvent.RegisterEventModel;
import com.neituiquan.gson.UserModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.utils.PositionUtils;
import com.neituiquan.work.MainActivity;
import com.neituiquan.work.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

/**
 * Created by Augustine on 2018/6/19.
 * <p>
 * email:nice_ohoh@163.com
 */

public class RegisterFragment extends BaseFragment implements View.OnFocusChangeListener, View.OnClickListener {

    public static RegisterFragment newInstance() {

        Bundle args = new Bundle();

        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private EditText registerFG_phoneEdit;
    private View registerFG_phoneBottomView;
    private EditText registerFG_codeEdit;
    private TextView registerFG_getCodeTv;
    private View registerFG_codeBottomView;
    private EditText registerFG_passwordEdit;
    private View registerFG_passwordBottomView;
    private TextView registerFG_startTv;
    private TextView registerFG_protocolTv;

    private int focusColor;

    private int unFocusColor;


    //纬度
    private  String latitude = "";

    //经度
    private  String longitude = "";

    //定位精确信息
    private  String accuracy = "";

    //省
    private  String province = "";

    //城市
    private  String city = "";

    //城区信息
    private  String district = "";

    private AMapLocationListener locationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            ((AccountActivity)getContext()).getLoadingDialog().dismiss();
            //定位成功
            if(aMapLocation.getErrorCode() == 0){
                latitude = String.valueOf(aMapLocation.getLatitude());
                longitude = String.valueOf(aMapLocation.getLongitude());
                accuracy = aMapLocation.getAddress();
                province = aMapLocation.getProvince();
                city = aMapLocation.getCity();
                district = aMapLocation.getDistrict();
            }
        }
    };

    private PositionUtils positionUtils;



    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_register,null);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initFocus();
        ((AccountActivity)getContext()).getLoadingDialog().show();
        positionUtils = new PositionUtils();
        positionUtils.initGaoDeLocation(getContext(),locationListener);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.registerFG_startTv:
                register();
                break;
        }
    }

    private void register(){
        String account = registerFG_phoneEdit.getText().toString();
        String password = registerFG_passwordEdit.getText().toString();
        account.trim();
        password.trim();
        if(account.equals("") || password.equals("")){
            ToastUtils.showShort("账号或密码为空");
            return;
        }
        if(!RegexUtils.isMobileSimple(account)){
            ToastUtils.showShort("手机号码不合法");
            return;
        }
        if(password.length() < 6){
            ToastUtils.showShort("密码至少6位");
        }
        HashMap<String,String> params = new HashMap<>();
        params.put("account",account);
        params.put("password",password);
        params.put("latitude",latitude);
        params.put("longitude",longitude);
        params.put("accuracy",accuracy);
        params.put("province",province);
        params.put("city",city);
        params.put("district",district);
        ((AccountActivity)getContext()).getLoadingDialog().show();
        HttpFactory.getHttpUtils().post(params, FinalData.BASE_URL + "/register",new RegisterEventModel());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void registerResult(RegisterEventModel eventModel){
        if(eventModel.isSuccess){
            UserModel userModel = new Gson().fromJson(eventModel.resultStr,UserModel.class);
            if(userModel.code == 0){

                App.getAppInstance().getUserInfoUtils().saveUserInfo(eventModel.resultStr);

//                //发送给 UserFragment
//                EventBus.getDefault().post(userModel);
//                ((AccountActivity)getContext()).finish();
                startActivity(new Intent(getContext(), MainActivity.class));
            }else{
                ToastUtils.showShort(userModel.msg);
            }
        }
        ((AccountActivity)getContext()).getLoadingDialog().dismiss();
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

    private void initFocus(){
        focusColor = ContextCompat.getColor(getContext(),R.color.themeColor);
        unFocusColor = ContextCompat.getColor(getContext(),R.color.lineColor);
        registerFG_phoneEdit.setOnFocusChangeListener(this);
        registerFG_codeEdit.setOnFocusChangeListener(this);
        registerFG_passwordEdit.setOnFocusChangeListener(this);
    }

    private void bindViews() {

        registerFG_phoneEdit = (EditText) findViewById(R.id.registerFG_phoneEdit);
        registerFG_phoneBottomView = (View) findViewById(R.id.registerFG_phoneBottomView);
        registerFG_codeEdit = (EditText) findViewById(R.id.registerFG_codeEdit);
        registerFG_getCodeTv = (TextView) findViewById(R.id.registerFG_getCodeTv);
        registerFG_codeBottomView = (View) findViewById(R.id.registerFG_codeBottomView);
        registerFG_passwordEdit = (EditText) findViewById(R.id.registerFG_passwordEdit);
        registerFG_passwordBottomView = (View) findViewById(R.id.registerFG_passwordBottomView);
        registerFG_startTv = (TextView) findViewById(R.id.registerFG_startTv);
        registerFG_protocolTv = (TextView) findViewById(R.id.registerFG_protocolTv);

        registerFG_startTv.setOnClickListener(this);

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.registerFG_phoneEdit:
                if(hasFocus){
                    registerFG_phoneBottomView.setBackgroundColor(focusColor);
                }else{
                    registerFG_phoneBottomView.setBackgroundColor(unFocusColor);
                }
                break;
            case R.id.registerFG_codeEdit:
                if(hasFocus){
                    registerFG_codeBottomView.setBackgroundColor(focusColor);
                }else{
                    registerFG_codeBottomView.setBackgroundColor(unFocusColor);
                }
                break;
            case R.id.registerFG_passwordEdit:
                if(hasFocus){
                    registerFG_passwordBottomView.setBackgroundColor(focusColor);
                }else{
                    registerFG_passwordBottomView.setBackgroundColor(unFocusColor);
                }
                break;
        }
    }
}
