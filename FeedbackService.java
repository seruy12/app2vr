package com.nibiru.lib.feedback;

/* JADX INFO: loaded from: classes.dex */
public interface FeedbackService {
    public static final int EVENT_COLLISION = 2;
    public static final int EVENT_SHOT = 1;
    public static final int POS_ALL = 255;
    public static final int POS_LEFT = 0;
    public static final int POS_RIGHT = 1;

    void endEvent(int i, int i2);

    boolean isServiceEnable();

    void setPoseState(int i, float f, float f2, float f3, float f4, float f5, float f6);

    void setSpeed(int i, float f, float f2);

    void setSpeed(int i, float[] fArr, float[] fArr2);

    void startEvent(int i, int i2, float f);

    void startEvent(int i, int i2, float[] fArr, float f);

    boolean startMotor(int i, int i2, long j);

    boolean startMotor(int i, int i2, long j, float f);

    void stopMotor(int i, int i2);

    void syncFeedbackData(FeedbackData[] feedbackDataArr);

    void syncOnce();
}
