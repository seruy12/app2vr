package com.nitro888.nitroaction360;

import android.widget.Toast;

/* JADX INFO: loaded from: classes.dex */
final class h implements Runnable {
    final /* synthetic */ MainActivity a;

    h(MainActivity mainActivity) {
        this.a = mainActivity;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Toast.makeText(this.a, "error,try reboot or retry", 1).show();
    }
}
