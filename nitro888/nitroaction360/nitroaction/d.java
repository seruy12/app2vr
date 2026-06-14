package com.nitro888.nitroaction360.nitroaction;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import com.nibiru.lib.controller.R;
import java.lang.reflect.Array;
import java.nio.Buffer;

/* JADX INFO: loaded from: classes.dex */
final class d {
    final /* synthetic */ c a;
    private Context b;
    private int g;
    private int h;
    private int i;
    private int j;
    private int k;
    private int l;
    private int m;
    private int n;
    private final com.nitro888.nitroaction360.a.b o;
    private final com.nitro888.nitroaction360.a.b p;
    private final com.nitro888.nitroaction360.a.b q;
    private final com.nitro888.nitroaction360.a.b r;
    private float[] c = new float[16];
    private float[] d = new float[16];
    private float[] e = new float[16];
    private float[] f = new float[16];
    private final float[][] s = (float[][]) Array.newInstance((Class<?>) Float.TYPE, 9, 3);

    public d(c cVar, Context context) {
        this.a = cVar;
        this.b = context;
        this.o = com.nitro888.nitroaction360.a.f.a(this.b, R.raw.sphere);
        this.p = com.nitro888.nitroaction360.a.f.a(this.b, R.raw.plane_sq);
        this.q = com.nitro888.nitroaction360.a.f.a(this.b, R.raw.dome);
        this.r = com.nitro888.nitroaction360.a.f.a(this.b, R.raw.plane_sq_gui);
        b();
    }

    private void a(com.nitro888.nitroaction360.a.b bVar, float[] fArr, float[] fArr2, int i) {
        e unused = this.a.o;
        if (e.b(this.a.m) == 0) {
            return;
        }
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(770, 771);
        GLES20.glBindTexture(36197, i);
        GLES20.glActiveTexture(33984);
        GLES20.glUniform1i(this.j, 0);
        bVar.b()[0].position(0);
        GLES20.glVertexAttribPointer(this.k, 3, 5126, false, 0, (Buffer) bVar.b()[0]);
        GLES20.glEnableVertexAttribArray(this.k);
        bVar.b()[1].position(0);
        GLES20.glVertexAttribPointer(this.m, 2, 5126, false, 0, (Buffer) bVar.b()[1]);
        GLES20.glEnableVertexAttribArray(this.m);
        bVar.b()[2].position(0);
        GLES20.glVertexAttribPointer(this.l, 3, 5126, false, 0, (Buffer) bVar.b()[2]);
        GLES20.glEnableVertexAttribArray(this.l);
        Matrix.multiplyMM(this.e, 0, fArr, 0, this.c, 0);
        GLES20.glUniformMatrix4fv(this.i, 1, false, this.d, 0);
        Matrix.multiplyMM(this.d, 0, this.f, 0, this.e, 0);
        GLES20.glUniformMatrix4fv(this.h, 1, false, this.d, 0);
        GLES20.glUniform4f(this.n, fArr2[0], fArr2[1], fArr2[2], fArr2[3]);
        GLES20.glDrawArrays(4, 0, bVar.a());
    }

