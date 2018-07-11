package com.neituiquan.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.neituiquan.utils.ColorUtils;

/**
 * Created by Augustine on 2018/7/6.
 * <p>
 * email:nice_ohoh@163.com
 *
 * 获取企业名字的第一个字，画一个圆形的icon
 *
 * 利用最薪资高低程度来替换icon的背景颜色
 *
 */

public class CompanyIconView extends View{

    private int startColor = Color.parseColor("#00B38A");

    private int endColor = Color.parseColor("#EE4335");

    private Paint paint;

    private TextPaint textPaint;

    private String contentText;

    private int viewHeight;

    private int viewWidth;

    private int maxValue = 100;

    public CompanyIconView(Context context) {
        super(context);
        init();
    }

    public CompanyIconView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
    }

    private void init(){
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setDither(true);

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setStrokeWidth(3);
    }

    public void setValues(String contentText,int value){
        this.contentText = contentText.substring(0,1);
        textPaint.setTextSize(50);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.WHITE);
        int color = ColorUtils.getCurrentColor(((float)value / maxValue),startColor,endColor);
        paint.setColor(color);
        invalidate();
    }


    RectF rectF = new RectF();


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(contentText == null){
            return;
        }

        rectF.top = 0;
        rectF.left = 0;
        rectF.right = viewWidth;
        rectF.bottom = viewHeight;
        canvas.drawArc(rectF,0,360,true,paint);

        canvas.drawText(contentText,
                viewWidth / 2,
                viewHeight/2 + baseLine(textPaint),
                textPaint);
    }


    private float baseLine(Paint p){
        Paint.FontMetrics m = p.getFontMetrics();
        return (m.descent - m.ascent) /2 - m.descent;

    }


}
