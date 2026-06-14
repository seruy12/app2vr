package com.nitro888.nitroaction360.nitroaction;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;
import com.nibiru.lib.controller.R;
import com.nitro888.nitroaction360.MainActivity;
import javax.microedition.khronos.egl.EGLConfig;

/* JADX INFO: loaded from: classes.dex */
public class c implements CardboardView.StereoRenderer {
    private static final String b = c.class.getSimpleName();
    private Context a;
    private d n;
    private float c = 1.0f;
    private float d = 0.06f;
    private float[] e = new float[16];
    private float[] f = new float[16];
    private float[] g = new float[16];
    private float[] h = new float[16];
    private int i = R.raw.plane_sq;
    private int j = 0;
    private float k = 0.0f;
    private float l = 1.0f;
    private int m = 1;
    private e o = null;
    private int p = -1;

    public c(Context context) {
        this.a = context;
        this.n = new d(this, context);
    }

    public final void a(float f) {
        this.k += 5.0f * f;
        if (f == 0.0f) {
            this.k = 0.0f;
        }
        if (this.k > 90.0f) {
            this.k = 90.0f;
        }
        if (this.k < -90.0f) {
            this.k = -90.0f;
        }
    }

    public final void a(int i) {
        this.i = i;
    }

    public final void a(e eVar) {
        this.o = eVar;
    }

    public final void b(float f) {
        if (R.raw.dome == this.i) {
            return;
        }
        if (f == 0.0f) {
            this.l = 1.0f;
        } else {
            this.l += 0.1f * f;
        }
    }

    public final void b(int i) {
        this.j = i;
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardView.StereoRenderer
    public void onDrawEye(Eye eye) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(16640);
        if (this.o != null) {
            this.o.b();
        }
        if (eye.getType() == 1) {
            Matrix.multiplyMM(this.g, 0, eye.getEyeView(), 0, this.f, 0);
            d dVar = this.n;
            eye.getPerspective(1.0f, 500.0f);
            dVar.a(this.g, eye.getType());
            return;
        }
        Matrix.multiplyMM(this.g, 0, eye.getEyeView(), 0, this.e, 0);
        d dVar2 = this.n;
        eye.getPerspective(1.0f, 500.0f);
        dVar2.a(this.g, eye.getType());
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardView.StereoRenderer
    public void onFinishFrame(Viewport viewport) {
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardView.StereoRenderer
    public void onNewFrame(HeadTransform headTransform) {
        float f = this.c * this.d;
        Matrix.setLookAtM(this.f, 0, -f, 0.0f, 0.01f, -f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        Matrix.setLookAtM(this.e, 0, f, 0.0f, 0.01f, f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        headTransform.getHeadView(this.h, 0);
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardView.StereoRenderer
    public void onRendererShutdown() {
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardView.StereoRenderer
    public void onSurfaceChanged(int i, int i2) {
        if (this.o != null) {
            this.o.a();
        }
        this.n.a(i, i2);
        ((MainActivity) this.a).i();
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardView.StereoRenderer
    public void onSurfaceCreated(EGLConfig eGLConfig) {
        this.n.a();
    }
}
