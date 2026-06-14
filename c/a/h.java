package com.c.a;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/* JADX INFO: loaded from: classes.dex */
final class h implements Closeable {
    private final InputStream a;
    private final Charset b;
    private byte[] c;
    private int d;
    private int e;

    public h(InputStream inputStream, Charset charset) {
        this(inputStream, charset, (byte) 0);
    }

    private h(InputStream inputStream, Charset charset, byte b) {
        if (inputStream == null || charset == null) {
            throw new NullPointerException();
        }
        if (!charset.equals(j.a)) {
            throw new IllegalArgumentException("Unsupported encoding");
        }
        this.a = inputStream;
        this.b = charset;
        this.c = new byte[8192];
    }

    private void c() throws IOException {
        int i = this.a.read(this.c, 0, this.c.length);
        if (i == -1) {
            throw new EOFException();
        }
        this.d = 0;
        this.e = i;
    }

    public final String a() {
        int i;
        String string;
        synchronized (this.a) {
            if (this.c == null) {
                throw new IOException("LineReader is closed");
            }
            if (this.d >= this.e) {
                c();
            }
            int i2 = this.d;
            while (true) {
                if (i2 == this.e) {
                    i iVar = new i(this, (this.e - this.d) + 80);
                    loop1: while (true) {
                        iVar.write(this.c, this.d, this.e - this.d);
                        this.e = -1;
                        c();
                        i = this.d;
                        while (i != this.e) {
                            if (this.c[i] == 10) {
                                break loop1;
                            }
                            i++;
                        }
                    }
                    if (i != this.d) {
                        iVar.write(this.c, this.d, i - this.d);
                    }
                    this.d = i + 1;
                    string = iVar.toString();
                } else if (this.c[i2] == 10) {
                    string = new String(this.c, this.d, ((i2 == this.d || this.c[i2 + (-1)] != 13) ? i2 : i2 - 1) - this.d, this.b.name());
                    this.d = i2 + 1;
                } else {
                    i2++;
                }
            }
            return string;
        }
    }

    public final boolean b() {
        return this.e == -1;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public final void close() {
        synchronized (this.a) {
            if (this.c != null) {
                this.c = null;
                this.a.close();
            }
        }
    }
}
