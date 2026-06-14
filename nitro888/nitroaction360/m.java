package com.nitro888.nitroaction360;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.ListView;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class m implements Runnable {
    final /* synthetic */ SelectorActivity a;
    private final /* synthetic */ List b;
    private final /* synthetic */ ListView c;
    private final /* synthetic */ b d;

    m(SelectorActivity selectorActivity, List list, ListView listView, b bVar) {
        this.a = selectorActivity;
        this.b = list;
        this.c = listView;
        this.d = bVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
        intent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> listQueryIntentActivities = this.a.getPackageManager().queryIntentActivities(intent, 0);
        Collections.sort(listQueryIntentActivities, new n(this));
        Iterator<ResolveInfo> it = listQueryIntentActivities.iterator();
        while (it.hasNext()) {
            this.b.add(it.next().activityInfo);
        }
        Log.d("FloatingFolder", "before");
        this.c.post(new o(this, this.d));
    }
}
