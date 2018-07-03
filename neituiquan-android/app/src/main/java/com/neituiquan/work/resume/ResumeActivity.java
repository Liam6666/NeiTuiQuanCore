package com.neituiquan.work.resume;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.base.BaseActivity;
import com.neituiquan.entity.UserResumeEntity;
import com.neituiquan.gson.UserModel;
import com.neituiquan.gson.UserResumeModel;
import com.neituiquan.httpEvent.AddResumeEventModel;
import com.neituiquan.httpEvent.RefreshResumeEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.work.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Augustine on 2018/6/19.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 个人简历
 *
 *
 */

public class ResumeActivity extends BaseActivity implements View.OnClickListener {


    private SmartRefreshLayout resumeUI_refreshLayout;
    private ScrollView resumeUI_scrollView;
    private FrameLayout resumeUI_headBGLayout;
    private ImageView resumeUI_headBGView;
    private CircleImageView resumeUI_headImg;
    private TextView resumeUI_nameTv;
    private TextView resumeUI_mottoTv;
    private LinearLayout resumeUI_editBaseInfoLayout;
    private TextView resumeUI_educationTv;
    private TextView resumeUI_workAgeTv;
    private TextView resumeUI_birthdayTv;
    private TextView resumeUI_targetCity;
    private TextView resumeUI_phoneTv;
    private TextView resumeUI_emailTv;
    private TextView resumeUI_targetWorkTv;
    private TextView resumeUI_targetSalaryTv;
    private LinearLayout resumeUI_editWorkLayout;
    private LinearLayout resumeUI_ELayout;
    private LinearLayout resumeUI_editSchoolLayout;
    private LinearLayout resumeUI_SLayout;
    private LinearLayout resumeUI_editProjectLayout;
    private LinearLayout resumeUI_PLayout;
    private LinearLayout resumeUI_editAWLayout;
    private LinearLayout resumeUI_ALayout;
    private TextView resumeUI_introductionTv;
    private View resumeUI_statusView;
    private ImageView resumeUI_backImg;
    private FrameLayout resumeUI_emptyLayout;
    private TextView resumeUI_addResumeTv;

    private UserModel userModel;

    private UserResumeModel resumeModel;

