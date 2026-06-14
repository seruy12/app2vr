package com.nitro888.nitroaction360;

import android.content.pm.ActivityInfo;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
final class c implements Runnable {
    final /* synthetic */ b a;
    private final /* synthetic */ ActivityInfo b;
    private final /* synthetic */ View c;
    private final /* synthetic */ e d;
    private final /* synthetic */ int e;

    c(b bVar, ActivityInfo activityInfo, View view, e eVar, int i) {
        this.a = bVar;
        this.b = activityInfo;
        this.c = view;
        this.d = eVar;
        this.e = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.c.post(new d(this, this.d, this.e, this.b.loadLabel(this.a.b), this.b.loadIcon(this.a.b)));
    }
}
