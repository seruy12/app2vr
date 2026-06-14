package com.google.protobuf.nano;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;

/* JADX INFO: loaded from: classes.dex */
public final class CodedOutputByteBufferNano {
    public static final int LITTLE_ENDIAN_32_SIZE = 4;
    public static final int LITTLE_ENDIAN_64_SIZE = 8;
    private static final int MAX_UTF8_EXPANSION = 3;
    private final ByteBuffer buffer;

    public class OutOfSpaceException extends IOException {
        private static final long serialVersionUID = -6947486886997889499L;

        OutOfSpaceException(int i, int i2) {
            super("CodedOutputStream was writing to a flat byte array and ran out of space (pos " + i + " limit " + i2 + ").");
        }
    }

    private CodedOutputByteBufferNano(ByteBuffer byteBuffer) {
        this.buffer = byteBuffer;
    }

    private CodedOutputByteBufferNano(byte[] bArr, int i, int i2) {
        this(ByteBuffer.wrap(bArr, i, i2));
    }

    public static int computeBoolSize(int i, boolean z) {
        return computeTagSize(i) + computeBoolSizeNoTag(z);
    }

    public static int computeBoolSizeNoTag(boolean z) {
        return 1;
    }

    public static int computeBytesSize(int i, byte[] bArr) {
        return computeTagSize(i) + computeBytesSizeNoTag(bArr);
    }

    public static int computeBytesSizeNoTag(byte[] bArr) {
        return computeRawVarint32Size(bArr.length) + bArr.length;
    }

    public static int computeDoubleSize(int i, double d) {
        return computeTagSize(i) + computeDoubleSizeNoTag(d);
    }

    public static int computeDoubleSizeNoTag(double d) {
        return 8;
    }

    public static int computeEnumSize(int i, int i2) {
        return computeTagSize(i) + computeEnumSizeNoTag(i2);
    }

    public static int computeEnumSizeNoTag(int i) {
        return computeRawVarint32Size(i);
    }

    public static int computeFixed32Size(int i, int i2) {
        return computeTagSize(i) + computeFixed32SizeNoTag(i2);
    }

    public static int computeFixed32SizeNoTag(int i) {
        return 4;
    }

    public static int computeFixed64Size(int i, long j) {
        return computeTagSize(i) + computeFixed64SizeNoTag(j);
    }

    public static int computeFixed64SizeNoTag(long j) {
        return 8;
    }

    public static int computeFloatSize(int i, float f) {
        return computeTagSize(i) + computeFloatSizeNoTag(f);
    }

    public static int computeFloatSizeNoTag(float f) {
        return 4;
    }

    public static int computeGroupSize(int i, MessageNano messageNano) {
        return (computeTagSize(i) * 2) + computeGroupSizeNoTag(messageNano);
    }

    public static int computeGroupSizeNoTag(MessageNano messageNano) {
        return messageNano.getSerializedSize();
    }

    public static int computeInt32Size(int i, int i2) {
        return computeTagSize(i) + computeInt32SizeNoTag(i2);
    }

    public static int computeInt32SizeNoTag(int i) {
        if (i >= 0) {
            return computeRawVarint32Size(i);
        }
        return 10;
    }

    public static int computeInt64Size(int i, long j) {
        return computeTagSize(i) + computeInt64SizeNoTag(j);
    }

    public static int computeInt64SizeNoTag(long j) {
        return computeRawVarint64Size(j);
    }

    public static int computeMessageSize(int i, MessageNano messageNano) {
        return computeTagSize(i) + computeMessageSizeNoTag(messageNano);
    }

    public static int computeMessageSizeNoTag(MessageNano messageNano) {
        int serializedSize = messageNano.getSerializedSize();
        return serializedSize + computeRawVarint32Size(serializedSize);
    }

    public static int computeRawVarint32Size(int i) {
        if ((i & (-128)) == 0) {
            return 1;
        }
        if ((i & (-16384)) == 0) {
            return 2;
        }
        if (((-2097152) & i) == 0) {
            return 3;
        }
        return ((-268435456) & i) == 0 ? 4 : 5;
    }

    public static int computeRawVarint64Size(long j) {
        if (((-128) & j) == 0) {
            return 1;
        }
        if (((-16384) & j) == 0) {
            return 2;
        }
        if (((-2097152) & j) == 0) {
            return 3;
        }
        if (((-268435456) & j) == 0) {
            return 4;
        }
        if (((-34359738368L) & j) == 0) {
            return 5;
        }
        if (((-4398046511104L) & j) == 0) {
            return 6;
        }
        if (((-562949953421312L) & j) == 0) {
            return 7;
        }
        if (((-72057594037927936L) & j) == 0) {
            return 8;
        }
        return (Long.MIN_VALUE & j) == 0 ? 9 : 10;
    }

