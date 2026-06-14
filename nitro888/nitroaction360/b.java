package com.nitro888.nitroaction360;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nibiru.lib.controller.R;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class b extends ArrayAdapter {
    LayoutInflater a;
    PackageManager b;
    int c;

    public b(Context context, List list) {
        super(context, R.layout.app_row, list);
        this.a = LayoutInflater.from(context);
        this.b = context.getPackageManager();
        this.c = R.layout.app_row;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public final View getView(int i, View view, ViewGroup viewGroup) {
        e eVar;
        View viewInflate;
        ActivityInfo activityInfo = (ActivityInfo) getItem(i);
        if (view == null) {
            viewInflate = this.a.inflate(this.c, viewGroup, false);
            eVar = new e(this);
            eVar.a = (ImageView) viewInflate.findViewById(R.id.icon);
            eVar.b = (TextView) viewInflate.findViewById(R.id.name);
            viewInflate.setTag(eVar);
        } else {
            eVar = (e) view.getTag();
            viewInflate = view;
        }
        eVar.c = i;
        new Thread(new c(this, activityInfo, viewInflate, eVar, i)).start();
        return viewInflate;
    }
}
