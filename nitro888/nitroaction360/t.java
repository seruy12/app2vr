package com.nitro888.nitroaction360;

import android.util.Log;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
final class t implements View.OnClickListener {
    final /* synthetic */ SelectorActivity a;

    t(SelectorActivity selectorActivity) {
        this.a = selectorActivity;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.a.a.closeDrawers();
        Log.i("vr", "joystick");
        new u(this).start();
    }
}
