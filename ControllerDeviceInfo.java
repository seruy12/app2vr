package com.nibiru.lib;

import android.os.Bundle;
import android.text.TextUtils;
import com.nibiru.lib.controller.C0013a;

/* JADX INFO: loaded from: classes.dex */
public class ControllerDeviceInfo extends C0013a {
    public static final int DRIVER_TYPE_AIMOKE = 8;
    public static final int DRIVER_TYPE_BODY = 19;
    public static final int DRIVER_TYPE_BT_HAN = 1;
    public static final int DRIVER_TYPE_BT_RDA = 3;
    public static final int DRIVER_TYPE_CUBAND = 14;
    public static final int DRIVER_TYPE_GENERIC = 4;
    public static final int DRIVER_TYPE_IWOWN = 10;
    public static final int DRIVER_TYPE_JMDM = 20;
    public static final int DRIVER_TYPE_KCT = 12;
    public static final int DRIVER_TYPE_KCT_2502 = 22;
    public static final int DRIVER_TYPE_LETV = 13;
    public static final int DRIVER_TYPE_MOGA = 5;
    public static final int DRIVER_TYPE_MOJING = 17;
    public static final int DRIVER_TYPE_MOTIONRING = 21;
    public static final int DRIVER_TYPE_MYO = 18;
    public static final int DRIVER_TYPE_NOD = 11;
    public static final int DRIVER_TYPE_NORMAL = 0;
    public static final int DRIVER_TYPE_OBANGLE = 7;
    public static final int DRIVER_TYPE_PCDEVICE = 200;
    public static final int DRIVER_TYPE_PCHOST = 100;
    public static final int DRIVER_TYPE_ROUTECH = 16;
    public static final int DRIVER_TYPE_USB_24G = 6;
    public static final int DRIVER_TYPE_WIPACE = 15;
    public static final int DRIVER_TYPE_YCX = 9;
    private String deviceAddr;
    private String deviceName;
    private int e;
    private int f;
    private int g;
    private int[] h;
    private long i;
    public boolean isFromScan;
    private long j;
    private int k;
    private int l;
    private int m;
    private boolean n;
    private String o;
    private int type;

    public ControllerDeviceInfo() {
        this.n = false;
        this.isFromScan = true;
    }

    public ControllerDeviceInfo(Bundle bundle) {
        super(bundle);
        this.n = false;
        this.isFromScan = true;
        this.deviceName = bundle.getString("deviceName");
        this.deviceAddr = bundle.getString("deviceAddr");
        this.e = bundle.getInt("source");
        this.f = bundle.getInt("driverType");
        this.type = bundle.getInt("type");
        this.g = bundle.getInt("keyMapId");
        this.h = bundle.getIntArray("keyMap");
        this.i = bundle.getLong("pid");
        this.j = bundle.getLong("vid");
        this.k = bundle.getInt("endpointIn");
        this.l = bundle.getInt("endpointOut");
        this.m = bundle.getInt("interfaceNum");
        this.n = bundle.getBoolean("auth");
        this.o = bundle.getString("realDeviceName");
    }

    public static ControllerDeviceInfo getGoogleDeviceInfo() {
        ControllerDeviceInfo controllerDeviceInfo = new ControllerDeviceInfo();
        int[] iArr = new int[256];
        iArr[21] = 21;
        iArr[22] = 22;
        iArr[19] = 19;
        iArr[20] = 20;
        iArr[99] = 98;
        iArr[100] = 96;
        iArr[96] = 99;
        iArr[97] = 97;
        iArr[109] = 109;
        iArr[108] = 108;
        iArr[102] = 102;
        iArr[104] = 104;
        iArr[103] = 103;
        iArr[105] = 105;
        iArr[106] = 106;
        iArr[107] = 107;
        iArr[66] = 23;
        iArr[23] = 23;
        controllerDeviceInfo.setDeviceName("Google Device");
        controllerDeviceInfo.setDeviceAddr("");
        controllerDeviceInfo.setDriverType(4);
        controllerDeviceInfo.setKeyMap(iArr);
        return controllerDeviceInfo;
    }

