package com.google.vrtoolkit.cardboard;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;

/* JADX INFO: loaded from: classes.dex */
public class CardboardView extends GLSurfaceView {
    private CardboardViewApi cardboardViewApi;
    private boolean rendererIsSet;

    public interface Renderer {
        void onDrawFrame(HeadTransform headTransform, Eye eye, Eye eye2);

        void onFinishFrame(Viewport viewport);

        void onRendererShutdown();

        void onSurfaceChanged(int i, int i2);

        void onSurfaceCreated(EGLConfig eGLConfig);
    }

    public interface StereoRenderer {
        void onDrawEye(Eye eye);

        void onFinishFrame(Viewport viewport);

        void onNewFrame(HeadTransform headTransform);

        void onRendererShutdown();

        void onSurfaceChanged(int i, int i2);

        void onSurfaceCreated(EGLConfig eGLConfig);
    }

    public CardboardView(Context context) {
        super(context);
        this.rendererIsSet = false;
        init(context);
    }

    public CardboardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.rendererIsSet = false;
        init(context);
    }

    private void init(Context context) {
        this.cardboardViewApi = ImplementationSelector.createCardboardViewApi(context, this);
        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);
    }

    public boolean getAlignmentMarkerEnabled() {
        return this.cardboardViewApi.getAlignmentMarkerEnabled();
    }

    public CardboardDeviceParams getCardboardDeviceParams() {
        return this.cardboardViewApi.getCardboardDeviceParams();
    }

    public boolean getChromaticAberrationCorrectionEnabled() {
        return this.cardboardViewApi.getChromaticAberrationCorrectionEnabled();
    }

    public void getCurrentEyeParams(HeadTransform headTransform, Eye eye, Eye eye2, Eye eye3, Eye eye4, Eye eye5) {
        this.cardboardViewApi.getCurrentEyeParams(headTransform, eye, eye2, eye3, eye4, eye5);
    }

    public boolean getDistortionCorrectionEnabled() {
        return this.cardboardViewApi.getDistortionCorrectionEnabled();
    }

    public boolean getGyroBiasEstimationEnabled() {
        return this.cardboardViewApi.getGyroBiasEstimationEnabled();
    }

    public HeadMountedDisplay getHeadMountedDisplay() {
        return this.cardboardViewApi.getHeadMountedDisplay();
    }

    public float getInterpupillaryDistance() {
        return this.cardboardViewApi.getInterpupillaryDistance();
    }

    public float getNeckModelFactor() {
        return this.cardboardViewApi.getNeckModelFactor();
    }

    public boolean getRestoreGLStateEnabled() {
        return this.cardboardViewApi.getRestoreGLStateEnabled();
    }

    public ScreenParams getScreenParams() {
        return this.cardboardViewApi.getScreenParams();
    }

    public boolean getSettingsButtonEnabled() {
        return this.cardboardViewApi.getSettingsButtonEnabled();
    }

    public boolean getVRMode() {
        return this.cardboardViewApi.getVRMode();
    }

    public boolean getVignetteEnabled() {
        return this.cardboardViewApi.getVignetteEnabled();
    }

    @Override // android.opengl.GLSurfaceView, android.view.SurfaceView, android.view.View
    public void onDetachedFromWindow() {
        if (this.rendererIsSet) {
            this.cardboardViewApi.onDetachedFromWindow();
        }
        super.onDetachedFromWindow();
    }

    @Override // android.opengl.GLSurfaceView
    public void onPause() {
        this.cardboardViewApi.onPause();
        if (this.rendererIsSet) {
            super.onPause();
        }
    }

    @Override // android.opengl.GLSurfaceView
    public void onResume() {
        if (this.rendererIsSet) {
            super.onResume();
        }
        this.cardboardViewApi.onResume();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.cardboardViewApi.onTouchEvent(motionEvent)) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.opengl.GLSurfaceView
    public void queueEvent(Runnable runnable) {
        if (this.rendererIsSet) {
            super.queueEvent(runnable);
        } else {
            runnable.run();
        }
    }

    public void renderUiLayer() {
        this.cardboardViewApi.renderUiLayer();
    }

    public void resetHeadTracker() {
        this.cardboardViewApi.resetHeadTracker();
    }

    public void setAlignmentMarkerEnabled(boolean z) {
        this.cardboardViewApi.setAlignmentMarkerEnabled(z);
    }

    public void setChromaticAberrationCorrectionEnabled(boolean z) {
        this.cardboardViewApi.setChromaticAberrationCorrectionEnabled(z);
    }

    public void setDistortionCorrectionEnabled(boolean z) {
        this.cardboardViewApi.setDistortionCorrectionEnabled(z);
    }

    public void setDistortionCorrectionScale(float f) {
        this.cardboardViewApi.setDistortionCorrectionScale(f);
    }

    public void setGyroBiasEstimationEnabled(boolean z) {
        this.cardboardViewApi.setGyroBiasEstimationEnabled(z);
    }

    public void setNeckModelEnabled(boolean z) {
        this.cardboardViewApi.setNeckModelEnabled(z);
    }

    public void setNeckModelFactor(float f) {
        this.cardboardViewApi.setNeckModelFactor(f);
    }

    @Override // android.opengl.GLSurfaceView
    public void setRenderer(GLSurfaceView.Renderer renderer) {
        throw new RuntimeException("Please use the CardboardView renderer interfaces");
    }

    public void setRenderer(Renderer renderer) {
        GLSurfaceView.Renderer renderer2 = this.cardboardViewApi.setRenderer(renderer);
        if (renderer2 == null) {
            return;
        }
        super.setRenderer(renderer2);
        this.rendererIsSet = true;
    }

    public void setRenderer(StereoRenderer stereoRenderer) {
        GLSurfaceView.Renderer renderer = this.cardboardViewApi.setRenderer(stereoRenderer);
        if (renderer == null) {
            return;
        }
        super.setRenderer(renderer);
        this.rendererIsSet = true;
    }

    public void setRestoreGLStateEnabled(boolean z) {
        this.cardboardViewApi.setRestoreGLStateEnabled(z);
    }

    public void setSettingsButtonEnabled(boolean z) {
        this.cardboardViewApi.setSettingsButtonEnabled(z);
    }

    public void setVRModeEnabled(boolean z) {
        this.cardboardViewApi.setVRModeEnabled(z);
    }

    public void setVignetteEnabled(boolean z) {
        this.cardboardViewApi.setVignetteEnabled(z);
    }

    public boolean uiLayerTouchEvent(MotionEvent motionEvent) {
        return this.cardboardViewApi.uiLayerTouchEvent(motionEvent);
    }

    public void undistortFramebuffer() {
        this.cardboardViewApi.undistortFramebuffer();
    }

    public void undistortTexture(int i) {
        this.cardboardViewApi.undistortTexture(i);
    }

    public void updateCardboardDeviceParams(CardboardDeviceParams cardboardDeviceParams) {
        this.cardboardViewApi.updateCardboardDeviceParams(cardboardDeviceParams);
    }

    public void updateScreenParams(ScreenParams screenParams) {
        this.cardboardViewApi.updateScreenParams(screenParams);
    }
}
