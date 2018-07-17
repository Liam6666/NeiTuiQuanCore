package com.neituiquan.dialog;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.neituiquan.base.BaseDialog;
import com.neituiquan.work.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Augustine on 2018/7/17.
 * <p>
 * email:nice_ohoh@163.com
 */

public class ListDialog extends BaseDialog {

    private View contentView;
    private RecyclerView dialog_recyclerView;

    private ListAdapter listAdapter;

    private List<String> dataList = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;

    public ListDialog(Context context) {
        super(context);
    }

    @Override
    public void init() {
        contentView = View.inflate(getContext(), R.layout.dialog_list,null);
        dialog_recyclerView = contentView.findViewById(R.id.dialog_recyclerView);
        setContentView(contentView);
        getLayoutParams().width = (int) (ScreenUtils.getScreenWidth() * 0.8f);
        setDialogParams(getLayoutParams());
    }

    @Override
    public void initList() {

    }

    public void setData(List<String> list){
        dataList.clear();
        dataList.addAll(list);
        linearLayoutManager = new LinearLayoutManager(getContext());
        dialog_recyclerView.setLayoutManager(linearLayoutManager);
        listAdapter = new ListAdapter();
        dialog_recyclerView.setAdapter(listAdapter);
    }

    public List<String> getDataList() {
        return dataList;
    }

    public ListAdapter getListAdapter() {
        return listAdapter;
    }

    public class ListAdapter extends RecyclerView.Adapter<ItemViewHolder>{


        private ListDialogCallBack listDialogCallBack;


        public void setListDialogCallBack(ListDialogCallBack listDialogCallBack) {
            this.listDialogCallBack = listDialogCallBack;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_dialog_list,parent,false);
            return new ItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            holder.item_textView.setText(dataList.get(position));
            holder.item_textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listDialogCallBack != null){
                        listDialogCallBack.onItemClick(position,dataList.get(position));
                    }
                    dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView item_textView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            item_textView = itemView.findViewById(R.id.item_textView);
        }
    }

    public interface ListDialogCallBack {

        public void onItemClick(int index,String msg);

    }
}
