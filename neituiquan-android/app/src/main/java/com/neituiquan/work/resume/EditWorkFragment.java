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

public class EditWorkFragment extends BaseFragment implements View.OnClickListener {

    public static EditWorkFragment newInstance(UserResumeEntity.ResumeWEntity wEntity) {
        
        Bundle args = new Bundle();
        args.putSerializable("wEntity",wEntity);
        EditWorkFragment fragment = new EditWorkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView editWorkFG_backImg;
    private TextView editWorkFG_saveTv;
    private EditText editWorkFG_startTimeTv;
    private EditText editWorkFG_endTimeTv;
    private EditText editWorkFG_nameTv;
    private EditText editWorkFG_jobTitleTv;
    private EditText editWorkFG_cityTv;
    private TextView editWorkFG_delTv;
    
    private UserResumeEntity.ResumeWEntity wEntity;
    
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_edit_work,null);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        wEntity = (UserResumeEntity.ResumeWEntity) getArguments().getSerializable("wEntity");
        if(wEntity == null){
            editWorkFG_delTv.setVisibility(View.GONE);
        }else{
            initValues();
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.editWorkFG_backImg:
                getFragmentManager().popBackStack();
            case R.id.editWorkFG_saveTv:
                if(wEntity == null){
                    save();
                }else{
                    saveChanged();
                }
                break;
            case R.id.editWorkFG_delTv:
                del();
                break;
        }
    }
    
    private void del(){
        String url = FinalData.BASE_URL + "/delUserResumeW?id="+wEntity.getId();
        HttpFactory.getHttpUtils().get(url,new UpdateResumeEventModel(EditResumeActivity.DEL_RESUME_W));
    }
    
    private void save(){
        wEntity = new UserResumeEntity.ResumeWEntity();
        wEntity.setUserId(App.getAppInstance().getUserInfoUtils().getUserInfo().data.getId());
        wEntity.setStartTime(editWorkFG_startTimeTv.getText().toString());
        wEntity.setEndTime(editWorkFG_endTimeTv.getText().toString());
        wEntity.setCompanyName(editWorkFG_nameTv.getText().toString());
        wEntity.setJobTitle(editWorkFG_jobTitleTv.getText().toString());
        wEntity.setCity(editWorkFG_cityTv.getText().toString());
        String json = new Gson().toJson(wEntity);
        String url = FinalData.BASE_URL + "/addUserResumeW";
        HttpFactory.getHttpUtils().post(json,url,new UpdateResumeEventModel(EditResumeActivity.SAVED_RESUME_W));
    }
    
    private void saveChanged(){
        wEntity.setStartTime(editWorkFG_startTimeTv.getText().toString());
        wEntity.setEndTime(editWorkFG_endTimeTv.getText().toString());
        wEntity.setCompanyName(editWorkFG_nameTv.getText().toString());
        wEntity.setJobTitle(editWorkFG_jobTitleTv.getText().toString());
        wEntity.setCity(editWorkFG_cityTv.getText().toString());
        String json = new Gson().toJson(wEntity);
        String url = FinalData.BASE_URL + "/updateUserResumeW";
        HttpFactory.getHttpUtils().post(json,url,new UpdateResumeEventModel(EditResumeActivity.UPDATE_RESUME_W));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void saveChangedResult(UpdateResumeEventModel eventModel){
        switch (eventModel.eventId){
            case EditResumeActivity.UPDATE_RESUME_W:
                if(eventModel.isSuccess){
                    ((EditResumeActivity)getContext()).refresh();
                    getFragmentManager().popBackStack();
                }
                break;
            case EditResumeActivity.SAVED_RESUME_W:
                if(eventModel.isSuccess){
                    ((EditResumeActivity)getContext()).refresh();
                    getFragmentManager().popBackStack();
                }
                break;
            case EditResumeActivity.DEL_RESUME_W:
                if(eventModel.isSuccess){
                    ((EditResumeActivity)getContext()).refresh();
                    getFragmentManager().popBackStack();
                }
                break;
        }
    }

    
    private void initValues(){
        editWorkFG_startTimeTv.setText(wEntity.getStartTime());
        editWorkFG_endTimeTv.setText(wEntity.getEndTime());
        editWorkFG_nameTv.setText(wEntity.getCompanyName());
        editWorkFG_jobTitleTv.setText(wEntity.getJobTitle());
        editWorkFG_cityTv.setText(wEntity.getCity());
    }


    
    
    private void bindViews() {

        editWorkFG_backImg = (ImageView) findViewById(R.id.editWorkFG_backImg);
        editWorkFG_saveTv = (TextView) findViewById(R.id.editWorkFG_saveTv);
        editWorkFG_startTimeTv = (EditText) findViewById(R.id.editWorkFG_startTimeTv);
        editWorkFG_endTimeTv = (EditText) findViewById(R.id.editWorkFG_endTimeTv);
        editWorkFG_nameTv = (EditText) findViewById(R.id.editWorkFG_nameTv);
        editWorkFG_jobTitleTv = (EditText) findViewById(R.id.editWorkFG_jobTitleTv);
        editWorkFG_cityTv = (EditText) findViewById(R.id.editWorkFG_cityTv);
        editWorkFG_delTv = (TextView) findViewById(R.id.editWorkFG_delTv);

        editWorkFG_backImg.setOnClickListener(this);
        editWorkFG_delTv.setOnClickListener(this);
        editWorkFG_saveTv.setOnClickListener(this);
    }

}
