package com.nitro888.nitroaction360;

import android.content.DialogInterface;

/* JADX INFO: loaded from: classes.dex */
final class p implements DialogInterface.OnClickListener {
    final /* synthetic */ SelectorActivity a;

    p(SelectorActivity selectorActivity) {
        this.a = selectorActivity;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.a.finish();
    }
}
