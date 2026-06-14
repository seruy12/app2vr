package com.google.protobuf.nano;

import com.google.protobuf.nano.CodedOutputByteBufferNano;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
class FieldData implements Cloneable {
    private Extension cachedExtension;
    private List unknownFieldData;
    private Object value;

    FieldData() {
        this.unknownFieldData = new ArrayList();
    }

    FieldData(Extension extension, Object obj) {
        this.cachedExtension = extension;
        this.value = obj;
    }

    private byte[] toByteArray() throws CodedOutputByteBufferNano.OutOfSpaceException {
        byte[] bArr = new byte[computeSerializedSize()];
        writeTo(CodedOutputByteBufferNano.newInstance(bArr));
        return bArr;
    }

    void addUnknownField(UnknownFieldData unknownFieldData) {
        this.unknownFieldData.add(unknownFieldData);
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public final FieldData m3clone() {
        FieldData fieldData = new FieldData();
        try {
            fieldData.cachedExtension = this.cachedExtension;
            if (this.unknownFieldData == null) {
                fieldData.unknownFieldData = null;
            } else {
                fieldData.unknownFieldData.addAll(this.unknownFieldData);
            }
            if (this.value != null) {
                if (this.value instanceof MessageNano) {
                    fieldData.value = ((MessageNano) this.value).mo1clone();
                } else if (this.value instanceof byte[]) {
                    fieldData.value = ((byte[]) this.value).clone();
                } else if (this.value instanceof byte[][]) {
                    byte[][] bArr = (byte[][]) this.value;
                    byte[][] bArr2 = new byte[bArr.length][];
                    fieldData.value = bArr2;
                    for (int i = 0; i < bArr.length; i++) {
                        bArr2[i] = (byte[]) bArr[i].clone();
                    }
                } else if (this.value instanceof boolean[]) {
                    fieldData.value = ((boolean[]) this.value).clone();
                } else if (this.value instanceof int[]) {
                    fieldData.value = ((int[]) this.value).clone();
                } else if (this.value instanceof long[]) {
                    fieldData.value = ((long[]) this.value).clone();
                } else if (this.value instanceof float[]) {
                    fieldData.value = ((float[]) this.value).clone();
                } else if (this.value instanceof double[]) {
                    fieldData.value = ((double[]) this.value).clone();
                } else if (this.value instanceof MessageNano[]) {
                    MessageNano[] messageNanoArr = (MessageNano[]) this.value;
                    MessageNano[] messageNanoArr2 = new MessageNano[messageNanoArr.length];
                    fieldData.value = messageNanoArr2;
                    for (int i2 = 0; i2 < messageNanoArr.length; i2++) {
                        messageNanoArr2[i2] = messageNanoArr[i2].mo1clone();
                    }
                }
            }
            return fieldData;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    int computeSerializedSize() {
        int iComputeSerializedSize = 0;
        if (this.value != null) {
            return this.cachedExtension.computeSerializedSize(this.value);
        }
        Iterator it = this.unknownFieldData.iterator();
        while (true) {
            int i = iComputeSerializedSize;
            if (!it.hasNext()) {
                return i;
            }
            iComputeSerializedSize = ((UnknownFieldData) it.next()).computeSerializedSize() + i;
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FieldData)) {
            return false;
        }
        FieldData fieldData = (FieldData) obj;
        if (this.value != null && fieldData.value != null) {
            if (this.cachedExtension == fieldData.cachedExtension) {
                return !this.cachedExtension.clazz.isArray() ? this.value.equals(fieldData.value) : this.value instanceof byte[] ? Arrays.equals((byte[]) this.value, (byte[]) fieldData.value) : this.value instanceof int[] ? Arrays.equals((int[]) this.value, (int[]) fieldData.value) : this.value instanceof long[] ? Arrays.equals((long[]) this.value, (long[]) fieldData.value) : this.value instanceof float[] ? Arrays.equals((float[]) this.value, (float[]) fieldData.value) : this.value instanceof double[] ? Arrays.equals((double[]) this.value, (double[]) fieldData.value) : this.value instanceof boolean[] ? Arrays.equals((boolean[]) this.value, (boolean[]) fieldData.value) : Arrays.deepEquals((Object[]) this.value, (Object[]) fieldData.value);
            }
            return false;
        }
        if (this.unknownFieldData != null && fieldData.unknownFieldData != null) {
            return this.unknownFieldData.equals(fieldData.unknownFieldData);
        }
        try {
            return Arrays.equals(toByteArray(), fieldData.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    UnknownFieldData getUnknownField(int i) {
        if (this.unknownFieldData != null && i < this.unknownFieldData.size()) {
            return (UnknownFieldData) this.unknownFieldData.get(i);
        }
        return null;
    }

    int getUnknownFieldSize() {
        if (this.unknownFieldData == null) {
            return 0;
        }
        return this.unknownFieldData.size();
    }

    Object getValue(Extension extension) {
        if (this.value == null) {
            this.cachedExtension = extension;
            this.value = extension.getValueFrom(this.unknownFieldData);
            this.unknownFieldData = null;
        } else if (this.cachedExtension != extension) {
            throw new IllegalStateException("Tried to getExtension with a differernt Extension.");
        }
        return this.value;
    }

    public int hashCode() {
        try {
            return Arrays.hashCode(toByteArray()) + 527;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    void setValue(Extension extension, Object obj) {
        this.cachedExtension = extension;
        this.value = obj;
        this.unknownFieldData = null;
    }

    void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws CodedOutputByteBufferNano.OutOfSpaceException {
        if (this.value != null) {
            this.cachedExtension.writeTo(this.value, codedOutputByteBufferNano);
            return;
        }
        Iterator it = this.unknownFieldData.iterator();
        while (it.hasNext()) {
            ((UnknownFieldData) it.next()).writeTo(codedOutputByteBufferNano);
        }
    }
}
