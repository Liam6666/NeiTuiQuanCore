package com.neituiquan.work.resume;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.base.BaseFragment;
import com.neituiquan.entity.UserResumeEntity;
import com.neituiquan.httpEvent.UpdateResumeEventModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.work.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Augustine on 2018/6/20.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 编辑获奖情况
 *
 *
 */

public class EditAWFragment extends BaseFragment implements View.OnClickListener {

    public static EditAWFragment newInstance(UserResumeEntity.ResumeAEntity aEntity) {

        Bundle args = new Bundle();
        args.putSerializable("aEntity",aEntity);
        EditAWFragment fragment = new EditAWFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView editawFG_backImg;
    private TextView editawFG_saveTv;
    private EditText editawFG_timeTv;
    private EditText editawFG_rewardNameTv;
    private TextView editawFG_delTv;

    private UserResumeEntity.ResumeAEntity aEntity;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_edit_aw,null);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
        aEntity = (UserResumeEntity.ResumeAEntity) getArguments().getSerializable("aEntity");
        if(aEntity == null){
            editawFG_delTv.setVisibility(View.GONE);
        }else{
            initValues();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.editawFG_backImg:
                getFragmentManager().popBackStack();
                break;
            case R.id.editawFG_saveTv:
                if(aEntity == null){
                    save();
                }else{
                    saveChanged();
                }
                break;
            case R.id.editawFG_delTv:
                del();
                break;
        }
    }

    private void del(){
        String url = FinalData.BASE_URL + "/delUserResumeA?id="+aEntity.getId();
        HttpFactory.getHttpUtils().get(url,new UpdateResumeEventModel(EditResumeActivity.DEL_RESUME_A));
    }

    /**
     * 添加
     */
    private void save(){
        aEntity = new UserResumeEntity.ResumeAEntity();
        aEntity.setUserId(App.getAppInstance().getUserInfoUtils().getUserInfo().data.getId());
        aEntity.setCreationTime(editawFG_timeTv.getText().toString());
        aEntity.setRewardName(editawFG_rewardNameTv.getText().toString());
        String url = FinalData.BASE_URL + "/addUserResumeA";
        String json = new Gson().toJson(aEntity);
        HttpFactory.getHttpUtils().post(json,url,new UpdateResumeEventModel(EditResumeActivity.SAVED_RESUME_A));
    }

    /**
     * 保存更改
     */
    private void saveChanged(){
        aEntity.setCreationTime(editawFG_timeTv.getText().toString());
        aEntity.setRewardName(editawFG_rewardNameTv.getText().toString());
        String url = FinalData.BASE_URL + "/updateUserResumeA";
        String json = new Gson().toJson(aEntity);
        HttpFactory.getHttpUtils().post(json,url,new UpdateResumeEventModel(EditResumeActivity.UPDATE_RESUME_A));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void saveChangedResult(UpdateResumeEventModel eventModel){
        if(eventModel.eventId == EditResumeActivity.UPDATE_RESUME_A){
            if(eventModel.isSuccess){
                ((EditResumeActivity)getContext()).refresh();
                getFragmentManager().popBackStack();
            }
        }else if(eventModel.eventId == EditResumeActivity.SAVED_RESUME_A){
            /**
             * 新增
             */
            if(eventModel.isSuccess){
                ((EditResumeActivity)getContext()).refresh();
                getFragmentManager().popBackStack();
            }

        }else if(eventModel.eventId == EditResumeActivity.DEL_RESUME_A){
            /**
             * 删除
             */
            if(eventModel.isSuccess){
                ((EditResumeActivity)getContext()).refresh();
                getFragmentManager().popBackStack();
            }
        }
    }

    private void initValues(){
        editawFG_timeTv.setText(aEntity.getCreationTime());
        editawFG_rewardNameTv.setText(aEntity.getRewardName());

    }


    private void bindViews() {

        editawFG_backImg = (ImageView) findViewById(R.id.editawFG_backImg);
        editawFG_saveTv = (TextView) findViewById(R.id.editawFG_saveTv);
        editawFG_timeTv = (EditText) findViewById(R.id.editawFG_timeTv);
        editawFG_rewardNameTv = (EditText) findViewById(R.id.editawFG_rewardNameTv);
        editawFG_delTv = (TextView) findViewById(R.id.editawFG_delTv);

        editawFG_backImg.setOnClickListener(this);
        editawFG_saveTv.setOnClickListener(this);
        editawFG_delTv.setOnClickListener(this);
    }


}
