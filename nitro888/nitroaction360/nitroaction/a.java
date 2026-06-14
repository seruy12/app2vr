package com.nitro888.nitroaction360.nitroaction;

import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import java.net.URL;

/* JADX INFO: loaded from: classes.dex */
final class a extends AsyncTask {
    final /* synthetic */ NAGUIRelativeLayout a;
    private View b;

    public a(NAGUIRelativeLayout nAGUIRelativeLayout, View view) {
        this.a = nAGUIRelativeLayout;
        this.b = view;
    }

    private BitmapDrawable a(String str) {
        try {
            return new BitmapDrawable(this.a.getResources(), new URL(str).openStream());
        } catch (Exception e) {
            Log.e(NAGUIRelativeLayout.a, e.getMessage(), e);
            return null;
        }
    }

    @Override // android.os.AsyncTask
    protected final /* synthetic */ Object doInBackground(Object... objArr) {
        return a(((String[]) objArr)[0]);
    }

    @Override // android.os.AsyncTask
    protected final /* synthetic */ void onPostExecute(Object obj) {
        this.b.setVisibility(0);
        this.b.setBackground((BitmapDrawable) obj);
    }
}
