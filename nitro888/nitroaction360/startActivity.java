package com.nitro888.nitroaction360;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageParser;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.Log;
import de.robv.android.xposed.XC_MethodHook;
import java.lang.reflect.Field;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
class startActivity extends XC_MethodHook {
    startActivity() {
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
    }

    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
        try {
            String str = (String) methodHookParam.args[1];
            Intent intent = (Intent) methodHookParam.args[2];
            if (str != null && str.equals("com.app360.app360") && intent.hasExtra("pkg")) {
                Log.i("INJECT", "callingpkg" + str);
                Log.i("INJECT", "startpkg" + intent.getStringExtra("pkg"));
                setVRMode(methodHookParam, intent.getStringExtra("pkg"));
            }
        } catch (Exception e) {
            Log.i("INJECT", "e" + e);
            e.printStackTrace();
            try {
                setVRMode(methodHookParam, null);
            } catch (Exception e2) {
                Log.i("INJECT", "e1" + e2);
                e.printStackTrace();
            }
        }
    }

    void setVRMode(XC_MethodHook.MethodHookParam methodHookParam, String str) throws IllegalAccessException, NoSuchFieldException {
        Log.i("INJECT", ">>>>>>>>>>>>>start Activity<<<<<<<<<<<<<<");
        Field declaredField = methodHookParam.thisObject.getClass().getClassLoader().loadClass("com.android.server.pm.PackageManagerService").getDeclaredField("sUserManager");
        declaredField.setAccessible(true);
        Object obj = declaredField.get(null);
        Log.i("INJECT", "ums" + obj);
        Field declaredField2 = methodHookParam.thisObject.getClass().getClassLoader().loadClass("com.android.server.pm.UserManagerService").getDeclaredField("mPm");
        declaredField2.setAccessible(true);
        Object obj2 = declaredField2.get(obj);
        Log.i("INJECT", "pms" + obj2);
        Field declaredField3 = methodHookParam.thisObject.getClass().getClassLoader().loadClass("com.android.server.pm.PackageManagerService").getDeclaredField("mActivities");
        declaredField3.setAccessible(true);
        Object obj3 = declaredField3.get(obj2);
        Log.i("INJECT", "activityintentresolver" + obj3);
        Field declaredField4 = methodHookParam.thisObject.getClass().getClassLoader().loadClass("com.android.server.pm.PackageManagerService$ActivityIntentResolver").getDeclaredField("mActivities");
        declaredField4.setAccessible(true);
        for (Map.Entry entry : ((Map) declaredField4.get(obj3)).entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            if (str == null || str.equals(((ComponentName) entry.getKey()).getClassName())) {
                ((PackageParser.Activity) entry.getValue()).info.flags |= ExploreByTouchHelper.INVALID_ID;
                ((PackageParser.Activity) entry.getValue()).info.launchMode = 0;
                ((PackageParser.Activity) entry.getValue()).info.taskAffinity = ((ComponentName) entry.getKey()).getPackageName();
            }
        }
    }
}
