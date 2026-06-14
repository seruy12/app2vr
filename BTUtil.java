package com.nibiru.lib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.nibiru.lib.controller.GlobalLog;
import com.nibiru.lib.controller.StickEvent;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class BTUtil {
    public static final String INPUT_UI = "com.nibiru.inputui";
    public static final String LOG_DIR = "sdcard/nibiru/log/";
    public static final int MAX_LOG_COUNT = 0;
    public static final String SERVICE_FEEDBACK = "com.nibiru.feedback.service";
    public static final String SERVICE_MAINUI = "com.nibiru.mainui";
    public static final String SERVICE_NAME = "com.nibiru.service";
    public static final String SERVICE_OPPO = "com.oppo.service";
    public static final String SERVICE_TELE = "com.nibiru.telecom.service";
    public static final String SERVICE_UI = "com.nibiru.view.devicelist";
    public static final String SUPPORT_SERVICE_NAME = "com.nibiru.support.service";
    public static final int VERSION_CURRENT = 0;
    public static final int VERSION_NOMARL = 0;
    public static final int VERSION_OPPO = 1;
    public static final int VERSION_TELE = 2;
    public static final String CHECK_PACKAGENAME = "com.nibiru";
    public static final String[] PACKAGE = {CHECK_PACKAGENAME, "com.nibiru.play", "com.nibiru.telecom", "com.oppo.nibiru"};
    private static boolean isStart = false;
    private static boolean c = false;
    private static boolean d = false;
    public static SimpleDateFormat mDateFormatLog = new SimpleDateFormat("MM_dd__HH_mm_ss", Locale.CHINA);

    public static String generateBTDeviceCode(BTDevice bTDevice) {
        return bTDevice == null ? "" : "" + bTDevice.getDeviceName() + "|" + bTDevice.getDeviceAddr() + "|" + bTDevice.getDeviceType() + "|" + bTDevice.getDeviceId() + "|" + bTDevice.getPlayerOrder() + "|" + bTDevice.isConnected() + "|" + bTDevice.getState();
    }

    public static String generateBTDeviceListCode(BTDevice[] bTDeviceArr) {
        if (bTDeviceArr == null) {
            return "";
        }
        int length = bTDeviceArr.length;
        String str = "";
        int i = 0;
        while (i < length) {
            String str2 = (str + generateBTDeviceCode(bTDeviceArr[i])) + "#";
            i++;
            str = str2;
        }
        return str;
    }

    public static KeyEvent generateKeyEvent(int i, int i2, int i3) {
        long jUptimeMillis = SystemClock.uptimeMillis();
        return new KeyEvent(jUptimeMillis, jUptimeMillis, i, i2, 0, 0, i3, 0, 0);
    }

    public static MotionEvent generateMotionEvent(byte b, byte b2, byte b3, byte b4, int i) {
        long jUptimeMillis = SystemClock.uptimeMillis();
        return MotionEvent.obtain(jUptimeMillis, jUptimeMillis, 2, b & 255, b2 & 255, 0.0f, 1.0f, 0, b3 & 255, b4 & 255, i, 0);
    }

    public static int getAPKVersion(Context context, String str) {
        if (str == null || context == null) {
            return -1;
        }
        try {
            return context.getPackageManager().getPackageInfo(str, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            GlobalLog.v("not found " + str);
            return -1;
        }
    }

    public static int getAndroidVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static String getApplicationSessionInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getCurrentLangType(Context context, boolean z) {
        if (!z) {
            return !isChina(context) ? 3 : 1;
        }
        String language = context.getResources().getConfiguration().locale.getLanguage();
        return (language == null || !language.startsWith("zh")) ? 3 : 1;
    }

    public static String getLogTimeDesc(long j) {
        return mDateFormatLog.format(new Date(j));
    }

    public static long getLongtime(String str) {
        try {
            return mDateFormatLog.parse(str).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public static MotionEvent getMotionEvent(StickEvent stickEvent) {
        int[] axisValue = stickEvent.getAxisValue();
        return MotionEvent.obtain(stickEvent.getTime(), stickEvent.getTime(), 2, axisValue[0], axisValue[1], 0.0f, 1.0f, 0, axisValue[2], axisValue[3], stickEvent.getPlayerOrder(), 0);
    }

    public static long getTimeStamp() {
        return System.currentTimeMillis();
    }

    public static int getVerCode(Context context) {
        if (context == null) {
            return -1;
        }
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean isChina(Context context) {
        if (context == null) {
            return true;
        }
        String iSO3Country = context.getResources().getConfiguration().locale.getISO3Country();
        return iSO3Country.startsWith("CHN") || iSO3Country.startsWith("CN");
    }

    public static boolean isExistApp(Context context, String str) {
        PackageManager packageManager;
        PackageInfo packageInfo;
        if (context == null || (packageManager = context.getPackageManager()) == null) {
            return false;
        }
        try {
            packageInfo = packageManager.getPackageInfo(str, 64);
        } catch (PackageManager.NameNotFoundException e) {
            GlobalLog.e(str + " can't find");
            packageInfo = null;
        }
        return packageInfo != null;
    }

    public static boolean isInTimeRange(int i, int i2) {
        int i3 = Calendar.getInstance().get(11);
        return i3 >= i && i3 < i2;
    }

    public static final String md5(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            byte[] bArrDigest = messageDigest.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bArrDigest) {
                String hexString = Integer.toHexString(b & 255);
                while (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                stringBuffer.append(hexString);
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "-1";
        }
    }

    public static BTDevice parseBTDevice(String str) {
        BTDevice[] bTDeviceList;
        if (str == null || (bTDeviceList = parseBTDeviceList(str)) == null || bTDeviceList.length <= 0) {
            return null;
        }
        return bTDeviceList[0];
    }

    public static BTDevice[] parseBTDeviceList(String str) {
        String[] strArrSplit;
        if (str == null || (strArrSplit = str.split("#")) == null) {
            return null;
        }
        if (strArrSplit.length == 0) {
            return new BTDevice[0];
        }
        ArrayList arrayList = new ArrayList();
        for (String str2 : strArrSplit) {
            String[] strArrSplit2 = str2.split("\\|");
            if (strArrSplit2.length >= 6) {
                try {
                    BTDevice bTDevice = new BTDevice();
                    bTDevice.setDeviceName(strArrSplit2[0]);
                    bTDevice.setDeviceAddr(strArrSplit2[1]);
                    bTDevice.setDeviceType(Integer.parseInt(strArrSplit2[2]));
                    bTDevice.setDeviceId(Integer.parseInt(strArrSplit2[3]));
                    bTDevice.setPlayerOrder(Integer.parseInt(strArrSplit2[4]));
                    bTDevice.setConnected(Boolean.parseBoolean(strArrSplit2[5]));
                    if (strArrSplit2.length > 6) {
                        bTDevice.setState(Integer.parseInt(strArrSplit2[6]));
                    }
                    arrayList.add(bTDevice);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return (BTDevice[]) arrayList.toArray(new BTDevice[arrayList.size()]);
    }

    public static KeyEvent parseKeyEvent(String str, int[] iArr) {
        if (str == null) {
            return null;
        }
        String[] strArrSplit = str.split("\\|");
        if (strArrSplit.length >= 5) {
            try {
                int i = Integer.parseInt(strArrSplit[0]);
                int i2 = Integer.parseInt(strArrSplit[1]);
                int i3 = Integer.parseInt(strArrSplit[2]);
                boolean z = Boolean.parseBoolean(strArrSplit[3]);
                int i4 = Integer.parseInt(strArrSplit[4]);
                long jUptimeMillis = SystemClock.uptimeMillis();
                if (iArr != null && i2 < iArr.length) {
                    i2 = iArr[i2];
                }
                return new KeyEvent(jUptimeMillis, jUptimeMillis, i, i2, z ? 1 : 0, i3, i4, 0, 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static MotionEvent translateJoystick(float f, float f2, float f3, float f4, float f5, float f6, long j) {
        if (getAndroidVersion() < 12) {
            return null;
        }
        MotionEvent.PointerProperties[] pointerPropertiesArr = {new MotionEvent.PointerProperties()};
        pointerPropertiesArr[0].clear();
        pointerPropertiesArr[0].id = 0;
        float f7 = f - 128.0f;
        float f8 = 128.0f - f2;
        float f9 = f3 - 128.0f;
        float f10 = 128.0f - f4;
        GlobalLog.e("LX: " + f7 + " LY: " + f8 + " RX: " + f9 + " RY:  " + f10);
        MotionEvent.PointerCoords pointerCoords = new MotionEvent.PointerCoords();
        pointerCoords.setAxisValue(0, f7);
        pointerCoords.setAxisValue(1, f8);
        pointerCoords.setAxisValue(11, f9);
        pointerCoords.setAxisValue(14, f10);
        pointerCoords.setAxisValue(15, f5);
        pointerCoords.setAxisValue(16, f6);
        MotionEvent.PointerCoords[] pointerCoordsArr = {pointerCoords};
        return getAndroidVersion() < 14 ? MotionEvent.obtain(j, j, 2, 1, new int[]{0}, pointerCoordsArr, 0, 0.0f, 0.0f, 65535, 0, 16777232, 0) : MotionEvent.obtain(j, j, 2, 1, pointerPropertiesArr, pointerCoordsArr, 0, 0, 0.0f, 0.0f, 65535, 0, 16777232, 0);
    }

    public static MotionEvent translateJoystick(MotionEvent motionEvent) {
        return translateJoystick(motionEvent.getX(), motionEvent.getY(), motionEvent.getXPrecision(), motionEvent.getYPrecision(), 0.0f, 0.0f, motionEvent.getDownTime());
    }

    public static KeyEvent translateKey(KeyEvent keyEvent, int i) {
        return new KeyEvent(keyEvent.getEventTime(), keyEvent.getEventTime(), keyEvent.getAction(), i, keyEvent.getRepeatCount(), keyEvent.getMetaState(), keyEvent.getDeviceId(), 0, 2);
    }

    public static MotionEvent translateMouse(float f, float f2, float f3, float f4, float f5, float f6, long j) {
        if (getAndroidVersion() < 12) {
            return null;
        }
        MotionEvent.PointerProperties[] pointerPropertiesArr = {new MotionEvent.PointerProperties()};
        pointerPropertiesArr[0].clear();
        pointerPropertiesArr[0].id = 0;
        pointerPropertiesArr[0].toolType = 3;
        float f7 = f - 128.0f;
        float f8 = 128.0f - f2;
        GlobalLog.e("LX: " + f7 + " LY: " + f8 + " RX: " + (f3 - 128.0f) + " RY:  " + (128.0f - f4));
        MotionEvent.PointerCoords pointerCoords = new MotionEvent.PointerCoords();
        pointerCoords.setAxisValue(0, 200.0f + f7);
        pointerCoords.setAxisValue(1, 400.0f + f8);
        MotionEvent.PointerCoords[] pointerCoordsArr = {pointerCoords};
        pointerCoordsArr[0].x = 200.0f + f7;
        pointerCoordsArr[0].y = 400.0f + f8;
        int[] iArr = {0};
        if (getAndroidVersion() < 14) {
            return MotionEvent.obtain(j, j, 2, 1, iArr, pointerCoordsArr, 0, 0.0f, 0.0f, 65535, 0, FragmentTransaction.TRANSIT_FRAGMENT_CLOSE, 0);
        }
        if (!isStart) {
            if (((int) f7) == 0 || ((int) f8) == 0) {
                return null;
            }
            isStart = true;
            return MotionEvent.obtain(j, j, 9, 1, pointerPropertiesArr, pointerCoordsArr, 0, 0, 0.0f, 0.0f, 8, 0, FragmentTransaction.TRANSIT_FRAGMENT_CLOSE, 0);
        }
        if (((int) f7) == 0 && ((int) f8) == 0) {
            isStart = false;
            return MotionEvent.obtain(j, j, 10, 1, pointerPropertiesArr, pointerCoordsArr, 0, 0, 0.0f, 0.0f, 8, 0, FragmentTransaction.TRANSIT_FRAGMENT_CLOSE, 0);
        }
        isStart = true;
        return MotionEvent.obtain(j, j, 7, 1, pointerPropertiesArr, pointerCoordsArr, 0, 0, 0.0f, 0.0f, 8, 0, FragmentTransaction.TRANSIT_FRAGMENT_CLOSE, 0);
    }

    public static MotionEvent translateTouch(int i, float f, float f2, long j) {
        float f3;
        float f4;
        if (getAndroidVersion() < 12) {
            return null;
        }
        MotionEvent.PointerProperties[] pointerPropertiesArr = {new MotionEvent.PointerProperties()};
        pointerPropertiesArr[0].clear();
        pointerPropertiesArr[0].id = 0;
        pointerPropertiesArr[0].toolType = 1;
        int i2 = (int) (f - 128.0f);
        int i3 = (int) (128.0f - f2);
        if (i == 0) {
            f3 = 300.0f + ((f - 128.0f) * 2.0f);
            f4 = ((128.0f - f2) * 2.0f) + 500.0f;
        } else {
            f3 = 900.0f + ((f - 128.0f) * 2.0f);
            f4 = ((128.0f - f2) * 2.0f) + 500.0f;
        }
        MotionEvent.PointerCoords[] pointerCoordsArr = {new MotionEvent.PointerCoords()};
        pointerCoordsArr[0].x = f3;
        pointerCoordsArr[0].y = f4;
        pointerCoordsArr[0].pressure = 0.66f;
        pointerCoordsArr[0].size = 0.5f;
        int[] iArr = {0};
        if (getAndroidVersion() < 14) {
            return MotionEvent.obtain(j, j, 2, 1, iArr, pointerCoordsArr, 0, 0.0f, 0.0f, 65535, 0, 4098, 0);
        }
        if (i == 0) {
            if (!c) {
                if (i2 == 0 && i3 == 0) {
                    GlobalLog.e("NULL LX: " + pointerCoordsArr[0].x + " LY: " + pointerCoordsArr[0].y);
                    return null;
                }
                c = true;
                GlobalLog.e("DOWN LX: " + pointerCoordsArr[0].x + " LY: " + pointerCoordsArr[0].y);
                return MotionEvent.obtain(j, j, 0, 1, pointerPropertiesArr, pointerCoordsArr, 0, 0, 0.0f, 0.0f, 8, 0, 4098, 0);
            }
            if (i2 == 0 && i3 == 0) {
                GlobalLog.e("UP LX: " + pointerCoordsArr[0].x + " LY: " + pointerCoordsArr[0].y);
                c = false;
                return MotionEvent.obtain(j, j, 1, 1, pointerPropertiesArr, pointerCoordsArr, 0, 0, 0.0f, 0.0f, 8, 0, 4098, 0);
            }
            c = true;
            GlobalLog.e("MOVE LX: " + pointerCoordsArr[0].x + " LY: " + pointerCoordsArr[0].y);
            return MotionEvent.obtain(j, j, 2, 1, pointerPropertiesArr, pointerCoordsArr, 0, 0, 0.0f, 0.0f, 8, 0, 4098, 0);
        }
        if (!d) {
            if (i2 == 0 && i3 == 0) {
                GlobalLog.e("NULL LX: " + pointerCoordsArr[0].x + " LY: " + pointerCoordsArr[0].y);
                return null;
            }
            d = true;
            GlobalLog.e("DOWN LX: " + pointerCoordsArr[0].x + " LY: " + pointerCoordsArr[0].y);
            return MotionEvent.obtain(j, j, 0, 1, pointerPropertiesArr, pointerCoordsArr, 0, 0, 0.0f, 0.0f, 8, 0, 4098, 0);
        }
        if (i2 == 0 && i3 == 0) {
            GlobalLog.e("UP LX: " + pointerCoordsArr[0].x + " LY: " + pointerCoordsArr[0].y);
            d = false;
            return MotionEvent.obtain(j, j, 1, 1, pointerPropertiesArr, pointerCoordsArr, 0, 0, 0.0f, 0.0f, 8, 0, 4098, 0);
        }
        d = true;
        GlobalLog.e("MOVE LX: " + pointerCoordsArr[0].x + " LY: " + pointerCoordsArr[0].y);
        return MotionEvent.obtain(j, j, 2, 1, pointerPropertiesArr, pointerCoordsArr, 0, 0, 0.0f, 0.0f, 8, 0, 4098, 0);
    }

    public static MotionEvent[] translateTouch(float f, float f2, float f3, float f4, long j) {
        if (getAndroidVersion() < 12) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        MotionEvent motionEventTranslateTouch = translateTouch(0, f, f2, j);
        if (motionEventTranslateTouch != null) {
            arrayList.add(motionEventTranslateTouch);
        }
        MotionEvent motionEventTranslateTouch2 = translateTouch(1, f3, f4, j);
        if (motionEventTranslateTouch2 != null) {
            arrayList.add(motionEventTranslateTouch2);
        }
        return (MotionEvent[]) arrayList.toArray(new MotionEvent[arrayList.size()]);
    }
}
