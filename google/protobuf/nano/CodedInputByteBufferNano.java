package com.google.protobuf.nano;

import com.nibiru.lib.controller.ExchangeUnit;

/* JADX INFO: loaded from: classes.dex */
public final class CodedInputByteBufferNano {
    private static final int DEFAULT_RECURSION_LIMIT = 64;
    private static final int DEFAULT_SIZE_LIMIT = 67108864;
    private final byte[] buffer;
    private int bufferPos;
    private int bufferSize;
    private int bufferSizeAfterLimit;
    private int bufferStart;
    private int lastTag;
    private int recursionDepth;
    private int currentLimit = Integer.MAX_VALUE;
    private int recursionLimit = 64;
    private int sizeLimit = DEFAULT_SIZE_LIMIT;

    private CodedInputByteBufferNano(byte[] bArr, int i, int i2) {
        this.buffer = bArr;
        this.bufferStart = i;
        this.bufferSize = i + i2;
        this.bufferPos = i;
    }

    public static int decodeZigZag32(int i) {
        return (i >>> 1) ^ (-(i & 1));
    }

    public static long decodeZigZag64(long j) {
        return (j >>> 1) ^ (-(1 & j));
    }

    public static CodedInputByteBufferNano newInstance(byte[] bArr) {
        return newInstance(bArr, 0, bArr.length);
    }

    public static CodedInputByteBufferNano newInstance(byte[] bArr, int i, int i2) {
        return new CodedInputByteBufferNano(bArr, i, i2);
    }

    private void recomputeBufferSizeAfterLimit() {
        this.bufferSize += this.bufferSizeAfterLimit;
        int i = this.bufferSize;
        if (i <= this.currentLimit) {
            this.bufferSizeAfterLimit = 0;
        } else {
            this.bufferSizeAfterLimit = i - this.currentLimit;
            this.bufferSize -= this.bufferSizeAfterLimit;
        }
    }

    public final void checkLastTagWas(int i) {
        if (this.lastTag != i) {
            throw InvalidProtocolBufferNanoException.invalidEndTag();
        }
    }

    public final int getBytesUntilLimit() {
        if (this.currentLimit == Integer.MAX_VALUE) {
            return -1;
        }
        return this.currentLimit - this.bufferPos;
    }

    public final byte[] getData(int i, int i2) {
        if (i2 == 0) {
            return WireFormatNano.EMPTY_BYTES;
        }
        byte[] bArr = new byte[i2];
        System.arraycopy(this.buffer, this.bufferStart + i, bArr, 0, i2);
        return bArr;
    }

    public final int getPosition() {
        return this.bufferPos - this.bufferStart;
    }

    public final boolean isAtEnd() {
        return this.bufferPos == this.bufferSize;
    }

    public final void popLimit(int i) {
        this.currentLimit = i;
        recomputeBufferSizeAfterLimit();
    }

    public final int pushLimit(int i) throws InvalidProtocolBufferNanoException {
        if (i < 0) {
            throw InvalidProtocolBufferNanoException.negativeSize();
        }
        int i2 = this.bufferPos + i;
        int i3 = this.currentLimit;
        if (i2 > i3) {
            throw InvalidProtocolBufferNanoException.truncatedMessage();
        }
        this.currentLimit = i2;
        recomputeBufferSizeAfterLimit();
        return i3;
    }

    public final boolean readBool() {
        return readRawVarint32() != 0;
    }

    public final byte[] readBytes() throws InvalidProtocolBufferNanoException {
        int rawVarint32 = readRawVarint32();
        if (rawVarint32 > this.bufferSize - this.bufferPos || rawVarint32 <= 0) {
            return readRawBytes(rawVarint32);
        }
        byte[] bArr = new byte[rawVarint32];
        System.arraycopy(this.buffer, this.bufferPos, bArr, 0, rawVarint32);
        this.bufferPos = rawVarint32 + this.bufferPos;
        return bArr;
    }

    public final double readDouble() {
        return Double.longBitsToDouble(readRawLittleEndian64());
    }

    public final int readEnum() {
        return readRawVarint32();
    }

