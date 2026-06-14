package com.nitro888.nitroaction360;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

/* JADX INFO: loaded from: classes.dex */
final class r implements DialogInterface.OnClickListener {
    final /* synthetic */ SelectorActivity a;

    r(SelectorActivity selectorActivity) {
        this.a = selectorActivity;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.a.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://repo.xposed.info/")));
        this.a.finish();
    }
}
