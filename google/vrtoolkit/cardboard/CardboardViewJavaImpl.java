package com.google.vrtoolkit.cardboard;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.sensors.HeadTracker;
import java.util.concurrent.CountDownLatch;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* JADX INFO: loaded from: classes.dex */
public class CardboardViewJavaImpl implements CardboardViewApi {
    private static final String TAG = CardboardViewJavaImpl.class.getSimpleName();
    private final GLSurfaceView glSurfaceView;
    private HeadTracker headTracker;
    private HeadMountedDisplayManager hmdManager;
    private CountDownLatch shutdownLatch;
    private UiLayer uiLayer;
    private boolean vrMode = true;
    private volatile boolean restoreGLStateEnabled = true;
    private volatile boolean distortionCorrectionEnabled = true;
    private volatile boolean chromaticAberrationCorrectionEnabled = false;
    private volatile boolean vignetteEnabled = true;
    private RendererHelper rendererHelper = new RendererHelper();

    class RendererHelper implements GLSurfaceView.Renderer {
        private boolean distortionCorrectionEnabled;
        private DistortionRenderer distortionRenderer;
        private HeadMountedDisplay hmd;
        private boolean invalidSurfaceSize;
        private final Eye leftEyeNoDistortion;
        private final float[] leftEyeTranslate;
        private boolean projectionChanged;
        private CardboardView.Renderer renderer;
        private final Eye rightEyeNoDistortion;
        private final float[] rightEyeTranslate;
        private boolean surfaceCreated;
        private boolean vrMode;
        private final HeadTransform headTransform = new HeadTransform();
        private final Eye monocular = new Eye(0);
        private final Eye leftEye = new Eye(1);
        private final Eye rightEye = new Eye(2);

