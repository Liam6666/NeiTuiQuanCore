package com.neituiquan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.neituiquan.database.ChatGroupEntity;
import com.neituiquan.work.R;
import com.neituiquan.work.chat.ChatActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Augustine on 2018/7/9.
 * <p>
 * email:nice_ohoh@163.com
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ItemViewHolder> {

    private Context context;

    private List<ChatGroupEntity> entityList = new ArrayList<>();

    public MessageAdapter(Context context, List<ChatGroupEntity> entityList) {
        this.context = context;
        this.entityList = entityList;
    }

    public void refresh(List<ChatGroupEntity> newList){
        this.entityList.clear();
        this.entityList.addAll(newList);
        notifyDataSetChanged();
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder itemViewHolder =
                new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_message,parent,false));
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final ChatGroupEntity entity = entityList.get(position);
        if(entity != null){
            holder.item_titleTv.setText(entity.getGroupName());
            holder.item_hintTv.setText(entity.getLastFromNickName() +" : "+entity.getLastChat());
            holder.item_timeTv.setText(entity.getLastChatTime());
            holder.item_iconTv.setText(entity.getNotReadCount());
            holder.item_headImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("groupId",entity.getGroupId());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return entityList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{

        CircleImageView item_headImg;
        TextView item_titleTv;
        TextView item_timeTv;
        TextView item_hintTv;
        TextView item_iconTv;


        public ItemViewHolder(View itemView) {
            super(itemView);
            item_headImg = itemView.findViewById(R.id.item_headImg);
            item_titleTv = itemView.findViewById(R.id.item_titleTv);
            item_timeTv = itemView.findViewById(R.id.item_timeTv);
            item_hintTv = itemView.findViewById(R.id.item_hintTv);
            item_iconTv = itemView.findViewById(R.id.item_iconTv);
        }
    }
}
