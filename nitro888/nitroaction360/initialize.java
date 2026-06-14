package com.nitro888.nitroaction360;

import android.util.Log;
import de.robv.android.xposed.XC_MethodHook;

/* JADX INFO: loaded from: classes.dex */
class initialize extends XC_MethodHook {
    static Float lastScale;

    initialize() {
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
        Log.i("VR", "initialize after:" + lastScale);
        try {
            Class.forName("android.view.MagnificationSpec").getDeclaredField("scale").setFloat(methodHookParam.thisObject, lastScale.floatValue());
        } catch (Exception e) {
            Log.i("VR", "e:" + e);
        }
    }

    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
        try {
            Float f = (Float) methodHookParam.args[0];
            Log.i("VR", "initialize: befor" + f);
            lastScale = f;
            methodHookParam.args[0] = Float.valueOf(1.0f);
        } catch (Exception e) {
            Log.i("VR", "eee:" + e);
        }
    }
}
