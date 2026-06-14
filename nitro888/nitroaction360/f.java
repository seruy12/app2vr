package com.nitro888.nitroaction360;

import android.content.Intent;

/* JADX INFO: loaded from: classes.dex */
final class f implements Runnable {
    final /* synthetic */ MainActivity a;

    f(MainActivity mainActivity) {
        this.a = mainActivity;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.a.k.a(-1);
        try {
            Intent intent = new Intent();
            intent.addFlags(268435456);
            if (MainActivity.d.get(this.a.getIntent().getStringExtra("pkg")) != null) {
                intent.setClassName(this.a.getIntent().getStringExtra("pkg"), (String) MainActivity.d.get(this.a.getIntent().getStringExtra("pkg")));
            } else {
                intent.setClassName(this.a.getIntent().getStringExtra("pkg"), this.a.getIntent().getStringExtra("cls"));
            }
            this.a.n.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
