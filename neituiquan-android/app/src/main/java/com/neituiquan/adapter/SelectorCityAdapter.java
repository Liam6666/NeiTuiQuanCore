package com.neituiquan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.neituiquan.entity.CitysEntity;
import com.neituiquan.work.R;

import java.util.List;

/**
 * Created by Augustine on 2018/6/27.
 * <p>
 * email:nice_ohoh@163.com
 */

public class SelectorCityAdapter extends BaseExpandableListAdapter {

    private List<CitysEntity> entityList;

    private Context context;

    private ChildItemCallBack childItemCallBack;

    public SelectorCityAdapter(List<CitysEntity> entityList, Context context) {
        this.entityList = entityList;
        this.context = context;
    }

    public void setChildItemCallBack(ChildItemCallBack childItemCallBack) {
        this.childItemCallBack = childItemCallBack;
    }

    @Override
    public int getGroupCount() {
        return entityList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return entityList.get(groupPosition).getCityList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return entityList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return entityList.get(groupPosition).getCityList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupItemHolder itemHolder;
        if(convertView == null){
            itemHolder = new GroupItemHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_city_selector_province,parent,false);
            itemHolder.textView = (TextView) convertView;
            convertView.setTag(itemHolder);
        }else{
            itemHolder = (GroupItemHolder) convertView.getTag();
        }
        itemHolder.textView.setText(entityList.get(groupPosition).getProvince());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildItemHolder itemHolder;
        if(convertView == null){
            itemHolder = new ChildItemHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_city_selector_city,parent,false);
            itemHolder.textView = (TextView) convertView;
            convertView.setTag(itemHolder);
        }else{
            itemHolder = (ChildItemHolder) convertView.getTag();
        }
        itemHolder.textView.setText(entityList.get(groupPosition).getCityList().get(childPosition));
        itemHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(childItemCallBack != null){
                    childItemCallBack.onChildItemClickListener(groupPosition,childPosition);
                }
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupItemHolder{

        TextView textView;
    }

    class ChildItemHolder{
        TextView textView;
    }


    public interface ChildItemCallBack{

        public void onChildItemClickListener(int groupIndex,int childIndex);

    }
}
