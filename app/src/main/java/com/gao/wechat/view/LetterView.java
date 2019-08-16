package com.gao.wechat.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.gao.wechat.R;

import org.jetbrains.annotations.NotNull;

public class LetterView extends View {

    private int textColorDefault = Color.BLACK;
    private int textColorSelected = ContextCompat.getColor(getContext(),R.color.colorAccent);
    private int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
            12, getResources().getDisplayMetrics());
    private int bgColor = Color.TRANSPARENT;
    private int bgColorTouch = Color.parseColor("#88999999");

    private int selectedIndex = -1;
    private int bgRadius = 9;
    private int marginTop = 0;
    private int maxItemHeight = bgRadius * 2 + 3;
    private int letterWidth = 0;
    private int letterHeight = 0;

    private TextPaint mTextPaint;
    private Paint mPaint;

    private TextView letterDialog;

    private String [] indexArray = {"↑", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};

    public LetterView(Context context) {
        this(context, null, 0);
    }

    public LetterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.LetterView, 0, 0);

        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.LetterView_textSize) {
                textSize = a.getDimensionPixelSize(attr, textSize);
            } else if (attr == R.styleable.LetterView_textColor) {
                textColorDefault = a.getColor(attr, textColorDefault);
            } else if (attr == R.styleable.LetterView_textColorSelect) {
                textColorSelected = a.getColor(attr, textColorSelected);
            } else if (attr == R.styleable.LetterView_backgroundPrimary) {
                bgColor = a.getColor(attr, bgColor);
            } else if (attr == R.styleable.LetterView_backgroundSelect) {
                bgColorTouch = a.getColor(attr, bgColorTouch);
            }
        }

        a.recycle();

        // 设置文字画笔
        mTextPaint = new TextPaint();
        mTextPaint.setColor(textColorDefault);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setAntiAlias(true);

        // 设置背景画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(bgColorTouch);

        bgRadius = getCircleRadius(mTextPaint);
        maxItemHeight = bgRadius * 2 + 3;
        letterHeight = Math.min(getHeight() / indexArray.length, maxItemHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        letterWidth = getWidth();
        letterHeight = Math.min(getHeight() / indexArray.length, maxItemHeight);
        marginTop = getMarginTop(letterHeight);

        for (int i = 0; i < indexArray.length; i++) {
            String index = indexArray[i];

            float x = letterWidth / 2.0F;
            float y = marginTop + letterHeight * (i + 1);
            int textHeight = getTextHeight(mTextPaint,index);

            if (i != selectedIndex) {
                mTextPaint.setColor(textColorDefault);
            } else {
                mTextPaint.setColor(textColorSelected);
                canvas.drawCircle(x, y - textHeight/2.0F, bgRadius, mPaint);
            }
            canvas.drawText(index, x, y, mTextPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                int position = (int) ((event.getY() - marginTop) / letterHeight);
                if (position == selectedIndex) {
                    return true;
                }
                if (position >= 0 && position < indexArray.length) {
                    selectedIndex = position;
                    invalidate();
                    if (mListener != null) {
                        mListener.onTouch(indexArray[position], position);
                    }
                    if (letterDialog != null) {
                        letterDialog.setText(indexArray[position]);
                        letterDialog.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mListener != null) {
                    mListener.onTouchResult();
                }
                if (letterDialog != null) {
                    letterDialog.setVisibility(View.GONE);
                }
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    public void setLetterDialog(TextView letterDialog) {
        this.letterDialog = letterDialog;
    }

    private int getTextHeight(@NotNull Paint paint, String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text,0, text.length(), rect);
        return rect.height();
    }

    private int getCircleRadius(Paint paint) {
        int sum = 0;
        for (String index : indexArray) {
            sum += getTextHeight(paint, index) - 3;
        }
        return sum / indexArray.length;
    }

    private int getMarginTop(int letterHeight) {
        return (getHeight() - (letterHeight * indexArray.length)) / 2;
    }

    private OnLetterTouchListener mListener;

    public void setOnLetterTouchListener(OnLetterTouchListener listener) {
        this.mListener = listener;
    }

    public interface OnLetterTouchListener {
        void onTouch(String tag, int position);
        void onTouchResult();
    }

}
