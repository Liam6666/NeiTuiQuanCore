package com.neituiquan.work.jobs;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.google.gson.Gson;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.base.BaseActivity;
import com.neituiquan.dialog.InputDialog;
import com.neituiquan.dialog.SinglePickerDialog;
import com.neituiquan.entity.JobsEntity;
import com.neituiquan.httpEvent.ReleaseJobEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.work.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Augustine on 2018/6/22.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 发布职位
 */

public class ReleaseJobsActivity extends BaseActivity implements View.OnClickListener {

    private View releaseJobUI_statusView;
    private ImageView releaseJobUI_backImg;
    private TextView releaseJobUI_releaseTv;
    private EditText releaseJobUI_titleTv;
    private ImageView releaseJobUI_addLabelImg;
    private LinearLayout releaseJobUI_labelLayout;
    private TextView releaseJobUI_educationTv;
    private EditText releaseJobUI_minSalaryTv;
    private EditText releaseJobUI_maxSalaryTv;
    private FrameLayout releaseJobUI_descriptionLayout;
    private EditText releaseJobUI_descriptionTv;
    private View releaseJobUI_emptyView;
    private ScrollView releaseJobUI_scrollView;
    private LinearLayout releaseJobUI_contentLayout;
    private EditText releaseJobUI_workAgeTv;
    private TextView releaseJobFG_delTv;
    private LinearLayout releaseJobUI_educationLayout;

    private int keyboardHeight;

    private JobsEntity jobsEntity;

    private List<String> labelList = new ArrayList<>();

    private InputDialog inputDialog;

    public static final int ADD = 0;

    public static final int UPDATE = 1;

    public static final int DELETE = 2;

    private SinglePickerDialog educationPickerDialog;//学历选择

