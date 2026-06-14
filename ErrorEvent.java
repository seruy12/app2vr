package com.nibiru.lib;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class ErrorEvent implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.nibiru.lib.ErrorEvent.1
        @Override // android.os.Parcelable.Creator
        public final /* synthetic */ Object createFromParcel(Parcel parcel) {
            return new ErrorEvent(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
            return new ErrorEvent[i];
        }
    };
    private int deviceId;
    protected String errorFull;
    protected String errorShort;

    public ErrorEvent() {
    }

    public ErrorEvent(Parcel parcel) {
        this.errorShort = parcel.readString();
        this.errorFull = parcel.readString();
        this.deviceId = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getDeviceId() {
        return this.deviceId;
    }

    public String getErrorFull() {
        return this.errorFull;
    }

    public String getErrorShort() {
        return this.errorShort;
    }

    public void setDeviceId(int i) {
        this.deviceId = i;
    }

    public void setErrorFull(String str) {
        this.errorFull = str;
    }

    public void setErrorShort(String str) {
        this.errorShort = str;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.errorShort);
        parcel.writeString(this.errorFull);
        parcel.writeInt(this.deviceId);
    }
}
