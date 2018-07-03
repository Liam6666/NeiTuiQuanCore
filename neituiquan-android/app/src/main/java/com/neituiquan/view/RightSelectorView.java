package com.neituiquan.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.neituiquan.work.R;

import java.util.List;

/**
 * Created by Augustine on 2018/6/27.
 * <p>
 * email:nice_ohoh@163.com
 */

public class RightSelectorView extends View {

    private Paint paint;

    private int viewHeight;

    private int viewWidth;

    private List<String> provinces;

    private int textSize = 30;

    private int textHeight;

    private float viewHeightProportion = 0.95f;

    private OnSelectorListener onSelectorListener;

    public RightSelectorView(Context context) {
        super(context);
    }


    public void init(List<String> provinces){
        this.provinces = provinces;
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(getContext(), R.color.themeColor));
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(textSize);
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lineColor));
    }

    public void setOnSelectorListener(OnSelectorListener onSelectorListener) {
        this.onSelectorListener = onSelectorListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(paint != null){
            drawText(canvas);
        }
    }

    private void drawText(Canvas canvas){
        int drawY = 0;

        int drawX = viewWidth / 2 - textSize / 2;

        textHeight = (int) (viewHeight * viewHeightProportion / provinces.size());

        for(int i = 0 ; i < provinces.size() ; i ++){
            drawY += textHeight;
            if(i == 0){
                drawY = (int) ((viewHeight - viewHeight * viewHeightProportion) / 2);
                firstTextY = drawY;
            }
            if(i == provinces.size() - 1){
                lastTextY = drawY;
            }
            canvas.drawText(provinces.get(i).substring(0,1),drawX,drawY,paint);
        }
    }

    private int downY;

    private int moveY;

    private int firstTextY;

    private int lastTextY;

    private int currentIndex;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downY = (int) event.getY();
                currentIndex = (moveY - firstTextY + textHeight) / textHeight;
                if(currentIndex >= 0 && currentIndex < provinces.size()){
                    if(onSelectorListener != null){
                        onSelectorListener.onSelectorIndex(currentIndex,provinces.get(currentIndex));
                    }
                }
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.highLineColor));
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = (int) event.getY();
                currentIndex = (moveY - firstTextY + textHeight) / textHeight;
                if(currentIndex >= 0 && currentIndex < provinces.size()){
                    if(onSelectorListener != null){
                        onSelectorListener.onSelectorIndex(currentIndex,provinces.get(currentIndex));
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lineColor));
                break;
        }
        return true;
    }


    public interface OnSelectorListener{

        public void onSelectorIndex(int index,String city);

    }
}
