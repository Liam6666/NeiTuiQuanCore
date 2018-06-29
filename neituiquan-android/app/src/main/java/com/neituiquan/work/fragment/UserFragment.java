package com.neituiquan.work.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.base.BaseFragment;
import com.neituiquan.gson.AbsModel;
import com.neituiquan.gson.StringModel;
import com.neituiquan.gson.UserModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.work.R;
import com.neituiquan.work.account.HeadImgActivity;
import com.neituiquan.work.account.SwitcherRoleActivity;
import com.neituiquan.work.company.BindCompanyActivity;
import com.neituiquan.work.company.CompanyDetailsActivity;
import com.neituiquan.work.jobs.ReleaseJobListActivity;
import com.neituiquan.work.jobs.ReleaseJobsActivity;
import com.neituiquan.work.resume.ResumeActivity;
import com.neituiquan.work.account.AccountActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by wangliang on 2018/6/17.
 */

public class UserFragment extends BaseFragment implements View.OnClickListener {

    public static UserFragment newInstance() {

        Bundle args = new Bundle();

        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private TextView userFG_nameTv;
    private TextView userFG_mottoTv;
    private CircleImageView userFG_headImg;
    private TextView userFG_bindCompanyTv;
    private LinearLayout userFG_intentionLayout;
    private LinearLayout userFG_myResumeLayout;
    private LinearLayout userFG_blackHouseLayout;
    private LinearLayout userFG_historyLayout;
    private LinearLayout userFG_publishLayout;
    private LinearLayout userFG_commentLayout;
    private LinearLayout userFG_bindCompanyLayout;
    private LinearLayout userFG_collectLayout;
    private LinearLayout userFG_feedbackLayout;
    private LinearLayout userFG_settingsLayout;
    private LinearLayout userFG_switcherRoleLayout;
    private TextView userFG_submitResumeTv;


    private UserModel userModel;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_user,null);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        userModel = App.getAppInstance().getUserInfoUtils().getUserInfo();
        if(userModel == null){
            //未登录
            initOutLoginUI();
        }else{
            initUserInfo();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.userFG_nameTv:
                if(userModel == null){
                    startActivity(new Intent(getContext(), AccountActivity.class));
                }
                break;
            case R.id.userFG_mottoTv:
                if(userModel == null){
                    startActivity(new Intent(getContext(), AccountActivity.class));
                }
                break;
            case R.id.userFG_headImg:
                startActivity(new Intent(getContext(),HeadImgActivity.class));
                break;
            case R.id.userFG_myResumeLayout:
                startActivity(new Intent(getContext(),ResumeActivity.class));
                break;
            case R.id.userFG_switcherRoleLayout:
                startActivity(new Intent(getContext(),SwitcherRoleActivity.class));
                break;
            case R.id.userFG_bindCompanyLayout:
                checkBindCompany();
                break;
            case R.id.userFG_publishLayout:
                startActivity(new Intent(getContext(),ReleaseJobListActivity.class));
                break;
        }
    }

    public void initOutLoginUI(){
        userFG_publishLayout.setVisibility(View.GONE);
        userFG_commentLayout.setVisibility(View.GONE);
        userFG_bindCompanyLayout.setVisibility(View.GONE);
        userFG_bindCompanyTv.setVisibility(View.GONE);
        userFG_intentionLayout.setVisibility(View.VISIBLE);
        userFG_myResumeLayout.setVisibility(View.VISIBLE);
        userFG_blackHouseLayout.setVisibility(View.VISIBLE);
        userFG_historyLayout.setVisibility(View.VISIBLE);
        userFG_submitResumeTv.setVisibility(View.VISIBLE);
    }

    private void initUserInfo(){
        userFG_nameTv.setText(userModel.data.getNickName());
        Glide.with(getContext()).load(FinalData.IMG + userModel.data.getHeadImg()).into(userFG_headImg);
        userFG_mottoTv.setText(userModel.data.getMotto());

        switcherMenuList();
    }

    private void checkBindCompany(){
        String url = FinalData.BASE_URL + "/bindCompanyState?id=" + App.getAppInstance().getUserInfoUtils().getUserInfo().data.getId();
        HttpFactory.getHttpUtils().get(url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                StringModel stringModel = new Gson().fromJson(response.body().string(),StringModel.class);
                if(stringModel.code == 0){
                    Intent intent = new Intent(getContext(),CompanyDetailsActivity.class);
                    intent.putExtra("companyId",stringModel.data);
                    startActivity(intent);
                }else{
                    startActivity(new Intent(getContext(),BindCompanyActivity.class));
                }
            }
        });
    }


    /**
     * 根据不同的用户身份切换不同的UI
     */
    private void switcherMenuList(){
        if(userModel == null){
            return;
        }
        String role = userModel.data.getRoleName();
        switch (role){
            case "找工作":
                userFG_publishLayout.setVisibility(View.GONE);
                userFG_commentLayout.setVisibility(View.GONE);
                userFG_bindCompanyLayout.setVisibility(View.GONE);
                userFG_bindCompanyTv.setVisibility(View.GONE);
                userFG_intentionLayout.setVisibility(View.VISIBLE);
                userFG_myResumeLayout.setVisibility(View.VISIBLE);
                userFG_blackHouseLayout.setVisibility(View.VISIBLE);
                userFG_historyLayout.setVisibility(View.VISIBLE);
                userFG_submitResumeTv.setVisibility(View.VISIBLE);
                break;
            case "找人":
            case "HR":
                userFG_publishLayout.setVisibility(View.VISIBLE);
                userFG_commentLayout.setVisibility(View.VISIBLE);
                userFG_bindCompanyLayout.setVisibility(View.VISIBLE);
                userFG_bindCompanyTv.setVisibility(View.VISIBLE);
                userFG_intentionLayout.setVisibility(View.GONE);
                userFG_myResumeLayout.setVisibility(View.GONE);
                userFG_blackHouseLayout.setVisibility(View.GONE);
                userFG_historyLayout.setVisibility(View.GONE);
                userFG_submitResumeTv.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 注册成功后 || 登录成功后
     * @param userModel
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(UserModel userModel){
        this.userModel = App.getAppInstance().getUserInfoUtils().getUserInfo();
        initUserInfo();
    }

    private void bindViews() {

        userFG_nameTv = (TextView) findViewById(R.id.userFG_nameTv);
        userFG_mottoTv = (TextView) findViewById(R.id.userFG_mottoTv);
        userFG_headImg = findViewById(R.id.userFG_headImg);
        userFG_bindCompanyTv = (TextView) findViewById(R.id.userFG_bindCompanyTv);
        userFG_intentionLayout = (LinearLayout) findViewById(R.id.userFG_intentionLayout);
        userFG_myResumeLayout = (LinearLayout) findViewById(R.id.userFG_myResumeLayout);
        userFG_blackHouseLayout = (LinearLayout) findViewById(R.id.userFG_blackHouseLayout);
        userFG_historyLayout = (LinearLayout) findViewById(R.id.userFG_historyLayout);
        userFG_publishLayout = (LinearLayout) findViewById(R.id.userFG_publishLayout);
        userFG_commentLayout = (LinearLayout) findViewById(R.id.userFG_commentLayout);
        userFG_bindCompanyLayout = (LinearLayout) findViewById(R.id.userFG_bindCompanyLayout);
        userFG_collectLayout = (LinearLayout) findViewById(R.id.userFG_collectLayout);
        userFG_feedbackLayout = (LinearLayout) findViewById(R.id.userFG_feedbackLayout);
        userFG_settingsLayout = (LinearLayout) findViewById(R.id.userFG_settingsLayout);
        userFG_switcherRoleLayout = findViewById(R.id.userFG_switcherRoleLayout);
        userFG_submitResumeTv = findViewById(R.id.userFG_submitResumeTv);

        userFG_nameTv.setOnClickListener(this);
        userFG_mottoTv.setOnClickListener(this);
        userFG_myResumeLayout.setOnClickListener(this);
        userFG_headImg.setOnClickListener(this);
        userFG_switcherRoleLayout.setOnClickListener(this);
        userFG_bindCompanyLayout.setOnClickListener(this);
        userFG_publishLayout.setOnClickListener(this);
    }


}
