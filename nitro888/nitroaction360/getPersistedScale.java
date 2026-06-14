package com.nitro888.nitroaction360;

import android.util.Log;
import de.robv.android.xposed.XC_MethodHook;

/* JADX INFO: loaded from: classes.dex */
class getPersistedScale extends XC_MethodHook {
    getPersistedScale() {
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
        Log.i("VR", "getPersistedScale");
        Float f = (Float) methodHookParam.getResult();
        methodHookParam.setResult(Float.valueOf(0.5f));
        Log.i("VR", "getPersistedScale ft" + f);
        Log.i("VR", "persist.sys.scale0.5");
        methodHookParam.setResult(Float.valueOf(Float.valueOf("0.5").floatValue()));
    }

    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
    }
}
