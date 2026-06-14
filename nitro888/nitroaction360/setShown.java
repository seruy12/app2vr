package com.nitro888.nitroaction360;

import de.robv.android.xposed.XC_MethodHook;

/* JADX INFO: loaded from: classes.dex */
class setShown extends XC_MethodHook {
    setShown() {
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
    }

    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
        methodHookParam.args[0] = Boolean.FALSE;
    }
}
