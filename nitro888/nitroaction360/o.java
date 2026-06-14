package com.nitro888.nitroaction360;

import android.util.Log;

/* JADX INFO: loaded from: classes.dex */
final class o implements Runnable {
    final /* synthetic */ m a;
    private final /* synthetic */ b b;

    o(m mVar, b bVar) {
        this.a = mVar;
        this.b = bVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Log.d("FloatingFolder", "after");
        this.b.notifyDataSetChanged();
    }
}
