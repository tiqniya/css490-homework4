
package com.css490.homework4.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.css490.homework4.util.BitmapLoader;

public class DrawingView extends View {

    private Path drawPath;
    private Paint drawPaint, canvasPaint;
    private int paintColor = DrawingColors.RED.getColor();
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;

    private Context context;
    private DisplayMetrics metrics;

    private int imageID;
    private boolean hasImage;
    private boolean drawingEnabled;
    private float stroke = DrawingStrokes.MEDIUM.getStroke();

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        setDimensions();
        setupDrawing();
    }

    private void setDimensions() {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        metrics = new DisplayMetrics();
        display.getMetrics(metrics);
    }

    private void setupDrawing() {

        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(stroke);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);

    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        if (hasImage) {
            canvasBitmap = BitmapLoader.loadBitmap(getResources(), imageID, metrics.widthPixels, metrics.heightPixels)
                                           .copy(Bitmap.Config.ARGB_8888, true);
        }
        else {
            canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }

        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (drawingEnabled) {
            float touchX = event.getX();
            float touchY = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    drawPath.moveTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    drawPath.lineTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_UP:
                    drawPath.lineTo(touchX, touchY);
                    drawCanvas.drawPath(drawPath, drawPaint);
                    drawPath.reset();
                    break;
                default:
                    return false;
            }

            invalidate();
        }
        return true;

    }

    public void setStroke(float stroke) {
        this.stroke = stroke;
        drawPaint.setStrokeWidth(stroke);
    }

    public void setColor(int color) {
        invalidate();
        paintColor = color;
        drawPaint.setColor(paintColor);
    }

    public void setImage(int imageID) {
        this.imageID = imageID;
        hasImage = true;
        invalidate();
    }

    public void enableDrawing(boolean drawingEnabled) {
        this.drawingEnabled = drawingEnabled;
    }



}