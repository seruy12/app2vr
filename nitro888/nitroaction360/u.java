package com.nitro888.nitroaction360;

import android.content.Intent;

/* JADX INFO: loaded from: classes.dex */
final class u extends Thread {
    final /* synthetic */ t a;

    u(t tVar) {
        this.a = tVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        super.run();
        Intent intent = new Intent();
        intent.setClass(this.a.a, MainActivity.class);
        intent.putExtra("pkg", "test");
        intent.putExtra("cls", "test");
        intent.putExtra("type", "start_setting");
        this.a.a.startActivity(intent);
    }
}