    public final int readFixed32() {
        return readRawLittleEndian32();
    }

    public final long readFixed64() {
        return readRawLittleEndian64();
    }

    public final float readFloat() {
        return Float.intBitsToFloat(readRawLittleEndian32());
    }

    public final void readGroup(MessageNano messageNano, int i) throws InvalidProtocolBufferNanoException {
        if (this.recursionDepth >= this.recursionLimit) {
            throw InvalidProtocolBufferNanoException.recursionLimitExceeded();
        }
        this.recursionDepth++;
        messageNano.mergeFrom(this);
        checkLastTagWas(WireFormatNano.makeTag(i, 4));
        this.recursionDepth--;
    }

    public final int readInt32() {
        return readRawVarint32();
    }

    public final long readInt64() {
        return readRawVarint64();
    }

    public final void readMessage(MessageNano messageNano) throws InvalidProtocolBufferNanoException {
        int rawVarint32 = readRawVarint32();
        if (this.recursionDepth >= this.recursionLimit) {
            throw InvalidProtocolBufferNanoException.recursionLimitExceeded();
        }
        int iPushLimit = pushLimit(rawVarint32);
        this.recursionDepth++;
        messageNano.mergeFrom(this);
        checkLastTagWas(0);
        this.recursionDepth--;
        popLimit(iPushLimit);
    }

    public final byte readRawByte() throws InvalidProtocolBufferNanoException {
        if (this.bufferPos == this.bufferSize) {
            throw InvalidProtocolBufferNanoException.truncatedMessage();
        }
        byte[] bArr = this.buffer;
        int i = this.bufferPos;
        this.bufferPos = i + 1;
        return bArr[i];
    }

    public final byte[] readRawBytes(int i) throws InvalidProtocolBufferNanoException {
        if (i < 0) {
            throw InvalidProtocolBufferNanoException.negativeSize();
        }
        if (this.bufferPos + i > this.currentLimit) {
            skipRawBytes(this.currentLimit - this.bufferPos);
            throw InvalidProtocolBufferNanoException.truncatedMessage();
        }
        if (i > this.bufferSize - this.bufferPos) {
            throw InvalidProtocolBufferNanoException.truncatedMessage();
        }
        byte[] bArr = new byte[i];
        System.arraycopy(this.buffer, this.bufferPos, bArr, 0, i);
        this.bufferPos += i;
        return bArr;
    }

    public final int readRawLittleEndian32() throws InvalidProtocolBufferNanoException {
        return (readRawByte() & 255) | ((readRawByte() & 255) << 8) | ((readRawByte() & 255) << 16) | ((readRawByte() & 255) << 24);
    }

    public final long readRawLittleEndian64() throws InvalidProtocolBufferNanoException {
        byte rawByte = readRawByte();
        return ((((long) readRawByte()) & 255) << 8) | (((long) rawByte) & 255) | ((((long) readRawByte()) & 255) << 16) | ((((long) readRawByte()) & 255) << 24) | ((((long) readRawByte()) & 255) << 32) | ((((long) readRawByte()) & 255) << 40) | ((((long) readRawByte()) & 255) << 48) | ((((long) readRawByte()) & 255) << 56);
    }

    public final int readRawVarint32() throws InvalidProtocolBufferNanoException {
        byte rawByte = readRawByte();
        if (rawByte >= 0) {
            return rawByte;
        }
        int i = rawByte & 127;
        byte rawByte2 = readRawByte();
        if (rawByte2 >= 0) {
            return i | (rawByte2 << 7);
        }
        int i2 = i | ((rawByte2 & 127) << 7);
        byte rawByte3 = readRawByte();
        if (rawByte3 >= 0) {
            return i2 | (rawByte3 << 14);
        }
        int i3 = i2 | ((rawByte3 & 127) << 14);
        byte rawByte4 = readRawByte();
        if (rawByte4 >= 0) {
            return i3 | (rawByte4 << 21);
        }
        int i4 = i3 | ((rawByte4 & 127) << 21);
        byte rawByte5 = readRawByte();
        int i5 = i4 | (rawByte5 << 28);
        if (rawByte5 >= 0) {
            return i5;
        }
        for (int i6 = 0; i6 < 5; i6++) {
            if (readRawByte() >= 0) {
                return i5;
            }
        }
        throw InvalidProtocolBufferNanoException.malformedVarint();
    }

