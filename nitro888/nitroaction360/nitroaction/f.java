package com.nitro888.nitroaction360.nitroaction;

import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.util.Log;
import android.view.Surface;

/* JADX INFO: loaded from: classes.dex */
final class f {
    final /* synthetic */ e a;
    private boolean b;
    private SurfaceTexture c;
    private Surface d;
    private int e;
    private Canvas f;
    private int g;
    private int h;

    private f(e eVar) {
        this.a = eVar;
        this.b = false;
        this.e = 0;
        this.g = 1024;
        this.h = 1024;
    }

    /* synthetic */ f(e eVar, byte b) {
        this(eVar);
    }

    public final void a() {
        this.b = true;
    }

    public final void a(int i) {
        this.g = i;
    }

    public final void b() {
        if (this.e == 0) {
            return;
        }
        synchronized (this) {
            if (this.b) {
                this.c.updateTexImage();
            }
        }
    }

    public final void b(int i) {
        this.h = i;
    }

    public final void c() {
        if (this.b) {
            if (this.d != null) {
                this.d.release();
            }
            if (this.c != null) {
                this.c.release();
            }
            this.d = null;
            this.c = null;
            this.e = 0;
            int[] iArr = new int[1];
            GLES20.glActiveTexture(33984);
            GLES20.glGenTextures(1, iArr, 0);
            e.a("Texture generate");
            GLES20.glBindTexture(36197, iArr[0]);
            e.a("Texture bind");
            GLES20.glTexParameterf(36197, 10241, 9729.0f);
            GLES20.glTexParameterf(36197, 10240, 9729.0f);
            GLES20.glTexParameteri(36197, 10242, 33071);
            GLES20.glTexParameteri(36197, 10243, 33071);
            this.e = iArr[0];
            if (this.e > 0) {
                this.c = new SurfaceTexture(this.e);
                this.c.setDefaultBufferSize(this.g, this.h);
                this.d = new Surface(this.c);
            }
        }
    }

    public final int d() {
        return this.e;
    }

    public final Surface e() {
        return this.d;
    }

    public final Canvas f() {
        this.f = null;
        if (this.b && this.d != null) {
            try {
                this.f = this.d.lockCanvas(null);
                this.f.drawColor(0, PorterDuff.Mode.CLEAR);
            } catch (Exception e) {
                Log.e(e.b, "error while rendering view to gl: " + e);
            }
        }
        return this.f;
    }

    public final void g() {
        if (this.b) {
            if (this.f != null) {
                this.d.unlockCanvasAndPost(this.f);
            }
            this.f = null;
        }
    }

    public final int h() {
        return this.g;
    }

    public final int i() {
        return this.h;
    }
}
