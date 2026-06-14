package com.nitro888.nitroaction360;

import android.view.View;
import android.widget.AdapterView;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class i implements AdapterView.OnItemClickListener {
    final /* synthetic */ SelectorActivity a;
    private final /* synthetic */ List b;

    i(SelectorActivity selectorActivity, List list) {
        this.a = selectorActivity;
        this.b = list;
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
        new j(this, this.b, i).start();
    }
}
