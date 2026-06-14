package com.nitro888.nitroaction360;

import android.util.Log;
import de.robv.android.xposed.XC_MethodHook;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
class getMagnificationSpecForWindowLocked extends XC_MethodHook {
    getMagnificationSpecForWindowLocked() {
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
        Log.i("VR", "getMagnificationSpecForWindowLocked");
        try {
            Object obj = methodHookParam.args[0];
            Field declaredField = methodHookParam.thisObject.getClass().getClassLoader().loadClass("com.android.server.wm.WindowState").getDeclaredField("mAttachedWindow");
            declaredField.setAccessible(true);
            Object obj2 = declaredField.get(obj);
            if (obj != null && obj.toString().contains("com.app360.app360")) {
                methodHookParam.setResult((Object) null);
            }
            if (obj2 == null || !obj2.toString().contains("com.app360.app360")) {
                return;
            }
            methodHookParam.setResult((Object) null);
        } catch (Exception e) {
            Log.i("VR", "e:" + e);
        }
    }

    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
    }
}
