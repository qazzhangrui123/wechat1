package com.gao.wechat.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;

public class BitmapUtil {

    public static Bitmap getBitmap(Context context, int resource) {
        Resources res = context.getResources();
        return BitmapFactory.decodeResource(res, resource);
    }

    public static Bitmap getBitmap(byte [] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    public static byte [] getBitmapByte(Context context, int res, Bitmap.CompressFormat format) {
        return getBitmapByte(getBitmap(context, res), format);
    }

    public static byte [] getBitmapByte(Bitmap bitmap, Bitmap.CompressFormat format) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(format, 100, out);
        return out.toByteArray();
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleW = ((float) width) / w;
        float scaleH = ((float) height) / h;
        matrix.postScale(scaleW, scaleH);
        return Bitmap.createBitmap(bitmap,0,0,w,h,matrix,true);
    }

}
