package com.nitro888.nitroaction360;

import android.content.pm.ResolveInfo;
import java.util.Comparator;

/* JADX INFO: loaded from: classes.dex */
final class n implements Comparator {
    final /* synthetic */ m a;

    n(m mVar) {
        this.a = mVar;
    }

    @Override // java.util.Comparator
    public final /* synthetic */ int compare(Object obj, Object obj2) {
        return ((ResolveInfo) obj).loadLabel(this.a.a.getPackageManager()).toString().compareTo(((ResolveInfo) obj2).loadLabel(this.a.a.getPackageManager()).toString());
    }
}