    public final long readRawVarint64() throws InvalidProtocolBufferNanoException {
        long j = 0;
        for (int i = 0; i < 64; i += 7) {
            byte rawByte = readRawByte();
            j |= ((long) (rawByte & 127)) << i;
            if ((rawByte & 128) == 0) {
                return j;
            }
        }
        throw InvalidProtocolBufferNanoException.malformedVarint();
    }

    public final int readSFixed32() {
        return readRawLittleEndian32();
    }

    public final long readSFixed64() {
        return readRawLittleEndian64();
    }

    public final int readSInt32() {
        return decodeZigZag32(readRawVarint32());
    }

    public final long readSInt64() {
        return decodeZigZag64(readRawVarint64());
    }

    public final String readString() throws InvalidProtocolBufferNanoException {
        int rawVarint32 = readRawVarint32();
        if (rawVarint32 > this.bufferSize - this.bufferPos || rawVarint32 <= 0) {
            return new String(readRawBytes(rawVarint32), ExchangeUnit.CHARSET);
        }
        String str = new String(this.buffer, this.bufferPos, rawVarint32, ExchangeUnit.CHARSET);
        this.bufferPos = rawVarint32 + this.bufferPos;
        return str;
    }

    public final int readTag() throws InvalidProtocolBufferNanoException {
        if (isAtEnd()) {
            this.lastTag = 0;
            return 0;
        }
        this.lastTag = readRawVarint32();
        if (this.lastTag == 0) {
            throw InvalidProtocolBufferNanoException.invalidTag();
        }
        return this.lastTag;
    }

    public final int readUInt32() {
        return readRawVarint32();
    }

    public final long readUInt64() {
        return readRawVarint64();
    }

    public final void resetSizeCounter() {
    }

    public final void rewindToPosition(int i) {
        if (i > this.bufferPos - this.bufferStart) {
            throw new IllegalArgumentException("Position " + i + " is beyond current " + (this.bufferPos - this.bufferStart));
        }
        if (i < 0) {
            throw new IllegalArgumentException("Bad position " + i);
        }
        this.bufferPos = this.bufferStart + i;
    }

    public final int setRecursionLimit(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Recursion limit cannot be negative: " + i);
        }
        int i2 = this.recursionLimit;
        this.recursionLimit = i;
        return i2;
    }

    public final int setSizeLimit(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Size limit cannot be negative: " + i);
        }
        int i2 = this.sizeLimit;
        this.sizeLimit = i;
        return i2;
    }

    public final boolean skipField(int i) {
        switch (WireFormatNano.getTagWireType(i)) {
            case 0:
                readInt32();
                return true;
            case 1:
                readRawLittleEndian64();
                return true;
            case 2:
                skipRawBytes(readRawVarint32());
                return true;
            case 3:
                skipMessage();
                checkLastTagWas(WireFormatNano.makeTag(WireFormatNano.getTagFieldNumber(i), 4));
                return true;
            case 4:
                return false;
            case 5:
                readRawLittleEndian32();
                return true;
            default:
                throw InvalidProtocolBufferNanoException.invalidWireType();
        }
    }

    public final void skipMessage() throws InvalidProtocolBufferNanoException {
        int tag;
        do {
            tag = readTag();
            if (tag == 0) {
                return;
            }
        } while (skipField(tag));
    }

    public final void skipRawBytes(int i) throws InvalidProtocolBufferNanoException {
        if (i < 0) {
            throw InvalidProtocolBufferNanoException.negativeSize();
        }
        if (this.bufferPos + i > this.currentLimit) {
            skipRawBytes(this.currentLimit - this.bufferPos);
            throw InvalidProtocolBufferNanoException.truncatedMessage();
        }
        if (i > this.bufferSize - this.bufferPos) {
            throw InvalidProtocolBufferNanoException.truncatedMessage();
        }
        this.bufferPos += i;
    }
}
