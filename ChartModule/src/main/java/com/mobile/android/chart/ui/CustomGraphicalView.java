package com.mobile.android.chart.ui;

import org.achartengine.model.SeriesSelection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.View;
import org.achartengine.ITouchHandler;
import org.achartengine.chart.AbstractChart;
import org.achartengine.chart.XYChart;
import org.achartengine.model.Point;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.tools.FitZoom;
import org.achartengine.tools.PanListener;
import org.achartengine.tools.Zoom;
import org.achartengine.tools.ZoomListener;

public class CustomGraphicalView extends View {
    private AbstractChart mChart;
    private DefaultRenderer mRenderer;
    private Rect mRect = new Rect();
    private Handler mHandler;
    private RectF mZoomR = new RectF();
    private Bitmap zoomInImage;
    private Bitmap zoomOutImage;
    private Bitmap fitZoomImage;
    private int zoomSize = 50;
    private static final int ZOOM_BUTTONS_COLOR = Color.argb(175, 150, 150, 150);
    private Zoom mZoomIn;
    private Zoom mZoomOut;
    private FitZoom mFitZoom;
    private Paint mPaint = new Paint();
    private ITouchHandler mTouchHandler;
    private float oldX;
    private float oldY;
    private boolean mDrawn;

    public CustomGraphicalView(Context context, AbstractChart chart) {
        super(context);
        this.mChart = chart;
        this.mHandler = new Handler();
        if(this.mChart instanceof XYChart) {
            this.mRenderer = ((XYChart)this.mChart).getRenderer();
        }

        if(this.mRenderer.isZoomButtonsVisible()) {
            this.zoomInImage = BitmapFactory.decodeStream(CustomGraphicalView.class.getResourceAsStream("image/zoom_in.png"));
            this.zoomOutImage = BitmapFactory.decodeStream(CustomGraphicalView.class.getResourceAsStream("image/zoom_out.png"));
            this.fitZoomImage = BitmapFactory.decodeStream(CustomGraphicalView.class.getResourceAsStream("image/zoom-1.png"));
        }

        if(this.mRenderer instanceof XYMultipleSeriesRenderer && ((XYMultipleSeriesRenderer)this.mRenderer).getMarginsColor() == 0) {
            ((XYMultipleSeriesRenderer)this.mRenderer).setMarginsColor(this.mPaint.getColor());
        }

        if(this.mRenderer.isZoomEnabled() && this.mRenderer.isZoomButtonsVisible() || this.mRenderer.isExternalZoomEnabled()) {
            this.mZoomIn = new Zoom(this.mChart, true, this.mRenderer.getZoomRate());
            this.mZoomOut = new Zoom(this.mChart, false, this.mRenderer.getZoomRate());
            this.mFitZoom = new FitZoom(this.mChart);
        }

        int version = 7;

        try {
            version = Integer.valueOf(VERSION.SDK).intValue();
        } catch (Exception var5) {
            ;
        }

        if(version < 7) {
            this.mTouchHandler = new CustomTouchHandlerOld(this, this.mChart);
        } else {
            this.mTouchHandler = new CustomTouchHandler(this, this.mChart);
        }
    }

    public SeriesSelection getCurrentSeriesAndPoint() {
        return this.mChart.getSeriesAndPointForScreenCoordinate(new Point(this.oldX, this.oldY));
    }

