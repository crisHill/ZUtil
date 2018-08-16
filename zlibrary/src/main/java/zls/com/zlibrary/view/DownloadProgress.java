package zls.com.zlibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import zls.com.zlibrary.R;

/**
 * TODO: document your custom view class.
 */
public class DownloadProgress extends View {

    private int mWidth;
    private int mHeight;
    private int borderColor;
    private float borderWidth;
    private int downloadingCompleteColor, downloadingUnCompleteColor;
    private int pauseCompleteColor, pauseUnCompleteColor;
    private float mProgress;
    private float mTextSize;

    public void setInfo(float mProgress, boolean downloading){
        this.mProgress = mProgress;
        this.downloading = downloading;
        invalidate();
    }

    private boolean downloading;
    private boolean showProgressText;

    private Paint paint;

    public DownloadProgress(Context context) {
        this(context, null);
    }

    public DownloadProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DownloadProgress(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DownloadProgress, defStyle, 0);

        borderColor = a.getColor(R.styleable.DownloadProgress_borderColor, Color.WHITE);
        borderWidth = a.getDimension(R.styleable.DownloadProgress_borderWidth, 2);
        downloadingCompleteColor = a.getColor(R.styleable.DownloadProgress_downloadingCompleteColor, Color.parseColor("#009FDA"));
        downloadingUnCompleteColor = a.getColor(R.styleable.DownloadProgress_downloadingUnCompleteColor, Color.parseColor("#43CBFD"));
        pauseCompleteColor = a.getColor(R.styleable.DownloadProgress_pauseCompleteColor, Color.parseColor("#9EA1A2"));
        pauseUnCompleteColor = a.getColor(R.styleable.DownloadProgress_pauseUnCompleteColor, Color.parseColor("#D3DBDE"));
        mProgress = a.getFloat(R.styleable.DownloadProgress_progress, 0);
        mTextSize = a.getDimension(R.styleable.DownloadProgress_textSize, 30);
        downloading = a.getBoolean(R.styleable.DownloadProgress_downloading, false);
        showProgressText = a.getBoolean(R.styleable.DownloadProgress_showProgressText, false);

        a.recycle();

        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(mTextSize);

    }

    private void drawBorder(Canvas canvas, int w, int h, float r){
        paint.setColor(borderColor);
        paint.setStrokeWidth(borderWidth);

        Path path = new Path();
        RectF rectF = new RectF(0, 0, w, h);
        path.addRoundRect(rectF, r, r, Path.Direction.CW);
        canvas.drawPath(path, paint);
    }

    private void drawMain(Canvas canvas, float w, float h, float r, int color){
        paint.setColor(color);
        paint.setStrokeWidth(1);

        Path path = new Path();
        RectF rectF = new RectF(borderWidth, borderWidth, w + borderWidth, h + borderWidth);
        path.addRoundRect(rectF, r, r, Path.Direction.CW);
        canvas.drawPath(path, paint);
    }

    private void drawLeftArc(Canvas canvas, float completeWidth, float r, float left, float top){
        float angle = (float) (Math.acos((r - completeWidth) / r) * 180 / Math.PI);
        float startAngle = 180 - angle;
        float sweepAngle = angle * 2;
        paint.setColor(downloading ? downloadingCompleteColor : pauseCompleteColor);
        canvas.drawArc(new RectF(left, top, left + r * 2, top + r * 2), startAngle, sweepAngle,false, paint);
    }

    private void drawRightArc(Canvas canvas, float w, float completeWidth, float r, float left, float top){
        float angle = (float) (Math.acos((completeWidth- w + r) / r) * 180 / Math.PI);
        float startAngle = 0 - angle;
        float sweepAngle = angle * 2;
        paint.setColor(downloading ? downloadingUnCompleteColor : pauseUnCompleteColor);
        canvas.drawArc(new RectF(left, top, left + r * 2, top + r * 2), startAngle, sweepAngle,false, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        float r = mHeight  / 2;

        float w = mWidth - borderWidth * 2;
        float h = mHeight - borderWidth * 2;
        float innerR = h / 2;
        float completeWidth = w * mProgress / 100;

        drawBorder(canvas, mWidth, mHeight, r);

        if(completeWidth < innerR){
            drawMain(canvas, w, h, innerR, downloading ? downloadingUnCompleteColor : pauseUnCompleteColor);
            drawLeftArc(canvas, completeWidth, innerR, borderWidth, borderWidth);
        }else if(completeWidth > w - innerR){
            drawMain(canvas, w, h, innerR, downloading ? downloadingCompleteColor : pauseCompleteColor);
            drawRightArc(canvas, w, completeWidth, innerR, mWidth - borderWidth - innerR * 2, borderWidth);
        }else {
            drawMain(canvas, w, h, innerR, downloading ? downloadingUnCompleteColor : pauseUnCompleteColor);
            drawLeftArc(canvas, innerR, innerR, borderWidth, borderWidth);

            paint.setColor(downloading ? downloadingCompleteColor : pauseCompleteColor);
            canvas.drawRect(borderWidth + innerR, borderWidth, borderWidth + completeWidth, borderWidth + innerR * 2, paint);
        }

        if(showProgressText){
            int p = (int)mProgress;
            String testString = p + "%";
            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.LEFT);
            Rect bounds = new Rect();
            paint.getTextBounds(testString, 0, testString.length(), bounds);
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            int baseline = (mHeight - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            canvas.drawText(testString,mWidth / 2 - bounds.width() / 2, baseline, paint);
        }

    }

}
