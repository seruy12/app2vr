package com.google.vrtoolkit.cardboard;

import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import com.google.vrtoolkit.cardboard.CardboardView;

/* JADX INFO: loaded from: classes.dex */
public interface CardboardViewApi {
    boolean getAlignmentMarkerEnabled();

    CardboardDeviceParams getCardboardDeviceParams();

    boolean getChromaticAberrationCorrectionEnabled();

    void getCurrentEyeParams(HeadTransform headTransform, Eye eye, Eye eye2, Eye eye3, Eye eye4, Eye eye5);

    boolean getDistortionCorrectionEnabled();

    boolean getGyroBiasEstimationEnabled();

    HeadMountedDisplay getHeadMountedDisplay();

    float getInterpupillaryDistance();

    float getNeckModelFactor();

    boolean getRestoreGLStateEnabled();

    ScreenParams getScreenParams();

    boolean getSettingsButtonEnabled();

    boolean getVRMode();

    boolean getVignetteEnabled();

    void onDetachedFromWindow();

    void onPause();

    void onResume();

    boolean onTouchEvent(MotionEvent motionEvent);

    void renderUiLayer();

    void resetHeadTracker();

    void setAlignmentMarkerEnabled(boolean z);

    void setChromaticAberrationCorrectionEnabled(boolean z);

    void setDistortionCorrectionEnabled(boolean z);

    void setDistortionCorrectionScale(float f);

    void setGyroBiasEstimationEnabled(boolean z);

    void setNeckModelEnabled(boolean z);

    void setNeckModelFactor(float f);

    GLSurfaceView.Renderer setRenderer(CardboardView.Renderer renderer);

    GLSurfaceView.Renderer setRenderer(CardboardView.StereoRenderer stereoRenderer);

    void setRestoreGLStateEnabled(boolean z);

    void setSettingsButtonEnabled(boolean z);

    void setVRModeEnabled(boolean z);

    void setVignetteEnabled(boolean z);

    boolean uiLayerTouchEvent(MotionEvent motionEvent);

    void undistortFramebuffer();

    void undistortTexture(int i);

    void updateCardboardDeviceParams(CardboardDeviceParams cardboardDeviceParams);

    void updateScreenParams(ScreenParams screenParams);
}
