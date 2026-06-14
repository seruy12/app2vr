package com.nibiru.lib.feedback;

import android.os.Bundle;
import android.util.SparseArray;
import com.nibiru.lib.controller.BaseEvent;
import com.nibiru.lib.utils.ByteParser;
import com.nibiru.lib.utils.C0036a;

/* JADX INFO: loaded from: classes.dex */
public class FeedbackData extends BaseEvent {
    public boolean isUse;
    private float lA;
    private float lB;
    private float lC;
    private float lD;
    private float lE;
    SparseArray lF;
    private float lx;
    private float ly;
    private float lz;
    private float pitch;
    private float roll;
    private float x;
    private float y;
    private float yaw;
    private float z;

    public FeedbackData(int i) {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.yaw = 0.0f;
        this.pitch = 0.0f;
        this.roll = 0.0f;
        this.lx = 0.0f;
        this.ly = 0.0f;
        this.lz = 0.0f;
        this.lA = 0.0f;
        this.lB = 0.0f;
        this.lC = 0.0f;
        this.lD = 0.0f;
        this.lE = 0.0f;
        this.isUse = false;
        this.lF = new SparseArray();
        this.playerOrder = i;
        super.setPlayerOrder(i);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FeedbackData(Bundle bundle) {
        super(bundle);
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.yaw = 0.0f;
        this.pitch = 0.0f;
        this.roll = 0.0f;
        this.lx = 0.0f;
        this.ly = 0.0f;
        this.lz = 0.0f;
        this.lA = 0.0f;
        this.lB = 0.0f;
        this.lC = 0.0f;
        this.lD = 0.0f;
        this.lE = 0.0f;
        this.isUse = false;
        this.lF = new SparseArray();
        this.x = bundle.getFloat("x", 0.0f);
        this.y = bundle.getFloat("y", 0.0f);
        this.z = bundle.getFloat("z", 0.0f);
        this.yaw = bundle.getFloat("yaw", 0.0f);
        this.pitch = bundle.getFloat("pitch", 0.0f);
        this.roll = bundle.getFloat("roll", 0.0f);
        this.lx = bundle.getFloat("speed", 0.0f);
        this.ly = bundle.getFloat("acc", 0.0f);
        this.lz = bundle.getFloat("speedX", 0.0f);
        this.lA = bundle.getFloat("speedY", 0.0f);
        this.lB = bundle.getFloat("speedZ", 0.0f);
        this.lC = bundle.getFloat("accX", 0.0f);
        this.lD = bundle.getFloat("accY", 0.0f);
        this.lE = bundle.getFloat("accZ", 0.0f);
        this.lF.clear();
        int[] intArray = bundle.getIntArray("event_ids");
        if (intArray == null || intArray.length <= 0) {
            return;
        }
        for (int i : intArray) {
            this.lF.put(i, new FeedbackEventData(bundle.getBundle("event_" + i)));
        }
    }

    public FeedbackData(ByteParser byteParser) {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.yaw = 0.0f;
        this.pitch = 0.0f;
        this.roll = 0.0f;
        this.lx = 0.0f;
        this.ly = 0.0f;
        this.lz = 0.0f;
        this.lA = 0.0f;
        this.lB = 0.0f;
        this.lC = 0.0f;
        this.lD = 0.0f;
        this.lE = 0.0f;
        this.isUse = false;
        this.lF = new SparseArray();
        if (byteParser == null) {
            return;
        }
        this.exchange = true;
        this.eventTime = System.currentTimeMillis();
        this.playerOrder = byteParser.nextByte();
        this.x = byteParser.nextSpecFloat();
        this.y = byteParser.nextSpecFloat();
        this.z = byteParser.nextSpecFloat();
        this.yaw = byteParser.nextSpecFloat();
        this.roll = byteParser.nextSpecFloat();
        this.pitch = byteParser.nextSpecFloat();
        this.ly = byteParser.nextSpecFloat();
        this.lx = byteParser.nextSpecFloat();
        this.lC = byteParser.nextSpecFloat();
        this.lD = byteParser.nextSpecFloat();
        this.lE = byteParser.nextSpecFloat();
        this.lz = byteParser.nextSpecFloat();
        this.lA = byteParser.nextSpecFloat();
        this.lB = byteParser.nextSpecFloat();
    }

    private String bf() {
        int i = 0;
        String str = "{";
        while (true) {
            int i2 = i;
            if (i2 >= this.lF.size()) {
                return str + "}";
            }
            str = str + ((FeedbackEventData) this.lF.get(this.lF.keyAt(i2)));
            i = i2 + 1;
        }
    }

    public static int bytes2Int(byte[] bArr) {
        int i = 0;
        for (int i2 = 0; i2 < 4; i2++) {
            i = (i << 8) | (bArr[i2] & 255);
        }
        return i;
    }

    public static int bytes2Int(byte[] bArr, int i) {
        int i2 = 0;
        for (int i3 = i; i3 < i + 4; i3++) {
            i2 = (i2 << 8) | (bArr[i3] & 255);
        }
        return i2;
    }

    public static long bytes2Long(byte[] bArr) {
        long j = 0;
        for (int i = 0; i < 8; i++) {
            j = (j << 8) | ((long) (bArr[i] & 255));
        }
        return j;
    }

    public static long bytes2Long(byte[] bArr, int i) {
        long j = 0;
        for (int i2 = i; i2 < i + 8; i2++) {
            j = (j << 8) | ((long) (bArr[i2] & 255));
        }
        return j;
    }

    public static byte[] int2Bytes(int i) {
        byte[] bArr = new byte[4];
        for (int i2 = 0; i2 < 4; i2++) {
            bArr[i2] = (byte) (i >> (32 - ((i2 + 1) << 3)));
        }
        return bArr;
    }

    public static byte int2OneByte(int i) {
        return (byte) i;
    }

    public static byte[] long2Bytes(long j) {
        byte[] bArr = new byte[8];
        for (int i = 0; i < 8; i++) {
            bArr[i] = (byte) ((j >> (64 - ((i + 1) << 3))) & 255);
        }
        return bArr;
    }

    public static int oneByte2Int(byte b) {
        return b > 0 ? b : b + 128 + 128;
    }

    public void addEvent(int i, float f) {
        if (this.lF == null) {
            return;
        }
        this.isUse = true;
        FeedbackEventData feedbackEventData = (FeedbackEventData) this.lF.get(i);
        if (feedbackEventData == null) {
            feedbackEventData = new FeedbackEventData(this.playerOrder, i);
        }
        feedbackEventData.lK = f;
        this.lF.put(i, feedbackEventData);
    }

    public void addEvent(int i, float f, float[] fArr) {
        if (this.lF == null) {
            return;
        }
        this.isUse = true;
        FeedbackEventData feedbackEventData = (FeedbackEventData) this.lF.get(i);
        if (feedbackEventData == null) {
            feedbackEventData = new FeedbackEventData(this.playerOrder, i);
        }
        feedbackEventData.lK = f;
        if (fArr != null && fArr.length >= 3) {
            feedbackEventData.lH = fArr[0];
            feedbackEventData.lI = fArr[1];
            feedbackEventData.lJ = fArr[2];
        }
        this.lF.put(i, feedbackEventData);
    }

    public synchronized void clear() {
        if (this.data != null) {
            this.data.clear();
        }
        this.isUse = false;
    }

    public float getAcc() {
        return this.ly;
    }

    public float getAccX() {
        return this.lC;
    }

    public float getAccY() {
        return this.lD;
    }

    public float getAccZ() {
        return this.lE;
    }

    public float getPitch() {
        return this.pitch;
    }

    public int getPlayer() {
        return this.playerOrder;
    }

    public float getRoll() {
        return this.roll;
    }

    @Override // com.nibiru.lib.controller.C0013a
    public Bundle getSendBundle() {
        Bundle sendBundle = super.getSendBundle();
        if (this.lF != null && this.lF.size() > 0) {
            int[] iArr = new int[this.lF.size()];
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= this.lF.size()) {
                    break;
                }
                int iKeyAt = this.lF.keyAt(i2);
                iArr[i2] = iKeyAt;
                FeedbackEventData feedbackEventData = (FeedbackEventData) this.lF.get(iKeyAt);
                if (feedbackEventData != null) {
                    sendBundle.putBundle("event_" + iKeyAt, feedbackEventData.getSendBundle());
                }
                sendBundle.putIntArray("event_ids", iArr);
                i = i2 + 1;
            }
        }
        return sendBundle;
    }

    public byte[] getSendBytes() {
        C0036a c0036a = new C0036a();
        c0036a.writeByte((byte) this.playerOrder);
        c0036a.i(this.x);
        c0036a.i(this.y);
        c0036a.i(this.z);
        c0036a.i(this.yaw);
        c0036a.i(this.roll);
        c0036a.i(this.pitch);
        c0036a.i(this.ly);
        c0036a.i(this.lx);
        c0036a.i(this.lC);
        c0036a.i(this.lD);
        c0036a.i(this.lE);
        c0036a.i(this.lz);
        c0036a.i(this.lA);
        c0036a.i(this.lB);
        byte[] bArrBi = c0036a.bi();
        c0036a.close();
        return bArrBi;
    }

    public float getSpeed() {
        return this.lx;
    }

    public float getSpeedX() {
        return this.lz;
    }

    public float getSpeedY() {
        return this.lA;
    }

    public float getSpeedZ() {
        return this.lB;
    }

    @Override // com.nibiru.lib.controller.BaseEvent
    public String getUnityMessage() {
        return null;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getZ() {
        return this.z;
    }

    public void removeEvent(int i) {
        if (this.lF == null) {
            return;
        }
        this.isUse = true;
        if (((FeedbackEventData) this.lF.get(i)) != null) {
            this.lF.remove(i);
        }
    }

    public void setAccData(float f, float f2, float f3) {
        this.lC = f;
        this.lD = f2;
        this.lE = f3;
        this.isUse = true;
        if (this.data != null) {
            this.data.putFloat("accX", f);
            this.data.putFloat("accY", f2);
            this.data.putFloat("accZ", f3);
        }
    }

    public void setPoseData(float f, float f2, float f3, float f4, float f5, float f6) {
        this.x = f;
        this.y = f2;
        this.z = f3;
        this.yaw = f4;
        this.roll = f5;
        this.pitch = f6;
        this.isUse = true;
        if (this.data != null) {
            this.data.putFloat("x", f);
            this.data.putFloat("y", f2);
            this.data.putFloat("z", f3);
            this.data.putFloat("yaw", f4);
            this.data.putFloat("roll", f5);
            this.data.putFloat("pitch", f6);
        }
    }

    public void setSpeedData(float f, float f2) {
        this.lx = f;
        this.ly = f2;
        this.isUse = true;
        if (this.data != null) {
            this.data.putFloat("speed", f);
            this.data.putFloat("acc", f2);
        }
    }

    public void setSpeedData(float f, float f2, float f3) {
        this.lz = f;
        this.lA = f2;
        this.lB = f3;
        this.isUse = true;
        if (this.data != null) {
            this.data.putFloat("speedX", f);
            this.data.putFloat("speedY", f2);
            this.data.putFloat("speedZ", f3);
        }
    }

    public String toString() {
        return "FeedbackData [player=" + this.playerOrder + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", yaw=" + this.yaw + ", roll=" + this.roll + ", pitch=" + this.pitch + ", speed=" + this.lx + ", acc=" + this.ly + ", speedX=" + this.lz + ", speedY=" + this.lA + ", speedZ=" + this.lB + ", accX=" + this.lC + ", accY=" + this.lD + ", accZ=" + this.lE + ", events=" + bf() + "]";
    }
}
