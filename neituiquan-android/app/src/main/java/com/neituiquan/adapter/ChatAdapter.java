package com.neituiquan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.neituiquan.App;
import com.neituiquan.database.ChatEntity;
import com.neituiquan.work.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Augustine on 2018/7/9.
 * <p>
 * email:nice_ohoh@163.com
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ItemViewHolder> {

    private Context context;

    private List<ChatEntity> entityList = new ArrayList<>();

    private String selfId = App.getAppInstance().getUserInfoUtils().getUserInfo().data.getId();

    private static final int ITEM_TYPE_OTHER = 0;

    private static final int ITEM_TYPE_SELF = 1;

    public ChatAdapter(Context context, List<ChatEntity> entityList) {
        this.context = context;
        this.entityList = entityList;
    }

    public void refresh(List<ChatEntity> newList){
        entityList.clear();
        entityList.addAll(newList);
        notifyDataSetChanged();
    }

    public void addData(ChatEntity entity){
        this.entityList.add(entity);
        notifyDataSetChanged();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder holder;
        if(viewType == ITEM_TYPE_OTHER){
            holder = new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_left,parent,false));
        }else{
            holder = new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_right,parent,false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.textView.setText(entityList.get(position).getMsgDetails());
    }

    @Override
    public int getItemCount() {
        return entityList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(entityList.get(position).getFromId().equals(selfId)){
            return ITEM_TYPE_SELF;
        }
        return ITEM_TYPE_OTHER;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
