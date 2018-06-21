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
 * 学习经历
 */

public class SchoolListFragment extends BaseFragment implements View.OnClickListener {

    public static SchoolListFragment newInstance() {

        Bundle args = new Bundle();

        SchoolListFragment fragment = new SchoolListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView schoolFG_backImg;
    private LinearLayout schoolFG_contentLayout;
    private TextView schoolFG_addTv;

    private UserResumeModel resumeModel;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_school_list,null);
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
            case R.id.schoolFG_backImg:
                ((EditResumeActivity)getContext()).finish();
                break;
            case R.id.schoolFG_addTv:
                startToEditAWFragment(null);
                break;
        }
    }

    private void initValues(){
        resumeModel = ((EditResumeActivity)getContext()).getResumeModel();
        schoolFG_contentLayout.removeAllViews();
        for(int i = 0 ; i < resumeModel.data.getResumeSList().size() ; i ++){
            schoolFG_contentLayout.addView(createItem(i));
        }
    }

    private LinearLayout createItem(int index){
        final UserResumeEntity.ResumeSEntity sEntity = resumeModel.data.getResumeSList().get(index);
        LinearLayout itemView = (LinearLayout) View.inflate(getContext(),R.layout.item_school_edit,null);
        TextView item_startTimeTv = itemView.findViewById(R.id.item_startTimeTv);
        LinearLayout item_editLayout = itemView.findViewById(R.id.item_editLayout);
        TextView item_endTimeTv = itemView.findViewById(R.id.item_endTimeTv);
        TextView item_titleTv = itemView.findViewById(R.id.item_titleTv);
        TextView item_educationTv = itemView.findViewById(R.id.item_educationTv);
        TextView item_professionTv = itemView.findViewById(R.id.item_professionTv);
        item_startTimeTv.setText(sEntity.getStartTime());
        item_endTimeTv.setText(sEntity.getEndTime());
        item_titleTv.setText(sEntity.getSchoolName());
        item_educationTv.setText(sEntity.getEducation());
        item_professionTv.setText(sEntity.getProfession());
        item_editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startToEditAWFragment(sEntity);
            }
        });
        return itemView;
    }


    private void startToEditAWFragment(UserResumeEntity.ResumeSEntity sEntity){
        final EditSchoolFragment editSchoolFragment = EditSchoolFragment.newInstance(sEntity);
        getFragmentManager()
                .beginTransaction()
                .add(R.id.editResumeUI_frameLayout,editSchoolFragment,"editSchoolFragment")
                .show(editSchoolFragment)
                .addToBackStack(null)
                .commit();
    }

    private void bindViews() {

        schoolFG_backImg = (ImageView) findViewById(R.id.schoolFG_backImg);
        schoolFG_contentLayout = (LinearLayout) findViewById(R.id.schoolFG_contentLayout);
        schoolFG_addTv = (TextView) findViewById(R.id.schoolFG_addTv);

        schoolFG_backImg.setOnClickListener(this);
        schoolFG_addTv.setOnClickListener(this);
    }


}
