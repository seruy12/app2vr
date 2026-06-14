package com.nibiru.lib.feedback;

import android.os.Bundle;
import com.nibiru.lib.controller.MotionSenseEvent;
import com.nibiru.lib.utils.ByteParser;
import com.nibiru.lib.utils.C0036a;

/* JADX INFO: loaded from: classes.dex */
public class FeedbackMotorData {
    private int H;
    private boolean exchange;
    private float lK;
    private long lL;
    private MOTOR_LOC lM;

    enum MOTOR_LOC {
        LEFT,
        RIGHT,
        ALL
    }

    public FeedbackMotorData(int i, int i2, long j, float f) {
        this.lL = 0L;
        this.exchange = false;
        this.lK = -255.0f;
        this.lM = MOTOR_LOC.ALL;
        this.H = i;
        this.lL = j;
        this.lK = f;
        if (i2 == 0) {
            this.lM = MOTOR_LOC.LEFT;
        } else if (i2 == 1) {
            this.lM = MOTOR_LOC.RIGHT;
        } else if (i2 == 255) {
            this.lM = MOTOR_LOC.ALL;
        }
    }

    public FeedbackMotorData(int i, long j, float f, MOTOR_LOC motor_loc) {
        this.lL = 0L;
        this.exchange = false;
        this.lK = -255.0f;
        this.lM = MOTOR_LOC.ALL;
        this.H = i;
        this.lL = j;
        this.lK = f;
        this.lM = motor_loc;
    }

    public FeedbackMotorData(Bundle bundle) {
        this.lL = 0L;
        this.exchange = false;
        this.lK = -255.0f;
        this.lM = MOTOR_LOC.ALL;
        this.H = bundle.getInt(MotionSenseEvent.KEY_PLAYER);
        this.lL = bundle.getLong("duration");
        int i = bundle.getInt("location");
        this.lM = (i >= MOTOR_LOC.values().length || i < 0) ? MOTOR_LOC.ALL : MOTOR_LOC.values()[i];
        this.lK = bundle.getFloat("strength");
        this.exchange = bundle.getBoolean("exchange");
    }

    public FeedbackMotorData(ByteParser byteParser) {
        this.lL = 0L;
        this.exchange = false;
        this.lK = -255.0f;
        this.lM = MOTOR_LOC.ALL;
        if (byteParser == null) {
            return;
        }
        this.exchange = true;
        this.H = byteParser.nextByte();
        int iNextInt = byteParser.nextInt();
        this.lM = (iNextInt >= MOTOR_LOC.values().length || iNextInt < 0) ? MOTOR_LOC.ALL : MOTOR_LOC.values()[iNextInt];
        this.lK = byteParser.nextSpecFloat();
        this.lL = byteParser.nextLong();
    }

    public Bundle getSendBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt(MotionSenseEvent.KEY_PLAYER, this.H);
        bundle.putLong("duration", this.lL);
        bundle.putInt("location", this.lM.ordinal());
        bundle.putFloat("strength", this.lK);
        bundle.putBoolean("exchange", this.exchange);
        return bundle;
    }

    public byte[] getSendBytes() {
        C0036a c0036a = new C0036a();
        c0036a.writeByte(this.H);
        c0036a.writeInt(this.lM.ordinal());
        c0036a.i(this.lK);
        c0036a.writeLong(this.lL);
        byte[] bArrBi = c0036a.bi();
        c0036a.close();
        return bArrBi;
    }

    public boolean isExchange() {
        return this.exchange;
    }

    public void setExchange(boolean z) {
        this.exchange = z;
    }

    public String toString() {
        return "FeedbackMotorData [player=" + this.H + ", duration=" + this.lL + ", exchange=" + this.exchange + ", strength=" + this.lK + ", location=" + this.lM + "]";
    }
}
