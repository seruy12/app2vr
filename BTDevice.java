package com.nibiru.lib;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.view.InputDevice;
import com.nibiru.lib.controller.ControllerKeyEvent;
import com.nibiru.lib.controller.ControllerService;
import com.nibiru.lib.controller.Feature;
import com.nibiru.lib.controller.MotionSenseEvent;
import com.nibiru.lib.controller.StickEvent;
import java.io.Serializable;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class BTDevice implements Parcelable, Serializable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.nibiru.lib.BTDevice.1
        @Override // android.os.Parcelable.Creator
        public final /* synthetic */ Object createFromParcel(Parcel parcel) {
            return new BTDevice(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
            return new BTDevice[i];
        }
    };
    public static final int GESTURE_DPAD_MODE = 1;
    public static final int GESTURE_NIBIRU_MODE = 0;
    public static final int GESTURE_SWING_MODE = 2;
    public static final int STATE_CONN = 1;
    public static final int STATE_CONNING = 0;
    public static final int STATE_DISCONN = 3;
    public static final int STATE_DISCONNING = 2;
    public static final int STATE_STOP = 4;
    protected static final long serialVersionUID = 4473651087026692626L;
    private BluetoothDevice a;
    protected int[] accArray;
    private boolean b;
    protected long connectTime;
    protected String deviceAddr;
    protected int deviceId;
    protected String deviceName;
    protected int deviceSource;
    protected int deviceType;
    protected long features;
    protected int gestureMode;
    protected int[] gycArray;
    protected boolean isAllowReConn;
    protected int isAutoConnect;
    protected boolean isConnected;
    protected boolean isExternal;
    protected boolean isSupportAcc;
    protected boolean isSupportGyro;
    protected int lastHatKey;
    protected ControllerKeyEvent lastKeyEvent;
    protected StickEvent lastStickEvent;
    protected ControllerDeviceInfo mDeviceInfo;
    protected int playerOrder;
    protected int state;
    public String tag;
    protected ControllerService.WEAR wearable;
    protected ControllerService.POSE wearpose;

    public BTDevice() {
        this.deviceName = "No Device";
        this.deviceId = -1;
        this.deviceType = -1;
        this.deviceSource = 0;
        this.playerOrder = 0;
        this.deviceAddr = "0:0:0:0:0:0";
        this.isConnected = false;
        this.connectTime = 0L;
        this.state = 3;
        this.isExternal = false;
        this.features = 0L;
        this.isAllowReConn = true;
        this.wearpose = ControllerService.POSE.UNKNOWN;
        this.wearable = ControllerService.WEAR.UNKNOWN;
        this.isSupportAcc = false;
        this.isSupportGyro = false;
        this.gestureMode = 2;
        this.isAutoConnect = 0;
        this.lastStickEvent = null;
        this.lastKeyEvent = null;
        this.lastHatKey = 0;
        this.b = false;
    }

    public BTDevice(BluetoothDevice bluetoothDevice) {
        this.deviceName = "No Device";
        this.deviceId = -1;
        this.deviceType = -1;
        this.deviceSource = 0;
        this.playerOrder = 0;
        this.deviceAddr = "0:0:0:0:0:0";
        this.isConnected = false;
        this.connectTime = 0L;
        this.state = 3;
        this.isExternal = false;
        this.features = 0L;
        this.isAllowReConn = true;
        this.wearpose = ControllerService.POSE.UNKNOWN;
        this.wearable = ControllerService.WEAR.UNKNOWN;
        this.isSupportAcc = false;
        this.isSupportGyro = false;
        this.gestureMode = 2;
        this.isAutoConnect = 0;
        this.lastStickEvent = null;
        this.lastKeyEvent = null;
        this.lastHatKey = 0;
        this.b = false;
        this.deviceAddr = bluetoothDevice.getAddress();
        this.deviceName = bluetoothDevice.getName();
        if (this.deviceName == null || this.deviceName.length() == 0) {
            this.deviceName = "Unknown Device";
        }
        this.state = 3;
        this.a = bluetoothDevice;
    }

    public BTDevice(Bundle bundle) {
        this.deviceName = "No Device";
        this.deviceId = -1;
        this.deviceType = -1;
        this.deviceSource = 0;
        this.playerOrder = 0;
        this.deviceAddr = "0:0:0:0:0:0";
        this.isConnected = false;
        this.connectTime = 0L;
        this.state = 3;
        this.isExternal = false;
        this.features = 0L;
        this.isAllowReConn = true;
        this.wearpose = ControllerService.POSE.UNKNOWN;
        this.wearable = ControllerService.WEAR.UNKNOWN;
        this.isSupportAcc = false;
        this.isSupportGyro = false;
        this.gestureMode = 2;
        this.isAutoConnect = 0;
        this.lastStickEvent = null;
        this.lastKeyEvent = null;
        this.lastHatKey = 0;
        this.b = false;
        if (bundle == null) {
            return;
        }
        this.deviceName = bundle.getString("device_name");
        this.deviceId = bundle.getInt("device_id");
        this.deviceType = bundle.getInt("device_type");
        this.deviceSource = bundle.getInt("device_source");
        this.playerOrder = bundle.getInt(MotionSenseEvent.KEY_PLAYER);
        this.deviceAddr = bundle.getString("addr");
        this.isConnected = bundle.getBoolean("is_connected");
        this.connectTime = bundle.getLong("connect_time");
        this.state = bundle.getInt("state");
        this.isExternal = bundle.getBoolean("is_external");
        this.features = bundle.getLong("feature");
        this.isSupportAcc = bundle.getBoolean("is_support_acc");
        this.isSupportGyro = bundle.getBoolean("is_support_gyro");
        this.accArray = bundle.getIntArray("acc_array");
        this.gycArray = bundle.getIntArray("gyc_array");
        this.gestureMode = bundle.getInt("gesture_mode");
        this.isAutoConnect = bundle.getInt("is_auto_connect");
        this.wearpose = ControllerService.POSE.values()[bundle.getInt("wearpose")];
        this.wearable = ControllerService.WEAR.values()[bundle.getInt("wearable")];
        this.tag = bundle.getString("tag");
    }

    public BTDevice(Parcel parcel) {
        this.deviceName = "No Device";
        this.deviceId = -1;
        this.deviceType = -1;
        this.deviceSource = 0;
        this.playerOrder = 0;
        this.deviceAddr = "0:0:0:0:0:0";
        this.isConnected = false;
        this.connectTime = 0L;
        this.state = 3;
        this.isExternal = false;
        this.features = 0L;
        this.isAllowReConn = true;
        this.wearpose = ControllerService.POSE.UNKNOWN;
        this.wearable = ControllerService.WEAR.UNKNOWN;
        this.isSupportAcc = false;
        this.isSupportGyro = false;
        this.gestureMode = 2;
        this.isAutoConnect = 0;
        this.lastStickEvent = null;
        this.lastKeyEvent = null;
        this.lastHatKey = 0;
        this.b = false;
        setConnectTime(parcel.readLong());
        setDeviceAddr(parcel.readString());
        setDeviceId(parcel.readInt());
        setDeviceName(parcel.readString());
        setDeviceType(parcel.readInt());
        setConnected(parcel.readInt() == 1);
        setPlayerOrder(parcel.readInt());
        setState(parcel.readInt());
        setExternal(parcel.readInt() == 1);
        setFeatures(parcel.readInt());
        setSupportAcc(parcel.readInt() == 1);
        setSupportGyro(parcel.readInt() == 1);
    }

    public BTDevice(InputDevice inputDevice) {
        this.deviceName = "No Device";
        this.deviceId = -1;
        this.deviceType = -1;
        this.deviceSource = 0;
        this.playerOrder = 0;
        this.deviceAddr = "0:0:0:0:0:0";
        this.isConnected = false;
        this.connectTime = 0L;
        this.state = 3;
        this.isExternal = false;
        this.features = 0L;
        this.isAllowReConn = true;
        this.wearpose = ControllerService.POSE.UNKNOWN;
        this.wearable = ControllerService.WEAR.UNKNOWN;
        this.isSupportAcc = false;
        this.isSupportGyro = false;
        this.gestureMode = 2;
        this.isAutoConnect = 0;
        this.lastStickEvent = null;
        this.lastKeyEvent = null;
        this.lastHatKey = 0;
        this.b = false;
        this.isExternal = true;
        this.isConnected = true;
        this.state = 1;
        this.deviceId = inputDevice.getId();
        this.deviceName = inputDevice.getName();
        this.deviceType = 0;
        this.connectTime = SystemClock.uptimeMillis();
        this.deviceAddr = "gen:" + inputDevice.getName() + ":" + inputDevice.getId();
    }

    public BTDevice(BTDevice bTDevice) {
        this.deviceName = "No Device";
        this.deviceId = -1;
        this.deviceType = -1;
        this.deviceSource = 0;
        this.playerOrder = 0;
        this.deviceAddr = "0:0:0:0:0:0";
        this.isConnected = false;
        this.connectTime = 0L;
        this.state = 3;
        this.isExternal = false;
        this.features = 0L;
        this.isAllowReConn = true;
        this.wearpose = ControllerService.POSE.UNKNOWN;
        this.wearable = ControllerService.WEAR.UNKNOWN;
        this.isSupportAcc = false;
        this.isSupportGyro = false;
        this.gestureMode = 2;
        this.isAutoConnect = 0;
        this.lastStickEvent = null;
        this.lastKeyEvent = null;
        this.lastHatKey = 0;
        this.b = false;
        this.isExternal = bTDevice.isExternal;
        this.isConnected = bTDevice.isConnected;
        this.state = bTDevice.state;
        this.deviceId = bTDevice.deviceId;
        this.deviceName = bTDevice.deviceName;
        this.deviceType = bTDevice.deviceType;
        this.connectTime = bTDevice.connectTime;
        this.playerOrder = bTDevice.playerOrder;
        this.deviceAddr = bTDevice.deviceAddr;
        this.deviceSource = bTDevice.getDeviceSource();
        this.a = bTDevice.getBluetoothDevice();
        this.wearable = bTDevice.getWearState();
        this.wearpose = bTDevice.getWearPose();
        this.features = bTDevice.getFeatures();
        this.isAllowReConn = bTDevice.isAllowReConn;
        this.tag = bTDevice.tag;
    }

    public BTDevice(String str) {
        this.deviceName = "No Device";
        this.deviceId = -1;
        this.deviceType = -1;
        this.deviceSource = 0;
        this.playerOrder = 0;
        this.deviceAddr = "0:0:0:0:0:0";
        this.isConnected = false;
        this.connectTime = 0L;
        this.state = 3;
        this.isExternal = false;
        this.features = 0L;
        this.isAllowReConn = true;
        this.wearpose = ControllerService.POSE.UNKNOWN;
        this.wearable = ControllerService.WEAR.UNKNOWN;
        this.isSupportAcc = false;
        this.isSupportGyro = false;
        this.gestureMode = 2;
        this.isAutoConnect = 0;
        this.lastStickEvent = null;
        this.lastKeyEvent = null;
        this.lastHatKey = 0;
        this.b = false;
        this.deviceName = "device";
        this.deviceId = -1;
        this.deviceAddr = str;
        this.deviceType = -1;
        this.deviceSource = 0;
    }

    public BTDevice(String str, int i, int i2, String str2, boolean z, int i3, boolean z2) {
        this.deviceName = "No Device";
        this.deviceId = -1;
        this.deviceType = -1;
        this.deviceSource = 0;
        this.playerOrder = 0;
        this.deviceAddr = "0:0:0:0:0:0";
        this.isConnected = false;
        this.connectTime = 0L;
        this.state = 3;
        this.isExternal = false;
        this.features = 0L;
        this.isAllowReConn = true;
        this.wearpose = ControllerService.POSE.UNKNOWN;
        this.wearable = ControllerService.WEAR.UNKNOWN;
        this.isSupportAcc = false;
        this.isSupportGyro = false;
        this.gestureMode = 2;
        this.isAutoConnect = 0;
        this.lastStickEvent = null;
        this.lastKeyEvent = null;
        this.lastHatKey = 0;
        this.b = false;
        this.isExternal = z;
        this.state = i3;
        this.isConnected = z2;
        this.deviceName = str;
        this.deviceId = i;
        this.deviceType = i2;
        this.deviceAddr = str2;
    }

    public void addFeatures(int i) {
        this.features |= (long) i;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            BTDevice bTDevice = (BTDevice) obj;
            return this.deviceAddr == null ? bTDevice.deviceAddr == null : this.deviceAddr.equals(bTDevice.deviceAddr);
        }
        return false;
    }

    public int[] getAccMaxArray() {
        return this.accArray;
    }

    public int getAccXMax() {
        return this.accArray[0];
    }

    public int getAccYMax() {
        return this.accArray[1];
    }

    public int getAccZMax() {
        return this.accArray[2];
    }

    public BluetoothDevice getBluetoothDevice() {
        return this.a;
    }

    public Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("device_name", this.deviceName);
        bundle.putInt("device_id", this.deviceId);
        bundle.putInt("device_type", this.deviceType);
        bundle.putInt("device_source", this.deviceSource);
        bundle.putInt(MotionSenseEvent.KEY_PLAYER, this.playerOrder);
        bundle.putString("addr", this.deviceAddr);
        bundle.putBoolean("is_connected", this.isConnected);
        bundle.putLong("connect_time", this.connectTime);
        bundle.putInt("state", this.state);
        bundle.putBoolean("is_external", this.isExternal);
        bundle.putLong("feature", this.features);
        bundle.putBoolean("is_support_acc", this.isSupportAcc);
        bundle.putBoolean("is_support_gyro", this.isSupportGyro);
        bundle.putIntArray("acc_array", this.accArray);
        bundle.putIntArray("gyc_array", this.gycArray);
        bundle.putInt("gesture_mode", this.gestureMode);
        bundle.putInt("is_auto_connect", this.isAutoConnect);
        bundle.putInt("wearpose", this.wearpose.ordinal());
        bundle.putInt("wearable", this.wearable.ordinal());
        bundle.putString("tag", this.tag);
        return bundle;
    }

    public long getConnectTime() {
        return this.connectTime;
    }

    public String getDeviceAddr() {
        return this.deviceAddr;
    }

    public int getDeviceId() {
        return this.deviceId;
    }

    public ControllerDeviceInfo getDeviceInfo() {
        return this.mDeviceInfo;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public int getDeviceSource() {
        return this.deviceSource;
    }

    public int getDeviceType() {
        return this.deviceType;
    }

    public long getFeatures() {
        return this.features;
    }

    public int getGestureMode() {
        return this.gestureMode;
    }

    public int[] getGycMaxArray() {
        return this.gycArray;
    }

    public int getGycXMax() {
        return this.gycArray[0];
    }

    public int getGycYMax() {
        return this.gycArray[1];
    }

    public int getGycZMax() {
        return this.gycArray[2];
    }

    public int getIsAutoConnect() {
        return this.isAutoConnect;
    }

    public int getLastHatKey() {
        return this.lastHatKey;
    }

    public ControllerKeyEvent getLastKeyEvent() {
        return this.lastKeyEvent;
    }

    public StickEvent getLastStickEvent() {
        return this.lastStickEvent;
    }

    public int getPlayerOrder() {
        return this.playerOrder;
    }

    public int getState() {
        return this.state;
    }

    public ControllerService.POSE getWearPose() {
        return this.wearpose;
    }

    public ControllerService.WEAR getWearState() {
        return this.wearable;
    }

    public int hashCode() {
        return (this.deviceAddr == null ? 0 : this.deviceAddr.hashCode()) + 31;
    }

    public boolean isAllowReConn() {
        return this.isAllowReConn;
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    public boolean isExchangeVirtualDevice() {
        return this.deviceAddr != null && this.deviceAddr.startsWith("virtual:exvd");
    }

    public boolean isExternal() {
        return this.deviceAddr != null && (this.deviceAddr.startsWith("sys") || this.deviceAddr.startsWith("virtual") || this.deviceAddr.startsWith("gen"));
    }

    public boolean isHost() {
        return this.playerOrder == 1;
    }

    public boolean isSupportAcc() {
        return this.isSupportAcc;
    }

    public boolean isSupportGyro() {
        return this.isSupportGyro;
    }

    public boolean isValid() {
        if (this.isExternal) {
            return true;
        }
        return !this.deviceAddr.equals("0:0:0:0:0:0") && this.deviceType >= 0;
    }

    public boolean isVirtual() {
        return this.b;
    }

    public void recordConnectTime() {
        this.connectTime = System.currentTimeMillis();
    }

    public void removeFeatures(int i) {
        this.features &= (long) (i ^ (-1));
    }

    public void setAccMaxArray(int[] iArr) {
        this.accArray = iArr;
    }

    public void setAllowReConn(boolean z) {
        this.isAllowReConn = z;
    }

    public void setConnectTime(long j) {
        this.connectTime = j;
    }

    public void setConnected(boolean z) {
        this.isConnected = z;
    }

    public void setDeviceAddr(String str) {
        this.deviceAddr = str;
    }

    public void setDeviceId(int i) {
        this.deviceId = i;
    }

    public void setDeviceInfo(ControllerDeviceInfo controllerDeviceInfo) {
        this.mDeviceInfo = controllerDeviceInfo;
        if (controllerDeviceInfo != null) {
            this.deviceSource = controllerDeviceInfo.getSource();
        }
    }

    public void setDeviceName(String str) {
        this.deviceName = str;
    }

    public void setDeviceSource(int i) {
        this.deviceSource = i;
    }

    public void setDeviceType(int i) {
        this.deviceType = i;
    }

    public void setExternal(boolean z) {
        this.isExternal = z;
    }

    public void setFeatures(long j) {
        this.features = j;
    }

    public void setGestureMode(int i) {
        this.gestureMode = i;
    }

    public void setGycMaxArray(int[] iArr) {
        this.gycArray = iArr;
    }

    public void setIsAutoConnect(int i) {
        this.isAutoConnect = i;
    }

    public void setLastHatKey(int i) {
        this.lastHatKey = i;
    }

    public void setLastKeyEvent(ControllerKeyEvent controllerKeyEvent) {
        this.lastKeyEvent = controllerKeyEvent;
    }

    public void setLastStickEvent(StickEvent stickEvent) {
        this.lastStickEvent = stickEvent;
    }

    public void setPlayerOrder(int i) {
        this.playerOrder = i;
    }

    public void setState(int i) {
        this.state = i;
    }

    public void setSupportAcc(boolean z) {
        this.isSupportAcc = z;
        if (z) {
            this.features |= 1;
        } else {
            this.features &= -2;
        }
    }

    public void setSupportGyro(boolean z) {
        this.isSupportGyro = z;
        if (this.isSupportAcc) {
            this.features |= 2;
        } else {
            this.features &= -3;
        }
    }

    public void setVirtual(boolean z) {
        this.b = z;
    }

    public void setWearPose(ControllerService.POSE pose) {
        this.wearpose = pose;
    }

    public void setWearState(ControllerService.WEAR wear) {
        this.wearable = wear;
    }

    public String toString() {
        return "BTDevice [deviceName=" + this.deviceName + ", deviceId=" + this.deviceId + ", deviceType=" + this.deviceType + ", state=" + this.state + ", deviceSource=" + this.deviceSource + ", playerOrder=" + this.playerOrder + ", deviceAddr=" + this.deviceAddr + ", isConnected=" + this.isConnected + ", connectTime=" + this.connectTime + ", state=" + this.state + ", isExternal=" + this.isExternal + ", Feature: " + Feature.getSupportFeatureName(this.features) + ", isSupportAcc=" + this.isSupportAcc + ", isSupportGyro=" + this.isSupportGyro + ", lastStickEvent=" + this.lastStickEvent + ", lastKeyEvent=" + this.lastKeyEvent + ", mDeviceInfo=" + this.mDeviceInfo + ", accArray=" + Arrays.toString(this.accArray) + ", gycArray=" + Arrays.toString(this.gycArray) + ", Wear Pose: " + this.wearpose.name() + ", Wear State: " + this.wearable.name() + "]";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.connectTime);
        if (this.deviceAddr == null) {
            this.deviceAddr = "";
        }
        parcel.writeString(this.deviceAddr);
        parcel.writeInt(this.deviceId);
        if (this.deviceName == null) {
            this.deviceName = "Unknown";
        }
        parcel.writeString(this.deviceName);
        parcel.writeInt(this.deviceType);
        parcel.writeInt(this.isConnected ? 1 : 0);
        parcel.writeInt(this.playerOrder);
        parcel.writeInt(this.state);
        parcel.writeInt(this.isExternal ? 1 : 0);
        parcel.writeInt((int) this.features);
        parcel.writeInt(this.isSupportAcc ? 1 : 0);
        parcel.writeInt(this.isSupportGyro ? 1 : 0);
    }
}
