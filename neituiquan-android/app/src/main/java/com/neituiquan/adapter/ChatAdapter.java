package com.neituiquan.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.neituiquan.App;
import com.neituiquan.database.ChatDBEntity;
import com.neituiquan.database.DBConstants;
import com.neituiquan.entity.ChatLoopEntity;
import com.neituiquan.utils.ChatUtils;
import com.neituiquan.utils.GlideUtils;
import com.neituiquan.work.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Augustine on 2018/7/9.
 * <p>
 * email:nice_ohoh@163.com
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ItemViewHolder> {

    private Context context;

    private List<ChatDBEntity> entityList = new ArrayList<>();

    private static final int ITEM_TYPE_OTHER = 0;

    private static final int ITEM_TYPE_SELF = 1;

    public ChatAdapter(Context context) {
        this.context = context;
    }

    public void addData(ChatDBEntity newData){
        this.entityList.add(0,newData);
        notifyDataSetChanged();
    }

    public void addData(List<ChatDBEntity> newData){
        this.entityList.addAll(newData);
        notifyDataSetChanged();
    }

    public void refresh(List<ChatDBEntity> newData){
        entityList.clear();
        addData(newData);
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
        ChatDBEntity entity = entityList.get(position);
        holder.itemChat_contentLayout.removeAllViews();
        if(ChatUtils.isImg(entity.getMsgDetails())){
            ImageView imageView = createImg();
            Glide.with(context).load(entity.getMsgDetails()).into(imageView);
            holder.itemChat_contentLayout.addView(imageView);
        }else{
            TextView textView = createTextView();
            textView.setText(entity.getMsgDetails());
            holder.itemChat_contentLayout.addView(textView);
        }
        if(entity.getIsFrom().equals(DBConstants.YES)){
            holder.itemChat_nickNameTv.setText("我");
            GlideUtils.load(entity.getFromHeadImg(),holder.itemChat_headImg);
        }else{
            holder.itemChat_nickNameTv.setText(entity.getFromNickName());
            GlideUtils.load(entity.getFromHeadImg(),holder.itemChat_headImg);
        }
    }


    @Override
    public int getItemCount() {
        return entityList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(entityList.get(position).getIsFrom().equals(DBConstants.YES)){
            return ITEM_TYPE_SELF;
        }
        return ITEM_TYPE_OTHER;
    }

    private TextView createTextView(){
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,-2);
        textView.setLayoutParams(params);
        textView.setTextColor(ContextCompat.getColor(context,R.color.highTextColor));
        textView.setTextSize(15);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        return textView;
    }

    private ImageView createImg(){
        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2,-2);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        LinearLayout itemChat_contentLayout;
        TextView itemChat_nickNameTv;
        CircleImageView itemChat_headImg;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemChat_contentLayout = itemView.findViewById(R.id.itemChat_contentLayout);
            itemChat_nickNameTv = itemView.findViewById(R.id.itemChat_nickNameTv);
            itemChat_headImg = itemView.findViewById(R.id.itemChat_headImg);
        }
    }
}
