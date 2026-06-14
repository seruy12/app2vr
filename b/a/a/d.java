package com.b.a.a;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* JADX INFO: loaded from: classes.dex */
final class d extends FilterInputStream {
    private final com.c.a.d a;
    private final OutputStream b;
    private final b c;
    private final boolean d;
    private boolean e;

    d(InputStream inputStream, OutputStream outputStream, com.c.a.d dVar, b bVar, boolean z) {
        super(inputStream);
        this.a = dVar;
        this.c = bVar;
        this.b = outputStream;
        this.d = z;
    }

    private void a() {
        synchronized (this.c) {
            if (this.e) {
                return;
            }
            this.e = true;
            try {
                this.b.close();
            } catch (IOException e) {
            }
            try {
                this.a.b();
            } catch (IOException e2) {
            }
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public final void close() throws IOException {
        synchronized (this.c) {
            if (this.e) {
                return;
            }
            this.e = true;
            super.close();
            this.a.a();
            if (this.d) {
                try {
                    this.c.flush();
                } catch (IOException e) {
                }
            }
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public final int read() throws IOException {
        int i = super.read();
        if (i != -1) {
            try {
                this.b.write(i);
            } catch (IOException e) {
                a();
            }
        }
        return i;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public final int read(byte[] bArr, int i, int i2) throws IOException {
        int i3 = super.read(bArr, i, i2);
        if (i3 > 0) {
            try {
                this.b.write(bArr, i, i3);
            } catch (IOException e) {
                a();
            }
        }
        return i3;
    }
}
