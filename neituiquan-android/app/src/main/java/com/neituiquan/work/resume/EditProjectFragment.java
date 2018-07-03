package com.neituiquan.work.resume;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.base.BaseFragment;
import com.neituiquan.dialog.DatePickerDialog;
import com.neituiquan.entity.UserResumeEntity;
import com.neituiquan.httpEvent.UpdateResumeEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.work.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Augustine on 2018/6/21.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 编辑项目经验
 */

public class EditProjectFragment extends BaseFragment implements View.OnClickListener {

    public static EditProjectFragment newInstance(UserResumeEntity.ResumePEntity pEntity) {

        Bundle args = new Bundle();
        args.putSerializable("pEntity",pEntity);
        EditProjectFragment fragment = new EditProjectFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private ImageView editProjectFG_backImg;
    private TextView editProjectFG_saveTv;
    private TextView editProjectFG_startTimeTv;
    private TextView editProjectFG_endTimeTv;
    private EditText editProjectFG_nameTv;
    private EditText editProjectFG_responsibilityTv;
    private EditText editProjectFG_linkTv;
    private EditText editProjectFG_absTv;
    private TextView editProjectFG_delTv;

    private UserResumeEntity.ResumePEntity pEntity;

    private DatePickerDialog startTimePickerDialog;

    private DatePickerDialog endTimePickerDialog;


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(),R.layout.fragment_edit_project,null);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        pEntity = (UserResumeEntity.ResumePEntity) getArguments().getSerializable("pEntity");
        if(pEntity == null){
            editProjectFG_delTv.setVisibility(View.GONE);
        }else{
            initValues();
        }
        initDialog();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.editProjectFG_backImg:
                getFragmentManager().popBackStack();
                break;
            case R.id.editProjectFG_saveTv:
                if(pEntity == null){
                    save();
                }else{
                    saveChanged();
                }
                break;
            case R.id.editProjectFG_delTv:
                del();
                break;
            case R.id.editProjectFG_startTimeTv:
                startTimePickerDialog.show();
                break;
            case R.id.editProjectFG_endTimeTv:
                endTimePickerDialog.show();
                break;
        }
    }

    private void initValues(){
        editProjectFG_startTimeTv.setText(pEntity.getStartTime());
        editProjectFG_endTimeTv.setText(pEntity.getEndTime());
        editProjectFG_nameTv.setText(pEntity.getProjectName());
        editProjectFG_responsibilityTv.setText(pEntity.getResponsibility());
        editProjectFG_linkTv.setText(pEntity.getLink());
        editProjectFG_absTv.setText(pEntity.getProjectAbs());
    }


    private void initDialog(){
        startTimePickerDialog = new DatePickerDialog(getContext());
        startTimePickerDialog.setDialogCallBack(new DatePickerDialog.DatePickerDialogCallBack() {
            @Override
            public void onSelect(String date) {
                editProjectFG_startTimeTv.setText(date);
            }
        });
        endTimePickerDialog = new DatePickerDialog(getContext());
        endTimePickerDialog.setDialogCallBack(new DatePickerDialog.DatePickerDialogCallBack() {
            @Override
            public void onSelect(String date) {
                editProjectFG_endTimeTv.setText(date);
            }
        });
    }

    private void del(){
        String url = FinalData.BASE_URL + "/delUserResumeP?id="+pEntity.getId();
        HttpFactory.getHttpUtils().get(url,new UpdateResumeEventModel(EditResumeActivity.DEL_RESUME_P));
    }

    public void save(){
        pEntity = new UserResumeEntity.ResumePEntity();
        pEntity.setUserId(App.getAppInstance().getUserInfoUtils().getUserInfo().data.getId());
        pEntity.setStartTime(editProjectFG_startTimeTv.getText().toString());
        pEntity.setEndTime(editProjectFG_endTimeTv.getText().toString());
        pEntity.setProjectName(editProjectFG_nameTv.getText().toString());
        pEntity.setResponsibility(editProjectFG_responsibilityTv.getText().toString());
        pEntity.setProjectAbs(editProjectFG_absTv.getText().toString());
        pEntity.setLink(editProjectFG_linkTv.getText().toString());
        String json = new Gson().toJson(pEntity);
        String url = FinalData.BASE_URL + "/addUserResumeP";
        HttpFactory.getHttpUtils().post(json,url,new UpdateResumeEventModel(EditResumeActivity.SAVED_RESUME_P));
    }

    public void saveChanged(){
        pEntity.setStartTime(editProjectFG_startTimeTv.getText().toString());
        pEntity.setEndTime(editProjectFG_endTimeTv.getText().toString());
        pEntity.setProjectName(editProjectFG_nameTv.getText().toString());
        pEntity.setResponsibility(editProjectFG_responsibilityTv.getText().toString());
        pEntity.setProjectAbs(editProjectFG_absTv.getText().toString());
        pEntity.setLink(editProjectFG_linkTv.getText().toString());
        String json = new Gson().toJson(pEntity);
        String url = FinalData.BASE_URL + "/updateUserResumeP";
        HttpFactory.getHttpUtils().post(json,url,new UpdateResumeEventModel(EditResumeActivity.UPDATE_RESUME_P));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void saveChangedResult(UpdateResumeEventModel eventModel){
        switch (eventModel.eventId){
            case EditResumeActivity.UPDATE_RESUME_P:
                if(eventModel.isSuccess){
                    ((EditResumeActivity)getContext()).refresh();
                    getFragmentManager().popBackStack();
                }
                break;
            case EditResumeActivity.SAVED_RESUME_P:
                if(eventModel.isSuccess){
                    ((EditResumeActivity)getContext()).refresh();
                    getFragmentManager().popBackStack();
                }
                break;
            case EditResumeActivity.DEL_RESUME_P:
                if(eventModel.isSuccess){
                    ((EditResumeActivity)getContext()).refresh();
                    getFragmentManager().popBackStack();
                }
                break;
        }
    }


    private void bindViews() {

        editProjectFG_backImg = (ImageView) findViewById(R.id.editProjectFG_backImg);
        editProjectFG_saveTv = (TextView) findViewById(R.id.editProjectFG_saveTv);
        editProjectFG_startTimeTv = (TextView) findViewById(R.id.editProjectFG_startTimeTv);
        editProjectFG_endTimeTv = (TextView) findViewById(R.id.editProjectFG_endTimeTv);
        editProjectFG_nameTv = (EditText) findViewById(R.id.editProjectFG_nameTv);
        editProjectFG_responsibilityTv = (EditText) findViewById(R.id.editProjectFG_responsibilityTv);
        editProjectFG_linkTv = (EditText) findViewById(R.id.editProjectFG_linkTv);
        editProjectFG_absTv = (EditText) findViewById(R.id.editProjectFG_absTv);
        editProjectFG_delTv = (TextView) findViewById(R.id.editProjectFG_delTv);

        editProjectFG_backImg.setOnClickListener(this);
        editProjectFG_saveTv.setOnClickListener(this);
        editProjectFG_delTv.setOnClickListener(this);
        editProjectFG_startTimeTv.setOnClickListener(this);
        editProjectFG_endTimeTv.setOnClickListener(this);
    }


}
