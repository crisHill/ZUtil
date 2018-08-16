package zls.com.zlibrary.util;

import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import zls.com.zlibrary.algorithm.FixedStepApproacher;

/**
 * Created by oop on 2018/8/15.
 */

public class StringUtil {

    public static boolean isEmpty(String s){
        return s == null || s.length() == 0;
    }

    /*
    *       获取固定宽度下最大的字体
    *       FixedStepApproacher.findMostAppropriateParam 返回的是 textSize
    */
    public static int getTextSizeUp2Width(int maxWidth, Paint paint, String text){
        Rect rect = new Rect();
        return FixedStepApproacher.findMostAppropriateParam(maxWidth, curParam -> {
            int textSize = curParam;
            paint.setTextSize(textSize);
            paint.getTextBounds(text, 0, text.length(), rect);
            return rect.width();
        });

    }

    /*
    *       获取固定高度下最大的字体
    *       FixedStepApproacher.findMostAppropriateParam 返回的是 textSize
    */
    public static int getTextSizeUp2Height(int maxHeight, Paint paint){
        return FixedStepApproacher.findMostAppropriateParam(maxHeight, curParam -> {
            paint.setTextSize(curParam);
            Paint.FontMetricsInt metrics = paint.getFontMetricsInt();
            return metrics.descent - metrics.ascent;
        });
    }

    public static int getTextSizeUp2Space(int maxWidth, int maxHeight, String text, Paint paint){
        return Math.min(getTextSizeUp2Width(maxWidth, paint, text), getTextSizeUp2Height(maxHeight, paint));
    }

    /*
        drawText 时 参数 y 指的是文字的 baseline
        前置条件：执行此方法前 paint 必须设置了 textSize
    */
    public static int getCenterVerticalTextBaseline(View view, Paint paint){
        Paint.FontMetricsInt metrics = paint.getFontMetricsInt();
        return (view.getHeight() - metrics.bottom - metrics.top) / 2;
    }

}
