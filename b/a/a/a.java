package com.b.a.a;

import java.io.InputStream;
import java.net.URLConnection;

/* JADX INFO: loaded from: classes.dex */
public final class a extends com.b.a.b.a {
    private final b a;
    private c b;
    private boolean c;
    private boolean d;
    private boolean e;

    private a(CharSequence charSequence, String str, b bVar) {
        super(charSequence, str);
        this.a = bVar;
    }

    public static a a(CharSequence charSequence, b bVar) {
        return new a(charSequence, "GET", bVar);
    }

    private void f() {
        if (this.b == null) {
            return;
        }
        this.b.close();
        this.b = null;
    }

    public final boolean a() {
        return this.d;
    }

    @Override // com.b.a.b.a
    protected final com.b.a.b.a b() {
        if (!this.c) {
            this.c = true;
            this.b = this.a.a((URLConnection) e());
            if (this.b != null) {
                e().setRequestProperty("If-None-Match", this.b.a);
            }
        }
        return super.b();
    }

    @Override // com.b.a.b.a
    public final int c() {
        int iC = super.c();
        if (iC == 304) {
            return 200;
        }
        f();
        return iC;
    }

    @Override // com.b.a.b.a
    public final InputStream d() {
        int iC = super.c();
        if (iC == 304 && this.b != null) {
            this.a.a();
            this.d = true;
            return this.b.b;
        }
        if (iC == 200) {
            this.a.b();
            InputStream inputStreamA = this.a.a(e(), this.e);
            if (inputStreamA != null) {
                return inputStreamA;
            }
        }
        f();
        return super.d();
    }
}
