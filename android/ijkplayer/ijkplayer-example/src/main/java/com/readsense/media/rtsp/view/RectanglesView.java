package com.readsense.media.rtsp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

import com.readsense.media.rtsp.R;

import cn.readsense.body.Body;


/**
 * View which draws rectangles.
 */
public class RectanglesView extends View {

    private final List<Rect> rectangles = new ArrayList<>();
    private final List<Point> recogPonit = new ArrayList<>();
    private final Paint strokePaint = new Paint();

    private volatile float widthScale = 1.0f;
    private volatile float heightScale = 1.0f;

    public void setScale(float widthScale, float heightScale) {
        this.widthScale = widthScale;
        this.heightScale = heightScale;
    }

    public float getWidthScale() {
        return widthScale;
    }

    public float getHeightScale() {
        return heightScale;
    }

    public RectanglesView(Context context) {
        super(context);
    }

    public RectanglesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        applyAttributes(context, attrs);
    }

    public RectanglesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        applyAttributes(context, attrs);
    }

    private void applyAttributes(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.RectanglesView);

        try {
            strokePaint.setStyle(Paint.Style.STROKE);
            strokePaint.setColor(
                    attributes.getColor(R.styleable.RectanglesView_rectanglesColor, Color.BLUE)
            );
            strokePaint.setStrokeWidth(
                    attributes.getDimensionPixelSize(R.styleable.RectanglesView_rectanglesStrokeWidth, 1)
            );
            strokePaint.setTextSize(32);
        } finally {
            attributes.recycle();
        }
    }

    /**
     * Updates rectangles which will be drawn.
     *
     * @param bodies body to draw.
     */
    public void setBody(@NonNull List<Body> bodies) {
        ensureMainThread();

        this.rectangles.clear();
        this.recogPonit.clear();
        /*this.rectangles.add(
                new Rect(100, 100, 200, 200)
        );*/
        for (Body body : bodies) {
            if (body == null) {
                continue;
            }
            float[] rect = body.getRect();
            //final int left = getWidth() - (int) (rect[0] * widthScale + rect[2] * widthScale);
            final int left = (int) (rect[0] * widthScale);
            final int top = (int) (rect[1] * heightScale);
            final int right = left + (int) (rect[2] * widthScale);
            final int bottom = top + (int) (rect[3] * heightScale);

            this.rectangles.add(
                    new Rect(left, top, right, bottom)
            );
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Rect rectangle : rectangles) {
            canvas.drawRect(rectangle, strokePaint);
        }
    }

    private void ensureMainThread() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new IllegalThreadStateException("This method must be called from the main thread");
        }
    }

}
