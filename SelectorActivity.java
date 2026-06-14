package com.nitro888.nitroaction360;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.nibiru.lib.controller.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class SelectorActivity extends Activity {
    static boolean b = false;
    static final String[] d = {"libhello.so", "libsupol.so", "GoogleDoor", "supolicy", "DemoInject2.apk"};
    static final String[] e = {"libhello.so", "libsupol.so", "GoogleDoor", "supolicy", "DemoInject2.apk"};
    DrawerLayout a;
    boolean c = false;
    private ActionBarDrawerToggle f;

    private static String a(String str, String str2) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", String.class, String.class).invoke(cls, str, "");
        } catch (Exception e2) {
            return str2;
        }
    }

    public static boolean a() {
        return a("ro.product.cpu.abi", "arm").contains("v8a");
    }

    public final void b() {
        try {
            File cacheDir = getCacheDir();
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            String[] strArr = a() ? d : null;
            if (strArr == null) {
                return;
            }
            for (int i = 0; i < strArr.length; i++) {
                InputStream inputStreamOpen = getAssets().open(strArr[i]);
                FileOutputStream fileOutputStream = new FileOutputStream(new File(cacheDir, e[i]));
                byte[] bArr = new byte[2048];
                while (true) {
                    int i2 = inputStreamOpen.read(bArr);
                    if (i2 < 0) {
                        break;
                    } else {
                        fileOutputStream.write(bArr, 0, i2);
                    }
                }
                fileOutputStream.close();
                inputStreamOpen.close();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getActionBar().setBackgroundDrawable(new ColorDrawable(SupportMenu.CATEGORY_MASK));
        setContentView(R.layout.selectorlayout);
        ListView listView = (ListView) findViewById(R.id.list);
        ArrayList arrayList = new ArrayList();
        listView.setClickable(true);
        b bVar = new b(this, arrayList);
        listView.setAdapter((ListAdapter) bVar);
        listView.setOnItemClickListener(new i(this, arrayList));
        new Thread(new m(this, arrayList, listView, bVar)).start();
        this.c = false;
        if (Build.VERSION.SDK_INT < 20) {
            new AlertDialog.Builder(this).setTitle("Warning").setMessage("For good user experience, only support Andorid5.0 + ").setNegativeButton("YES", new p(this)).create().show();
            return;
        }
        if (!XposedStatus.isModuleActive()) {
            Log.d("INJECT", "ModuleActived");
            new AlertDialog.Builder(this).setTitle("Warning").setMessage("xposed module disabled,using root mode?").setPositiveButton("YES", new q(this)).setNegativeButton("Install Xposed", new r(this)).create().show();
        }
        this.a = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.f = new s(this, this, this.a);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeAsUpIndicator(R.drawable.ic_settings);
        findViewById(R.id.joystick).setOnClickListener(new t(this));
        findViewById(R.id.help).setOnClickListener(new v(this));
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (this.f.onOptionsItemSelected(menuItem)) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