    public double[] toRealPoint(int scale) {
        if(this.mChart instanceof XYChart) {
            XYChart chart = (XYChart)this.mChart;
            return chart.toRealPoint(this.oldX, this.oldY, scale);
        } else {
            return null;
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.getClipBounds(this.mRect);
        int top = this.mRect.top;
        int left = this.mRect.left;
        int width = this.mRect.width();
        int height = this.mRect.height();
        if(this.mRenderer.isInScroll()) {
            top = 0;
            left = 0;
            width = this.getMeasuredWidth();
            height = this.getMeasuredHeight();
        }

        this.mChart.draw(canvas, left, top, width, height, this.mPaint);
        /*if(this.mRenderer != null && this.mRenderer.isZoomEnabled() && this.mRenderer.isZoomButtonsVisible()) {
            this.mPaint.setColor(ZOOM_BUTTONS_COLOR);
            this.zoomSize = Math.max(this.zoomSize, Math.min(width, height) / 7);
            this.mZoomR.set((float) (left + width - this.zoomSize * 3), (float) (top + height) - (float) this.zoomSize * 0.775F, (float) (left + width), (float) (top + height));
            //canvas.drawRoundRect(this.mZoomR, (float)(this.zoomSize / 3), (float)(this.zoomSize / 3), this.mPaint);
            //float buttonY = (float)(top + height) - (float)this.zoomSize * 0.625F;
            //canvas.drawBitmap(this.zoomInImage, (float)(left + width) - (float)this.zoomSize * 2.75F, buttonY, (Paint)null);
            //canvas.drawBitmap(this.zoomOutImage, (float)(left + width) - (float)this.zoomSize * 1.75F, buttonY, (Paint)null);
            //canvas.drawBitmap(this.fitZoomImage, (float)(left + width) - (float)this.zoomSize * 0.75F, buttonY, (Paint)null);
        }

        this.mDrawn = true;*/
    }

    public void setZoomRate(float rate) {
        if(this.mZoomIn != null && this.mZoomOut != null) {
            this.mZoomIn.setZoomRate(rate);
            this.mZoomOut.setZoomRate(rate);
        }

    }

    public void zoomIn() {
        if(this.mZoomIn != null) {
            this.mZoomIn.apply(0);
            this.repaint();
        }

    }

    public void zoomOut() {
        if(this.mZoomOut != null) {
            this.mZoomOut.apply(0);
            this.repaint();
        }

    }

    public void zoomReset() {
        if(this.mFitZoom != null) {
            this.mFitZoom.apply();
            this.mZoomIn.notifyZoomResetListeners();
            this.repaint();
        }

    }

    public void addZoomListener(ZoomListener listener, boolean onButtons, boolean onPinch) {
        if(onButtons) {
            if(this.mZoomIn != null) {
                this.mZoomIn.addZoomListener(listener);
                this.mZoomOut.addZoomListener(listener);
            }

            if(onPinch) {
                this.mTouchHandler.addZoomListener(listener);
            }
        }

    }

    public synchronized void removeZoomListener(ZoomListener listener) {
        if(this.mZoomIn != null) {
            this.mZoomIn.removeZoomListener(listener);
            this.mZoomOut.removeZoomListener(listener);
        }

        this.mTouchHandler.removeZoomListener(listener);
    }

    public void addPanListener(PanListener listener) {
        this.mTouchHandler.addPanListener(listener);
    }

    public void removePanListener(PanListener listener) {
        this.mTouchHandler.removePanListener(listener);
    }

    protected RectF getZoomRectangle() {
        return this.mZoomR;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == 0) {
            this.oldX = event.getX();
            this.oldY = event.getY();
        }

        return this.mRenderer != null && this.mDrawn && (this.mRenderer.isPanEnabled() || this.mRenderer.isZoomEnabled()) && this.mTouchHandler.handleTouch(event)?true:super.onTouchEvent(event);
    }

    public void repaint() {
        this.mHandler.post(new Runnable() {
            public void run() {
                CustomGraphicalView.this.invalidate();
            }
        });
    }

    public void repaint(final int left, final int top, final int right, final int bottom) {
        this.mHandler.post(new Runnable() {
            public void run() {
                CustomGraphicalView.this.invalidate(left, top, right, bottom);
            }
        });
    }

    public Bitmap toBitmap() {
        this.setDrawingCacheEnabled(false);
        if(!this.isDrawingCacheEnabled()) {
            this.setDrawingCacheEnabled(true);
        }

        if(this.mRenderer.isApplyBackgroundColor()) {
            this.setDrawingCacheBackgroundColor(this.mRenderer.getBackgroundColor());
        }

        this.setDrawingCacheQuality(1048576);
        return this.getDrawingCache(true);
    }
}
