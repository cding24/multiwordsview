package com.linghu.multiwordview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.linghu.multiwords.R;

/**
 * Created by linghu on 2017/9/13.
 * 多个词组文字控件
 *
 */
public class MultiWordsView extends AppCompatTextView {

    private int viewWidth = -1;
    private int viewHeight = -1;
    private int lineHeight = 20;
    private int horSpace = 15;   //水平的间距
    private int verSpace = 8;    //垂直的行间距
    private int txtSize = 14;
    private int txtColor = 0x444444 /*0xFF666666*/;

    private Paint paint;
    private String[] contents;
    private Paint.FontMetricsInt fontMetrics;

    public MultiWordsView(Context context) {
        super(context);
    }

    public MultiWordsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public MultiWordsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        txtSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, txtSize, dm);
        horSpace = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, horSpace, dm);
        verSpace = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, verSpace, dm);
        lineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, lineHeight, dm);

        if(attrs != null){
            TypedArray all = context.obtainStyledAttributes(attrs, R.styleable.MultiWordsView);
            lineHeight = all.getDimensionPixelOffset(R.styleable.MultiWordsView_multiLineHeight, lineHeight);
            horSpace = all.getDimensionPixelOffset(R.styleable.MultiWordsView_multiHspace, horSpace);
            verSpace = all.getDimensionPixelOffset(R.styleable.MultiWordsView_multiVspace, verSpace);
            txtSize = all.getDimensionPixelSize(R.styleable.MultiWordsView_multiTextsize, txtSize);
            txtColor = all.getColor(R.styleable.MultiWordsView_multiTextColor, txtColor);
            all.recycle();
        }

        paint = new Paint();
        paint.setTextSize(txtSize);
        paint.setColor(txtColor);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    public void setContents(String[] strings){
        this.contents = strings;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(contents == null || contents.length == 0){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }else{
            viewHeight = MeasureSpec.getSize(heightMeasureSpec);
            if(viewHeight <= 0){
                int tmpWidth = 0;
                int tmpHeight= 0;

                viewWidth = MeasureSpec.getSize(widthMeasureSpec);
                for(String item : contents){
                    Rect rect = new Rect();
                    paint.getTextBounds(item, 0, item.length(), rect);
                    if(tmpHeight <= 0){
                        tmpHeight = (int)(lineHeight + getPaddingTop());
                    }
                    if(tmpWidth + rect.width() + horSpace >= viewWidth){ //换一行绘制
                        tmpWidth = 0;
                        tmpHeight += lineHeight;
                        tmpHeight += verSpace;
                        tmpWidth += rect.width();
                    }else{ //当前行绘制
                        tmpWidth += horSpace;
                        tmpWidth += rect.width();
                    }
                }

                viewHeight = tmpHeight + getPaddingBottom();
                super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY));
            }else{
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(this.contents != null && this.contents.length > 0){
            int tmpWidth = 0;
            int tmpHeight= 0;
            if(viewWidth <= 0 || viewHeight <= 0){
                return;
            }
            if(fontMetrics == null){
                fontMetrics = paint.getFontMetricsInt();
            }


            Rect rect = new Rect();
            for(String item : contents){
                paint.getTextBounds(item, 0, item.length(), rect);
                if(tmpHeight <= 0){
                    tmpHeight = (int)(lineHeight + verSpace);
                }

                if(tmpWidth + rect.width()  + horSpace >= viewWidth){ //换一行绘制
                    tmpWidth = 0;
                    Rect targetRect = new Rect(tmpWidth, tmpHeight, tmpWidth+rect.width(), tmpHeight+lineHeight);
                    int baseLine = (int)((targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2.0f);
//                    canvas.drawText(item, tmpWidth, (int)(tmpHeight+lineHeight*5.0/6.0f), paint);
                    canvas.drawText(item, tmpWidth, baseLine, paint);
                    tmpHeight += lineHeight;
                    tmpHeight += verSpace;
                    tmpWidth += rect.width();
                }else{ //当前行绘制
                    if(tmpWidth > 0){
                        tmpWidth += horSpace;
                    }
                    Rect targetRect = new Rect(tmpWidth, tmpHeight-verSpace-lineHeight, tmpWidth+rect.width(), tmpHeight-verSpace);
                    int baseLine = (int)((targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2.0f);
//                    canvas.drawText(item, tmpWidth, (int)(tmpHeight-lineHeight-verSpace+lineHeight*5.0/6.0f), paint);
                    canvas.drawText(item, tmpWidth, baseLine, paint);
                    tmpWidth += rect.width();
                }
            }
        }
    }

}