package com.hmct.screencapture;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Window;
import com.nibiru.lib.controller.R;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes.dex */
public class MainActivity extends FragmentActivity {
    private static WeakReference a;

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        a = new WeakReference(this);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_main);
        if (bundle == null) {
            FragmentTransaction fragmentTransactionBeginTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransactionBeginTransaction.replace(R.id.sample_content_fragment, new a());
            fragmentTransactionBeginTransaction.commit();
        }
        getWindow().setGravity(5);
        Window window = getWindow();
        int i = getResources().getDisplayMetrics().widthPixels;
        int i2 = getResources().getDisplayMetrics().heightPixels;
        int identifier = getResources().getIdentifier("status_bar_height", "dimen", "android");
        window.setLayout(i, (identifier > 0 ? getResources().getDimensionPixelSize(identifier) : 0) + i2);
        if (getResources().getConfiguration().orientation == 1) {
            Log.v("MainActivity", "is port we return");
        }
    }
}
