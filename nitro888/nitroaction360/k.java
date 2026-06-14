package com.nitro888.nitroaction360;

import android.widget.Toast;

/* JADX INFO: loaded from: classes.dex */
final class k implements Runnable {
    final /* synthetic */ j a;

    k(j jVar) {
        this.a = jVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Toast.makeText(this.a.a.a, "Processing failed, please try again", 1).show();
    }
}
