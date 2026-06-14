package com.nitro888.nitroaction360;

import android.util.Log;
import de.robv.android.xposed.XC_MethodHook;

/* JADX INFO: loaded from: classes.dex */
class canMagnifying extends XC_MethodHook {
    canMagnifying() {
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
        Log.i("VR", "canMagnifying");
        try {
            int iIntValue = ((Integer) methodHookParam.args[1]).intValue();
            Log.i("VR", "canMagnifying" + iIntValue);
            if (iIntValue == 2003) {
                methodHookParam.setResult(false);
            }
        } catch (Exception e) {
            Log.i("VR", "e:" + e);
        }
    }

    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
    }
}
