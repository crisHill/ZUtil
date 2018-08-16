package zls.com.zlibrary;

import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by oop on 2018/8/15.
 */

public class StringUtil {

    public static boolean isEmpty(String s){
        return s == null || s.length() == 0;
    }

    public static int getTextSizeUp2Width(int maxWidth, Paint paint, String text){
        Rect rect = new Rect();
        int textSize = 1;
        int strWidth = 0;
        int stepSpace = 10;
        while (Math.abs(maxWidth - strWidth) > 1){
            if(maxWidth > strWidth){
                textSize += stepSpace;
            }else {
                textSize -= stepSpace;
            }

            paint.setTextSize(textSize);
            paint.getTextBounds(text, 0, text.length(), rect);
            strWidth = rect.width();
            if(strWidth > maxWidth){
                stepSpace = 1;
            }
        }

        return textSize;
    }

    public static int getTextSizeUp2Height(int maxHeight, Paint paint){
        int textSize = 1;
        int strHeight = 0;
        int stepSpace = 10;
        while (maxHeight - strHeight > 1){
            if(maxHeight > strHeight){
                textSize += stepSpace;
            }else {
                textSize -= stepSpace;
            }
            paint.setTextSize(textSize);
            Paint.FontMetricsInt metrics = paint.getFontMetricsInt();
            strHeight = metrics.descent - metrics.ascent;
            if(strHeight > maxHeight){
                stepSpace = 1;
            }
        }

        return textSize;
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
