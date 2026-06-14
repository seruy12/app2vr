package com.nitro888.nitroaction360;

import android.content.DialogInterface;
import android.widget.Toast;

/* JADX INFO: loaded from: classes.dex */
final class q implements DialogInterface.OnClickListener {
    final /* synthetic */ SelectorActivity a;

    q(SelectorActivity selectorActivity) {
        this.a = selectorActivity;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        if (SelectorActivity.a()) {
            this.a.c = true;
        } else {
            Toast.makeText(this.a, "root mode only support arm64", 1).show();
            this.a.finish();
        }
    }
}
