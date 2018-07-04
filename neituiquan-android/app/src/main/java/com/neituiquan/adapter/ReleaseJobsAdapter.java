package com.neituiquan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neituiquan.entity.JobsEntity;
import com.neituiquan.utils.Millis2Date;
import com.neituiquan.work.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by Augustine on 2018/6/22.
 * <p>
 * email:nice_ohoh@163.com
 */

public class ReleaseJobsAdapter extends RecyclerView.Adapter<ReleaseJobsAdapter.ItemViewHolder> {

    private Context context;

    private List<JobsEntity> entityList;

    private ReleaseJobsAdapterCallBack callBack;

    private static final int EMPTY = -1;

    private static final int DEFAULT = 0;

    public ReleaseJobsAdapter(Context context, List<JobsEntity> entityList) {
        this.context = context;
        this.entityList = entityList;
    }

    public void setCallBack(ReleaseJobsAdapterCallBack callBack) {
        this.callBack = callBack;
    }

    public void refresh(List<JobsEntity> entityList){
        this.entityList.clear();
        this.entityList.addAll(entityList);
        notifyDataSetChanged();
    }

    public void del(String id){
        for(JobsEntity entity : entityList){
            if(entity.getId().equals(id)){
                entityList.remove(entity);
                break;
            }
        }
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
                itemView = LayoutInflater.from(context).inflate(R.layout.item_release_job,parent,false);
                break;
        }
        return new ItemViewHolder(itemView,viewType);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        final JobsEntity entity = entityList.get(position);
        if(entity.itemType == EMPTY){
            return;
        }
        holder.item_titleTv.setText(entity.getTitle());
        holder.item_salaryTv.setText(entity.getMinSalary() +"K—" + entity.getMaxSalary()+"K");
        holder.item_absTv.setText(entity.getEducation() + " " + entity.getCity() +" "+ entity.getWorkAge() );
        String time = Millis2Date.simpleMillis2Date(entity.getCreateTime());
        holder.item_timeTv.setText(time);
        holder.item_labelsLayout.removeAllViews();
        try {
            JSONArray jsonArray = new JSONArray(entity.getLabels());
            for(int i = 0; i < jsonArray.length() ; i ++){
                TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.item_jobs_label,holder.item_labelsLayout,false);
                textView.setText(jsonArray.getString(i));
                holder.item_labelsLayout.addView(textView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBack != null){
                    callBack.onItemClick(entity,position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(callBack != null){
                    callBack.onLongItemClick(entity,position);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if(entityList.size() == 0){
            entityList.add(new JobsEntity(EMPTY));
        }else{
            for(JobsEntity entity : entityList){
                if(entity.itemType == EMPTY){
                    entityList.remove(entity);
                }
            }
        }
        return entityList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return entityList.get(position).itemType;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView item_titleTv;
        TextView item_salaryTv;
        TextView item_absTv;
        TextView item_timeTv;
        LinearLayout item_labelsLayout;

        View itemView;

        public ItemViewHolder(View itemView,int viewType) {
            super(itemView);
            this.itemView = itemView;
            if(viewType == DEFAULT){
                item_titleTv = (TextView) itemView.findViewById(R.id.item_titleTv);
                item_salaryTv = (TextView) itemView.findViewById(R.id.item_salaryTv);
                item_absTv = (TextView) itemView.findViewById(R.id.item_absTv);
                item_timeTv = (TextView) itemView.findViewById(R.id.item_timeTv);
                item_labelsLayout = (LinearLayout) itemView.findViewById(R.id.item_labelsLayout);
            }
        }
    }

    public interface ReleaseJobsAdapterCallBack{

        public void onItemClick(JobsEntity entity,int position);

        public void onLongItemClick(JobsEntity entity, int position);
    }
}
