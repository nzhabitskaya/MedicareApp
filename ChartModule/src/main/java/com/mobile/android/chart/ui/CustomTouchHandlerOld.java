package com.mobile.android.chart.ui;

import android.graphics.RectF;
import android.view.MotionEvent;

import org.achartengine.ITouchHandler;
import org.achartengine.chart.AbstractChart;
import org.achartengine.chart.XYChart;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.tools.Pan;
import org.achartengine.tools.PanListener;
import org.achartengine.tools.ZoomListener;

public class CustomTouchHandlerOld implements ITouchHandler {
    private DefaultRenderer mRenderer;
    private float oldX;
    private float oldY;
    private RectF zoomR = new RectF();
    private Pan mPan;
    private CustomGraphicalView graphicalView;

    public CustomTouchHandlerOld(CustomGraphicalView view, AbstractChart chart) {
        this.graphicalView = view;
        this.zoomR = this.graphicalView.getZoomRectangle();
        if(chart instanceof XYChart) {
            this.mRenderer = ((XYChart)chart).getRenderer();
        }

        if(this.mRenderer.isPanEnabled()) {
            this.mPan = new Pan(chart);
        }

    }

    public boolean handleTouch(MotionEvent event) {
        int action = event.getAction();
        if(this.mRenderer != null && action == 2) {
            if(this.oldX >= 0.0F || this.oldY >= 0.0F) {
                float newX = event.getX();
                float newY = event.getY();
                if(this.mRenderer.isPanEnabled()) {
                    this.mPan.apply(this.oldX, this.oldY, newX, newY);
                }

                this.oldX = newX;
                this.oldY = newY;
                this.graphicalView.repaint();
                return true;
            }
        } else if(action == 0) {
            this.oldX = event.getX();
            this.oldY = event.getY();
            if(this.mRenderer != null && this.mRenderer.isZoomEnabled() && this.zoomR.contains(this.oldX, this.oldY)) {
                if(this.oldX < this.zoomR.left + this.zoomR.width() / 3.0F) {
                    this.graphicalView.zoomIn();
                } else if(this.oldX < this.zoomR.left + this.zoomR.width() * 2.0F / 3.0F) {
                    this.graphicalView.zoomOut();
                } else {
                    this.graphicalView.zoomReset();
                }

                return true;
            }
        } else if(action == 1) {
            this.oldX = 0.0F;
            this.oldY = 0.0F;
        }

        return !this.mRenderer.isClickEnabled();
    }

    public void addZoomListener(ZoomListener listener) {
    }

    public void removeZoomListener(ZoomListener listener) {
    }

    public void addPanListener(PanListener listener) {
        if(this.mPan != null) {
            this.mPan.addPanListener(listener);
        }

    }

    public void removePanListener(PanListener listener) {
        if(this.mPan != null) {
            this.mPan.removePanListener(listener);
        }

    }
}

