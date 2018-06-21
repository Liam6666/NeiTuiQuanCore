package com.neituiquan.work.resume;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neituiquan.base.BaseFragment;
import com.neituiquan.entity.UserResumeEntity;
import com.neituiquan.gson.UserResumeModel;
import com.neituiquan.work.R;

/**
 * Created by Augustine on 2018/6/21.
 * <p>
 * email:nice_ohoh@163.com
 */

public class WorkListFragment extends BaseFragment implements View.OnClickListener {

    public static WorkListFragment newInstance() {

        Bundle args = new Bundle();

        WorkListFragment fragment = new WorkListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView workFG_backImg;
    private LinearLayout workFG_contentLayout;
    private TextView workFG_addTv;

    private UserResumeModel resumeModel;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_work_list,null);
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
    public void refresh() {
        super.refresh();
        initValues();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.workFG_backImg:
                ((EditResumeActivity)getContext()).finish();
                break;
            case R.id.workFG_addTv:
                startToEditAWFragment(null);
                break;
        }
    }

    private void initValues(){
        resumeModel = ((EditResumeActivity)getContext()).getResumeModel();
        workFG_contentLayout.removeAllViews();
        for(int i = 0 ; i < resumeModel.data.getResumeWList().size() ; i ++){
            workFG_contentLayout.addView(createItem(i));
        }
    }

    private LinearLayout createItem(int index){
        final UserResumeEntity.ResumeWEntity wEntity = resumeModel.data.getResumeWList().get(index);
        LinearLayout itemView = (LinearLayout) View.inflate(getContext(),R.layout.item_work_edit,null);
        TextView item_startTimeTv = itemView.findViewById(R.id.item_startTimeTv);
        LinearLayout item_editLayout = itemView.findViewById(R.id.item_editLayout);
        TextView item_endTimeTv = itemView.findViewById(R.id.item_endTimeTv);
        TextView item_titleTv = itemView.findViewById(R.id.item_titleTv);
        TextView item_jobTitleTv = itemView.findViewById(R.id.item_jobTitleTv);
        TextView item_cityTv = itemView.findViewById(R.id.item_cityTv);
        item_startTimeTv.setText(wEntity.getStartTime());
        item_endTimeTv.setText(wEntity.getEndTime());
        item_titleTv.setText(wEntity.getCompanyName());
        item_jobTitleTv.setText(wEntity.getJobTitle());
        item_cityTv.setText(wEntity.getCity());
        item_editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startToEditAWFragment(wEntity);
            }
        });
        return itemView;
    }


    private void startToEditAWFragment(UserResumeEntity.ResumeWEntity wEntity){
        final EditWorkFragment editWorkFragment = EditWorkFragment.newInstance(wEntity);
        getFragmentManager()
                .beginTransaction()
                .add(R.id.editResumeUI_frameLayout,editWorkFragment,"editWorkFragment")
                .show(editWorkFragment)
                .addToBackStack(null)
                .commit();
    }


    private void bindViews() {

        workFG_backImg = (ImageView) findViewById(R.id.workFG_backImg);
        workFG_contentLayout = (LinearLayout) findViewById(R.id.workFG_contentLayout);
        workFG_addTv = (TextView) findViewById(R.id.workFG_addTv);

        workFG_backImg.setOnClickListener(this);
        workFG_addTv.setOnClickListener(this);
    }

}
