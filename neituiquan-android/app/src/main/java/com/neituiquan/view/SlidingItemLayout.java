package com.neituiquan.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;


/**
 * Created by Augustine on 2018/6/22.
 * <p>
 * email:nice_ohoh@163.com
 */

public class SlidingItemLayout extends HorizontalScrollView {

    private LinearLayout contentLayout;

    private View visibleLayout;

    private View menuLayout;

    private boolean isOpen = false;

    private int menuLayoutWidth;

    private int viewHeight;

    private SlidingItemStateListener slidingItemStateListener;

    public SlidingItemLayout(Context context) {
        super(context);
        init();
    }

    public SlidingItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentLayout = (LinearLayout) getChildAt(0);
        visibleLayout = contentLayout.getChildAt(0);
        visibleLayout.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(),-1));
        menuLayout = contentLayout.getChildAt(1);
        menuLayoutWidth = SizeUtils.getMeasuredWidth(menuLayout);
    }

    private void init() {
        setHorizontalScrollBarEnabled(false);//禁用滚动条
    }


    public void close() {
        isOpen = false;
        smoothScrollTo(0, 0);
        if(slidingItemStateListener != null){
            slidingItemStateListener.onClose();
        }
    }


    public void open() {
        isOpen = true;
        smoothScrollTo(menuLayoutWidth, 0);
        if(slidingItemStateListener != null){
            slidingItemStateListener.onOpen();
        }
    }

    public void noAnimationClose(){
        isOpen = false;
        scrollTo(0,0);
    }

    public void noAnimationOpen(){
        isOpen = true;
        scrollTo(menuLayoutWidth,0);

    }


    public boolean isOpen() {
        return isOpen;
    }

    public void setSlidingItemStateListener(SlidingItemStateListener slidingItemStateListener) {
        this.slidingItemStateListener = slidingItemStateListener;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if(!isOpen){
                //如果是关闭状态
                if (scrollX > menuLayoutWidth / 3 && scrollX > oldScrollX) {
                    //当滑动幅度大于menuLayoutWidth/3的时候
                    open();
                    return true;
                } else {
                    close();
                    return true;
                }
            }else{
                //如果是打开状态
                if(scrollX < (menuLayoutWidth - menuLayoutWidth / 3) && oldScrollX > scrollX){
                    close();
                    return true;
                }else{
                    open();
                    return true;
                }
            }

        }
        return super.onTouchEvent(ev);
    }

    private int scrollX;

    private int oldScrollX;

    @Override
    protected void onScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        super.onScrollChanged(scrollX, scrollY, oldScrollX, oldScrollY);
        this.scrollX = scrollX;
        this.oldScrollX = oldScrollX;

    }

    public interface SlidingItemStateListener{

        public void onOpen();

        public void onClose();
    }

}
