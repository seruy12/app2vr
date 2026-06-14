package com.b.a.b;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;

/* JADX INFO: loaded from: classes.dex */
public final class e extends BufferedOutputStream {
    private final CharsetEncoder a;

    public final e a(String str) throws IOException {
        ByteBuffer byteBufferEncode = this.a.encode(CharBuffer.wrap(str));
        super.write(byteBufferEncode.array(), 0, byteBufferEncode.limit());
        return this;
    }
}