    public static int computeSFixed32Size(int i, int i2) {
        return computeTagSize(i) + computeSFixed32SizeNoTag(i2);
    }

    public static int computeSFixed32SizeNoTag(int i) {
        return 4;
    }

    public static int computeSFixed64Size(int i, long j) {
        return computeTagSize(i) + computeSFixed64SizeNoTag(j);
    }

    public static int computeSFixed64SizeNoTag(long j) {
        return 8;
    }

    public static int computeSInt32Size(int i, int i2) {
        return computeTagSize(i) + computeSInt32SizeNoTag(i2);
    }

    public static int computeSInt32SizeNoTag(int i) {
        return computeRawVarint32Size(encodeZigZag32(i));
    }

    public static int computeSInt64Size(int i, long j) {
        return computeTagSize(i) + computeSInt64SizeNoTag(j);
    }

    public static int computeSInt64SizeNoTag(long j) {
        return computeRawVarint64Size(encodeZigZag64(j));
    }

    public static int computeStringSize(int i, String str) {
        return computeTagSize(i) + computeStringSizeNoTag(str);
    }

    public static int computeStringSizeNoTag(String str) {
        int iEncodedLength = encodedLength(str);
        return iEncodedLength + computeRawVarint32Size(iEncodedLength);
    }

    public static int computeTagSize(int i) {
        return computeRawVarint32Size(WireFormatNano.makeTag(i, 0));
    }

    public static int computeUInt32Size(int i, int i2) {
        return computeTagSize(i) + computeUInt32SizeNoTag(i2);
    }

    public static int computeUInt32SizeNoTag(int i) {
        return computeRawVarint32Size(i);
    }

    public static int computeUInt64Size(int i, long j) {
        return computeTagSize(i) + computeUInt64SizeNoTag(j);
    }

    public static int computeUInt64SizeNoTag(long j) {
        return computeRawVarint64Size(j);
    }

    private static int encode(CharSequence charSequence, byte[] bArr, int i, int i2) {
        int i3;
        int length = charSequence.length();
        int i4 = 0;
        int i5 = i + i2;
        while (i4 < length && i4 + i < i5) {
            char cCharAt = charSequence.charAt(i4);
            if (cCharAt >= 128) {
                break;
            }
            bArr[i + i4] = (byte) cCharAt;
            i4++;
        }
        if (i4 == length) {
            return i + length;
        }
        int i6 = i + i4;
        while (i4 < length) {
            char cCharAt2 = charSequence.charAt(i4);
            if (cCharAt2 < 128 && i6 < i5) {
                i3 = i6 + 1;
                bArr[i6] = (byte) cCharAt2;
            } else if (cCharAt2 < 2048 && i6 <= i5 - 2) {
                int i7 = i6 + 1;
                bArr[i6] = (byte) ((cCharAt2 >>> 6) | 960);
                i3 = i7 + 1;
                bArr[i7] = (byte) ((cCharAt2 & '?') | 128);
            } else {
                if ((cCharAt2 >= 55296 && 57343 >= cCharAt2) || i6 > i5 - 3) {
                    if (i6 > i5 - 4) {
                        throw new ArrayIndexOutOfBoundsException("Failed writing " + cCharAt2 + " at index " + i6);
                    }
                    if (i4 + 1 != charSequence.length()) {
                        i4++;
                        char cCharAt3 = charSequence.charAt(i4);
                        if (Character.isSurrogatePair(cCharAt2, cCharAt3)) {
                            int codePoint = Character.toCodePoint(cCharAt2, cCharAt3);
                            int i8 = i6 + 1;
                            bArr[i6] = (byte) ((codePoint >>> 18) | 240);
                            int i9 = i8 + 1;
                            bArr[i8] = (byte) (((codePoint >>> 12) & 63) | 128);
                            int i10 = i9 + 1;
                            bArr[i9] = (byte) (((codePoint >>> 6) & 63) | 128);
                            i3 = i10 + 1;
                            bArr[i10] = (byte) ((codePoint & 63) | 128);
                        }
                    }
                    throw new IllegalArgumentException("Unpaired surrogate at index " + (i4 - 1));
                }
                int i11 = i6 + 1;
                bArr[i6] = (byte) ((cCharAt2 >>> '\f') | 480);
                int i12 = i11 + 1;
                bArr[i11] = (byte) (((cCharAt2 >>> 6) & 63) | 128);
                i3 = i12 + 1;
                bArr[i12] = (byte) ((cCharAt2 & '?') | 128);
            }
            i4++;
            i6 = i3;
        }
        return i6;
    }

