package com.neituiquan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neituiquan.entity.JobListEntity;
import com.neituiquan.work.R;
import com.neituiquan.work.fragment.HomePageFragment;

import java.util.List;

/**
 * Created by Augustine on 2018/6/25.
 * <p>
 * email:nice_ohoh@163.com
 */

public class HomePageJobAdapter extends RecyclerView.Adapter<HomePageJobAdapter.ItemViewHolder> {

    private Context context;

    private List<JobListEntity> entityList;

    public HomePageJobAdapter(Context context, List<JobListEntity> entityList) {
        this.context = context;
        this.entityList = entityList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_jobs_edit,parent,false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        JobListEntity entity = entityList.get(position);
        holder.item_title.setText(entity.getTitle());
        holder.item_salaryTv.setText(entity.getMinSalary() + "K—" +entity.getMaxSalary()+ "K");
        holder.id_labels.setText(entity.getLabels());
        holder.item_companyNameTv.setText(entity.getCompanyName());
    }

    @Override
    public int getItemCount() {
        return entityList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView item_title;
        TextView item_salaryTv;
        TextView id_labels;
        TextView item_timeTv;
        LinearLayout item_labelsLayout;
        TextView item_companyNameTv;


        public ItemViewHolder(View itemView) {
            super(itemView);
            item_title = (TextView) itemView.findViewById(R.id.item_title);
            item_salaryTv = (TextView) itemView.findViewById(R.id.item_salaryTv);
            id_labels = (TextView) itemView.findViewById(R.id.id_labels);
            item_timeTv = (TextView) itemView.findViewById(R.id.item_timeTv);
            item_labelsLayout = (LinearLayout) itemView.findViewById(R.id.item_labelsLayout);
            item_companyNameTv = (TextView) itemView.findViewById(R.id.item_companyNameTv);
        }
    }
}
