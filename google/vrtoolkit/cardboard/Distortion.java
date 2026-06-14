package com.google.vrtoolkit.cardboard;

import java.lang.reflect.Array;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class Distortion {
    private static final float[] DEFAULT_COEFFICIENTS = {0.441f, 0.156f};
    private float[] coefficients;

    public Distortion() {
        this.coefficients = (float[]) DEFAULT_COEFFICIENTS.clone();
    }

    public Distortion(Distortion distortion) {
        setCoefficients(distortion.coefficients);
    }

    public static Distortion parseFromProtobuf(float[] fArr) {
        Distortion distortion = new Distortion();
        distortion.setCoefficients(fArr);
        return distortion;
    }

    private static double[] solveLeastSquares(double[][] dArr, double[] dArr2) {
        int length = dArr.length;
        int length2 = dArr[0].length;
        double[][] dArr3 = (double[][]) Array.newInstance((Class<?>) Double.TYPE, length2, length2);
        for (int i = 0; i < length2; i++) {
            for (int i2 = 0; i2 < length2; i2++) {
                double d = 0.0d;
                for (int i3 = 0; i3 < length; i3++) {
                    d += dArr[i3][i2] * dArr[i3][i];
                }
                dArr3[i2][i] = d;
            }
        }
        double[][] dArr4 = (double[][]) Array.newInstance((Class<?>) Double.TYPE, length2, length2);
        if (length2 != 2) {
            throw new RuntimeException("solveLeastSquares: only 2 coefficients currently supported, " + length2 + " given.");
        }
        double d2 = (dArr3[0][0] * dArr3[1][1]) - (dArr3[0][1] * dArr3[1][0]);
        dArr4[0][0] = dArr3[1][1] / d2;
        dArr4[1][1] = dArr3[0][0] / d2;
        dArr4[0][1] = (-dArr3[1][0]) / d2;
        dArr4[1][0] = (-dArr3[0][1]) / d2;
        double[] dArr5 = new double[length2];
        for (int i4 = 0; i4 < length2; i4++) {
            double d3 = 0.0d;
            for (int i5 = 0; i5 < length; i5++) {
                d3 += dArr[i5][i4] * dArr2[i5];
            }
            dArr5[i4] = d3;
        }
        double[] dArr6 = new double[length2];
        for (int i6 = 0; i6 < length2; i6++) {
            double d4 = 0.0d;
            for (int i7 = 0; i7 < length2; i7++) {
                d4 += dArr4[i7][i6] * dArr5[i7];
            }
            dArr6[i6] = d4;
        }
        return dArr6;
    }

    public float distort(float f) {
        return distortionFactor(f) * f;
    }

    public float distortInverse(float f) {
        float f2 = f / 0.9f;
        float fDistort = f - distort(f2);
        float f3 = f2;
        float f4 = f * 0.9f;
        while (Math.abs(f4 - f3) > 1.0E-4d) {
            float fDistort2 = f - distort(f4);
            float f5 = f4 - (((f4 - f3) / (fDistort2 - fDistort)) * fDistort2);
            f3 = f4;
            f4 = f5;
            fDistort = fDistort2;
        }
        return f4;
    }

    public float distortionFactor(float f) {
        float f2 = 1.0f;
        float f3 = f * f;
        float f4 = 1.0f;
        for (float f5 : this.coefficients) {
            f2 *= f3;
            f4 += f5 * f2;
        }
        return f4;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof Distortion) {
            return Arrays.equals(this.coefficients, ((Distortion) obj).coefficients);
        }
        return false;
    }

    public Distortion getApproximateInverseDistortion(float f) {
        double[][] dArr = (double[][]) Array.newInstance((Class<?>) Double.TYPE, 10, 2);
        double[] dArr2 = new double[10];
        for (int i = 0; i < 10; i++) {
            float f2 = ((i + 1) * f) / 10.0f;
            double dDistort = distort(f2);
            double d = dDistort;
            for (int i2 = 0; i2 < 2; i2++) {
                d *= dDistort * dDistort;
                dArr[i][i2] = d;
            }
            dArr2[i] = ((double) f2) - dDistort;
        }
        double[] dArrSolveLeastSquares = solveLeastSquares(dArr, dArr2);
        float[] fArr = new float[dArrSolveLeastSquares.length];
        for (int i3 = 0; i3 < dArrSolveLeastSquares.length; i3++) {
            fArr[i3] = (float) dArrSolveLeastSquares[i3];
        }
        Distortion distortion = new Distortion();
        distortion.setCoefficients(fArr);
        return distortion;
    }

    public float[] getCoefficients() {
        return this.coefficients;
    }

    public void setCoefficients(float[] fArr) {
        this.coefficients = fArr != null ? (float[]) fArr.clone() : new float[0];
    }

    public float[] toProtobuf() {
        return (float[]) this.coefficients.clone();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{\n  coefficients: [");
        for (int i = 0; i < this.coefficients.length; i++) {
            sb.append(Float.toString(this.coefficients[i]));
            if (i < this.coefficients.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("],\n}");
        return sb.toString();
    }
}
