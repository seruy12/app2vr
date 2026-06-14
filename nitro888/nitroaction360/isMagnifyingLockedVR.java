package com.nitro888.nitroaction360;

import android.util.Log;
import de.robv.android.xposed.XC_MethodHook;

/* JADX INFO: loaded from: classes.dex */
class isMagnifyingLockedVR extends XC_MethodHook {
    isMagnifyingLockedVR() {
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
        Log.i("VR", "isMagnifyingLocked");
        try {
            Float f = (Float) methodHookParam.thisObject.getClass().getClassLoader().loadClass("android.view.MagnificationSpec").getDeclaredField("scale").get(methodHookParam.thisObject.getClass().getClassLoader().loadClass("com.android.server.wm.AccessibilityController.DisplayMagnifier$MagnifiedViewport").getDeclaredField("mMagnificationSpec").get(methodHookParam.thisObject));
            Log.i("VR", "isMagnifyingLocked scale:" + f);
            methodHookParam.setResult(Boolean.valueOf(f.floatValue() < 0.9f));
        } catch (Exception e) {
            Log.i("VR", "e:" + e);
        }
    }

    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
    }
}
