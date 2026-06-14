package com.b.a.a;

import com.c.a.g;
import java.io.Closeable;
import java.io.InputStream;

/* JADX INFO: loaded from: classes.dex */
public final class c implements Closeable {
    public final String a;
    public final InputStream b;
    private final g c;

    private c(String str, InputStream inputStream, g gVar) {
        this.a = str;
        this.b = inputStream;
        this.c = gVar;
    }

    /* synthetic */ c(String str, InputStream inputStream, g gVar, byte b) {
        this(str, inputStream, gVar);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public final void close() {
        this.c.close();
    }
}
