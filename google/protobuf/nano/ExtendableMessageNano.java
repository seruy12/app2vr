package com.google.protobuf.nano;

import com.google.protobuf.nano.CodedOutputByteBufferNano;

/* JADX INFO: loaded from: classes.dex */
public abstract class ExtendableMessageNano extends MessageNano {
    protected FieldArray unknownFieldData;

    @Override // com.google.protobuf.nano.MessageNano
    /* JADX INFO: renamed from: clone */
    public ExtendableMessageNano mo1clone() {
        ExtendableMessageNano extendableMessageNano = (ExtendableMessageNano) super.mo1clone();
        InternalNano.cloneUnknownFieldData(this, extendableMessageNano);
        return extendableMessageNano;
    }

    @Override // com.google.protobuf.nano.MessageNano
    protected int computeSerializedSize() {
        if (this.unknownFieldData == null) {
            return 0;
        }
        int iComputeSerializedSize = 0;
        for (int i = 0; i < this.unknownFieldData.size(); i++) {
            iComputeSerializedSize += this.unknownFieldData.dataAt(i).computeSerializedSize();
        }
        return iComputeSerializedSize;
    }

    public final Object getExtension(Extension extension) {
        FieldData fieldData;
        if (this.unknownFieldData == null || (fieldData = this.unknownFieldData.get(WireFormatNano.getTagFieldNumber(extension.tag))) == null) {
            return null;
        }
        return fieldData.getValue(extension);
    }

    public final boolean hasExtension(Extension extension) {
        return (this.unknownFieldData == null || this.unknownFieldData.get(WireFormatNano.getTagFieldNumber(extension.tag)) == null) ? false : true;
    }

    public final ExtendableMessageNano setExtension(Extension extension, Object obj) {
        FieldData fieldData = null;
        int tagFieldNumber = WireFormatNano.getTagFieldNumber(extension.tag);
        if (obj != null) {
            if (this.unknownFieldData == null) {
                this.unknownFieldData = new FieldArray();
            } else {
                fieldData = this.unknownFieldData.get(tagFieldNumber);
            }
            if (fieldData == null) {
                this.unknownFieldData.put(tagFieldNumber, new FieldData(extension, obj));
            } else {
                fieldData.setValue(extension, obj);
            }
        } else if (this.unknownFieldData != null) {
            this.unknownFieldData.remove(tagFieldNumber);
            if (this.unknownFieldData.isEmpty()) {
                this.unknownFieldData = null;
            }
        }
        return this;
    }

    protected final boolean storeUnknownField(CodedInputByteBufferNano codedInputByteBufferNano, int i) {
        int position = codedInputByteBufferNano.getPosition();
        if (!codedInputByteBufferNano.skipField(i)) {
            return false;
        }
        int tagFieldNumber = WireFormatNano.getTagFieldNumber(i);
        UnknownFieldData unknownFieldData = new UnknownFieldData(i, codedInputByteBufferNano.getData(position, codedInputByteBufferNano.getPosition() - position));
        FieldData fieldData = null;
        if (this.unknownFieldData == null) {
            this.unknownFieldData = new FieldArray();
        } else {
            fieldData = this.unknownFieldData.get(tagFieldNumber);
        }
        if (fieldData == null) {
            fieldData = new FieldData();
            this.unknownFieldData.put(tagFieldNumber, fieldData);
        }
        fieldData.addUnknownField(unknownFieldData);
        return true;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws CodedOutputByteBufferNano.OutOfSpaceException {
        if (this.unknownFieldData == null) {
            return;
        }
        for (int i = 0; i < this.unknownFieldData.size(); i++) {
            this.unknownFieldData.dataAt(i).writeTo(codedOutputByteBufferNano);
        }
    }
}
