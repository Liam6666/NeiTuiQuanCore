package com.neituiquan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neituiquan.entity.JobsEntity;
import com.neituiquan.work.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_release_job,parent,false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        final JobsEntity entity = entityList.get(position);
        holder.item_titleTv.setText(entity.getTitle());
        holder.item_salaryTv.setText(entity.getMinSalary() +"—" + entity.getMaxSalary());
        holder.item_absTv.setText(entity.getEducation() + " " + entity.getCity() +" "+ entity.getWorkAge() );
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(Long.parseLong(entity.getCreateTime()));
        Date date = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        holder.item_timeTv.setText(sdf.format(date));
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
        return entityList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView item_titleTv;
        TextView item_salaryTv;
        TextView item_absTv;
        TextView item_timeTv;
        LinearLayout item_labelsLayout;

        View itemView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            item_titleTv = (TextView) itemView.findViewById(R.id.item_titleTv);
            item_salaryTv = (TextView) itemView.findViewById(R.id.item_salaryTv);
            item_absTv = (TextView) itemView.findViewById(R.id.item_absTv);
            item_timeTv = (TextView) itemView.findViewById(R.id.item_timeTv);
            item_labelsLayout = (LinearLayout) itemView.findViewById(R.id.item_labelsLayout);
        }
    }

    public interface ReleaseJobsAdapterCallBack{

        public void onItemClick(JobsEntity entity,int position);

        public void onLongItemClick(JobsEntity entity, int position);
    }
}
