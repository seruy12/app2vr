package com.c.a;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/* JADX INFO: loaded from: classes.dex */
public final class d {
    final /* synthetic */ a a;
    private final f b;
    private final boolean[] c;
    private boolean d;
    private boolean e;

    private d(a aVar, f fVar) {
        this.a = aVar;
        this.b = fVar;
        this.c = fVar.d ? null : new boolean[aVar.i];
    }

    /* synthetic */ d(a aVar, f fVar, byte b) {
        this(aVar, fVar);
    }

    public final OutputStream a(int i) {
        OutputStream eVar;
        FileOutputStream fileOutputStream;
        if (i < 0 || i >= this.a.i) {
            throw new IllegalArgumentException("Expected index " + i + " to be greater than 0 and less than the maximum value count of " + this.a.i);
        }
        synchronized (this.a) {
            if (this.b.e != this) {
                throw new IllegalStateException();
            }
            if (!this.b.d) {
                this.c[i] = true;
            }
            File fileB = this.b.b(i);
            try {
                fileOutputStream = new FileOutputStream(fileB);
            } catch (FileNotFoundException e) {
                this.a.c.mkdirs();
                try {
                    fileOutputStream = new FileOutputStream(fileB);
                } catch (FileNotFoundException e2) {
                    eVar = a.p;
                }
            }
            eVar = new e(this, fileOutputStream, (byte) 0);
        }
        return eVar;
    }

    public final void a() {
        if (this.d) {
            this.a.a(this, false);
            this.a.c(this.b.b);
        } else {
            this.a.a(this, true);
        }
        this.e = true;
    }

    public final void a(String str) throws Throwable {
        OutputStreamWriter outputStreamWriter;
        try {
            outputStreamWriter = new OutputStreamWriter(a(0), j.b);
            try {
                outputStreamWriter.write(str);
                j.a(outputStreamWriter);
            } catch (Throwable th) {
                th = th;
                j.a(outputStreamWriter);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            outputStreamWriter = null;
        }
    }

    public final void b() {
        this.a.a(this, false);
    }
}