    private List<String> educationSelectorList = new ArrayList<>();

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_release_job);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        changedSoft();
        initStatusBar();
        jobsEntity = (JobsEntity) getIntent().getSerializableExtra("jobsEntity");
        inputDialog = new InputDialog(this);
        inputDialog.setInputDialogCallBack(inputDialogCallBack);
        if(jobsEntity == null){
            releaseJobFG_delTv.setVisibility(View.GONE);
        }else{
            initValues();
        }
        initEducationPicker();
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.releaseJobUI_backImg:
                finish();
                break;
            case R.id.releaseJobUI_releaseTv:
                if(jobsEntity == null){
                    save();
                }else{
                    saveChanged();
                }
                break;
            case R.id.releaseJobUI_addLabelImg:
                inputDialog.show();
                break;
            case R.id.releaseJobFG_delTv:
                del();
                break;
            case R.id.releaseJobUI_educationLayout:
                educationPickerDialog.show(releaseJobUI_educationTv.getText().toString());
                break;
        }
    }

    private void initEducationPicker(){
        educationSelectorList.add("高中");
        educationSelectorList.add("大专/专科");
        educationSelectorList.add("本科");
        educationSelectorList.add("硕士");
        educationSelectorList.add("博士");
        educationSelectorList.add("其他");
        educationPickerDialog = new SinglePickerDialog(this);
        educationPickerDialog.setContentData(educationSelectorList);
        educationPickerDialog.setDialogCallBack(new SinglePickerDialog.SinglePickerDialogCallBack() {
            @Override
            public void onSelect(String info) {
                releaseJobUI_educationTv.setText(info);
            }
        });
    }

    private void initValues(){
        releaseJobUI_titleTv.setText(jobsEntity.getTitle());
        releaseJobUI_educationTv.setText(jobsEntity.getEducation());
        releaseJobUI_minSalaryTv.setText(jobsEntity.getMinSalary());
        releaseJobUI_maxSalaryTv.setText(jobsEntity.getMaxSalary());
        releaseJobUI_workAgeTv.setText(jobsEntity.getWorkAge());
        releaseJobUI_descriptionTv.setText(jobsEntity.getDescription());
        try {
            JSONArray jsonArray = new JSONArray(jobsEntity.getLabels());
            for(int i = 0 ; i < jsonArray.length() ; i ++){
                labelList.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(String label : labelList){
            final TextView textView = (TextView) LayoutInflater.from(ReleaseJobsActivity.this).inflate(R.layout.item_company_labels,releaseJobUI_labelLayout,false);
            textView.setText(label);
            releaseJobUI_labelLayout.addView(textView);
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    releaseJobUI_labelLayout.removeView(textView);
                    labelList.remove(textView.getText().toString());
                    return true;
                }
            });
        }
    }

    private void save(){
        jobsEntity = new JobsEntity();
        jobsEntity.setUserId(App.getAppInstance().getUserInfoUtils().getUserInfo().data.getId());
        jobsEntity.setTitle(releaseJobUI_titleTv.getText().toString());
        jobsEntity.setLabels(new Gson().toJson(labelList));
        jobsEntity.setWorkAge(releaseJobUI_workAgeTv.getText().toString());
        jobsEntity.setEducation(releaseJobUI_educationTv.getText().toString());
        jobsEntity.setMinSalary(releaseJobUI_minSalaryTv.getText().toString());
        jobsEntity.setMaxSalary(releaseJobUI_maxSalaryTv.getText().toString());
        jobsEntity.setDescription(releaseJobUI_descriptionTv.getText().toString());
        String url = FinalData.BASE_URL + "/addJobs";
        String json = new Gson().toJson(jobsEntity);
        HttpFactory.getHttpUtils().post(json,url,new ReleaseJobEventModel(ADD));
    }

    private void saveChanged(){
        jobsEntity.setTitle(releaseJobUI_titleTv.getText().toString());
        jobsEntity.setLabels(new Gson().toJson(labelList));
        jobsEntity.setWorkAge(releaseJobUI_workAgeTv.getText().toString());
        jobsEntity.setEducation(releaseJobUI_educationTv.getText().toString());
        jobsEntity.setMinSalary(releaseJobUI_minSalaryTv.getText().toString());
        jobsEntity.setMaxSalary(releaseJobUI_maxSalaryTv.getText().toString());
        jobsEntity.setDescription(releaseJobUI_descriptionTv.getText().toString());
        String url = FinalData.BASE_URL + "/updateJobs";
        String json = new Gson().toJson(jobsEntity);
        HttpFactory.getHttpUtils().post(json,url,new ReleaseJobEventModel(UPDATE));
    }

    private void del(){
        String url = FinalData.BASE_URL + "/delJobs?id=" + jobsEntity.getId();
        HttpFactory.getHttpUtils().get(url,new ReleaseJobEventModel(DELETE));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void releaseResult(ReleaseJobEventModel eventModel){
        switch (eventModel.eventId){
            case ADD:
                if(eventModel.isSuccess){
                    finish();
                }
                break;
            case UPDATE:
                if(eventModel.isSuccess){
                    finish();
                }
                break;
            case DELETE:
                if(eventModel.isSuccess){
                    finish();
                }
                break;
        }
    }

    private void changedSoft(){
        releaseJobUI_descriptionTv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) releaseJobUI_descriptionLayout.getLayoutParams();
                    params.height += SizeUtils.dp2px(500);
                    releaseJobUI_descriptionLayout.setLayoutParams(params);
                    LinearLayout.LayoutParams emptyParams = (LinearLayout.LayoutParams) releaseJobUI_emptyView.getLayoutParams();
                    emptyParams.height = keyboardHeight;
                    releaseJobUI_emptyView.setLayoutParams(emptyParams);
                    scrollToBottom(releaseJobUI_scrollView,releaseJobUI_contentLayout);
                }else{
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) releaseJobUI_descriptionLayout.getLayoutParams();
                    params.height = SizeUtils.dp2px(100);
                    releaseJobUI_descriptionLayout.setLayoutParams(params);
                    LinearLayout.LayoutParams emptyParams = (LinearLayout.LayoutParams) releaseJobUI_emptyView.getLayoutParams();
                    emptyParams.height = SizeUtils.dp2px(100);
                    releaseJobUI_emptyView.setLayoutParams(emptyParams);
                }
            }
        });
        KeyboardUtils.registerSoftInputChangedListener(this, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                if(height != 0){
                    keyboardHeight = height;
                }
            }
        });
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
            final TextView textView = (TextView) LayoutInflater.from(ReleaseJobsActivity.this).inflate(R.layout.item_company_labels,releaseJobUI_labelLayout,false);
            textView.setText(inputMsg);
            releaseJobUI_labelLayout.addView(textView);
            labelList.add(inputMsg);
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    releaseJobUI_labelLayout.removeView(textView);
                    labelList.remove(textView.getText().toString());
                    return true;
                }
            });
        }
    };



    private void initStatusBar(){
        int barHeight = BarUtils.getStatusBarHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,barHeight);
        releaseJobUI_statusView.setLayoutParams(params);
    }

    private void bindViews() {

        releaseJobUI_statusView = (View) findViewById(R.id.releaseJobUI_statusView);
        releaseJobUI_backImg = (ImageView) findViewById(R.id.releaseJobUI_backImg);
        releaseJobUI_releaseTv = (TextView) findViewById(R.id.releaseJobUI_releaseTv);
        releaseJobUI_titleTv = (EditText) findViewById(R.id.releaseJobUI_titleTv);
        releaseJobUI_addLabelImg = (ImageView) findViewById(R.id.releaseJobUI_addLabelImg);
        releaseJobUI_labelLayout = (LinearLayout) findViewById(R.id.releaseJobUI_labelLayout);
        releaseJobUI_educationTv = (TextView) findViewById(R.id.releaseJobUI_educationTv);
        releaseJobUI_minSalaryTv = (EditText) findViewById(R.id.releaseJobUI_minSalaryTv);
        releaseJobUI_maxSalaryTv = (EditText) findViewById(R.id.releaseJobUI_maxSalaryTv);
        releaseJobUI_descriptionLayout = (FrameLayout) findViewById(R.id.releaseJobUI_descriptionLayout);
        releaseJobUI_descriptionTv = (EditText) findViewById(R.id.releaseJobUI_descriptionTv);
        releaseJobUI_emptyView = (View) findViewById(R.id.releaseJobUI_emptyView);
        releaseJobUI_scrollView = findViewById(R.id.releaseJobUI_scrollView);
        releaseJobUI_contentLayout = findViewById(R.id.releaseJobUI_contentLayout);
        releaseJobUI_workAgeTv = findViewById(R.id.releaseJobUI_workAgeTv);
        releaseJobFG_delTv = findViewById(R.id.releaseJobFG_delTv);
        releaseJobUI_educationLayout = findViewById(R.id.releaseJobUI_educationLayout);
        releaseJobUI_backImg.setOnClickListener(this);
        releaseJobUI_addLabelImg.setOnClickListener(this);
        releaseJobUI_releaseTv.setOnClickListener(this);
        releaseJobFG_delTv.setOnClickListener(this);
        releaseJobUI_educationLayout.setOnClickListener(this);
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

    @Override
    protected void onDestroy() {
        KeyboardUtils.unregisterSoftInputChangedListener(this);
        KeyboardUtils.fixSoftInputLeaks(this);
        super.onDestroy();
    }

}
