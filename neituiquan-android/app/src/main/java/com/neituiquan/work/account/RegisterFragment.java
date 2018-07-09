package com.neituiquan.work.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.constant.RegexConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.base.BaseFragment;
import com.neituiquan.entity.UserEntity;
import com.neituiquan.gson.StringModel;
import com.neituiquan.httpEvent.GetCodeEventModel;
import com.neituiquan.httpEvent.RegisterEventModel;
import com.neituiquan.gson.UserModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.utils.PositionUtils;
import com.neituiquan.utils.VerificationCodeUtils;
import com.neituiquan.work.MainActivity;
import com.neituiquan.work.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

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
    private EditText registerFG_emailEdit;
    private View registerFG_emailBottomView;


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

    private static final int GET_CODE = 593;

    private String vCode = null;

    private CountDownTimer countDownTimer;

    private boolean timeDownFinish = true;

    private PositionUtils.PositionCallBack locationListener = new PositionUtils.PositionCallBack() {
        @Override
        public void mapLocation(PositionUtils.LocationEntity locationEntity) {
            ((AccountActivity)getContext()).getLoadingDialog().dismiss();
            //定位成功
            if(locationEntity.getErrorCode().equals("0")){
                latitude = String.valueOf(locationEntity.getLatitude());
                longitude = String.valueOf(locationEntity.getLongitude());
                accuracy = locationEntity.getAddress();
                province = locationEntity.getProvince();
                city = locationEntity.getCity();
                district = locationEntity.getDistrict();
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

        positionUtils = new PositionUtils();
        if(!PermissionUtils.isGranted(FinalData.PERMISSIONS)){
            PermissionUtils.permission(FinalData.PERMISSIONS).callback(new PermissionUtils.SimpleCallback() {
                @Override
                public void onGranted() {
                    ((AccountActivity)getContext()).getLoadingDialog().show();
                    positionUtils.initGaoDeLocation(getContext(),locationListener);
                }

                @Override
                public void onDenied() {
                    ((AccountActivity)getContext()).getLoadingDialog().dismiss();
                    ToastUtils.showShort("拒绝使用位置信息，将会影响您的正常使用");
                }
            }).request();
        }else{
            ((AccountActivity)getContext()).getLoadingDialog().show();
            positionUtils.initGaoDeLocation(getContext(),locationListener);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.registerFG_startTv:
                register();
                break;
            case R.id.registerFG_getCodeTv:
                getEmailCode();
                break;
        }
    }

    private void getEmailCode(){
        if(!timeDownFinish){
            ToastUtils.showShort("稍后再试");
            return;
        }
        String email = registerFG_emailEdit.getText().toString();
        email.trim();
        if(StringUtils.isEmpty(email)){
            ToastUtils.showShort("邮箱不能为空");
            return;
        }
        if(!RegexUtils.isEmail(email)){
            ToastUtils.showShort("邮箱地址有误");
            return;
        }
        timeDownFinish = false;
        registerFG_getCodeTv.setTextColor(ContextCompat.getColor(getContext(),R.color.buttonActiveColor));
        countDownTimer = new CountDownTimer(60*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                registerFG_getCodeTv.setText(millisUntilFinished / 1000 +"s后重新获取");
            }

            @Override
            public void onFinish() {
                registerFG_getCodeTv.setText("获取验证码");
                registerFG_getCodeTv.setTextColor(ContextCompat.getColor(getContext(),R.color.themeColor));
                timeDownFinish = true;
            }
        };
        countDownTimer.start();
        String url = FinalData.BASE_URL + "/sendCodeEmail?mailPath=%s&code=%s";
        vCode = VerificationCodeUtils.getCode();
        url = String.format(url,email,vCode);
        HttpFactory.getHttpUtils().get(url,new GetCodeEventModel(GET_CODE));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getCodeResult(GetCodeEventModel eventModel){
        if(eventModel.eventId == GET_CODE){

            if(eventModel.isSuccess){
                StringModel model = new Gson().fromJson(eventModel.resultStr,StringModel.class);
                if(model.code == 0){
                    ToastUtils.showShort("验证码发送成功");
                }else{
                    ToastUtils.showShort("验证码发送失败，检查邮箱地址是否有效");
                    registerFG_getCodeTv.setText("获取验证码");
                    registerFG_getCodeTv.setTextColor(ContextCompat.getColor(getContext(),R.color.themeColor));
                    timeDownFinish = true;
                    countDownTimer.cancel();
                }
            }
        }
    }

    private void register(){
        String account = registerFG_phoneEdit.getText().toString();
        String password = registerFG_passwordEdit.getText().toString();
        String email = registerFG_emailEdit.getText().toString();
        String inputCode = registerFG_codeEdit.getText().toString();
        account = account.trim();
        password = password.trim();
        email = email.trim();
        if(account.equals("") || password.equals("")){
            ToastUtils.showShort("账号或密码为空");
            return;
        }
        if(vCode == null){
            ToastUtils.showShort("先获取验证码");
            return;
        }
        if(!inputCode.equals(vCode)){
            ToastUtils.showShort("验证码有误");
            return;
        }
        if(!RegexUtils.isMobileSimple(account)){
            ToastUtils.showShort("手机号码不合法");
            return;
        }
        if(password.length() < 6){
            ToastUtils.showShort("密码至少6位");
        }
        UserEntity entity = new UserEntity();
        entity.setAccount(account);
        entity.setPassword(password);
        entity.setLatitude(latitude);
        entity.setLongitude(longitude);
        entity.setAccuracy(accuracy);
        entity.setProvince(province);
        entity.setCity(city);
        entity.setDistrict(district);
        ((AccountActivity)getContext()).getLoadingDialog().show();
        HttpFactory.getHttpUtils().post(new Gson().toJson(entity), FinalData.BASE_URL + "/register",new RegisterEventModel());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void registerResult(RegisterEventModel eventModel){
        if(eventModel.isSuccess){
            UserModel userModel = new Gson().fromJson(eventModel.resultStr,UserModel.class);
            if(userModel.code == 0){

                App.getAppInstance().getUserInfoUtils().saveUserInfo(eventModel.resultStr);

//                //发送给 UserFragment
//                EventBus.getDefault().post(userModel);
                ((AccountActivity)getContext()).finish();
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
        if(positionUtils.getLocationClient() != null){
            positionUtils.getLocationClient().stopLocation();
        }
    }


    private void initFocus(){
        focusColor = ContextCompat.getColor(getContext(),R.color.themeColor);
        unFocusColor = ContextCompat.getColor(getContext(),R.color.lineColor);
        registerFG_phoneEdit.setOnFocusChangeListener(this);
        registerFG_codeEdit.setOnFocusChangeListener(this);
        registerFG_passwordEdit.setOnFocusChangeListener(this);
        registerFG_emailEdit.setOnFocusChangeListener(this);
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
        registerFG_emailEdit = findViewById(R.id.registerFG_emailEdit);
        registerFG_emailBottomView = findViewById(R.id.registerFG_emailBottomView);
        registerFG_startTv.setOnClickListener(this);
        registerFG_getCodeTv.setOnClickListener(this);

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
            case R.id.registerFG_emailEdit:
                if(hasFocus){
                    registerFG_emailBottomView.setBackgroundColor(focusColor);
                }else{
                    registerFG_emailBottomView.setBackgroundColor(unFocusColor);
                }
                break;
        }
    }
}
