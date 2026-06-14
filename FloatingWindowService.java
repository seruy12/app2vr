package com.hmct.screencapture;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

/* JADX INFO: loaded from: classes.dex */
public class FloatingWindowService extends Service {
    private static Context a;

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        Log.v("vr", "111newConfig.orientation = " + configuration.orientation);
        if (configuration.orientation == 1) {
            a.b();
            a.a();
            try {
                ((WindowManager) getApplicationContext().getSystemService("window")).removeView(a.a);
            } catch (Exception e) {
            }
            stopSelf();
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        a = getApplicationContext();
        startForeground(100, new Notification(0, null, System.currentTimeMillis()));
    }

    @Override // android.app.Service
    public void onDestroy() {
        try {
            ((WindowManager) getApplicationContext().getSystemService("window")).removeView(a.a);
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        String action = intent.getAction();
        if ("com.hmct.vrmode.off".equals(action)) {
            a.b();
            a.a();
            try {
                ((WindowManager) getApplicationContext().getSystemService("window")).removeView(a.a);
            } catch (Exception e) {
            }
            stopSelf();
        }
        if ("com.hmct.vrmode.on".equals(action)) {
            if (a.a != null) {
                Toast.makeText(this, "please retry", 1).show();
                System.exit(0);
            }
            Intent intent2 = new Intent();
            intent2.setClass(this, MainActivity.class);
            intent2.addFlags(268435456);
            startActivity(intent2);
        }
        return super.onStartCommand(intent, i, i2);
    }
}
