package com.neituiquan.work.account;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.base.BaseFragment;
import com.neituiquan.httpEvent.RegisterEventModel;
import com.neituiquan.gson.UserModel;
import com.neituiquan.net.HttpFactory;
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


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_register,null);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initFocus();

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
        if(account.equals("") || password.equals("")){
            ToastUtils.showShort("账号或密码为空");
            return;
        }
        HashMap<String,String> params = new HashMap<>();
        params.put("account",account);
        params.put("password",password);

        HttpFactory.getHttpUtils().post(params, FinalData.BASE_URL + "/register",new RegisterEventModel());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void registerResult(RegisterEventModel eventModel){
        if(eventModel.isSuccess){
            UserModel userModel = new Gson().fromJson(eventModel.resultStr,UserModel.class);
            if(userModel.code == 0){

                App.getAppInstance().getUserInfoUtils().saveUserInfo(eventModel.resultStr);

                //发送给 UserFragment
                EventBus.getDefault().post(userModel);
                ((AccountActivity)getContext()).finish();
            }else{
                ToastUtils.showShort(userModel.msg);
            }
        }
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
