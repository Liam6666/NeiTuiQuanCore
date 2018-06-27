package com.neituiquan.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.neituiquan.FinalData;

/**
 * Created by Augustine on 2018/6/27.
 * <p>
 * email:nice_ohoh@163.com
 */

public class AutoLoadRecyclerView extends RecyclerView {

    private final String TAG = "AutoLoadRecyclerView";

    private boolean isMoveToBottom = false;

    private OnLoadMoreCallBack onLoadMoreCallBack;

    public AutoLoadRecyclerView(Context context) {
        super(context);
    }

    public AutoLoadRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnLoadMoreCallBack(OnLoadMoreCallBack onLoadMoreCallBack) {
        this.onLoadMoreCallBack = onLoadMoreCallBack;
    }

    public void loadComplete() {
        this.isMoveToBottom = false;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (!canScrollVertically(1)) {
            if (!isMoveToBottom) {
                if(FinalData.DEBUG){
                    Log.i(TAG, "滑动到底部");
                }
                if (onLoadMoreCallBack != null) {
                    onLoadMoreCallBack.onLoadMore();
                    isMoveToBottom = true;
                }
            }
        }
    }


    public interface OnLoadMoreCallBack{

        public void onLoadMore();

    }
}
