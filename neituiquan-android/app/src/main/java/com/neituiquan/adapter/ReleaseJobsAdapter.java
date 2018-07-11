package com.neituiquan.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neituiquan.FinalData;
import com.neituiquan.entity.ReleaseJobsEntity;
import com.neituiquan.work.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Augustine on 2018/6/22.
 * <p>
 * email:nice_ohoh@163.com
 */

public class ReleaseJobsAdapter extends RecyclerView.Adapter<ReleaseJobsAdapter.ItemViewHolder>{

    private Context context;

    private List<ReleaseJobsEntity> list = new ArrayList<>();

    private AdapterCallBack callBack;

    public ReleaseJobsAdapter(Context context) {
        this.context = context;
    }

    public void setCallBack(AdapterCallBack callBack) {
        this.callBack = callBack;
    }

    public void addData(ReleaseJobsEntity newData){
        this.list.add(newData);
        notifyDataSetChanged();
    }

    public void addData(List<ReleaseJobsEntity> newData){
        this.list.addAll(newData);
        notifyDataSetChanged();
    }

    public void refresh(List<ReleaseJobsEntity> newData){
        this.list.clear();
        addData(newData);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder itemViewHolder = null;
        switch (viewType){
            case FinalData.ITEM_DEFAULT:
                itemViewHolder = new ItemViewHolder(createView(R.layout.item_release_job,parent),viewType);
                break;
            case FinalData.ITEM_EMPTY:
                itemViewHolder = new ItemViewHolder(createView(R.layout.item_empty,parent),viewType);
                break;
        }
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        final ReleaseJobsEntity entity = list.get(position);
        int viewType = entity.itemType;
        switch (viewType){
            case FinalData.ITEM_DEFAULT:
                holder.item_titleTv.setText(entity.getTitle());
                holder.item_stateTv.setText(entity.getState());
                holder.item_cityTv.setText(entity.getCity());
                holder.item_workAgeTv.setText(entity.getWorkAge()+"年");
                holder.item_educationTv.setText(entity.getEducation());
                holder.item_labelsLayout.removeAllViews();
                try {
                    JSONArray jsonArray = new JSONArray(entity.getLabels());
                    for(int i = 0 ; i < jsonArray.length() ; i ++){
                        View labelView = View.inflate(context,R.layout.item_jobs_label,null);
                        ((TextView)labelView).setText(jsonArray.getString(i));
                        holder.item_labelsLayout.addView(labelView);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                holder.item_contentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(callBack != null){
                            callBack.onItemClick(entity,position);
                        }
                    }
                });
                break;
            case FinalData.ITEM_EMPTY:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).itemType;
    }

    public View createView(int layoutId,ViewGroup parent){
        return LayoutInflater.from(context).inflate(layoutId,parent,false);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{


        private LinearLayout item_contentLayout;
        private TextView item_titleTv;
        private TextView item_stateTv;
        private TextView item_cityTv;
        private TextView item_workAgeTv;
        private TextView item_educationTv;
        private LinearLayout item_labelsLayout;

        public ItemViewHolder(View itemView,int viewType) {
            super(itemView);
            switch (viewType){
                case FinalData.ITEM_DEFAULT:
                    item_contentLayout = itemView.findViewById(R.id.item_contentLayout);
                    item_titleTv = itemView.findViewById(R.id.item_titleTv);
                    item_stateTv = itemView.findViewById(R.id.item_stateTv);
                    item_cityTv = itemView.findViewById(R.id.item_cityTv);
                    item_workAgeTv = itemView.findViewById(R.id.item_workAgeTv);
                    item_educationTv = itemView.findViewById(R.id.item_educationTv);
                    item_labelsLayout = itemView.findViewById(R.id.item_labelsLayout);
                    break;
            }
        }
    }


    public interface AdapterCallBack{

        public void onItemClick(ReleaseJobsEntity entity,int position);

    }
}
