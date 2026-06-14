package com.nitro888.nitroaction360;

import android.content.res.XModuleResources;
import android.content.res.XResources;
import android.os.Build;
import android.util.Log;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/* JADX INFO: loaded from: classes.dex */
public class MainXposed implements IXposedHookLoadPackage, IXposedHookZygoteInit {
    static String MODULE_PATH = null;
    static XSharedPreferences mPref;
    public static XModuleResources sModRes;

    static void HookAndroidL(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        Log.i("INJECT", "HookAndroidL");
        Class clsFindClass = XposedHelpers.findClass("com.android.server.am.ActivityManagerService", loadPackageParam.classLoader);
        Log.i("INJECT", "initInject" + clsFindClass);
        isMagnifyingLocked ismagnifyinglocked = new isMagnifyingLocked();
        startActivity startactivity = new startActivity();
        XposedBridge.hookAllMethods(clsFindClass, "checkPermission", ismagnifyinglocked);
        XposedBridge.hookAllMethods(clsFindClass, "startActivity", startactivity);
    }

    private static void initInject(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        ClassLoader classLoader = loadPackageParam.classLoader;
        if (Build.VERSION.SDK_INT >= 20) {
            HookAndroidL(loadPackageParam);
            VRXposed.initInject(loadPackageParam);
        }
    }

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        String str = loadPackageParam.packageName;
        if ("android".equals(str)) {
            initInject(loadPackageParam);
        }
        if (str.equals("com.app360.app360")) {
            Log.i("INJECT", "module enabled " + str);
            XposedHelpers.findAndHookMethod("com.nitro888.nitroaction360.XposedStatus", loadPackageParam.classLoader, "isModuleActive", new Object[]{XC_MethodReplacement.returnConstant(true)});
        }
    }

    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {
        String str = startupParam.modulePath;
        MODULE_PATH = str;
        sModRes = XModuleResources.createInstance(str, (XResources) null);
    }
}
