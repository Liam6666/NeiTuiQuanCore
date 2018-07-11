package com.neituiquan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.neituiquan.FinalData;
import com.neituiquan.entity.JobListEntity;
import com.neituiquan.utils.Millis2Date;
import com.neituiquan.view.CompanyIconView;
import com.neituiquan.view.HomePageHeaderView;
import com.neituiquan.work.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Augustine on 2018/7/6.
 * <p>
 * email:nice_ohoh@163.com
 */

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.ItemViewHolder>{

    private Context context;

    private List<JobListEntity> entityList = new ArrayList<>();

    private HomePageAdapterCallBack callBack;

    private String city;

    public HomePageAdapter(Context context) {
        this.context = context;
    }

    public void setCity(String city) {
        this.city = city;
        notifyDataSetChanged();
    }

    public void setCallBack(HomePageAdapterCallBack callBack) {
        this.callBack = callBack;
    }

    public void addList(List<JobListEntity> list){
        entityList.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(JobListEntity entity){
        if(entity.itemType == FinalData.ITEM_BANNER){
            entityList.add(0,entity);
        }else{
            entityList.add(entity);
        }
        notifyDataSetChanged();
    }

    public void refresh(List<JobListEntity> list){
        entityList.clear();
        addList(list);
    }


    public void removeEmptyView(){
        for(JobListEntity entity : entityList){
            if(entity.itemType == FinalData.ITEM_EMPTY){
                entityList.remove(entity);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public List<JobListEntity> getEntityList() {
        return entityList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder itemViewHolder = null;
        switch (viewType){
            case FinalData.ITEM_BANNER:
                itemViewHolder = new ItemViewHolder(new HomePageHeaderView(context),viewType);
                break;
            case FinalData.ITEM_DEFAULT:
                itemViewHolder =
                        new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home_page_jobs,parent,false),viewType);
                break;
            case FinalData.ITEM_EMPTY:
                itemViewHolder =
                        new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_empty,parent,false),viewType);
                break;
        }
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        int viewType = entityList.get(position).itemType;
        final JobListEntity entity = entityList.get(position);
        switch (viewType){
            case FinalData.ITEM_BANNER:
                holder.view_locationLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(callBack != null){
                            callBack.onLocationClick(holder.view_locationTv);
                        }
                    }
                });
                if(!StringUtils.isEmpty(city)){
                    holder.view_locationTv.setText(city);
                }
                break;
            case FinalData.ITEM_DEFAULT:
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
                holder.item_iconView.setValues(entity.getCompanyName(),Integer.valueOf(entity.getMaxSalary()));
                holder.view_moreIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(callBack != null){
                            callBack.onItemMoreClick(entity,position);
                        }
                    }
                });
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
                holder.item_emptyTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(callBack != null){
                            callBack.onEmptyClick();
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return entityList.get(position).itemType;
    }

    @Override
    public int getItemCount() {
        return entityList.size();
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView item_title;
        TextView item_salaryTv;
        TextView id_labels;
        TextView item_timeTv;
        LinearLayout item_labelsLayout;
        TextView item_companyNameTv;
        CompanyIconView item_iconView;
        ImageView view_moreIcon;

        TextView item_emptyTv;

        LinearLayout view_locationLayout;
        TextView view_locationTv;

        LinearLayout item_contentLayout;

        public ItemViewHolder(View itemView,int viewType) {
            super(itemView);
            switch (viewType){
                case FinalData.ITEM_BANNER:
                    view_locationLayout = itemView.findViewById(R.id.view_locationLayout);
                    view_locationTv = itemView.findViewById(R.id.view_locationTv);
                    break;
                case FinalData.ITEM_DEFAULT:
                    item_contentLayout = itemView.findViewById(R.id.item_contentLayout);
                    view_moreIcon = itemView.findViewById(R.id.view_moreIcon);
                    item_iconView = itemView.findViewById(R.id.item_iconView);
                    item_title = (TextView) itemView.findViewById(R.id.item_title);
                    item_salaryTv = (TextView) itemView.findViewById(R.id.item_salaryTv);
                    id_labels = (TextView) itemView.findViewById(R.id.id_labels);
                    item_timeTv = (TextView) itemView.findViewById(R.id.item_timeTv);
                    item_labelsLayout = (LinearLayout) itemView.findViewById(R.id.item_labelsLayout);
                    item_companyNameTv = (TextView) itemView.findViewById(R.id.item_companyNameTv);
                    break;
                case FinalData.ITEM_EMPTY:
                    item_emptyTv = itemView.findViewById(R.id.item_emptyTv);
                    break;
            }
        }
    }


    public interface HomePageAdapterCallBack{

        public void onEmptyClick();

        public void onItemClick(JobListEntity entity,int position);

        public void onItemMoreClick(JobListEntity entity,int position);

        public void onLocationClick(TextView locationTv);
    }
}
