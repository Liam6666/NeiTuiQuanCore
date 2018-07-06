package com.neituiquan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neituiquan.entity.JobListEntity;
import com.neituiquan.utils.Millis2Date;
import com.neituiquan.work.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by Augustine on 2018/6/25.
 * <p>
 * email:nice_ohoh@163.com
 */

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ItemViewHolder> {

    private Context context;

    private List<JobListEntity> entityList;

    private static final int EMPTY = -1;

    private static final int DEFAULT = 0;

    public JobsAdapter(Context context, List<JobListEntity> entityList) {
        this.context = context;
        this.entityList = entityList;
    }

    public void addNewData(List<JobListEntity> entityList){
        this.entityList.addAll(entityList);
        notifyDataSetChanged();
    }

    public void refresh(List<JobListEntity> entityList){
        this.entityList.clear();
        notifyDataSetChanged();
        this.entityList.addAll(entityList);
        notifyDataSetChanged();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (viewType){
            case EMPTY:
                itemView = LayoutInflater.from(context).inflate(R.layout.item_empty,parent,false);
                break;
            case DEFAULT:
                itemView = LayoutInflater.from(context).inflate(R.layout.item_home_page_jobs,parent,false);
                break;
        }
        return new ItemViewHolder(itemView,viewType);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        JobListEntity entity = entityList.get(position);
        if(entity.itemType == DEFAULT){
            holder.item_title.setText(entity.getTitle());
            holder.item_salaryTv.setText(entity.getMinSalary() + "K—" +entity.getMaxSalary()+ "K");
            holder.item_labelsLayout.removeAllViews();
            try {
                JSONArray jsonArray = new JSONArray(entity.getLabels());
                for(int i = 0 ; i < jsonArray.length() ; i ++){
                    View item = LayoutInflater.from(context).inflate(R.layout.item_jobs_label,holder.item_labelsLayout,false);
                    holder.item_labelsLayout.addView(item);
                    ((TextView)item).setText(jsonArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            holder.id_labels.setText(entity.getCity() +"  "+ entity.getEducation() + "  "+ entity.getWorkAge());
            holder.item_companyNameTv.setText(entity.getCompanyName());
            String time = Millis2Date.simpleMillis2Date(entity.getCreateTime());
            holder.item_timeTv.setText(time);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return entityList.get(position).itemType;
    }

    @Override
    public int getItemCount() {
        if(entityList.size() == 0){
            entityList.add(new JobListEntity(EMPTY));
        }
        return entityList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView item_title;
        TextView item_salaryTv;
        TextView id_labels;
        TextView item_timeTv;
        LinearLayout item_labelsLayout;
        TextView item_companyNameTv;


        public ItemViewHolder(View itemView,int viewType) {
            super(itemView);
            if(viewType == DEFAULT){
                item_title = (TextView) itemView.findViewById(R.id.item_title);
                item_salaryTv = (TextView) itemView.findViewById(R.id.item_salaryTv);
                id_labels = (TextView) itemView.findViewById(R.id.id_labels);
                item_timeTv = (TextView) itemView.findViewById(R.id.item_timeTv);
                item_labelsLayout = (LinearLayout) itemView.findViewById(R.id.item_labelsLayout);
                item_companyNameTv = (TextView) itemView.findViewById(R.id.item_companyNameTv);
            }
        }
    }
}