        public RendererHelper() {
            this.hmd = new HeadMountedDisplay(CardboardViewJavaImpl.this.getHeadMountedDisplay());
            updateFieldOfView(this.leftEye.getFov(), this.rightEye.getFov());
            this.leftEyeNoDistortion = new Eye(1);
            this.rightEyeNoDistortion = new Eye(2);
            this.distortionRenderer = new DistortionRenderer();
            this.distortionRenderer.setRestoreGLStateEnabled(CardboardViewJavaImpl.this.restoreGLStateEnabled);
            this.distortionRenderer.setChromaticAberrationCorrectionEnabled(CardboardViewJavaImpl.this.chromaticAberrationCorrectionEnabled);
            this.distortionRenderer.setVignetteEnabled(CardboardViewJavaImpl.this.vignetteEnabled);
            this.leftEyeTranslate = new float[16];
            this.rightEyeTranslate = new float[16];
            this.vrMode = CardboardViewJavaImpl.this.vrMode;
            this.distortionCorrectionEnabled = CardboardViewJavaImpl.this.distortionCorrectionEnabled;
            this.projectionChanged = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void getCurrentEyeParamsFromRenderingThread(HeadTransform headTransform, Eye eye, Eye eye2, Eye eye3, Eye eye4, Eye eye5) {
            getFrameParams(headTransform, eye, eye2, eye3);
            System.arraycopy(eye.getEyeView(), 0, this.leftEyeNoDistortion.getEyeView(), 0, 16);
            System.arraycopy(eye2.getEyeView(), 0, this.rightEyeNoDistortion.getEyeView(), 0, 16);
            if (eye.getProjectionChanged()) {
                getFovAndViewportNoDistortionCorrection(eye4, eye5);
            }
        }

        private void getFovAndViewportNoDistortionCorrection(Eye eye, Eye eye2) {
            ScreenParams screenParams = this.hmd.getScreenParams();
            CardboardDeviceParams cardboardDeviceParams = this.hmd.getCardboardDeviceParams();
            Distortion distortion = cardboardDeviceParams.getDistortion();
            float virtualEyeToScreenDistance = getVirtualEyeToScreenDistance();
            float interLensDistance = (cardboardDeviceParams.getInterLensDistance() / 2.0f) / virtualEyeToScreenDistance;
            float widthMeters = screenParams.getWidthMeters() / virtualEyeToScreenDistance;
            float heightMeters = screenParams.getHeightMeters() / virtualEyeToScreenDistance;
            float width = screenParams.getWidth() / widthMeters;
            float height = screenParams.getHeight() / heightMeters;
            float f = (widthMeters / 2.0f) - interLensDistance;
            float yEyeOffsetMeters = cardboardDeviceParams.getYEyeOffsetMeters(screenParams) / virtualEyeToScreenDistance;
            FieldOfView leftEyeMaxFov = cardboardDeviceParams.getLeftEyeMaxFov();
            float fMin = Math.min(f, distortion.distortInverse((float) Math.tan(Math.toRadians(leftEyeMaxFov.getLeft()))));
            float fMin2 = Math.min(interLensDistance, distortion.distortInverse((float) Math.tan(Math.toRadians(leftEyeMaxFov.getRight()))));
            float fMin3 = Math.min(yEyeOffsetMeters, distortion.distortInverse((float) Math.tan(Math.toRadians(leftEyeMaxFov.getBottom()))));
            float fMin4 = Math.min(heightMeters - yEyeOffsetMeters, distortion.distortInverse((float) Math.tan(Math.toRadians(leftEyeMaxFov.getTop()))));
            FieldOfView fov = eye.getFov();
            fov.setLeft((float) Math.toDegrees(Math.atan(fMin)));
            fov.setRight((float) Math.toDegrees(Math.atan(fMin2)));
            fov.setBottom((float) Math.toDegrees(Math.atan(fMin3)));
            fov.setTop((float) Math.toDegrees(Math.atan(fMin4)));
            Viewport viewport = eye.getViewport();
            viewport.x = (int) (((f - fMin) * width) + 0.5f);
            viewport.width = ((int) (((fMin2 + f) * width) + 0.5f)) - viewport.x;
            viewport.y = (int) (((yEyeOffsetMeters - fMin3) * height) + 0.5f);
            viewport.height = ((int) (((fMin4 + yEyeOffsetMeters) * height) + 0.5f)) - viewport.y;
            eye.setProjectionChanged();
            FieldOfView fov2 = eye2.getFov();
            fov2.setLeft(fov.getRight());
            fov2.setRight(fov.getLeft());
            fov2.setBottom(fov.getBottom());
            fov2.setTop(fov.getTop());
            Viewport viewport2 = eye2.getViewport();
            viewport2.width = viewport.width;
            viewport2.height = viewport.height;
            viewport2.x = (screenParams.getWidth() - viewport.x) - viewport2.width;
            viewport2.y = viewport.y;
            eye2.setProjectionChanged();
        }

        private void getFrameParams(HeadTransform headTransform, Eye eye, Eye eye2, Eye eye3) {
            CardboardDeviceParams cardboardDeviceParams = this.hmd.getCardboardDeviceParams();
            ScreenParams screenParams = this.hmd.getScreenParams();
            CardboardViewJavaImpl.this.headTracker.getLastHeadView(headTransform.getHeadView(), 0);
            float interLensDistance = cardboardDeviceParams.getInterLensDistance() * 0.5f;
            if (this.vrMode) {
                Matrix.setIdentityM(this.leftEyeTranslate, 0);
                Matrix.setIdentityM(this.rightEyeTranslate, 0);
                Matrix.translateM(this.leftEyeTranslate, 0, interLensDistance, 0.0f, 0.0f);
                Matrix.translateM(this.rightEyeTranslate, 0, -interLensDistance, 0.0f, 0.0f);
                Matrix.multiplyMM(eye.getEyeView(), 0, this.leftEyeTranslate, 0, headTransform.getHeadView(), 0);
                Matrix.multiplyMM(eye2.getEyeView(), 0, this.rightEyeTranslate, 0, headTransform.getHeadView(), 0);
            } else {
                System.arraycopy(headTransform.getHeadView(), 0, eye3.getEyeView(), 0, headTransform.getHeadView().length);
            }
            if (this.projectionChanged) {
                eye3.getViewport().setViewport(0, 0, this.vrMode ? screenParams.getWidth() : CardboardViewJavaImpl.this.glSurfaceView.getWidth(), this.vrMode ? screenParams.getHeight() : CardboardViewJavaImpl.this.glSurfaceView.getHeight());
                CardboardViewJavaImpl.this.uiLayer.updateViewport(eye3.getViewport());
                if (this.vrMode) {
                    updateFieldOfView(eye.getFov(), eye2.getFov());
                    if (this.distortionCorrectionEnabled) {
                        this.distortionRenderer.onFovChanged(this.hmd, eye.getFov(), eye2.getFov(), getVirtualEyeToScreenDistance());
                    }
                } else {
                    updateMonocularFieldOfView(eye3.getFov());
                }
                eye.setProjectionChanged();
                eye2.setProjectionChanged();
                eye3.setProjectionChanged();
                this.projectionChanged = false;
            }
            if (this.distortionCorrectionEnabled && this.distortionRenderer.haveViewportsChanged()) {
                this.distortionRenderer.updateViewports(eye.getViewport(), eye2.getViewport());
            }
        }

        private float getVirtualEyeToScreenDistance() {
            return this.hmd.getCardboardDeviceParams().getScreenToLensDistance();
        }

        private void updateFieldOfView(FieldOfView fieldOfView, FieldOfView fieldOfView2) {
            CardboardDeviceParams cardboardDeviceParams = this.hmd.getCardboardDeviceParams();
            ScreenParams screenParams = this.hmd.getScreenParams();
            Distortion distortion = cardboardDeviceParams.getDistortion();
            float virtualEyeToScreenDistance = getVirtualEyeToScreenDistance();
            float widthMeters = (screenParams.getWidthMeters() - cardboardDeviceParams.getInterLensDistance()) / 2.0f;
            float interLensDistance = cardboardDeviceParams.getInterLensDistance() / 2.0f;
            float heightMeters = screenParams.getHeightMeters() - cardboardDeviceParams.getYEyeOffsetMeters(screenParams);
            float degrees = (float) Math.toDegrees(Math.atan(distortion.distort(widthMeters / virtualEyeToScreenDistance)));
            float degrees2 = (float) Math.toDegrees(Math.atan(distortion.distort(interLensDistance / virtualEyeToScreenDistance)));
            float degrees3 = (float) Math.toDegrees(Math.atan(distortion.distort(r6 / virtualEyeToScreenDistance)));
            float degrees4 = (float) Math.toDegrees(Math.atan(distortion.distort(heightMeters / virtualEyeToScreenDistance)));
            fieldOfView.setLeft(Math.min(degrees, cardboardDeviceParams.getLeftEyeMaxFov().getLeft()));
            fieldOfView.setRight(Math.min(degrees2, cardboardDeviceParams.getLeftEyeMaxFov().getRight()));
            fieldOfView.setBottom(Math.min(degrees3, cardboardDeviceParams.getLeftEyeMaxFov().getBottom()));
            fieldOfView.setTop(Math.min(degrees4, cardboardDeviceParams.getLeftEyeMaxFov().getTop()));
            fieldOfView2.setLeft(fieldOfView.getRight());
            fieldOfView2.setRight(fieldOfView.getLeft());
            fieldOfView2.setBottom(fieldOfView.getBottom());
            fieldOfView2.setTop(fieldOfView.getTop());
        }

        private void updateMonocularFieldOfView(FieldOfView fieldOfView) {
            float degrees = (float) Math.toDegrees(Math.atan((Math.tan(Math.toRadians(22.5d)) * ((double) CardboardViewJavaImpl.this.glSurfaceView.getWidth())) / ((double) CardboardViewJavaImpl.this.glSurfaceView.getHeight())));
            fieldOfView.setLeft(degrees);
            fieldOfView.setRight(degrees);
            fieldOfView.setBottom(22.5f);
            fieldOfView.setTop(22.5f);
        }

        public void getCurrentEyeParams(final HeadTransform headTransform, final Eye eye, final Eye eye2, final Eye eye3, final Eye eye4, final Eye eye5) {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            CardboardViewJavaImpl.this.queueEvent(new Runnable() { // from class: com.google.vrtoolkit.cardboard.CardboardViewJavaImpl.RendererHelper.13
                @Override // java.lang.Runnable
                public void run() {
                    RendererHelper.this.getCurrentEyeParamsFromRenderingThread(headTransform, eye, eye2, eye3, eye4, eye5);
                    countDownLatch.countDown();
                }
            });
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                Log.e(CardboardViewJavaImpl.TAG, "Interrupted while reading frame params: " + e.toString());
            }
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public void onDrawFrame(GL10 gl10) {
            if (this.renderer == null || !this.surfaceCreated || this.invalidSurfaceSize) {
                return;
            }
            getCurrentEyeParamsFromRenderingThread(this.headTransform, this.leftEye, this.rightEye, this.monocular, this.leftEyeNoDistortion, this.rightEyeNoDistortion);
            if (!this.vrMode) {
                this.renderer.onDrawFrame(this.headTransform, this.monocular, null);
            } else if (this.distortionCorrectionEnabled) {
                this.distortionRenderer.beforeDrawFrame();
                this.renderer.onDrawFrame(this.headTransform, this.leftEye, this.rightEye);
                this.distortionRenderer.afterDrawFrame();
            } else {
                this.renderer.onDrawFrame(this.headTransform, this.leftEyeNoDistortion, this.rightEyeNoDistortion);
            }
            this.renderer.onFinishFrame(this.monocular.getViewport());
            if (this.vrMode) {
                CardboardViewJavaImpl.this.uiLayer.draw();
            }
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public void onSurfaceChanged(GL10 gl10, int i, int i2) {
            if (this.renderer == null || !this.surfaceCreated) {
                return;
            }
            ScreenParams screenParams = this.hmd.getScreenParams();
            if (!this.vrMode || (i == screenParams.getWidth() && i2 == screenParams.getHeight())) {
                this.invalidSurfaceSize = false;
            } else {
                if (!this.invalidSurfaceSize) {
                    GLES20.glClear(16384);
                    Log.w(CardboardViewJavaImpl.TAG, "Surface size " + i + "x" + i2 + " does not match the expected screen size " + screenParams.getWidth() + "x" + screenParams.getHeight() + ". Rendering is disabled.");
                }
                this.invalidSurfaceSize = true;
            }
            this.projectionChanged = true;
            this.renderer.onSurfaceChanged(i, i2);
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
            if (this.renderer == null) {
                return;
            }
            this.surfaceCreated = true;
            this.renderer.onSurfaceCreated(eGLConfig);
            CardboardViewJavaImpl.this.uiLayer.initializeGl();
        }

        public void renderUiLayer() {
            CardboardViewJavaImpl.this.queueEvent(new Runnable() { // from class: com.google.vrtoolkit.cardboard.CardboardViewJavaImpl.RendererHelper.12
                @Override // java.lang.Runnable
                public void run() {
                    CardboardViewJavaImpl.this.uiLayer.draw();
                }
            });
        }

        public void setCardboardDeviceParams(CardboardDeviceParams cardboardDeviceParams) {
            final CardboardDeviceParams cardboardDeviceParams2 = new CardboardDeviceParams(cardboardDeviceParams);
            CardboardViewJavaImpl.this.queueEvent(new Runnable() { // from class: com.google.vrtoolkit.cardboard.CardboardViewJavaImpl.RendererHelper.2
                @Override // java.lang.Runnable
                public void run() {
                    RendererHelper.this.hmd.setCardboardDeviceParams(cardboardDeviceParams2);
                    RendererHelper.this.projectionChanged = true;
                }
            });
        }

        public void setChromaticAberrationCorrectionEnabled(final boolean z) {
            CardboardViewJavaImpl.this.queueEvent(new Runnable() { // from class: com.google.vrtoolkit.cardboard.CardboardViewJavaImpl.RendererHelper.8
                @Override // java.lang.Runnable
                public void run() {
                    RendererHelper.this.distortionRenderer.setChromaticAberrationCorrectionEnabled(z);
                }
            });
        }

        public void setDistortionCorrectionEnabled(final boolean z) {
            CardboardViewJavaImpl.this.queueEvent(new Runnable() { // from class: com.google.vrtoolkit.cardboard.CardboardViewJavaImpl.RendererHelper.4
                @Override // java.lang.Runnable
                public void run() {
                    RendererHelper.this.distortionCorrectionEnabled = z;
                    RendererHelper.this.projectionChanged = true;
                }
            });
        }

        public void setDistortionCorrectionScale(final float f) {
            CardboardViewJavaImpl.this.queueEvent(new Runnable() { // from class: com.google.vrtoolkit.cardboard.CardboardViewJavaImpl.RendererHelper.5
                @Override // java.lang.Runnable
                public void run() {
                    RendererHelper.this.distortionRenderer.setResolutionScale(f);
                }
            });
        }

        public void setRenderer(CardboardView.Renderer renderer) {
            this.renderer = renderer;
        }

        public void setRestoreGLStateEnabled(final boolean z) {
            CardboardViewJavaImpl.this.queueEvent(new Runnable() { // from class: com.google.vrtoolkit.cardboard.CardboardViewJavaImpl.RendererHelper.7
                @Override // java.lang.Runnable
                public void run() {
                    RendererHelper.this.distortionRenderer.setRestoreGLStateEnabled(z);
                }
            });
        }

        public void setScreenParams(ScreenParams screenParams) {
            final ScreenParams screenParams2 = new ScreenParams(screenParams);
            CardboardViewJavaImpl.this.queueEvent(new Runnable() { // from class: com.google.vrtoolkit.cardboard.CardboardViewJavaImpl.RendererHelper.3
                @Override // java.lang.Runnable
                public void run() {
                    RendererHelper.this.hmd.setScreenParams(screenParams2);
                    RendererHelper.this.projectionChanged = true;
                }
            });
        }

        public void setVRModeEnabled(final boolean z) {
            CardboardViewJavaImpl.this.queueEvent(new Runnable() { // from class: com.google.vrtoolkit.cardboard.CardboardViewJavaImpl.RendererHelper.6
                @Override // java.lang.Runnable
                public void run() {
                    if (RendererHelper.this.vrMode == z) {
                        return;
                    }
                    RendererHelper.this.vrMode = z;
                    if (RendererHelper.this.renderer instanceof StereoRendererHelper) {
                        ((StereoRendererHelper) RendererHelper.this.renderer).setVRModeEnabled(z);
                    }
                    RendererHelper.this.projectionChanged = true;
                    RendererHelper.this.onSurfaceChanged(null, RendererHelper.this.hmd.getScreenParams().getWidth(), RendererHelper.this.hmd.getScreenParams().getHeight());
                }
            });
        }

        public void setVignetteEnabled(final boolean z) {
            CardboardViewJavaImpl.this.queueEvent(new Runnable() { // from class: com.google.vrtoolkit.cardboard.CardboardViewJavaImpl.RendererHelper.9
                @Override // java.lang.Runnable
                public void run() {
                    RendererHelper.this.distortionRenderer.setVignetteEnabled(z);
                    RendererHelper.this.projectionChanged = true;
                }
            });
        }

        public void shutdown() {
            CardboardViewJavaImpl.this.queueEvent(new Runnable() { // from class: com.google.vrtoolkit.cardboard.CardboardViewJavaImpl.RendererHelper.1
                @Override // java.lang.Runnable
                public void run() {
                    if (RendererHelper.this.renderer != null && RendererHelper.this.surfaceCreated) {
                        RendererHelper.this.surfaceCreated = false;
                        RendererHelper.this.renderer.onRendererShutdown();
                    }
                    CardboardViewJavaImpl.this.shutdownLatch.countDown();
                }
            });
        }

        public void undistortFramebuffer() {
            CardboardViewJavaImpl.this.queueEvent(new Runnable() { // from class: com.google.vrtoolkit.cardboard.CardboardViewJavaImpl.RendererHelper.11
                @Override // java.lang.Runnable
                public void run() {
                    RendererHelper.this.distortionRenderer.undistortFramebuffer();
                }
            });
        }

        public void undistortTexture(final int i) {
            CardboardViewJavaImpl.this.queueEvent(new Runnable() { // from class: com.google.vrtoolkit.cardboard.CardboardViewJavaImpl.RendererHelper.10
                @Override // java.lang.Runnable
                public void run() {
                    RendererHelper.this.distortionRenderer.undistortTexture(i);
                }
            });
        }
    }

    class StereoRendererHelper implements CardboardView.Renderer {
        private final CardboardView.StereoRenderer stereoRenderer;
        private boolean vrMode;

        public StereoRendererHelper(CardboardView.StereoRenderer stereoRenderer) {
            this.stereoRenderer = stereoRenderer;
            this.vrMode = CardboardViewJavaImpl.this.vrMode;
        }

        @Override // com.google.vrtoolkit.cardboard.CardboardView.Renderer
        public void onDrawFrame(HeadTransform headTransform, Eye eye, Eye eye2) {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glClear(16640);
            this.stereoRenderer.onNewFrame(headTransform);
            GLES20.glEnable(3089);
            eye.getViewport().setGLViewport();
            eye.getViewport().setGLScissor();
            this.stereoRenderer.onDrawEye(eye);
            if (eye2 == null) {
                return;
            }
            eye2.getViewport().setGLViewport();
            eye2.getViewport().setGLScissor();
            this.stereoRenderer.onDrawEye(eye2);
        }

        @Override // com.google.vrtoolkit.cardboard.CardboardView.Renderer
        public void onFinishFrame(Viewport viewport) {
            viewport.setGLViewport();
            viewport.setGLScissor();
            this.stereoRenderer.onFinishFrame(viewport);
        }

        @Override // com.google.vrtoolkit.cardboard.CardboardView.Renderer
        public void onRendererShutdown() {
            this.stereoRenderer.onRendererShutdown();
        }

        @Override // com.google.vrtoolkit.cardboard.CardboardView.Renderer
        public void onSurfaceChanged(int i, int i2) {
            if (this.vrMode) {
                this.stereoRenderer.onSurfaceChanged(i / 2, i2);
            } else {
                this.stereoRenderer.onSurfaceChanged(i, i2);
            }
        }

        @Override // com.google.vrtoolkit.cardboard.CardboardView.Renderer
        public void onSurfaceCreated(EGLConfig eGLConfig) {
            this.stereoRenderer.onSurfaceCreated(eGLConfig);
        }

        public void setVRModeEnabled(final boolean z) {
            CardboardViewJavaImpl.this.queueEvent(new Runnable() { // from class: com.google.vrtoolkit.cardboard.CardboardViewJavaImpl.StereoRendererHelper.1
                @Override // java.lang.Runnable
                public void run() {
                    StereoRendererHelper.this.vrMode = z;
                }
            });
        }
    }

    public CardboardViewJavaImpl(Context context, GLSurfaceView gLSurfaceView) {
        this.glSurfaceView = gLSurfaceView;
        this.headTracker = HeadTracker.createFromContext(context);
        this.hmdManager = new HeadMountedDisplayManager(context);
        this.uiLayer = new UiLayer(context);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void queueEvent(Runnable runnable) {
        this.glSurfaceView.queueEvent(runnable);
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public boolean getAlignmentMarkerEnabled() {
        return this.uiLayer.getAlignmentMarkerEnabled();
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public CardboardDeviceParams getCardboardDeviceParams() {
        return this.hmdManager.getHeadMountedDisplay().getCardboardDeviceParams();
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public boolean getChromaticAberrationCorrectionEnabled() {
        return this.chromaticAberrationCorrectionEnabled;
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public void getCurrentEyeParams(HeadTransform headTransform, Eye eye, Eye eye2, Eye eye3, Eye eye4, Eye eye5) {
        this.rendererHelper.getCurrentEyeParams(headTransform, eye, eye2, eye3, eye4, eye5);
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public boolean getDistortionCorrectionEnabled() {
        return this.distortionCorrectionEnabled;
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public boolean getGyroBiasEstimationEnabled() {
        return this.headTracker.getGyroBiasEstimationEnabled();
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public HeadMountedDisplay getHeadMountedDisplay() {
        return this.hmdManager.getHeadMountedDisplay();
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public float getInterpupillaryDistance() {
        return getCardboardDeviceParams().getInterLensDistance();
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public float getNeckModelFactor() {
        return this.headTracker.getNeckModelFactor();
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public boolean getRestoreGLStateEnabled() {
        return this.restoreGLStateEnabled;
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public ScreenParams getScreenParams() {
        return this.hmdManager.getHeadMountedDisplay().getScreenParams();
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public boolean getSettingsButtonEnabled() {
        return this.uiLayer.getSettingsButtonEnabled();
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public boolean getVRMode() {
        return this.vrMode;
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public boolean getVignetteEnabled() {
        return this.vignetteEnabled;
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public void onDetachedFromWindow() {
        if (this.shutdownLatch == null) {
            this.shutdownLatch = new CountDownLatch(1);
            this.rendererHelper.shutdown();
            try {
                this.shutdownLatch.await();
            } catch (InterruptedException e) {
                Log.e(TAG, "Interrupted during shutdown: " + e.toString());
            }
            this.shutdownLatch = null;
        }
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public void onPause() {
        this.hmdManager.onPause();
        this.headTracker.stopTracking();
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public void onResume() throws Throwable {
        this.hmdManager.onResume();
        this.rendererHelper.setCardboardDeviceParams(getCardboardDeviceParams());
        this.headTracker.startTracking();
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.uiLayer.onTouchEvent(motionEvent);
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public void renderUiLayer() {
        this.rendererHelper.renderUiLayer();
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public void resetHeadTracker() {
        this.headTracker.resetTracker();
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public void setAlignmentMarkerEnabled(boolean z) {
        this.uiLayer.setAlignmentMarkerEnabled(z);
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public void setChromaticAberrationCorrectionEnabled(boolean z) {
        this.chromaticAberrationCorrectionEnabled = z;
        this.rendererHelper.setChromaticAberrationCorrectionEnabled(z);
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public void setDistortionCorrectionEnabled(boolean z) {
        this.distortionCorrectionEnabled = z;
        this.rendererHelper.setDistortionCorrectionEnabled(z);
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public void setDistortionCorrectionScale(float f) {
        this.rendererHelper.setDistortionCorrectionScale(f);
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public void setGyroBiasEstimationEnabled(boolean z) {
        this.headTracker.setGyroBiasEstimationEnabled(z);
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public void setNeckModelEnabled(boolean z) {
        this.headTracker.setNeckModelEnabled(z);
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public void setNeckModelFactor(float f) {
        this.headTracker.setNeckModelFactor(f);
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public GLSurfaceView.Renderer setRenderer(CardboardView.Renderer renderer) {
        if (renderer == null) {
            return null;
        }
        this.rendererHelper.setRenderer(renderer);
        return this.rendererHelper;
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public GLSurfaceView.Renderer setRenderer(CardboardView.StereoRenderer stereoRenderer) {
        return setRenderer(stereoRenderer != null ? new StereoRendererHelper(stereoRenderer) : null);
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public void setRestoreGLStateEnabled(boolean z) {
        this.restoreGLStateEnabled = z;
        this.rendererHelper.setRestoreGLStateEnabled(z);
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public void setSettingsButtonEnabled(boolean z) {
        this.uiLayer.setSettingsButtonEnabled(z);
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public void setVRModeEnabled(boolean z) {
        this.vrMode = z;
        this.rendererHelper.setVRModeEnabled(z);
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public void setVignetteEnabled(boolean z) {
        this.vignetteEnabled = z;
        this.rendererHelper.setVignetteEnabled(z);
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public boolean uiLayerTouchEvent(MotionEvent motionEvent) {
        return this.uiLayer.onTouchEvent(motionEvent);
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public void undistortFramebuffer() {
        this.rendererHelper.undistortFramebuffer();
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public void undistortTexture(int i) {
        this.rendererHelper.undistortTexture(i);
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public void updateCardboardDeviceParams(CardboardDeviceParams cardboardDeviceParams) {
        if (this.hmdManager.updateCardboardDeviceParams(cardboardDeviceParams)) {
            this.rendererHelper.setCardboardDeviceParams(getCardboardDeviceParams());
        }
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardViewApi
    public void updateScreenParams(ScreenParams screenParams) {
        if (this.hmdManager.updateScreenParams(screenParams)) {
            this.rendererHelper.setScreenParams(getScreenParams());
        }
    }
}
