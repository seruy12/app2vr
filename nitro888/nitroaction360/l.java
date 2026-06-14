package com.nitro888.nitroaction360;

import android.widget.Toast;

/* JADX INFO: loaded from: classes.dex */
final class l implements Runnable {
    final /* synthetic */ j a;

    l(j jVar) {
        this.a = jVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Toast.makeText(this.a.a.a, " Stand up the phone in horizontal screen state and waiting for loading ...", 1).show();
    }
}
