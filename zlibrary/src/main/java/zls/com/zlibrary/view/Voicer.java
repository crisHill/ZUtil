package zls.com.zlibrary.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import zls.com.zlibrary.StringUtil;

/**
 * TODO: document your custom view class.
 */
public class Voicer extends View {

    //默认属性
    private final int COLOR_ONE = Color.parseColor("#87CEFA");
    private final int COLOR_TWO = Color.parseColor("#55FFCC");
    private final int SLEEP_TIME = 150;
    private final String STR_ON = "listening";
    private final String STR_OFF = "speak";
    private final int ALPHA_OFF = 50;
    private final int ALPHA_ON = 150;
    private final int RING_WIDTH = 5;

    //用户可以设置属性
    //private int color1, color2;
    private int textSize;

    //状态相关属性
    private Paint paint;
    private int maxTextSize;
    private boolean on;
    private int curRadius;
    private int curColor = COLOR_TWO;
    private String curText = STR_OFF;
    private int curAlpha = ALPHA_ON;//Alpha取值范围为0~255，数值越小越透明

    //间接获取的属性
    private int maxRadius, minRadius, radiusChangeStep;
    //private Subscription subscription = null;
    private Disposable disposable;


    public Voicer(Context context) {
        this(context, null);
    }

    public Voicer(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        setOnClickListener(v -> switchOnOff());
    }

    private void switchOnOff() {
        on = !on;
        if(on){
            curAlpha = ALPHA_ON;
            curColor = COLOR_TWO;
            curText = STR_ON;
            startCircling();
        }else {
            curAlpha = ALPHA_OFF;
            curColor = COLOR_ONE;
            curText = STR_OFF;
            invalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        on = false;
        disposable = null;
    }

    private void startCircling() {
        disposable = Observable.interval(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if(on){
                            curRadius += radiusChangeStep;
                            if(curRadius > maxRadius){
                                curRadius = minRadius;
                            }

                            if(curColor == COLOR_ONE){
                                curColor = COLOR_TWO;
                            }else {
                                curColor = COLOR_ONE;
                            }

                            invalidate();

                        }else {
                            if(disposable != null){
                                disposable.dispose();
                            }
                        }
                    }
                });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);

        if(maxTextSize == 0){
            maxTextSize =  StringUtil.getTextSizeUp2Space(getWidth(), getHeight(), curText, paint);
            textSize = (int) (maxTextSize * 0.5);

            minRadius = 20;
            maxRadius = Math.max(Math.min(getWidth(), getHeight()) / 2 - 2, minRadius + 2);
            radiusChangeStep = Math.max((maxRadius - minRadius) / 5, 1);

            curRadius = (maxRadius + minRadius) / 2;
            curColor = COLOR_ONE;
            curAlpha = ALPHA_OFF;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
        drawRing(canvas);
        drawText(canvas);
    }

    private void drawBg(Canvas canvas){
        if(getBackground() instanceof ColorDrawable){
            paint.setColor(((ColorDrawable)getBackground()).getColor());
        }
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(curAlpha);//取值范围为0~255，数值越小越透明
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, Math.min(getWidth() / 2, getHeight() / 2) - 1, paint);
    }

    private void drawRing(Canvas canvas){
        paint.setColor(curColor);
        paint.setStrokeWidth(RING_WIDTH);//环的宽度
        paint.setStyle(Paint.Style.STROKE); //绘制空心圆
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, curRadius, paint);
    }

    private void drawText(Canvas canvas){
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(curText, getWidth() / 2, StringUtil.getCenterVerticalTextBaseline(this, paint), paint);

    }
}