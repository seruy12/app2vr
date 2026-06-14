package com.nitro888.nitroaction360.a;

/* JADX INFO: loaded from: classes.dex */
public final class e {
    /* JADX WARN: Removed duplicated region for block: B:11:0x0040  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int a(int r5, java.lang.String r6) {
        /*
            r0 = 0
            int r1 = android.opengl.GLES20.glCreateShader(r5)
            if (r1 == 0) goto L40
            android.opengl.GLES20.glShaderSource(r1, r6)
            android.opengl.GLES20.glCompileShader(r1)
            r2 = 1
            int[] r2 = new int[r2]
            r3 = 35713(0x8b81, float:5.0045E-41)
            android.opengl.GLES20.glGetShaderiv(r1, r3, r2, r0)
            r2 = r2[r0]
            if (r2 != 0) goto L40
            java.lang.String r2 = "ShaderHelper"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "Error compiling shader: "
            r3.<init>(r4)
            java.lang.String r4 = android.opengl.GLES20.glGetShaderInfoLog(r1)
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            android.util.Log.e(r2, r3)
            android.opengl.GLES20.glDeleteShader(r1)
        L35:
            if (r0 != 0) goto L3f
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            java.lang.String r1 = "Error creating shader."
            r0.<init>(r1)
            throw r0
        L3f:
            return r0
        L40:
            r0 = r1
            goto L35
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nitro888.nitroaction360.a.e.a(int, java.lang.String):int");
    }
}
