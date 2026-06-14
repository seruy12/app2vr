package com.nitro888.nitroaction360.a;

import java.nio.FloatBuffer;

/* JADX INFO: loaded from: classes.dex */
public class b {
    private static final String a = b.class.getSimpleName();
    private final int b;
    private final FloatBuffer[] c = new FloatBuffer[3];

    public b(int i, FloatBuffer[] floatBufferArr) {
        this.b = i;
        this.c[0] = floatBufferArr[0];
        this.c[1] = floatBufferArr[1];
        this.c[2] = floatBufferArr[2];
    }

    public final int a() {
        return this.b;
    }

    public final FloatBuffer[] b() {
        return this.c;
    }
}
