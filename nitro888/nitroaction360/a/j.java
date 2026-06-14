package com.nitro888.nitroaction360.a;

/* JADX INFO: loaded from: classes.dex */
public class j {
    private static final String a = j.class.getSimpleName();
    private k[] b;

    public final com.a.a.c a(int i, int i2) {
        if (!this.b[i].a) {
            return null;
        }
        com.a.a.a aVarA = this.b[i].a();
        return (com.a.a.c) ((com.a.a.b) aVarA.b.get(i2 / aVarA.a)).a.get(i2 % aVarA.a);
    }
}