    public void addKeyPair(int i, int i2) {
        if (this.h != null && i >= 0 && i <= this.h.length - 1) {
            this.h[i] = i2;
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            ControllerDeviceInfo controllerDeviceInfo = (ControllerDeviceInfo) obj;
            if (this.f == controllerDeviceInfo.f && this.f == 6) {
                return this.i == controllerDeviceInfo.i && this.j == controllerDeviceInfo.j && TextUtils.equals(this.deviceName, controllerDeviceInfo.deviceName);
            }
            if (this.deviceName == null) {
                if (controllerDeviceInfo.deviceName != null) {
                    return false;
                }
            } else if (!this.deviceName.equals(controllerDeviceInfo.deviceName)) {
                return false;
            }
            return this.f == controllerDeviceInfo.f && this.g == controllerDeviceInfo.g && this.type == controllerDeviceInfo.type;
        }
        return false;
    }

    public String getDeviceAddr() {
        return this.deviceAddr;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public int getDriverType() {
        return this.f;
    }

    public int getEndpointIn() {
        return this.k;
    }

    public int getEndpointOut() {
        return this.l;
    }

    public int getInterfaceNum() {
        return this.m;
    }

    public int[] getKeyMap() {
        return this.h;
    }

    public int getKeyMapId() {
        return this.g;
    }

    public long getPid() {
        return this.i;
    }

    public String getRealDeviceName() {
        return this.o;
    }

    public int getSource() {
        return this.e;
    }

    public int getTransKey(int i, int i2) {
        if (i2 < 0 || i2 > 255) {
            return -1;
        }
        if (this.h != null) {
            return this.h[i2];
        }
        this.h = new int[512];
        return 0;
    }

    public int getType() {
        return this.type;
    }

    public long getVid() {
        return this.j;
    }

    public int hashCode() {
        return (((((((this.deviceName == null ? 0 : this.deviceName.hashCode()) + 31) * 31) + this.f) * 31) + this.g) * 31) + this.type;
    }

    public boolean isAuth() {
        return this.n;
    }

    public boolean matchAddr(String str) {
        return this.deviceAddr != null && str != null && this.deviceAddr.length() >= 4 && str.startsWith(this.deviceAddr);
    }

    public void setAuth(boolean z) {
        this.n = z;
        setBoolean("auth", this.n);
    }

    public void setDeviceAddr(String str) {
        this.deviceAddr = str;
        setString("deviceAddr", str);
    }

    public void setDeviceName(String str) {
        this.deviceName = str;
        setString("deviceName", str);
    }

    public void setDriverType(int i) {
        this.f = i;
        setInt("driverType", i);
    }

    public void setEndpointIn(int i) {
        this.k = i;
        setLong("endpointIn", i);
    }

    public void setEndpointOut(int i) {
        this.l = i;
        setLong("endpointOut", i);
    }

    public void setInterfaceNum(int i) {
        this.m = i;
        setLong("interfaceNum", i);
    }

    public void setKeyMap(int[] iArr) {
        this.h = iArr;
        if (this.data != null) {
            this.data.putIntArray("keyMap", iArr);
        }
    }

    public void setKeyMapId(int i) {
        this.g = i;
        setInt("keyMapId", i);
    }

    public void setPid(long j) {
        this.i = j;
        setLong("pid", j);
    }

    public void setRealDeviceName(String str) {
        this.o = str;
        setString("realDeviceName", str);
    }

    public void setSource(int i) {
        this.e = i;
        setInt("source", i);
    }

    public void setType(int i) {
        this.type = i;
        setInt("type", i);
    }

    public void setVid(long j) {
        this.j = j;
        setLong("vid", j);
    }

    public String toString() {
        return "ControllerDeviceInfo [deviceName=" + this.deviceName + ", deviceAddr=" + this.deviceAddr + ", source=" + this.e + ", driverType=" + this.f + ", type=" + this.type + ", keyMapId=" + this.g + ", pid=" + this.i + ", vid=" + this.j + ", endpointIn=" + this.k + ", endpointOut=" + this.l + ", interfaceNum=" + this.m + "]";
    }
}
