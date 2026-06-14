package com.c.a;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public final class a implements Closeable {
    static final Pattern a = Pattern.compile("[a-z0-9_-]{1,120}");
    private static final OutputStream p = new c();
    private final File c;
    private final File d;
    private final File e;
    private final File f;
    private Writer k;
    private int m;
    private long j = 0;
    private final LinkedHashMap l = new LinkedHashMap(0, 0.75f, true);
    private long n = 0;
    final ThreadPoolExecutor b = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue());
    private final Callable o = new b(this);
    private final int g = 1;
    private final int i = 2;
    private long h = 5242880;

    private a(File file) {
        this.c = file;
        this.d = new File(file, "journal");
        this.e = new File(file, "journal.tmp");
        this.f = new File(file, "journal.bkp");
    }

    public static a a(File file) throws IOException {
        if (5242880 <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        File file2 = new File(file, "journal.bkp");
        if (file2.exists()) {
            File file3 = new File(file, "journal");
            if (file3.exists()) {
                file2.delete();
            } else {
                a(file2, file3, false);
            }
        }
        a aVar = new a(file);
        if (aVar.d.exists()) {
            try {
                aVar.c();
                aVar.d();
                return aVar;
            } catch (IOException e) {
                System.out.println("DiskLruCache " + file + " is corrupt: " + e.getMessage() + ", removing");
                aVar.close();
                j.a(aVar.c);
            }
        }
        file.mkdirs();
        a aVar2 = new a(file);
        aVar2.e();
        return aVar2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0037 A[Catch: all -> 0x0012, TryCatch #0 {, blocks: (B:4:0x0002, B:6:0x000c, B:7:0x0011, B:12:0x0017, B:15:0x001e, B:31:0x0088, B:33:0x0090, B:34:0x00a7, B:35:0x00a8, B:37:0x00b2, B:38:0x00b6, B:17:0x0022, B:19:0x0026, B:21:0x0037, B:23:0x0062, B:24:0x006c, B:26:0x0079, B:28:0x007f, B:46:0x00ec, B:39:0x00ba, B:41:0x00c0, B:43:0x00c6, B:44:0x00e4, B:45:0x00e8), top: B:48:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x007f A[Catch: all -> 0x0012, TRY_LEAVE, TryCatch #0 {, blocks: (B:4:0x0002, B:6:0x000c, B:7:0x0011, B:12:0x0017, B:15:0x001e, B:31:0x0088, B:33:0x0090, B:34:0x00a7, B:35:0x00a8, B:37:0x00b2, B:38:0x00b6, B:17:0x0022, B:19:0x0026, B:21:0x0037, B:23:0x0062, B:24:0x006c, B:26:0x0079, B:28:0x007f, B:46:0x00ec, B:39:0x00ba, B:41:0x00c0, B:43:0x00c6, B:44:0x00e4, B:45:0x00e8), top: B:48:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00ba A[Catch: all -> 0x0012, TryCatch #0 {, blocks: (B:4:0x0002, B:6:0x000c, B:7:0x0011, B:12:0x0017, B:15:0x001e, B:31:0x0088, B:33:0x0090, B:34:0x00a7, B:35:0x00a8, B:37:0x00b2, B:38:0x00b6, B:17:0x0022, B:19:0x0026, B:21:0x0037, B:23:0x0062, B:24:0x006c, B:26:0x0079, B:28:0x007f, B:46:0x00ec, B:39:0x00ba, B:41:0x00c0, B:43:0x00c6, B:44:0x00e4, B:45:0x00e8), top: B:48:0x0002 }] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00ec A[Catch: all -> 0x0012, TRY_LEAVE, TryCatch #0 {, blocks: (B:4:0x0002, B:6:0x000c, B:7:0x0011, B:12:0x0017, B:15:0x001e, B:31:0x0088, B:33:0x0090, B:34:0x00a7, B:35:0x00a8, B:37:0x00b2, B:38:0x00b6, B:17:0x0022, B:19:0x0026, B:21:0x0037, B:23:0x0062, B:24:0x006c, B:26:0x0079, B:28:0x007f, B:46:0x00ec, B:39:0x00ba, B:41:0x00c0, B:43:0x00c6, B:44:0x00e4, B:45:0x00e8), top: B:48:0x0002 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized void a(com.c.a.d r11, boolean r12) {
        /*
            Method dump skipped, instruction units count: 277
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.c.a.a.a(com.c.a.d, boolean):void");
    }

    private static void a(File file, File file2, boolean z) throws IOException {
        if (z) {
            b(file2);
        }
        if (!file.renameTo(file2)) {
            throw new IOException();
        }
    }

    private static void b(File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }

    private void c() {
        String strA;
        String strSubstring;
        h hVar = new h(new FileInputStream(this.d), j.a);
        try {
            String strA2 = hVar.a();
            String strA3 = hVar.a();
            String strA4 = hVar.a();
            String strA5 = hVar.a();
            String strA6 = hVar.a();
            if (!"libcore.io.DiskLruCache".equals(strA2) || !"1".equals(strA3) || !Integer.toString(this.g).equals(strA4) || !Integer.toString(this.i).equals(strA5) || !"".equals(strA6)) {
                throw new IOException("unexpected journal header: [" + strA2 + ", " + strA3 + ", " + strA5 + ", " + strA6 + "]");
            }
            int i = 0;
            while (true) {
                try {
                    strA = hVar.a();
                    int iIndexOf = strA.indexOf(32);
                    if (iIndexOf == -1) {
                        throw new IOException("unexpected journal line: " + strA);
                    }
                    int i2 = iIndexOf + 1;
                    int iIndexOf2 = strA.indexOf(32, i2);
                    if (iIndexOf2 == -1) {
                        String strSubstring2 = strA.substring(i2);
                        if (iIndexOf == 6 && strA.startsWith("REMOVE")) {
                            this.l.remove(strSubstring2);
                            i++;
                        } else {
                            strSubstring = strSubstring2;
                        }
                    } else {
                        strSubstring = strA.substring(i2, iIndexOf2);
                    }
                    f fVar = (f) this.l.get(strSubstring);
                    if (fVar == null) {
                        fVar = new f(this, strSubstring, (byte) 0);
                        this.l.put(strSubstring, fVar);
                    }
                    if (iIndexOf2 != -1 && iIndexOf == 5 && strA.startsWith("CLEAN")) {
                        String[] strArrSplit = strA.substring(iIndexOf2 + 1).split(" ");
                        fVar.d = true;
                        fVar.e = null;
                        f.a(fVar, strArrSplit);
                    } else if (iIndexOf2 == -1 && iIndexOf == 5 && strA.startsWith("DIRTY")) {
                        fVar.e = new d(this, fVar, (byte) 0);
                    } else if (iIndexOf2 != -1 || iIndexOf != 4 || !strA.startsWith("READ")) {
                        break;
                    }
                    i++;
                } catch (EOFException e) {
                    this.m = i - this.l.size();
                    if (hVar.b()) {
                        e();
                    } else {
                        this.k = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.d, true), j.a));
                    }
                    j.a(hVar);
                    return;
                }
            }
            throw new IOException("unexpected journal line: " + strA);
        } catch (Throwable th) {
            j.a(hVar);
            throw th;
        }
    }

    private synchronized d d(String str) {
        f fVar;
        d dVar;
        g();
        e(str);
        f fVar2 = (f) this.l.get(str);
        if (-1 == -1 || (fVar2 != null && fVar2.f == -1)) {
            if (fVar2 == null) {
                f fVar3 = new f(this, str, (byte) 0);
                this.l.put(str, fVar3);
                fVar = fVar3;
            } else if (fVar2.e != null) {
                dVar = null;
            } else {
                fVar = fVar2;
            }
            dVar = new d(this, fVar, (byte) 0);
            fVar.e = dVar;
            this.k.write("DIRTY " + str + '\n');
            this.k.flush();
        } else {
            dVar = null;
        }
        return dVar;
    }

    private void d() throws IOException {
        b(this.e);
        Iterator it = this.l.values().iterator();
        while (it.hasNext()) {
            f fVar = (f) it.next();
            if (fVar.e == null) {
                for (int i = 0; i < this.i; i++) {
                    this.j += fVar.c[i];
                }
            } else {
                fVar.e = null;
                for (int i2 = 0; i2 < this.i; i2++) {
                    b(fVar.a(i2));
                    b(fVar.b(i2));
                }
                it.remove();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void e() {
        if (this.k != null) {
            this.k.close();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.e), j.a));
        try {
            bufferedWriter.write("libcore.io.DiskLruCache");
            bufferedWriter.write("\n");
            bufferedWriter.write("1");
            bufferedWriter.write("\n");
            bufferedWriter.write(Integer.toString(this.g));
            bufferedWriter.write("\n");
            bufferedWriter.write(Integer.toString(this.i));
            bufferedWriter.write("\n");
            bufferedWriter.write("\n");
            for (f fVar : this.l.values()) {
                if (fVar.e != null) {
                    bufferedWriter.write("DIRTY " + fVar.b + '\n');
                } else {
                    bufferedWriter.write("CLEAN " + fVar.b + fVar.a() + '\n');
                }
            }
            bufferedWriter.close();
            if (this.d.exists()) {
                a(this.d, this.f, true);
            }
            a(this.e, this.d, false);
            this.f.delete();
            this.k = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.d, true), j.a));
        } catch (Throwable th) {
            bufferedWriter.close();
            throw th;
        }
    }

    private static void e(String str) {
        if (!a.matcher(str).matches()) {
            throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,120}: \"" + str + "\"");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean f() {
        return this.m >= 2000 && this.m >= this.l.size();
    }

    private void g() {
        if (this.k == null) {
            throw new IllegalStateException("cache is closed");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        while (this.j > this.h) {
            c((String) ((Map.Entry) this.l.entrySet().iterator().next()).getKey());
        }
    }

    public final synchronized g a(String str) {
        g gVar;
        g();
        e(str);
        f fVar = (f) this.l.get(str);
        if (fVar != null && fVar.d) {
            InputStream[] inputStreamArr = new InputStream[this.i];
            for (int i = 0; i < this.i; i++) {
                try {
                    inputStreamArr[i] = new FileInputStream(fVar.a(i));
                } catch (FileNotFoundException e) {
                    for (int i2 = 0; i2 < this.i && inputStreamArr[i2] != null; i2++) {
                        j.a(inputStreamArr[i2]);
                    }
                    gVar = null;
                }
            }
            this.m++;
            this.k.append((CharSequence) ("READ " + str + '\n'));
            if (f()) {
                this.b.submit(this.o);
            }
            gVar = new g(this, str, fVar.f, inputStreamArr, fVar.c, (byte) 0);
        } else {
            gVar = null;
        }
        return gVar;
    }

    public final synchronized void a() {
        g();
        h();
        this.k.flush();
    }

    public final d b(String str) {
        return d(str);
    }

    public final synchronized boolean c(String str) {
        boolean z;
        synchronized (this) {
            g();
            e(str);
            f fVar = (f) this.l.get(str);
            if (fVar == null || fVar.e != null) {
                z = false;
            } else {
                for (int i = 0; i < this.i; i++) {
                    File fileA = fVar.a(i);
                    if (fileA.exists() && !fileA.delete()) {
                        throw new IOException("failed to delete " + fileA);
                    }
                    this.j -= fVar.c[i];
                    fVar.c[i] = 0;
                }
                this.m++;
                this.k.append((CharSequence) ("REMOVE " + str + '\n'));
                this.l.remove(str);
                if (f()) {
                    this.b.submit(this.o);
                }
                z = true;
            }
        }
        return z;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public final synchronized void close() {
        if (this.k != null) {
            for (f fVar : new ArrayList(this.l.values())) {
                if (fVar.e != null) {
                    fVar.e.b();
                }
            }
            h();
            this.k.close();
            this.k = null;
        }
    }
}
