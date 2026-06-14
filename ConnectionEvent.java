package com.nibiru.lib;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class ConnectionEvent implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.nibiru.lib.ConnectionEvent.1
        @Override // android.os.Parcelable.Creator
        public final /* synthetic */ Object createFromParcel(Parcel parcel) {
            return new ConnectionEvent(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
            return new ConnectionEvent[i];
        }
    };
    protected BTDevice device;
    protected int state;

    public ConnectionEvent() {
    }

    public ConnectionEvent(int i, BTDevice bTDevice) {
        this.state = i;
        this.device = bTDevice;
    }

    public ConnectionEvent(Parcel parcel) {
        this.state = parcel.readInt();
        this.device = new BTDevice(parcel);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public BTDevice getDevice() {
        return this.device;
    }

    public int getState() {
        return this.state;
    }

    public void setDevice(BTDevice bTDevice) {
        this.device = bTDevice;
    }

    public void setState(int i) {
        this.state = i;
    }

    public String toString() {
        return "ConnectionEvent [device=" + this.device + ", state=" + this.state + "]";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.state);
        this.device.writeToParcel(parcel, i);
    }
}
