package com.b.a.b;

import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class d extends RuntimeException {
    public d(IOException iOException) {
        super(iOException);
    }

    @Override // java.lang.Throwable
    public final /* bridge */ /* synthetic */ Throwable getCause() {
        return (IOException) super.getCause();
    }
}
