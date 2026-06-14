package com.nitro888.nitroaction360.a;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public class f {
    private static final String a = f.class.getSimpleName();

    public static b a(Context context, int i) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(i)));
        while (true) {
            try {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                if (line.startsWith("v ")) {
                    arrayList.add(line.substring(2));
                }
                if (line.startsWith("vt ")) {
                    arrayList2.add(line.substring(3));
                }
                if (line.startsWith("vn ")) {
                    arrayList3.add(line.substring(3));
                }
                if (line.startsWith("f ")) {
                    arrayList4.add(line.substring(2));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        float[] fArr = new float[arrayList4.size() * 3 * 3];
        float[] fArr2 = new float[arrayList4.size() * 3 * 3];
        float[] fArr3 = new float[arrayList4.size() * 3 * 2];
        short[] sArr = new short[arrayList4.size() * 3];
        Iterator it = arrayList4.iterator();
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (it.hasNext()) {
            String[] strArrSplit = ((String) it.next()).split(" ");
            int length = strArrSplit.length;
            int i6 = 0;
            int i7 = i2;
            int i8 = i3;
            int i9 = i5;
            int i10 = i4;
            while (i6 < length) {
                String str = strArrSplit[i6];
                int i11 = i9 + 1;
                sArr[i9] = (short) i9;
                String[] strArrSplit2 = str.split("/");
                String str2 = (String) arrayList.get(Integer.parseInt(strArrSplit2[0]) - 1);
                String str3 = (String) arrayList2.get(Integer.parseInt(strArrSplit2[1]) - 1);
                String str4 = (String) arrayList3.get(Integer.parseInt(strArrSplit2[2]) - 1);
                String[] strArrSplit3 = str2.split(" ");
                String[] strArrSplit4 = str3.split(" ");
                String[] strArrSplit5 = str4.split(" ");
                int length2 = strArrSplit3.length;
                int i12 = 0;
                while (i12 < length2) {
                    fArr[i7] = Float.parseFloat(strArrSplit3[i12]);
                    i12++;
                    i7++;
                }
                int length3 = strArrSplit4.length;
                int i13 = 0;
                while (i13 < length3) {
                    fArr3[i8] = Float.parseFloat(strArrSplit4[i13]);
                    i13++;
                    i8++;
                }
                int length4 = strArrSplit5.length;
                int i14 = 0;
                while (i14 < length4) {
                    fArr2[i10] = Float.parseFloat(strArrSplit5[i14]);
                    i14++;
                    i10++;
                }
                i6++;
                i9 = i11;
            }
            i5 = i9;
            i4 = i10;
            i3 = i8;
            i2 = i7;
        }
        floatBufferArr[0].put(fArr).position(0);
        floatBufferArr[1].put(fArr3).position(0);
        FloatBuffer[] floatBufferArr = {ByteBuffer.allocateDirect(fArr.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer(), ByteBuffer.allocateDirect(fArr3.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer(), ByteBuffer.allocateDirect(fArr2.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()};
        floatBufferArr[2].put(fArr2).position(0);
        return new b(sArr.length, floatBufferArr);
    }
}
