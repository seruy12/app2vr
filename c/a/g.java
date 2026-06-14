package com.c.a;

import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/* JADX INFO: loaded from: classes.dex */
public final class g implements Closeable {
    final /* synthetic */ a a;
    private final String b;
    private final long c;
    private final InputStream[] d;
    private final long[] e;

    private g(a aVar, String str, long j, InputStream[] inputStreamArr, long[] jArr) {
        this.a = aVar;
        this.b = str;
        this.c = j;
        this.d = inputStreamArr;
        this.e = jArr;
    }

    /* synthetic */ g(a aVar, String str, long j, InputStream[] inputStreamArr, long[] jArr, byte b) {
        this(aVar, str, j, inputStreamArr, jArr);
    }

    public final InputStream a() {
        return this.d[1];
    }

    public final String b() {
        return j.a((Reader) new InputStreamReader(this.d[0], j.b));
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public final void close() {
        for (InputStream inputStream : this.d) {
            j.a(inputStream);
        }
    }
}
