package com.nitro888.nitroaction360;

import android.content.res.XModuleResources;
import android.content.res.XResources;
import android.os.Build;
import android.util.Log;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/* JADX INFO: loaded from: classes.dex */
public class VRXposed implements IXposedHookZygoteInit {
    static String MODULE_PATH = null;
    static XSharedPreferences mPref;
    public static XModuleResources sModRes;

    static void HookAndroid44(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        ClassLoader classLoader = loadPackageParam.classLoader;
        Class clsFindClass = XposedHelpers.findClass("com.android.server.accessibility.ScreenMagnifier", classLoader);
        Log.i("VR", "initInject" + clsFindClass);
        Class clsFindClass2 = XposedHelpers.findClass("com.android.server.accessibility.ScreenMagnifier$MagnificationController", classLoader);
        Log.i("VR", "initInject" + clsFindClass2);
        Class clsFindClass3 = XposedHelpers.findClass("android.view.MagnificationSpec", classLoader);
        Log.i("VR", "initInject" + clsFindClass3);
        Class clsFindClass4 = XposedHelpers.findClass("com.android.server.wm.AccessibilityController.DisplayMagnifier$MagnifiedViewport", classLoader);
        Log.i("VR", "initInject" + clsFindClass4);
        Class clsFindClass5 = XposedHelpers.findClass("com.android.server.wm.AccessibilityController.DisplayMagnifier$MagnifiedViewport$ViewportWindow", classLoader);
        Log.i("VR", "initInject" + clsFindClass5);
        Class clsFindClass6 = XposedHelpers.findClass("com.android.server.wm.AccessibilityController.DisplayMagnifier", classLoader);
        Log.i("VR", "initInject" + clsFindClass6);
        XposedBridge.hookAllMethods(clsFindClass, "persistScale", new persistScale());
        XposedBridge.hookAllMethods(clsFindClass, "getPersistedScale", new getPersistedScale());
        XposedBridge.hookAllMethods(clsFindClass2, "isMagnifying", new isMagnifying());
        XposedBridge.hookAllMethods(clsFindClass2, "updateMagnificationSpec", new updateMagnificationSpec());
        XposedBridge.hookAllMethods(clsFindClass2, "setScaleAndMagnifiedRegionCenter", new setScaleAndMagnifiedRegionCenter());
        XposedBridge.hookAllMethods(clsFindClass3, "initialize", new initialize());
        XposedBridge.hookAllMethods(clsFindClass4, "isMagnifyingLocked", new isMagnifyingLockedVR());
        XposedBridge.hookAllMethods(clsFindClass5, "setShown", new setShown());
        XposedBridge.hookAllMethods(clsFindClass6, "getMagnificationSpecForWindowLocked", new getMagnificationSpecForWindowLocked());
    }

    public static void initInject(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (Build.VERSION.SDK_INT >= 21) {
            HookAndroid44(loadPackageParam);
        }
    }

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        String str = loadPackageParam.packageName;
        Log.i("VR", "vr sdk " + Build.VERSION.SDK_INT);
        initInject(loadPackageParam);
    }

    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) {
        String str = startupParam.modulePath;
        MODULE_PATH = str;
        sModRes = XModuleResources.createInstance(str, (XResources) null);
    }
}
