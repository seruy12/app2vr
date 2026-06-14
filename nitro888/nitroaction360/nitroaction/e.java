package com.nitro888.nitroaction360.nitroaction;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.Surface;
import com.nibiru.lib.controller.R;

/* JADX INFO: loaded from: classes.dex */
public class e {
    private static final String b = e.class.getSimpleName();
    private static f[] c = new f[3];
    private Context a;

    public e(Context context) {
        byte b2 = 0;
        this.a = context;
        for (int i = 0; i < c.length; i++) {
            c[i] = new f(this, b2);
            c[i].a();
        }
    }

    public static void a(int i) {
        c[i].c();
    }

    public static void a(int i, int i2) {
        c[i].a(i2);
    }

    public static void a(String str) {
        while (true) {
            int iGlGetError = GLES20.glGetError();
            if (iGlGetError == 0) {
                return;
            } else {
                Log.e(b, String.valueOf(str) + ": glError " + GLUtils.getEGLErrorString(iGlGetError));
            }
        }
    }

    public static int b(int i) {
        return c[i].d();
    }

    public static void b(int i, int i2) {
        c[i].b(i2);
    }

    public static int c(int i) {
        return c[i].h();
    }

    public static Canvas c() {
        return c[0].f();
    }

    public static int d(int i) {
        return c[i].i();
    }

    public static void d() {
        c[0].g();
    }

    public static Surface e() {
        return c[1].e();
    }

    public final void a() {
        a(0, ((Activity) this.a).findViewById(R.id.GUI).getWidth());
        b(0, ((Activity) this.a).findViewById(R.id.GUI).getHeight());
        for (int i = 0; i < c.length; i++) {
            a(i);
        }
        Log.d(b, "onSurfaceChanged()");
    }

    public final void b() {
        synchronized (this) {
            for (int i = 0; i < c.length; i++) {
                c[i].b();
            }
        }
    }
}
