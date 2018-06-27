package com.neituiquan.work.account;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.base.BaseFragment;
import com.neituiquan.gson.UserModel;
import com.neituiquan.httpEvent.LoginEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.utils.PositionUtils;
import com.neituiquan.work.R;
import com.neituiquan.work.company.BindCompanyActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

/**
 * Created by Augustine on 2018/6/19.
 * <p>
 * email:nice_ohoh@163.com
 */

public class LoginFragment extends BaseFragment implements View.OnFocusChangeListener, View.OnClickListener {

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private EditText loginFG_phoneEdit;
    private View loginFG_phoneBottomView;
    private EditText loginFG_passwordEdit;
    private View loginFG_passwordBottomView;
    private TextView loginFG_loginTv;
    private TextView loginFG_forgetPasswordTv;

    private int focusColor;

    private int unFocusColor;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_login,null);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initFocus();
    }

    private void initFocus(){
        focusColor = ContextCompat.getColor(getContext(),R.color.themeColor);
        unFocusColor = ContextCompat.getColor(getContext(),R.color.lineColor);
        loginFG_phoneEdit.setOnFocusChangeListener(this);
        loginFG_passwordEdit.setOnFocusChangeListener(this);
    }

    private void bindViews() {

        loginFG_phoneEdit = (EditText) findViewById(R.id.loginFG_phoneEdit);
        loginFG_phoneBottomView = (View) findViewById(R.id.loginFG_phoneBottomView);
        loginFG_passwordEdit = (EditText) findViewById(R.id.loginFG_passwordEdit);
        loginFG_passwordBottomView = (View) findViewById(R.id.loginFG_passwordBottomView);
        loginFG_loginTv = (TextView) findViewById(R.id.loginFG_loginTv);
        loginFG_forgetPasswordTv = (TextView) findViewById(R.id.loginFG_forgetPasswordTv);

        loginFG_loginTv.setOnClickListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.loginFG_phoneEdit:
                if(hasFocus){
                    loginFG_phoneBottomView.setBackgroundColor(focusColor);
                }else{
                    loginFG_phoneBottomView.setBackgroundColor(unFocusColor);
                }
                break;
            case R.id.loginFG_passwordEdit:
                if(hasFocus){
                    loginFG_passwordBottomView.setBackgroundColor(focusColor);
                }else{
                    loginFG_passwordBottomView.setBackgroundColor(unFocusColor);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.loginFG_loginTv:
                login();
                break;
        }
    }

    private void login(){
        String account = loginFG_phoneEdit.getText().toString();
        String password = loginFG_passwordEdit.getText().toString();
        if(account.equals("") || password.equals("")){
            ToastUtils.showShort("账号或密码为空");
            return;
        }

        ((AccountActivity)getContext()).getLoadingDialog().show();
        HashMap<String,String> params = new HashMap<>();
        params.put("account",account);
        params.put("password",password);
        HttpFactory.getHttpUtils().post(params, FinalData.BASE_URL + "/login",new LoginEventModel());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginResult(LoginEventModel eventModel){
        ((AccountActivity)getContext()).getLoadingDialog().dismiss();
        if(eventModel.isSuccess){
            UserModel userModel = new Gson().fromJson(eventModel.resultStr,UserModel.class);
            if(userModel.code == 0){

                App.getAppInstance().getUserInfoUtils().saveUserInfo(eventModel.resultStr);

                //发送给UserFragment
                EventBus.getDefault().post(userModel);
                ((AccountActivity)getContext()).finish();
            }else{
                ToastUtils.showShort(userModel.msg);
            }
        }
    }

}
