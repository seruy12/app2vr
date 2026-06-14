package com.google.protobuf.nano;

import java.io.IOException;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public abstract class MessageNano {
    protected volatile int cachedSize = -1;

    public static final MessageNano mergeFrom(MessageNano messageNano, byte[] bArr) {
        return mergeFrom(messageNano, bArr, 0, bArr.length);
    }

    public static final MessageNano mergeFrom(MessageNano messageNano, byte[] bArr, int i, int i2) throws InvalidProtocolBufferNanoException {
        try {
            CodedInputByteBufferNano codedInputByteBufferNanoNewInstance = CodedInputByteBufferNano.newInstance(bArr, i, i2);
            messageNano.mergeFrom(codedInputByteBufferNanoNewInstance);
            codedInputByteBufferNanoNewInstance.checkLastTagWas(0);
            return messageNano;
        } catch (InvalidProtocolBufferNanoException e) {
            throw e;
        } catch (IOException e2) {
            throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).");
        }
    }

    public static final boolean messageNanoEquals(MessageNano messageNano, MessageNano messageNano2) {
        int serializedSize;
        if (messageNano == messageNano2) {
            return true;
        }
        if (messageNano == null || messageNano2 == null || messageNano.getClass() != messageNano2.getClass() || messageNano2.getSerializedSize() != (serializedSize = messageNano.getSerializedSize())) {
            return false;
        }
        byte[] bArr = new byte[serializedSize];
        byte[] bArr2 = new byte[serializedSize];
        toByteArray(messageNano, bArr, 0, serializedSize);
        toByteArray(messageNano2, bArr2, 0, serializedSize);
        return Arrays.equals(bArr, bArr2);
    }

    public static final void toByteArray(MessageNano messageNano, byte[] bArr, int i, int i2) {
        try {
            CodedOutputByteBufferNano codedOutputByteBufferNanoNewInstance = CodedOutputByteBufferNano.newInstance(bArr, i, i2);
            messageNano.writeTo(codedOutputByteBufferNanoNewInstance);
            codedOutputByteBufferNanoNewInstance.checkNoSpaceLeft();
        } catch (IOException e) {
            throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", e);
        }
    }

    public static final byte[] toByteArray(MessageNano messageNano) {
        byte[] bArr = new byte[messageNano.getSerializedSize()];
        toByteArray(messageNano, bArr, 0, bArr.length);
        return bArr;
    }

    @Override // 
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public MessageNano mo1clone() {
        return (MessageNano) super.clone();
    }

    protected int computeSerializedSize() {
        return 0;
    }

    public int getCachedSize() {
        if (this.cachedSize < 0) {
            getSerializedSize();
        }
        return this.cachedSize;
    }

    public int getSerializedSize() {
        int iComputeSerializedSize = computeSerializedSize();
        this.cachedSize = iComputeSerializedSize;
        return iComputeSerializedSize;
    }

    public abstract MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano);

    public String toString() {
        return MessageNanoPrinter.print(this);
    }

    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) {
    }
}
