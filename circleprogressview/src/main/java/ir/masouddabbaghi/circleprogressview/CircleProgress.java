package ir.masouddabbaghi.circleprogressview;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

public class CircleProgress extends View {

    private int min = 0;
    private int max = 100;
    private int progress = 0;
    private int color = Color.DKGRAY;
    private float strokeWidth = 4;
    private boolean autoColored = false;
    private boolean showPercent = true;

    private static final float FACTOR = 0.3f;

    private Paint backgroundPaint;
    private Paint foregroundPaint;
    private Paint percentPaint;

    private RectF rectF;
    Rect bounds;

    public CircleProgress(Context context) {
        super(context);
        init(context);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // Attribute
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgress, defStyleAttr, 0);

        min = typedArray.getInteger(R.styleable.CircleProgress_circleProgress_Min, 0);
        max = typedArray.getInteger(R.styleable.CircleProgress_circleProgress_Max, 100);
        if (max < min) {
            max = min;
        }

        progress = typedArray.getInteger(R.styleable.CircleProgress_circleProgress_Progress, min);
        if (progress < min) {
            progress = min;
        }

        color = typedArray.getColor(R.styleable.CircleProgress_circleProgress_Color, Color.DKGRAY);
        strokeWidth = typedArray.getDimension(R.styleable.CircleProgress_circleProgress_StrokeWidth, 4);
        autoColored = typedArray.getBoolean(R.styleable.CircleProgress_circleProgress_AutoColored, false);
        showPercent = typedArray.getBoolean(R.styleable.CircleProgress_circleProgress_ShowPercent, true);
        typedArray.recycle();

        // Initialize
        init(context);
    }

    private void init(Context context) {
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(adjustAlpha(color, FACTOR));
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);

        foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        foregroundPaint.setColor(color);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(strokeWidth);

        rectF = new RectF();
        bounds = new Rect();

        percentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        percentPaint.setColor(color);
        percentPaint.setTextAlign(Paint.Align.CENTER);
        percentPaint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
        float density = context.getResources().getDisplayMetrics().density;
        percentPaint.setTextSize(20 * density);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        int min = Math.min(width, height);
        setMeasuredDimension(min, min);

        rectF.set(strokeWidth / 2, strokeWidth / 2, min - strokeWidth / 2, min - strokeWidth / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int sweepAngel = (((progress - min) * 360) / (max - min));
        int percent = (((progress - min) * 100) / (max - min));

        if (autoColored) {
            int red = (100 - percent) * 2;
            int green = (percent * 2);
            int blue = 20;

            int newColor = Color.rgb(red, green, blue);
            backgroundPaint.setColor(adjustAlpha(newColor, FACTOR));
            foregroundPaint.setColor(newColor);
            percentPaint.setColor(newColor);
        } else {
            backgroundPaint.setColor(adjustAlpha(color, FACTOR));
            foregroundPaint.setColor(color);
            percentPaint.setColor(color);
        }
        canvas.drawOval(rectF, backgroundPaint);
        canvas.drawArc(rectF, -90, sweepAngel, false, foregroundPaint);

        if (showPercent) {
            String percentLabel = percent + " %";
            float x = (float) (getPaddingLeft() + (getWidth() - getPaddingLeft() - getPaddingRight()) / 2);
            float y = (float) (getPaddingTop() + (getHeight() - getPaddingTop() - getPaddingBottom()) / 2);
            percentPaint.getTextBounds(percentLabel, 0, percentLabel.length(), bounds);
            y += (float) (bounds.height() / 2);
            canvas.drawText(percentLabel, x, y, percentPaint);
        }

    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = Math.max(min, 0);
        invalidate();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = (max > 0) ? max : min;
        invalidate();
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        if (progress < min)
            this.progress = min;
        else this.progress = Math.min(progress, max);
        invalidate();
    }

    public int getColor() {
        return color;
    }

    public void setColor(@ColorInt int color) {
        this.color = color;
        foregroundPaint.setColor(color);
        backgroundPaint.setColor(adjustAlpha(color, 0.1f));
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        if (strokeWidth < 0 || strokeWidth == this.strokeWidth)
            return;
        this.strokeWidth = strokeWidth;
        foregroundPaint.setStrokeWidth(strokeWidth);
        backgroundPaint.setStrokeWidth(strokeWidth);
        invalidate();
        requestLayout();
    }

    public boolean isAutoColored() {
        return autoColored;
    }

    public void setAutoColored(boolean autoColored) {
        if (this.autoColored == autoColored)
            return;
        this.autoColored = autoColored;
        invalidate();
    }

    public boolean isShowPercent() {
        return showPercent;
    }

    public void setShowPercent(boolean showPercent) {
        if (this.showPercent == showPercent)
            return;
        this.showPercent = showPercent;
        invalidate();
    }

    private int adjustAlpha(int color, float factor) {
        if (factor < 0.0f || factor > 1.0f)
            return color;
        float alpha = Math.round(Color.alpha(color) * factor);
        return Color.argb(
                (int) alpha,
                Color.red(color),
                Color.green(color),
                Color.blue(color)
        );
    }

    public void setProgressWithAnimation(int value) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "progress", value);
        long duration = Math.abs(value - progress) * 2000L / (max - min);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }
}

