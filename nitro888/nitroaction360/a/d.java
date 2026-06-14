package com.nitro888.nitroaction360.a;

/* JADX INFO: loaded from: classes.dex */
public final class d {
    private static float[] a() {
        return new float[]{1.0f, 1.0f, 0.0f, 0.0f};
    }

    public static float[] a(int i, int i2) {
        switch (i) {
            case 1:
                float[] fArr = new float[4];
                if (i2 == 1) {
                    fArr[0] = 0.5f;
                    fArr[1] = 1.0f;
                    fArr[2] = 0.0f;
                    fArr[3] = 0.0f;
                    return fArr;
                }
                if (i2 != 2) {
                    return a();
                }
                fArr[0] = 0.5f;
                fArr[1] = 1.0f;
                fArr[2] = 0.5f;
                fArr[3] = 0.0f;
                return fArr;
            case 2:
                float[] fArr2 = new float[4];
                if (i2 == 1) {
                    fArr2[0] = 1.0f;
                    fArr2[1] = 0.5f;
                    fArr2[2] = 0.0f;
                    fArr2[3] = 0.0f;
                    return fArr2;
                }
                if (i2 != 2) {
                    return a();
                }
                fArr2[0] = 1.0f;
                fArr2[1] = 0.5f;
                fArr2[2] = 0.0f;
                fArr2[3] = 0.5f;
                return fArr2;
            case 3:
                float[] fArr3 = new float[4];
                if (i2 == 1) {
                    fArr3[0] = 1.0f;
                    fArr3[1] = 0.5f;
                    fArr3[2] = 0.0f;
                    fArr3[3] = 0.5f;
                    return fArr3;
                }
                if (i2 != 2) {
                    return a();
                }
                fArr3[0] = 1.0f;
                fArr3[1] = 0.5f;
                fArr3[2] = 0.0f;
                fArr3[3] = 0.0f;
                return fArr3;
            default:
                return a();
        }
    }
}
