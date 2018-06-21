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
 *
 * 项目经历
 *
 *
 */

public class ProjectListFragment extends BaseFragment implements View.OnClickListener {

    public static ProjectListFragment newInstance() {

        Bundle args = new Bundle();

        ProjectListFragment fragment = new ProjectListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView projectFG_backImg;
    private LinearLayout projectFG_contentLayout;
    private TextView projectFG_addTv;

    private UserResumeModel resumeModel;


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_project_list,null);
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

    private void initValues(){
        resumeModel = ((EditResumeActivity)getContext()).getResumeModel();
        projectFG_contentLayout.removeAllViews();
        for(int i = 0 ; i < resumeModel.data.getResumePList().size() ; i ++){
            projectFG_contentLayout.addView(createItem(i));
        }
    }

    private LinearLayout createItem(int index){
        final UserResumeEntity.ResumePEntity pEntity = resumeModel.data.getResumePList().get(index);
        LinearLayout itemView = (LinearLayout) View.inflate(getContext(),R.layout.item_project_edit,null);
        TextView item_startTimeTv = itemView.findViewById(R.id.item_startTimeTv);
        LinearLayout item_editLayout = itemView.findViewById(R.id.item_editLayout);
        TextView item_endTimeTv = itemView.findViewById(R.id.item_endTimeTv);
        TextView item_titleTv = itemView.findViewById(R.id.item_titleTv);
        TextView item_responsibilityTv = itemView.findViewById(R.id.item_responsibilityTv);
        TextView item_bodyTv = itemView.findViewById(R.id.item_bodyTv);
        TextView item_linkTv = itemView.findViewById(R.id.item_linkTv);
        item_startTimeTv.setText(pEntity.getStartTime());
        item_endTimeTv.setText(pEntity.getEndTime());
        item_titleTv.setText(pEntity.getProjectName());
        item_responsibilityTv.setText(pEntity.getResponsibility());
        item_bodyTv.setText(pEntity.getProjectAbs());
        item_linkTv.setText(pEntity.getLink());
        item_editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startToEditAWFragment(pEntity);
            }
        });
        return itemView;
    }

    private void startToEditAWFragment(UserResumeEntity.ResumePEntity pEntity){
        final EditProjectFragment editProjectFragment = EditProjectFragment.newInstance(pEntity);
        getFragmentManager()
                .beginTransaction()
                .add(R.id.editResumeUI_frameLayout,editProjectFragment,"editProjectFragment")
                .show(editProjectFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.projectFG_backImg:
                ((EditResumeActivity)getContext()).finish();
                break;
            case R.id.projectFG_addTv:
                startToEditAWFragment(null);
                break;
        }
    }

    private void bindViews() {

        projectFG_backImg = (ImageView) findViewById(R.id.projectFG_backImg);
        projectFG_contentLayout = (LinearLayout) findViewById(R.id.projectFG_contentLayout);
        projectFG_addTv = (TextView) findViewById(R.id.projectFG_addTv);
        projectFG_addTv.setOnClickListener(this);
        projectFG_backImg.setOnClickListener(this);
    }


}
