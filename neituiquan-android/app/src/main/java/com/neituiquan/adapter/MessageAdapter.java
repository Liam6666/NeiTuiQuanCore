package com.neituiquan.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neituiquan.database.ChatGroupDBEntity;
import com.neituiquan.database.DBConstants;
import com.neituiquan.entity.MessageEntity;
import com.neituiquan.utils.GlideUtils;
import com.neituiquan.utils.Millis2Date;
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

    private List<ChatGroupDBEntity> entityList = new ArrayList<>();

    public MessageAdapter(Context context) {
        this.context = context;
    }

    public void addData(List<ChatGroupDBEntity> newData){
        this.entityList.addAll(newData);
        notifyDataSetChanged();
    }

    public void addData(ChatGroupDBEntity newData){
        this.entityList.add(newData);
        notifyDataSetChanged();
    }

    public void refresh(List<ChatGroupDBEntity> newData){
        this.entityList.clear();
        addData(newData);
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder itemViewHolder =
                new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_message,parent,false));
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final ChatGroupDBEntity entity = entityList.get(position);
        if(entity != null){
            holder.item_titleTv.setText(entity.getOsNickName());
            if(entity.getLastChatEntity() != null){
                if(entity.getLastChatEntity().getIsFrom().equals(DBConstants.YES)){
                    holder.item_hintTv.setText("我 : "+entity.getLastChatEntity().getMsgDetails());
                }else{
                    holder.item_hintTv.setText(entity.getLastChatEntity().getReceiveNickName() + " : "+entity.getLastChatEntity().getMsgDetails());
                }
            }
            holder.item_timeTv.setText(Millis2Date.simpleMillis2Date(entity.getLastChatEntity().getCreateTime()));
            holder.item_iconTv.setText(entity.getNotReadCount());
            holder.item_contentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("otherSideId",entity.getOtherSideId());
                    context.startActivity(intent);
                }
            });
            GlideUtils.load(entity.getOsHeadImg(),holder.item_headImg);
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
        LinearLayout item_contentLayout;


        public ItemViewHolder(View itemView) {
            super(itemView);
            item_contentLayout = itemView.findViewById(R.id.item_contentLayout);
            item_headImg = itemView.findViewById(R.id.item_headImg);
            item_titleTv = itemView.findViewById(R.id.item_titleTv);
            item_timeTv = itemView.findViewById(R.id.item_timeTv);
            item_hintTv = itemView.findViewById(R.id.item_hintTv);
            item_iconTv = itemView.findViewById(R.id.item_iconTv);
        }
    }
}
