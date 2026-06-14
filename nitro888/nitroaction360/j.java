package com.nitro888.nitroaction360;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.text.TextUtils;
import android.util.Log;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class j extends Thread {
    final /* synthetic */ i a;
    private final /* synthetic */ List b;
    private final /* synthetic */ int c;

    j(i iVar, List list, int i) {
        this.a = iVar;
        this.b = list;
        this.c = i;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        String strA;
        super.run();
        if (XposedStatus.isModuleActive()) {
            strA = "";
        } else {
            this.a.a.b();
            Log.i("INJECT", "complete copyRunnerAssetsToCache");
            strA = new w(this.a.a).a("android.permission.MANAGE_ACTIVITY_STACKS");
        }
        Log.i("INJECT", "complete ExecuteAsRoot ret:" + strA);
        if (TextUtils.isEmpty(strA)) {
            this.a.a.runOnUiThread(new l(this));
        } else {
            this.a.a.runOnUiThread(new k(this));
        }
        ActivityInfo activityInfo = (ActivityInfo) this.b.get(this.c);
        Intent intent = new Intent();
        intent.setClass(this.a.a, MainActivity.class);
        intent.putExtra("pkg", activityInfo.packageName);
        intent.putExtra("cls", activityInfo.name);
        intent.putExtra("type", "start_play");
        this.a.a.startActivity(intent);
    }
}
