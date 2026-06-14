package com.nibiru.lib;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.nibiru.lib.controller.C0013a;

/* JADX INFO: loaded from: classes.dex */
public class DriverDef extends C0013a implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: com.nibiru.lib.DriverDef.1
        @Override // android.os.Parcelable.Creator
        public final /* synthetic */ Object createFromParcel(Parcel parcel) {
            return new DriverDef(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
            return new DriverDef[i];
        }
    };
    private int deviceType;
    private int f;
    private String p;

    public DriverDef(int i, int i2, String str) {
        this.f = i;
        this.deviceType = i2;
        this.p = str;
        setInt("driverType", i);
        setString("driverName", str);
        setInt("deviceType", i2);
    }

    public DriverDef(Bundle bundle) {
        super(bundle);
        this.f = bundle.getInt("driverType");
        this.deviceType = bundle.getInt("deviceType");
        this.p = bundle.getString("driverName");
    }

    public DriverDef(Parcel parcel) {
        this.f = parcel.readInt();
        this.deviceType = parcel.readInt();
        this.p = parcel.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getDeviceType() {
        return this.deviceType;
    }

    public String getDriverName() {
        return this.p;
    }

    public int getDriverType() {
        return this.f;
    }

    public void setDeviceType(int i) {
        setInt("deviceType", i);
        this.deviceType = i;
    }

    public void setDriverName(String str) {
        setString("driverName", str);
        this.p = str;
    }

    public void setDriverType(int i) {
        setInt("driverType", i);
        this.f = i;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.f);
        parcel.writeInt(this.deviceType);
        parcel.writeString(this.p);
    }
}
