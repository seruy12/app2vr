package com.nitro888.nitroaction360;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Toast;
import com.nibiru.lib.controller.R;
import java.io.File;

/* JADX INFO: loaded from: classes.dex */
final class g implements Runnable {
    final /* synthetic */ MainActivity a;

    g(MainActivity mainActivity) {
        this.a = mainActivity;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.a.findViewById(R.id.game_img).setVisibility(0);
        if (new File("/sdcard/g.png").exists()) {
            this.a.findViewById(R.id.game_img).setBackground(new BitmapDrawable(BitmapFactory.decodeFile("/sdcard/g.png")));
        } else {
            Toast.makeText(this.a, "you should put game screenshot in /sdcard/g.png first,then mapping joystick to touch screen", 1).show();
            this.a.finish();
        }
    }
}
