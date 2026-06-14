package com.nibiru.lib.feedback;

import android.os.Bundle;
import com.nibiru.lib.controller.MotionSenseEvent;
import com.nibiru.lib.utils.ByteParser;
import com.nibiru.lib.utils.C0036a;

/* JADX INFO: loaded from: classes.dex */
public class FeedbackEventData {
    int H;
    private long eventTime;
    private boolean exchange;
    int id;
    boolean lG;
    float lH;
    float lI;
    float lJ;
    float lK;

    public FeedbackEventData(int i, int i2) {
        this.lG = true;
        this.lH = -255.0f;
        this.lI = -255.0f;
        this.lJ = -255.0f;
        this.eventTime = 0L;
        this.exchange = false;
        this.lK = -255.0f;
        this.H = i;
        this.id = i2;
    }

    public FeedbackEventData(Bundle bundle) {
        this.lG = true;
        this.lH = -255.0f;
        this.lI = -255.0f;
        this.lJ = -255.0f;
        this.eventTime = 0L;
        this.exchange = false;
        this.lK = -255.0f;
        this.id = bundle.getInt("id");
        this.H = bundle.getInt(MotionSenseEvent.KEY_PLAYER);
        this.lH = bundle.getFloat("axisX");
        this.lI = bundle.getFloat("axisY");
        this.lJ = bundle.getFloat("axisZ");
        this.lK = bundle.getFloat("strength");
        this.lG = bundle.getBoolean("triggle");
        this.exchange = bundle.getBoolean("exchange");
    }

    public FeedbackEventData(ByteParser byteParser) {
        this.lG = true;
        this.lH = -255.0f;
        this.lI = -255.0f;
        this.lJ = -255.0f;
        this.eventTime = 0L;
        this.exchange = false;
        this.lK = -255.0f;
        if (byteParser == null) {
            return;
        }
        this.exchange = true;
        this.eventTime = System.currentTimeMillis();
        this.H = byteParser.nextByte();
        this.id = byteParser.nextInt();
        this.lH = byteParser.nextSpecFloat();
        this.lI = byteParser.nextSpecFloat();
        this.lJ = byteParser.nextSpecFloat();
        this.lK = byteParser.nextSpecFloat();
        this.lG = byteParser.nextByte() > 0;
    }

    public float getAxis_x() {
        return this.lH;
    }

    public float getAxis_y() {
        return this.lI;
    }

    public float getAxis_z() {
        return this.lJ;
    }

    public long getEventTime() {
        return this.eventTime;
    }

    public int getId() {
        return this.id;
    }

    public int getPlayer() {
        return this.H;
    }

    public Bundle getSendBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("id", this.id);
        bundle.putInt(MotionSenseEvent.KEY_PLAYER, this.H);
        bundle.putFloat("axisX", this.lH);
        bundle.putFloat("axisY", this.lI);
        bundle.putFloat("axisZ", this.lJ);
        bundle.putFloat("strength", this.lK);
        bundle.putBoolean("triggle", this.lG);
        bundle.putBoolean("exchange", this.exchange);
        return bundle;
    }

    public byte[] getSendBytes() {
        C0036a c0036a = new C0036a();
        c0036a.writeByte(this.H);
        c0036a.writeInt(this.id);
        c0036a.i(this.lH);
        c0036a.i(this.lI);
        c0036a.i(this.lJ);
        c0036a.i(this.lK);
        c0036a.writeInt(this.lG ? 1 : 0);
        byte[] bArrBi = c0036a.bi();
        c0036a.close();
        return bArrBi;
    }

    public float getStrength() {
        return this.lK;
    }

    public boolean isExchange() {
        return this.exchange;
    }

    public boolean isTriggle() {
        return this.lG;
    }

    public void setAxis_x(float f) {
        this.lH = f;
    }

    public void setAxis_y(float f) {
        this.lI = f;
    }

    public void setAxis_z(float f) {
        this.lJ = f;
    }

    public void setEventTime(long j) {
        this.eventTime = j;
    }

    public void setExchange(boolean z) {
        this.exchange = z;
    }

    public void setId(int i) {
        this.id = i;
    }

    public void setPlayer(int i) {
        this.H = i;
    }

    public void setStrength(float f) {
        this.lK = f;
    }

    public void setTriggle(boolean z) {
        this.lG = z;
    }

    public String toString() {
        return "FeedbackEventData [id=" + this.id + ", player=" + this.H + ", axis_x=" + this.lH + ", axis_y=" + this.lI + ", axis_z=" + this.lJ + ", strength=" + this.lK + "]";
    }
}
