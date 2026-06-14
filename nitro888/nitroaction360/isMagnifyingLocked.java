package com.nitro888.nitroaction360;

import de.robv.android.xposed.XC_MethodHook;

/* JADX INFO: loaded from: classes.dex */
class isMagnifyingLocked extends XC_MethodHook {
    isMagnifyingLocked() {
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
        String str;
        Object obj = methodHookParam.args[0];
        if ((obj instanceof String) && (str = (String) obj) != null && str.contains("MANAGE_ACTIVITY_STACKS")) {
            methodHookParam.setResult(0);
        }
    }

    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
    }
}