    private static void encode(CharSequence charSequence, ByteBuffer byteBuffer) {
        if (byteBuffer.isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        if (!byteBuffer.hasArray()) {
            encodeDirect(charSequence, byteBuffer);
            return;
        }
        try {
            byteBuffer.position(encode(charSequence, byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining()) - byteBuffer.arrayOffset());
        } catch (ArrayIndexOutOfBoundsException e) {
            BufferOverflowException bufferOverflowException = new BufferOverflowException();
            bufferOverflowException.initCause(e);
            throw bufferOverflowException;
        }
    }

    private static void encodeDirect(CharSequence charSequence, ByteBuffer byteBuffer) {
        int length = charSequence.length();
        int i = 0;
        while (i < length) {
            char cCharAt = charSequence.charAt(i);
            if (cCharAt < 128) {
                byteBuffer.put((byte) cCharAt);
            } else if (cCharAt < 2048) {
                byteBuffer.put((byte) ((cCharAt >>> 6) | 960));
                byteBuffer.put((byte) ((cCharAt & '?') | 128));
            } else {
                if (cCharAt >= 55296 && 57343 >= cCharAt) {
                    if (i + 1 != charSequence.length()) {
                        i++;
                        char cCharAt2 = charSequence.charAt(i);
                        if (Character.isSurrogatePair(cCharAt, cCharAt2)) {
                            int codePoint = Character.toCodePoint(cCharAt, cCharAt2);
                            byteBuffer.put((byte) ((codePoint >>> 18) | 240));
                            byteBuffer.put((byte) (((codePoint >>> 12) & 63) | 128));
                            byteBuffer.put((byte) (((codePoint >>> 6) & 63) | 128));
                            byteBuffer.put((byte) ((codePoint & 63) | 128));
                        }
                    }
                    throw new IllegalArgumentException("Unpaired surrogate at index " + (i - 1));
                }
                byteBuffer.put((byte) ((cCharAt >>> '\f') | 480));
                byteBuffer.put((byte) (((cCharAt >>> 6) & 63) | 128));
                byteBuffer.put((byte) ((cCharAt & '?') | 128));
            }
            i++;
        }
    }

    public static int encodeZigZag32(int i) {
        return (i << 1) ^ (i >> 31);
    }

    public static long encodeZigZag64(long j) {
        return (j << 1) ^ (j >> 63);
    }

    private static int encodedLength(CharSequence charSequence) {
        int length = charSequence.length();
        int i = 0;
        while (i < length && charSequence.charAt(i) < 128) {
            i++;
        }
        int i2 = i;
        int iEncodedLengthGeneral = length;
        while (true) {
            if (i2 < length) {
                char cCharAt = charSequence.charAt(i2);
                if (cCharAt >= 2048) {
                    iEncodedLengthGeneral += encodedLengthGeneral(charSequence, i2);
                    break;
                }
                i2++;
                iEncodedLengthGeneral = ((127 - cCharAt) >>> 31) + iEncodedLengthGeneral;
            } else {
                break;
            }
        }
        if (iEncodedLengthGeneral < length) {
            throw new IllegalArgumentException("UTF-8 length does not fit in int: " + (((long) iEncodedLengthGeneral) + 4294967296L));
        }
        return iEncodedLengthGeneral;
    }

    private static int encodedLengthGeneral(CharSequence charSequence, int i) {
        int length = charSequence.length();
        int i2 = 0;
        int i3 = i;
        while (i3 < length) {
            char cCharAt = charSequence.charAt(i3);
            if (cCharAt < 2048) {
                i2 += (127 - cCharAt) >>> 31;
            } else {
                i2 += 2;
                if (55296 <= cCharAt && cCharAt <= 57343) {
                    if (Character.codePointAt(charSequence, i3) < 65536) {
                        throw new IllegalArgumentException("Unpaired surrogate at index " + i3);
                    }
                    i3++;
                }
            }
            i3++;
        }
        return i2;
    }

    public static CodedOutputByteBufferNano newInstance(byte[] bArr) {
        return newInstance(bArr, 0, bArr.length);
    }

    public static CodedOutputByteBufferNano newInstance(byte[] bArr, int i, int i2) {
        return new CodedOutputByteBufferNano(bArr, i, i2);
    }

    public final void checkNoSpaceLeft() {
        if (spaceLeft() != 0) {
            throw new IllegalStateException("Did not write as much data as expected.");
        }
    }

    public final int position() {
        return this.buffer.position();
    }

    public final void reset() {
        this.buffer.clear();
    }

    public final int spaceLeft() {
        return this.buffer.remaining();
    }

    public final void writeBool(int i, boolean z) throws OutOfSpaceException {
        writeTag(i, 0);
        writeBoolNoTag(z);
    }

    public final void writeBoolNoTag(boolean z) throws OutOfSpaceException {
        writeRawByte((byte) (z ? 1 : 0));
    }

    public final void writeBytes(int i, byte[] bArr) throws OutOfSpaceException {
        writeTag(i, 2);
        writeBytesNoTag(bArr);
    }

    public final void writeBytesNoTag(byte[] bArr) throws OutOfSpaceException {
        writeRawVarint32(bArr.length);
        writeRawBytes(bArr);
    }

    public final void writeDouble(int i, double d) throws OutOfSpaceException {
        writeTag(i, 1);
        writeDoubleNoTag(d);
    }

    public final void writeDoubleNoTag(double d) throws OutOfSpaceException {
        writeRawLittleEndian64(Double.doubleToLongBits(d));
    }

    public final void writeEnum(int i, int i2) throws OutOfSpaceException {
        writeTag(i, 0);
        writeRawVarint32(i2);
    }

    public final void writeEnumNoTag(int i) throws OutOfSpaceException {
        writeRawVarint32(i);
    }

    public final void writeFixed32(int i, int i2) throws OutOfSpaceException {
        writeTag(i, 5);
        writeRawLittleEndian32(i2);
    }

    public final void writeFixed32NoTag(int i) throws OutOfSpaceException {
        writeRawLittleEndian32(i);
    }

    public final void writeFixed64(int i, long j) throws OutOfSpaceException {
        writeTag(i, 1);
        writeRawLittleEndian64(j);
    }

    public final void writeFixed64NoTag(long j) throws OutOfSpaceException {
        writeRawLittleEndian64(j);
    }

    public final void writeFloat(int i, float f) throws OutOfSpaceException {
        writeTag(i, 5);
        writeFloatNoTag(f);
    }

    public final void writeFloatNoTag(float f) throws OutOfSpaceException {
        writeRawLittleEndian32(Float.floatToIntBits(f));
    }

    public final void writeGroup(int i, MessageNano messageNano) throws OutOfSpaceException {
        writeTag(i, 3);
        messageNano.writeTo(this);
        writeTag(i, 4);
    }

    public final void writeGroupNoTag(MessageNano messageNano) {
        messageNano.writeTo(this);
    }

    public final void writeInt32(int i, int i2) throws OutOfSpaceException {
        writeTag(i, 0);
        writeInt32NoTag(i2);
    }

    public final void writeInt32NoTag(int i) throws OutOfSpaceException {
        if (i >= 0) {
            writeRawVarint32(i);
        } else {
            writeRawVarint64(i);
        }
    }

    public final void writeInt64(int i, long j) throws OutOfSpaceException {
        writeTag(i, 0);
        writeRawVarint64(j);
    }

    public final void writeInt64NoTag(long j) throws OutOfSpaceException {
        writeRawVarint64(j);
    }

    public final void writeMessage(int i, MessageNano messageNano) throws OutOfSpaceException {
        writeTag(i, 2);
        writeMessageNoTag(messageNano);
    }

    public final void writeMessageNoTag(MessageNano messageNano) throws OutOfSpaceException {
        writeRawVarint32(messageNano.getCachedSize());
        messageNano.writeTo(this);
    }

    public final void writeRawByte(byte b) throws OutOfSpaceException {
        if (!this.buffer.hasRemaining()) {
            throw new OutOfSpaceException(this.buffer.position(), this.buffer.limit());
        }
        this.buffer.put(b);
    }

    public final void writeRawByte(int i) throws OutOfSpaceException {
        writeRawByte((byte) i);
    }

    public final void writeRawBytes(byte[] bArr) throws OutOfSpaceException {
        writeRawBytes(bArr, 0, bArr.length);
    }

    public final void writeRawBytes(byte[] bArr, int i, int i2) throws OutOfSpaceException {
        if (this.buffer.remaining() < i2) {
            throw new OutOfSpaceException(this.buffer.position(), this.buffer.limit());
        }
        this.buffer.put(bArr, i, i2);
    }

    public final void writeRawLittleEndian32(int i) throws OutOfSpaceException {
        writeRawByte((byte) (i & 255));
        writeRawByte((byte) ((i >> 8) & 255));
        writeRawByte((byte) ((i >> 16) & 255));
        writeRawByte((byte) ((i >> 24) & 255));
    }

    public final void writeRawLittleEndian64(long j) throws OutOfSpaceException {
        writeRawByte((byte) (((int) j) & 255));
        writeRawByte((byte) (((int) (j >> 8)) & 255));
        writeRawByte((byte) (((int) (j >> 16)) & 255));
        writeRawByte((byte) (((int) (j >> 24)) & 255));
        writeRawByte((byte) (((int) (j >> 32)) & 255));
        writeRawByte((byte) (((int) (j >> 40)) & 255));
        writeRawByte((byte) (((int) (j >> 48)) & 255));
        writeRawByte((byte) (((int) (j >> 56)) & 255));
    }

    public final void writeRawVarint32(int i) throws OutOfSpaceException {
        while ((i & (-128)) != 0) {
            writeRawByte((byte) ((i & 127) | 128));
            i >>>= 7;
        }
        writeRawByte((byte) i);
    }

    public final void writeRawVarint64(long j) throws OutOfSpaceException {
        while (((-128) & j) != 0) {
            writeRawByte((byte) ((((int) j) & 127) | 128));
            j >>>= 7;
        }
        writeRawByte((byte) j);
    }

    public final void writeSFixed32(int i, int i2) throws OutOfSpaceException {
        writeTag(i, 5);
        writeRawLittleEndian32(i2);
    }

    public final void writeSFixed32NoTag(int i) throws OutOfSpaceException {
        writeRawLittleEndian32(i);
    }

    public final void writeSFixed64(int i, long j) throws OutOfSpaceException {
        writeTag(i, 1);
        writeRawLittleEndian64(j);
    }

    public final void writeSFixed64NoTag(long j) throws OutOfSpaceException {
        writeRawLittleEndian64(j);
    }

    public final void writeSInt32(int i, int i2) throws OutOfSpaceException {
        writeTag(i, 0);
        writeSInt32NoTag(i2);
    }

    public final void writeSInt32NoTag(int i) throws OutOfSpaceException {
        writeRawVarint32(encodeZigZag32(i));
    }

    public final void writeSInt64(int i, long j) throws OutOfSpaceException {
        writeTag(i, 0);
        writeSInt64NoTag(j);
    }

    public final void writeSInt64NoTag(long j) throws OutOfSpaceException {
        writeRawVarint64(encodeZigZag64(j));
    }

    public final void writeString(int i, String str) throws OutOfSpaceException {
        writeTag(i, 2);
        writeStringNoTag(str);
    }

    public final void writeStringNoTag(String str) throws OutOfSpaceException {
        try {
            int iComputeRawVarint32Size = computeRawVarint32Size(str.length());
            if (iComputeRawVarint32Size != computeRawVarint32Size(str.length() * 3)) {
                writeRawVarint32(encodedLength(str));
                encode(str, this.buffer);
                return;
            }
            int iPosition = this.buffer.position();
            this.buffer.position(iPosition + iComputeRawVarint32Size);
            encode(str, this.buffer);
            int iPosition2 = this.buffer.position();
            this.buffer.position(iPosition);
            writeRawVarint32((iPosition2 - iPosition) - iComputeRawVarint32Size);
            this.buffer.position(iPosition2);
        } catch (BufferOverflowException e) {
            throw new OutOfSpaceException(this.buffer.position(), this.buffer.limit());
        }
    }

    public final void writeTag(int i, int i2) throws OutOfSpaceException {
        writeRawVarint32(WireFormatNano.makeTag(i, i2));
    }

    public final void writeUInt32(int i, int i2) throws OutOfSpaceException {
        writeTag(i, 0);
        writeRawVarint32(i2);
    }

    public final void writeUInt32NoTag(int i) throws OutOfSpaceException {
        writeRawVarint32(i);
    }

    public final void writeUInt64(int i, long j) throws OutOfSpaceException {
        writeTag(i, 0);
        writeRawVarint64(j);
    }

    public final void writeUInt64NoTag(long j) throws OutOfSpaceException {
        writeRawVarint64(j);
    }
}
