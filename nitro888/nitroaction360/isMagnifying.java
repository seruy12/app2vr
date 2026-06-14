package com.nitro888.nitroaction360;

import android.util.Log;
import de.robv.android.xposed.XC_MethodHook;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
class isMagnifying extends XC_MethodHook {
    isMagnifying() {
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
        Log.i("VR", "isMagnifying");
        Object obj = methodHookParam.thisObject;
        try {
            Field declaredField = methodHookParam.thisObject.getClass().getClassLoader().loadClass("com.android.server.accessibility.ScreenMagnifier$MagnificationController").getDeclaredField("mCurrentMagnificationSpec");
            declaredField.setAccessible(true);
            Field declaredField2 = Class.forName("android.view.MagnificationSpec").getDeclaredField("scale");
            declaredField2.setAccessible(true);
            Float f = (Float) declaredField2.get(declaredField.get(obj));
            Log.i("VR", "isMagnifying scale:" + f);
            methodHookParam.setResult(Boolean.valueOf(f.floatValue() < 0.9f));
        } catch (Exception e) {
            Log.i("VR", "e:" + e);
        }
    }

    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
    }
}