    private void b() {
        for (int i = 0; i < this.s.length; i++) {
            int i2 = i % 3;
            int i3 = i / 3;
            this.s[i][0] = i2 == 0 ? -4 : i2 == 1 ? 0 : 4;
            this.s[i][1] = i3 == 0 ? 4 : i3 == 1 ? 0 : -4;
            this.s[i][2] = -10.0f;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x008f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void a() {
        /*
            r7 = this;
            r6 = 1
            r1 = 0
            r0 = 0
            android.opengl.GLES20.glClearColor(r1, r1, r1, r1)
            r1 = 2884(0xb44, float:4.041E-42)
            android.opengl.GLES20.glEnable(r1)
            r1 = 2929(0xb71, float:4.104E-42)
            android.opengl.GLES20.glEnable(r1)
            android.content.Context r1 = r7.b
            r2 = 2131034117(0x7f050005, float:1.7678742E38)
            java.lang.String r1 = com.nitro888.nitroaction360.a.c.a(r1, r2)
            android.content.Context r2 = r7.b
            r3 = 2131034113(0x7f050001, float:1.7678734E38)
            java.lang.String r2 = com.nitro888.nitroaction360.a.c.a(r2, r3)
            r3 = 35633(0x8b31, float:4.9932E-41)
            int r3 = com.nitro888.nitroaction360.a.e.a(r3, r1)
            r1 = 35632(0x8b30, float:4.9931E-41)
            int r2 = com.nitro888.nitroaction360.a.e.a(r1, r2)
            r1 = 3
            java.lang.String[] r4 = new java.lang.String[r1]
            java.lang.String r1 = "a_Position"
            r4[r0] = r1
            java.lang.String r1 = "a_Normal"
            r4[r6] = r1
            r1 = 2
            java.lang.String r5 = "a_TexCoordinate"
            r4[r1] = r5
            int r1 = android.opengl.GLES20.glCreateProgram()
            if (r1 == 0) goto L8f
            android.opengl.GLES20.glAttachShader(r1, r3)
            android.opengl.GLES20.glAttachShader(r1, r2)
            int r3 = r4.length
            r2 = r0
        L4e:
            if (r2 < r3) goto L84
            android.opengl.GLES20.glLinkProgram(r1)
            int[] r2 = new int[r6]
            r3 = 35714(0x8b82, float:5.0046E-41)
            android.opengl.GLES20.glGetProgramiv(r1, r3, r2, r0)
            r2 = r2[r0]
            if (r2 != 0) goto L8f
            java.lang.String r2 = "ShaderHelper"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "Error compiling program: "
            r3.<init>(r4)
            java.lang.String r4 = android.opengl.GLES20.glGetProgramInfoLog(r1)
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            android.util.Log.e(r2, r3)
            android.opengl.GLES20.glDeleteProgram(r1)
        L7a:
            if (r0 != 0) goto L8c
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            java.lang.String r1 = "Error creating program."
            r0.<init>(r1)
            throw r0
        L84:
            r5 = r4[r2]
            android.opengl.GLES20.glBindAttribLocation(r1, r2, r5)
            int r2 = r2 + 1
            goto L4e
        L8c:
            r7.g = r0
            return
        L8f:
            r0 = r1
            goto L7a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nitro888.nitroaction360.nitroaction.d.a():void");
    }

    public final void a(int i, int i2) {
        GLES20.glViewport(0, 0, i, i2);
        float f = i / i2;
        Matrix.frustumM(this.f, 0, -f, f, -1.0f, 1.0f, 1.0f, 500.0f);
    }

    public final void a(float[] fArr, int i) {
        if (this.a.o == null) {
        }
        float[] fArrA = com.nitro888.nitroaction360.a.d.a(this.a.j, i);
        GLES20.glUseProgram(this.g);
        this.h = GLES20.glGetUniformLocation(this.g, "u_MVPMatrix");
        this.i = GLES20.glGetUniformLocation(this.g, "u_MVMatrix");
        this.j = GLES20.glGetUniformLocation(this.g, "u_Texture");
        this.n = GLES20.glGetUniformLocation(this.g, "u_Offset");
        this.k = GLES20.glGetAttribLocation(this.g, "a_Position");
        this.l = GLES20.glGetAttribLocation(this.g, "a_Normal");
        this.m = GLES20.glGetAttribLocation(this.g, "a_TexCoordinate");
        float f = this.a.k;
        float f2 = this.a.l;
        e unused = this.a.o;
        int iC = e.c(this.a.m);
        e unused2 = this.a.o;
        float[] fArr2 = {f, 1.0f * f2, (e.d(this.a.m) / iC) * f2, 1.0f};
        Matrix.setIdentityM(this.c, 0);
        Matrix.translateM(this.c, 0, 0.0f, 0.0f, 0.0f);
        Matrix.setRotateM(this.c, 0, fArr2[0], 1.0f, 0.0f, 0.0f);
        switch (this.a.i) {
            case R.raw.dome /* 2131034112 */:
                Matrix.scaleM(this.c, 0, 1.0f, 1.0f, 1.0f);
                com.nitro888.nitroaction360.a.b bVar = this.q;
                e unused3 = this.a.o;
                a(bVar, fArr, fArrA, e.b(this.a.m));
                break;
            case R.raw.sphere /* 2131034116 */:
                Matrix.scaleM(this.c, 0, 1.0f, 1.0f, 1.0f);
                com.nitro888.nitroaction360.a.b bVar2 = this.o;
                e unused4 = this.a.o;
                a(bVar2, fArr, fArrA, e.b(this.a.m));
                break;
            default:
                Matrix.scaleM(this.c, 0, fArr2[1], fArr2[2], fArr2[3]);
                com.nitro888.nitroaction360.a.b bVar3 = this.p;
                e unused5 = this.a.o;
                a(bVar3, fArr, fArrA, e.b(this.a.m));
                break;
        }
    }
}
