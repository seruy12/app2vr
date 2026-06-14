package com.google.protobuf.nano.android;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import java.lang.reflect.Array;

/* JADX INFO: loaded from: classes.dex */
public final class ParcelableMessageNanoCreator implements Parcelable.Creator {
    private static final String TAG = "PMNCreator";
    private final Class mClazz;

    public ParcelableMessageNanoCreator(Class cls) {
        this.mClazz = cls;
    }

    static void writeToParcel(Class cls, MessageNano messageNano, Parcel parcel) {
        parcel.writeString(cls.getName());
        parcel.writeByteArray(MessageNano.toByteArray(messageNano));
    }

    @Override // android.os.Parcelable.Creator
    public final MessageNano createFromParcel(Parcel parcel) {
        MessageNano messageNano;
        InstantiationException e;
        IllegalAccessException e2;
        ClassNotFoundException e3;
        InvalidProtocolBufferNanoException e4;
        String string = parcel.readString();
        byte[] bArrCreateByteArray = parcel.createByteArray();
        try {
            messageNano = (MessageNano) Class.forName(string).newInstance();
            try {
                MessageNano.mergeFrom(messageNano, bArrCreateByteArray);
            } catch (InvalidProtocolBufferNanoException e5) {
                e4 = e5;
                Log.e(TAG, "Exception trying to create proto from parcel", e4);
            } catch (ClassNotFoundException e6) {
                e3 = e6;
                Log.e(TAG, "Exception trying to create proto from parcel", e3);
            } catch (IllegalAccessException e7) {
                e2 = e7;
                Log.e(TAG, "Exception trying to create proto from parcel", e2);
            } catch (InstantiationException e8) {
                e = e8;
                Log.e(TAG, "Exception trying to create proto from parcel", e);
            }
        } catch (InvalidProtocolBufferNanoException e9) {
            messageNano = null;
            e4 = e9;
        } catch (ClassNotFoundException e10) {
            messageNano = null;
            e3 = e10;
        } catch (IllegalAccessException e11) {
            messageNano = null;
            e2 = e11;
        } catch (InstantiationException e12) {
            messageNano = null;
            e = e12;
        }
        return messageNano;
    }

    @Override // android.os.Parcelable.Creator
    public final MessageNano[] newArray(int i) {
        return (MessageNano[]) Array.newInstance((Class<?>) this.mClazz, i);
    }
}
