package com.nitro888.nitroaction360;

import android.app.Activity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import com.nibiru.lib.controller.R;

/* JADX INFO: loaded from: classes.dex */
final class s extends ActionBarDrawerToggle {
    final /* synthetic */ SelectorActivity a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    s(SelectorActivity selectorActivity, Activity activity, DrawerLayout drawerLayout) {
        super(activity, drawerLayout, R.drawable.ic_keyboard_arrow_right_grey600_48dp, 0, 0);
        this.a = selectorActivity;
    }

    @Override // android.support.v4.app.ActionBarDrawerToggle, android.support.v4.widget.DrawerLayout.DrawerListener
    public final void onDrawerClosed(View view) {
        this.a.invalidateOptionsMenu();
    }

    @Override // android.support.v4.app.ActionBarDrawerToggle, android.support.v4.widget.DrawerLayout.DrawerListener
    public final void onDrawerOpened(View view) {
        this.a.invalidateOptionsMenu();
    }
}
