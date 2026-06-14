package com.nitro888.nitroaction360;

import de.robv.android.xposed.XC_MethodHook;

/* JADX INFO: loaded from: classes.dex */
class setScaleAndMagnifiedRegionCenter extends XC_MethodHook {
    setScaleAndMagnifiedRegionCenter() {
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
    }

    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
        methodHookParam.args[1] = Float.valueOf(0.0f);
        methodHookParam.args[2] = Float.valueOf(0.0f);
    }
}
