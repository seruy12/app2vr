package com.nitro888.nitroaction360;

import android.graphics.drawable.Drawable;

/* JADX INFO: loaded from: classes.dex */
final class d implements Runnable {
    final /* synthetic */ c a;
    private final /* synthetic */ e b;
    private final /* synthetic */ int c;
    private final /* synthetic */ CharSequence d;
    private final /* synthetic */ Drawable e;

    d(c cVar, e eVar, int i, CharSequence charSequence, Drawable drawable) {
        this.a = cVar;
        this.b = eVar;
        this.c = i;
        this.d = charSequence;
        this.e = drawable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (this.b.c == this.c) {
            this.b.b.setText(this.d);
            this.b.a.setImageDrawable(this.e);
        }
    }
}
