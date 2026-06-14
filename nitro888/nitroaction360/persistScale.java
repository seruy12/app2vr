package com.nitro888.nitroaction360;

import android.util.Log;
import de.robv.android.xposed.XC_MethodHook;

/* JADX INFO: loaded from: classes.dex */
class persistScale extends XC_MethodHook {
    persistScale() {
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
        Log.i("VR", "persistScale");
    }

    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
    }
}
