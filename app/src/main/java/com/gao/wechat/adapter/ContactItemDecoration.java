package com.gao.wechat.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gao.wechat.util.DisplayUtil;

import java.util.List;

public class ContactItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;

    private List<String> tags;

    private int headerHeight;
    private int headerPadding;
    private int headerItemCount;

    private Paint mPaint;
    private TextPaint mTextPaint;

    public ContactItemDecoration(Context context) {
        this(context, 0);
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setPadding(int padding) {
        this.headerPadding = DisplayUtil.dp2px(mContext, padding);
    }

    public ContactItemDecoration(Context context, int headerItemCount) {
        this.mContext = context;
        this.headerItemCount = headerItemCount;
        this.headerHeight = DisplayUtil.dp2px(mContext, 35);

        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#FFEEEEEE"));
        mPaint.setAntiAlias(true);

        mTextPaint = new TextPaint();
        mTextPaint.setColor(Color.parseColor("#FF555555"));
        mTextPaint.setTextSize(DisplayUtil.sp2px(context, 14));
        mTextPaint.setAntiAlias(true);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (tags == null || tags.size() == 0) {
            return;
        }

        int position = parent.getChildAdapterPosition(view);
        int realPos = position - headerItemCount;
        if (position == headerItemCount || (position > headerItemCount &&
                !tags.get(realPos).equals(tags.get(realPos - 1)))) {
            outRect.set(0, headerHeight, 0, 0);
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent,
                       @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (tags == null || tags.size() == 0) {
            return;
        }
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            int realPos = position - headerItemCount;
            if (position == headerItemCount || (position > headerItemCount &&
                    !tags.get(realPos).equals(tags.get(realPos - 1)))) {
                String tag = tags.get(realPos);
                if (mListener == null) {
                    drawHeader(c, parent, view, tag);
                } else {
                    mListener.onDrawHeader(c, mPaint, mTextPaint,
                            getHeaderCoordinate(parent, view), realPos);
                }
            }
        }
    }

    private int [] getHeaderCoordinate(RecyclerView parent, View view) {
        RecyclerView.LayoutParams params =
                (RecyclerView.LayoutParams) view.getLayoutParams();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int bottom = view.getTop() - params.topMargin;
        int top = bottom - headerHeight;
        return new int [] {left, top, right, bottom};
    }

    private void drawHeader(Canvas c, RecyclerView parent, View view, String tag) {
        int [] params = getHeaderCoordinate(parent, view);
        c.drawRect(params[0],params[1],params[2],params[3],mPaint);
        int x = params[0] + headerPadding;
        int y = params[1] + (headerHeight + getTextHeight(mTextPaint, tag)) / 2;
        c.drawText(tag, x, y, mTextPaint);
    }

    private int getTextHeight(TextPaint textPaint, String text) {
        Rect rect = new Rect();
        textPaint.getTextBounds(text,0,text.length(),rect);
        return rect.height();
    }

    private OnDrawItemListener mListener;

    public void setOnDrawItemListener(OnDrawItemListener listener) {
        this.mListener = listener;
    }

    public interface OnDrawItemListener {
        /**
         * 设置绘制Header的监听器
         * @param canvas 绘图板
         * @param paint 画笔
         * @param textPaint 文字画笔
         * @param params 参数，包括left，top，right，bottom。
         *               分别代表Header所在区域的四个坐标值
         * @param position 位置（不包括固定在头部的元素）
         */
        void onDrawHeader(Canvas canvas, Paint paint, TextPaint textPaint,
                          int [] params, int position);
    }

}
