package com.nitro888.nitroaction360;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import de.robv.android.xposed.XC_MethodHook;
import java.lang.reflect.Field;

/* JADX INFO: loaded from: classes.dex */
class updateMagnificationSpec extends XC_MethodHook {
    updateMagnificationSpec() {
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
        Log.i("VR", "updateMagnificationSpec");
        Object obj = methodHookParam.thisObject;
        try {
            Field declaredField = methodHookParam.thisObject.getClass().getClassLoader().loadClass("com.android.server.accessibility.ScreenMagnifier$MagnificationController").getDeclaredField("mCurrentMagnificationSpec");
            Field declaredField2 = methodHookParam.thisObject.getClass().getClassLoader().loadClass("android.view.MagnificationSpec").getDeclaredField("scale");
            Field declaredField3 = methodHookParam.thisObject.getClass().getClassLoader().loadClass("android.view.MagnificationSpec").getDeclaredField("offsetX");
            Field declaredField4 = methodHookParam.thisObject.getClass().getClassLoader().loadClass("android.view.MagnificationSpec").getDeclaredField("offsetY");
            Field declaredField5 = methodHookParam.thisObject.getClass().getClassLoader().loadClass("com.android.server.accessibility.ScreenMagnifier").getDeclaredField("mContext");
            declaredField5.setAccessible(true);
            Field declaredField6 = obj.getClass().getDeclaredField("this$0");
            declaredField6.setAccessible(true);
            Context context = (Context) declaredField5.get(declaredField6.get(obj));
            Log.i("VR", "updateMagnificationSpec context:" + context);
            try {
                if (context.getResources().getConfiguration().orientation == 2) {
                    Intent intent = new Intent("com.hmct.vrmode.on");
                    intent.setClassName("com.app360.app360", "com.hmct.screencapture.FloatingWindowService");
                    context.startService(intent);
                }
            } catch (Exception e) {
                Log.i("VR", "updateMagnificationSpec e:" + e);
            }
            declaredField.setAccessible(true);
            declaredField2.setAccessible(true);
            declaredField3.setAccessible(true);
            declaredField4.setAccessible(true);
            Float f = (Float) declaredField2.get(declaredField.get(obj));
            Float f2 = (Float) declaredField3.get(declaredField.get(obj));
            Float f3 = (Float) declaredField4.get(declaredField.get(obj));
            Log.i("VR", "scale" + f);
            Log.i("VR", "x" + f2);
            Log.i("VR", "y" + f3);
            String strValueOf = String.valueOf(context.getResources().getDisplayMetrics().heightPixels / 4);
            Log.i("VR", "persist.sys.offsetx:0.0");
            Log.i("VR", "persist.sys.offsety:" + strValueOf);
            if (strValueOf == null) {
                declaredField3.setFloat(declaredField.get(obj), 360.0f);
                declaredField4.setFloat(declaredField.get(obj), 640.0f);
            } else {
                float fFloatValue = Float.valueOf("0.0").floatValue();
                float fFloatValue2 = Float.valueOf(strValueOf).floatValue();
                declaredField3.setFloat(declaredField.get(obj), fFloatValue);
                declaredField4.setFloat(declaredField.get(obj), fFloatValue2);
            }
        } catch (Exception e2) {
            Log.i("VR", "e:" + e2);
        }
    }

    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
        Log.i("VR", "updateMagnificationSpec");
    }
}