    public static final int REFRESH_ID = 2222;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_resume);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        initStatusBar();
        userModel = App.getAppInstance().getUserInfoUtils().getUserInfo();
        refresh();
        getLoadingDialog().show();
    }

    /**
     * 不允许编辑
     */
    private void noEdit(){
        resumeUI_editBaseInfoLayout.setVisibility(View.GONE);
        resumeUI_editWorkLayout.setVisibility(View.GONE);
        resumeUI_editProjectLayout.setVisibility(View.GONE);
        resumeUI_editAWLayout.setVisibility(View.GONE);
        resumeUI_editSchoolLayout.setVisibility(View.GONE);

    }

    private void initStatusBar() {
        int barHeight = BarUtils.getStatusBarHeight();
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, barHeight);
        resumeUI_statusView.setLayoutParams(params);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) resumeUI_headBGLayout.getLayoutParams();
                params1.height += BarUtils.getStatusBarHeight();
                resumeUI_headBGLayout.setLayoutParams(params1);
            }
        });
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        int editType = 0;
        int id = v.getId();
        switch (id){
            case R.id.resumeUI_backImg:
                finish();
                break;
            case R.id.resumeUI_editBaseInfoLayout:
                intent = new Intent(this,EditResumeActivity.class);
                editType = 0;
                intent.putExtra("editType",editType);
                intent.putExtra("resumeModel",resumeModel);
                break;
            case R.id.resumeUI_editWorkLayout:
                intent = new Intent(this,EditResumeActivity.class);
                editType = 1;
                intent.putExtra("editType",editType);
                intent.putExtra("resumeModel",resumeModel);
                break;
            case R.id.resumeUI_editSchoolLayout:
                intent = new Intent(this,EditResumeActivity.class);
                editType = 2;
                intent.putExtra("editType",editType);
                intent.putExtra("resumeModel",resumeModel);
                break;
            case R.id.resumeUI_editProjectLayout:
                intent = new Intent(this,EditResumeActivity.class);
                editType = 3;
                intent.putExtra("editType",editType);
                intent.putExtra("resumeModel",resumeModel);
                break;
            case R.id.resumeUI_editAWLayout:
                intent = new Intent(this,EditResumeActivity.class);
                editType = 4;
                intent.putExtra("editType",editType);
                intent.putExtra("resumeModel",resumeModel);
                break;
            case R.id.resumeUI_addResumeTv:
                //去添加简历
                addResume();
                break;
            case R.id.resumeUI_headImg:

                break;
        }
        if(intent != null){
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void addResume(){
        String url = FinalData.BASE_URL + "/addUserResume";
        UserResumeEntity entity = new UserResumeEntity();
        entity.setUserId(App.getAppInstance().getUserInfoUtils().getUserInfo().data.getId());

        String json = new Gson().toJson(entity);

        HttpFactory.getHttpUtils().post(json,url,new AddResumeEventModel());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addResumeResult(AddResumeEventModel eventModel){
        if(eventModel.isSuccess){
            resumeUI_emptyLayout.setVisibility(View.GONE);
            refresh();
        }
    }

    /**
     * 刷新方法
     */
    private void refresh(){
        String url = FinalData.BASE_URL + "/getUserResume?userId=" + userModel.data.getId();
        HttpFactory.getHttpUtils().get(url,new RefreshResumeEventModel(REFRESH_ID));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshResult(RefreshResumeEventModel eventModel){
        getLoadingDialog().dismiss();
        if(eventModel.eventId == REFRESH_ID){
            if(eventModel.isSuccess){
                resumeModel = new Gson().fromJson(eventModel.resultStr,UserResumeModel.class);
                userModel = App.getAppInstance().getUserInfoUtils().getUserInfo();
                if(resumeModel.code == 0){
                    initValues();
                }else{
                    //未添加简历
                    resumeUI_emptyLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    private void initValues(){
        resumeUI_emptyLayout.setVisibility(View.GONE);
        Glide.with(this).load(FinalData.IMG + userModel.data.getHeadImg()).into(resumeUI_headImg);
        resumeUI_nameTv.setText(userModel.data.getNickName());
        resumeUI_mottoTv.setText(userModel.data.getMotto());
        resumeUI_educationTv.setText(resumeModel.data.getEducation());
        resumeUI_workAgeTv.setText(resumeModel.data.getWorkAge());
        resumeUI_birthdayTv.setText(resumeModel.data.getBirthday());
        resumeUI_targetCity.setText(resumeModel.data.getTargetCity());
        resumeUI_phoneTv.setText(userModel.data.getAccount());
        resumeUI_emailTv.setText(userModel.data.getEmail());
        resumeUI_targetWorkTv.setText(resumeModel.data.getTargetWork());
        resumeUI_targetSalaryTv.setText(resumeModel.data.getTargetSalary());
        resumeUI_introductionTv.setText(resumeModel.data.getIntroduction());
        resumeUI_PLayout.removeAllViews();
        resumeUI_ALayout.removeAllViews();
        resumeUI_SLayout.removeAllViews();
        resumeUI_ELayout.removeAllViews();
        for(UserResumeEntity.ResumePEntity pEntity : resumeModel.data.getResumePList()){
            resumeUI_PLayout.addView(createProjectItems(pEntity));
        }
        for(UserResumeEntity.ResumeAEntity aEntity : resumeModel.data.getResumeAList()){
            resumeUI_ALayout.addView(createAWItems(aEntity));
        }
        for(UserResumeEntity.ResumeSEntity sEntity : resumeModel.data.getResumeSList()){
            resumeUI_SLayout.addView(createSchoolItems(sEntity));
        }
        for(UserResumeEntity.ResumeWEntity wEntity : resumeModel.data.getResumeWList()){
            resumeUI_ELayout.addView(createSchoolItems(wEntity));
        }
    }

    private LinearLayout createProjectItems(UserResumeEntity.ResumePEntity pEntity){
        LinearLayout itemView = (LinearLayout) View.inflate(this,R.layout.item_project,null);
        TextView itemProject_timeTv = itemView.findViewById(R.id.itemProject_timeTv);
        TextView itemProject_nameTv = itemView.findViewById(R.id.itemProject_nameTv);
        TextView itemProject_responsibilityTv = itemView.findViewById(R.id.itemProject_responsibilityTv);
        TextView itemProject_absTv = itemView.findViewById(R.id.itemProject_absTv);
        itemProject_timeTv.setText(pEntity.getStartTime() + "——" + pEntity.getEndTime());
        itemProject_nameTv.setText(pEntity.getProjectName());
        itemProject_responsibilityTv.setText(pEntity.getResponsibility());
        itemProject_absTv.setText(pEntity.getProjectAbs());
        return itemView;
    }

    private LinearLayout createAWItems(UserResumeEntity.ResumeAEntity aEntity){
        LinearLayout itemView = (LinearLayout) View.inflate(this,R.layout.item_aw,null);
        TextView itemAW_timeTv = itemView.findViewById(R.id.itemAW_timeTv);
        TextView itemAW_absTv = itemView.findViewById(R.id.itemAW_absTv);
        itemAW_timeTv.setText(aEntity.getCreationTime());
        itemAW_absTv.setText(aEntity.getRewardName());
        return itemView;
    }

    private LinearLayout createSchoolItems(UserResumeEntity.ResumeSEntity sEntity){
        LinearLayout itemView = (LinearLayout) View.inflate(this,R.layout.item_school,null);
        TextView itemSchool_timeTv = itemView.findViewById(R.id.itemSchool_timeTv);
        TextView itemSchool_nameTv = itemView.findViewById(R.id.itemSchool_nameTv);
        TextView itemSchool_professionTv = itemView.findViewById(R.id.itemSchool_professionTv);
        TextView itemSchool_educationTv = itemView.findViewById(R.id.itemSchool_educationTv);
        itemSchool_timeTv.setText(sEntity.getStartTime() + "——" + sEntity.getEndTime());
        itemSchool_nameTv.setText(sEntity.getSchoolName());
        itemSchool_professionTv.setText(sEntity.getProfession());
        itemSchool_educationTv.setText(sEntity.getEducation());
        return itemView;
    }

    private LinearLayout createSchoolItems(UserResumeEntity.ResumeWEntity wEntity){
        LinearLayout itemView = (LinearLayout) View.inflate(this,R.layout.item_work,null);
        TextView itemWork_timeTv = itemView.findViewById(R.id.itemWork_timeTv);
        TextView itemWork_nameTv = itemView.findViewById(R.id.itemWork_nameTv);
        TextView itemWork_cityTv = itemView.findViewById(R.id.itemWork_cityTv);
        TextView itemWork_jobTitleTv = itemView.findViewById(R.id.itemWork_jobTitleTv);
        itemWork_timeTv.setText(wEntity.getStartTime() + "——" + wEntity.getEndTime());
        itemWork_nameTv.setText(wEntity.getCompanyName());
        itemWork_cityTv.setText(wEntity.getCity());
        itemWork_jobTitleTv.setText(wEntity.getJobTitle());
        return itemView;
    }

    private void bindViews() {
        resumeUI_refreshLayout = (com.scwang.smartrefresh.layout.SmartRefreshLayout) findViewById(R.id.resumeUI_refreshLayout);
        resumeUI_scrollView = (ScrollView) findViewById(R.id.resumeUI_scrollView);
        resumeUI_headBGLayout = (FrameLayout) findViewById(R.id.resumeUI_headBGLayout);
        resumeUI_headBGView = (ImageView) findViewById(R.id.resumeUI_headBGView);
        resumeUI_headImg = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.resumeUI_headImg);
        resumeUI_nameTv = (TextView) findViewById(R.id.resumeUI_nameTv);
        resumeUI_mottoTv = (TextView) findViewById(R.id.resumeUI_mottoTv);
        resumeUI_editBaseInfoLayout = (LinearLayout) findViewById(R.id.resumeUI_editBaseInfoLayout);
        resumeUI_educationTv = (TextView) findViewById(R.id.resumeUI_educationTv);
        resumeUI_workAgeTv = (TextView) findViewById(R.id.resumeUI_workAgeTv);
        resumeUI_birthdayTv = (TextView) findViewById(R.id.resumeUI_birthdayTv);
        resumeUI_targetCity = (TextView) findViewById(R.id.resumeUI_targetCity);
        resumeUI_phoneTv = (TextView) findViewById(R.id.resumeUI_phoneTv);
        resumeUI_emailTv = (TextView) findViewById(R.id.resumeUI_emailTv);
        resumeUI_targetWorkTv = (TextView) findViewById(R.id.resumeUI_targetWorkTv);
        resumeUI_targetSalaryTv = (TextView) findViewById(R.id.resumeUI_targetSalaryTv);
        resumeUI_editWorkLayout = (LinearLayout) findViewById(R.id.resumeUI_editWorkLayout);
        resumeUI_ELayout = (LinearLayout) findViewById(R.id.resumeUI_ELayout);
        resumeUI_editSchoolLayout = (LinearLayout) findViewById(R.id.resumeUI_editSchoolLayout);
        resumeUI_SLayout = (LinearLayout) findViewById(R.id.resumeUI_SLayout);
        resumeUI_editProjectLayout = (LinearLayout) findViewById(R.id.resumeUI_editProjectLayout);
        resumeUI_PLayout = (LinearLayout) findViewById(R.id.resumeUI_PLayout);
        resumeUI_editAWLayout = (LinearLayout) findViewById(R.id.resumeUI_editAWLayout);
        resumeUI_ALayout = (LinearLayout) findViewById(R.id.resumeUI_ALayout);
        resumeUI_introductionTv = (TextView) findViewById(R.id.resumeUI_introductionTv);
        resumeUI_statusView = (View) findViewById(R.id.resumeUI_statusView);
        resumeUI_backImg = (ImageView) findViewById(R.id.resumeUI_backImg);
        resumeUI_emptyLayout = findViewById(R.id.resumeUI_emptyLayout);
        resumeUI_addResumeTv = findViewById(R.id.resumeUI_addResumeTv);


        resumeUI_backImg.setOnClickListener(this);
        resumeUI_editBaseInfoLayout.setOnClickListener(this);
        resumeUI_editWorkLayout.setOnClickListener(this);
        resumeUI_editSchoolLayout.setOnClickListener(this);
        resumeUI_editProjectLayout.setOnClickListener(this);
        resumeUI_editAWLayout.setOnClickListener(this);
        resumeUI_addResumeTv.setOnClickListener(this);
        resumeUI_headImg.setOnClickListener(this);
    }


}
