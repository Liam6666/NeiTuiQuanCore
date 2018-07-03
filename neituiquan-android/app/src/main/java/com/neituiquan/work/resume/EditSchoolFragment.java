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
 */

public class EditSchoolFragment extends BaseFragment implements View.OnClickListener {

    public static EditSchoolFragment newInstance(UserResumeEntity.ResumeSEntity sEntity) {

        Bundle args = new Bundle();
        args.putSerializable("sEntity",sEntity);
        EditSchoolFragment fragment = new EditSchoolFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView editSchoolFG_backImg;
    private TextView editSchoolFG_saveTv;
    private TextView editSchoolFG_startTimeTv;
    private TextView editSchoolFG_endTimeTv;
    private EditText editSchoolFG_nameTv;
    private EditText editSchoolFG_educationTv;
    private EditText editSchoolFG_professionTv;
    private TextView editSchoolFG_delTv;

    private UserResumeEntity.ResumeSEntity sEntity;

    private DatePickerDialog startTimePickerDialog;

    private DatePickerDialog endTimePickerDialog;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_edit_school,null);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        sEntity = (UserResumeEntity.ResumeSEntity) getArguments().getSerializable("sEntity");
        if(sEntity == null){
            editSchoolFG_delTv.setVisibility(View.GONE);
        }else{
            initValues();
        }
        initDialog();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.editSchoolFG_backImg:
                getFragmentManager().popBackStack();
                break;
            case R.id.editSchoolFG_saveTv:
                if(sEntity == null){
                    save();
                }else{
                    saveChanged();
                }
                break;
            case R.id.editSchoolFG_delTv:
                del();
                break;
            case R.id.editSchoolFG_startTimeTv:
                startTimePickerDialog.show();
                break;
            case R.id.editSchoolFG_endTimeTv:
                endTimePickerDialog.show();
                break;
        }
    }

    private void initDialog(){
        startTimePickerDialog = new DatePickerDialog(getContext());
        startTimePickerDialog.setDialogCallBack(new DatePickerDialog.DatePickerDialogCallBack() {
            @Override
            public void onSelect(String date) {
                editSchoolFG_startTimeTv.setText(date);
            }
        });
        endTimePickerDialog = new DatePickerDialog(getContext());
        endTimePickerDialog.setDialogCallBack(new DatePickerDialog.DatePickerDialogCallBack() {
            @Override
            public void onSelect(String date) {
                editSchoolFG_endTimeTv.setText(date);
            }
        });
    }

    private void initValues(){
        editSchoolFG_startTimeTv.setText(sEntity.getStartTime());
        editSchoolFG_endTimeTv.setText(sEntity.getEndTime());
        editSchoolFG_nameTv.setText(sEntity.getSchoolName());
        editSchoolFG_educationTv.setText(sEntity.getEducation());
        editSchoolFG_professionTv.setText(sEntity.getProfession());
    }

    private void del(){
        String url = FinalData.BASE_URL + "/delUserResumeS?id="+sEntity.getId();
        HttpFactory.getHttpUtils().get(url,new UpdateResumeEventModel(EditResumeActivity.UPDATE_RESUME_S));
    }

    private void save(){
        sEntity = new UserResumeEntity.ResumeSEntity();
        sEntity.setUserId(App.getAppInstance().getUserInfoUtils().getUserInfo().data.getId());
        sEntity.setStartTime(editSchoolFG_startTimeTv.getText().toString());
        sEntity.setEndTime(editSchoolFG_endTimeTv.getText().toString());
        sEntity.setSchoolName(editSchoolFG_nameTv.getText().toString());
        sEntity.setEducation(editSchoolFG_educationTv.getText().toString());
        sEntity.setProfession(editSchoolFG_professionTv.getText().toString());
        String json = new Gson().toJson(sEntity);
        String url = FinalData.BASE_URL + "/addUserResumeS";
        HttpFactory.getHttpUtils().post(json,url,new UpdateResumeEventModel(EditResumeActivity.SAVED_RESUME_S));
    }

    private void saveChanged(){
        sEntity.setUserId(App.getAppInstance().getUserInfoUtils().getUserInfo().data.getId());
        sEntity.setStartTime(editSchoolFG_startTimeTv.getText().toString());
        sEntity.setEndTime(editSchoolFG_endTimeTv.getText().toString());
        sEntity.setSchoolName(editSchoolFG_nameTv.getText().toString());
        sEntity.setEducation(editSchoolFG_educationTv.getText().toString());
        sEntity.setProfession(editSchoolFG_professionTv.getText().toString());
        String json = new Gson().toJson(sEntity);
        String url = FinalData.BASE_URL + "/updateUserResumeS";
        HttpFactory.getHttpUtils().post(json,url,new UpdateResumeEventModel(EditResumeActivity.UPDATE_RESUME_S));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void saveChangedResult(UpdateResumeEventModel eventModel){
        switch (eventModel.eventId){
            case EditResumeActivity.UPDATE_RESUME_S:
                if(eventModel.isSuccess){
                    ((EditResumeActivity)getContext()).refresh();
                    getFragmentManager().popBackStack();
                }
                break;
            case EditResumeActivity.SAVED_RESUME_S:
                if(eventModel.isSuccess){
                    ((EditResumeActivity)getContext()).refresh();
                    getFragmentManager().popBackStack();
                }
                break;
            case EditResumeActivity.DEL_RESUME_S:
                if(eventModel.isSuccess){
                    ((EditResumeActivity)getContext()).refresh();
                    getFragmentManager().popBackStack();
                }
                break;
        }
    }


    private void bindViews() {

        editSchoolFG_backImg = (ImageView) findViewById(R.id.editSchoolFG_backImg);
        editSchoolFG_saveTv = (TextView) findViewById(R.id.editSchoolFG_saveTv);
        editSchoolFG_startTimeTv = (TextView) findViewById(R.id.editSchoolFG_startTimeTv);
        editSchoolFG_endTimeTv = (TextView) findViewById(R.id.editSchoolFG_endTimeTv);
        editSchoolFG_nameTv = (EditText) findViewById(R.id.editSchoolFG_nameTv);
        editSchoolFG_educationTv = (EditText) findViewById(R.id.editSchoolFG_educationTv);
        editSchoolFG_professionTv = (EditText) findViewById(R.id.editSchoolFG_professionTv);
        editSchoolFG_delTv = (TextView) findViewById(R.id.editSchoolFG_delTv);

        editSchoolFG_backImg.setOnClickListener(this);
        editSchoolFG_saveTv.setOnClickListener(this);
        editSchoolFG_delTv.setOnClickListener(this);
        editSchoolFG_startTimeTv.setOnClickListener(this);
        editSchoolFG_endTimeTv.setOnClickListener(this);
    }

}
