package com.nitro888.nitroaction360;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

/* JADX INFO: loaded from: classes.dex */
final class v implements View.OnClickListener {
    final /* synthetic */ SelectorActivity a;

    v(SelectorActivity selectorActivity) {
        this.a = selectorActivity;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.a.a.closeDrawers();
        Log.i("vr", "help");
        this.a.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://repo.xposed.info/module/com.app360.app360")));
    }
}
