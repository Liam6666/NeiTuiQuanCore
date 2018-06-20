package com.neituiquan.work.resume;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.neituiquan.App;
import com.neituiquan.FinalData;
import com.neituiquan.base.BaseFragment;
import com.neituiquan.entity.UserResumeEntity;
import com.neituiquan.gson.UserResumeModel;
import com.neituiquan.net.HttpFactory;
import com.neituiquan.work.R;

import java.io.IOException;
import java.lang.ref.SoftReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Augustine on 2018/6/20.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 获奖情况
 *
 * Award-winning
 *
 */

public class AWListFragment extends BaseFragment implements View.OnClickListener {

    public static AWListFragment newInstance() {

        Bundle args = new Bundle();

        AWListFragment fragment = new AWListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView awFG_backImg;
    private LinearLayout awFG_contentLayout;
    private TextView awFG_addTv;

    private UserResumeModel resumeModel;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_aw_list,null);
    }

    @Override
    public void initList(Bundle savedInstanceState) {
        bindViews();
    }

    @Override
    public void onLazyInitList() {
        super.onLazyInitList();
        initValues();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.awFG_backImg:
                ((EditResumeActivity)getContext()).finish();
                break;
            case R.id.awFG_addTv:
                startToEditAWFragment(null);
                break;
        }
    }

    public void initValues(){
        resumeModel = ((EditResumeActivity)getContext()).getResumeModel();
        awFG_contentLayout.removeAllViews();
        for(int i = 0 ; i < resumeModel.data.getResumeAList().size() ; i ++){
            awFG_contentLayout.addView(createItem(i));
        }
    }

    public void refresh(){
        final Handler handler = new Handler(){
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
                if(msg.what == 123){
                    resumeModel = (UserResumeModel) FinalData.resumeModelSoftReference.get();
                    awFG_contentLayout.removeAllViews();
                    for(int i = 0 ; i < resumeModel.data.getResumeAList().size() ; i ++){
                        awFG_contentLayout.addView(createItem(i));
                    }
                }
            }
        };
        String url = FinalData.BASE_URL + "/getUserResume?userId=" + App.getAppInstance().getUserInfoUtils().getUserInfo().data.getId();
        HttpFactory.getHttpUtils().get(url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                UserResumeModel model = new Gson().fromJson(result,UserResumeModel.class);
                FinalData.resumeModelSoftReference = new SoftReference(model);
                handler.sendEmptyMessage(123);
            }
        });
    }

    private LinearLayout createItem(int index){
        final UserResumeEntity.ResumeAEntity aEntity = resumeModel.data.getResumeAList().get(index);
        LinearLayout itemView = (LinearLayout) View.inflate(getContext(),R.layout.item_aw_edit,null);
        TextView item_titleTv = itemView.findViewById(R.id.item_titleTv);
        LinearLayout item_editLayout = itemView.findViewById(R.id.item_editLayout);
        TextView item_bodyTv = itemView.findViewById(R.id.item_bodyTv);
        item_bodyTv.setText(aEntity.getRewardName());
        item_titleTv.setText(aEntity.getCreationTime());
        item_editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startToEditAWFragment(aEntity);
            }
        });
        return itemView;
    }


    private void startToEditAWFragment(UserResumeEntity.ResumeAEntity aEntity){
        final EditAWFragment editAWFragment = EditAWFragment.newInstance(aEntity);
        getFragmentManager()
                .beginTransaction()
                .add(R.id.editResumeUI_frameLayout,editAWFragment,"editAWFragment")
                .show(editAWFragment)
                .addToBackStack(null)
                .commit();
    }

    private void bindViews() {

        awFG_backImg = (ImageView) findViewById(R.id.awFG_backImg);
        awFG_contentLayout = (LinearLayout) findViewById(R.id.awFG_contentLayout);
        awFG_addTv = (TextView) findViewById(R.id.awFG_addTv);
        awFG_backImg.setOnClickListener(this);
        awFG_addTv.setOnClickListener(this);
    }


}
