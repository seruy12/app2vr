package com.c.a;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
final class f {
    final /* synthetic */ a a;
    private final String b;
    private final long[] c;
    private boolean d;
    private d e;
    private long f;

    private f(a aVar, String str) {
        this.a = aVar;
        this.b = str;
        this.c = new long[aVar.i];
    }

    /* synthetic */ f(a aVar, String str, byte b) {
        this(aVar, str);
    }

    private static IOException a(String[] strArr) throws IOException {
        throw new IOException("unexpected journal line: " + Arrays.toString(strArr));
    }

    static /* synthetic */ void a(f fVar, String[] strArr) throws IOException {
        if (strArr.length != fVar.a.i) {
            throw a(strArr);
        }
        for (int i = 0; i < strArr.length; i++) {
            try {
                fVar.c[i] = Long.parseLong(strArr[i]);
            } catch (NumberFormatException e) {
                throw a(strArr);
            }
        }
    }

    public final File a(int i) {
        return new File(this.a.c, String.valueOf(this.b) + "." + i);
    }

    public final String a() {
        StringBuilder sb = new StringBuilder();
        for (long j : this.c) {
            sb.append(' ').append(j);
        }
        return sb.toString();
    }

    public final File b(int i) {
        return new File(this.a.c, String.valueOf(this.b) + "." + i + ".tmp");
    }
}
