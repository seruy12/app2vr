package com.nitro888.nitroaction360.cardboard;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

/* JADX INFO: loaded from: classes.dex */
public class NACardboardOverlayView extends LinearLayout {
    private final b a;
    private final b b;
    private AlphaAnimation c;

    public NACardboardOverlayView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOrientation(0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1, 1.0f);
        layoutParams.setMargins(0, 0, 0, 0);
        this.a = new b(this, context, attributeSet);
        this.a.setLayoutParams(layoutParams);
        addView(this.a);
        this.b = new b(this, context, attributeSet);
        this.b.setLayoutParams(layoutParams);
        addView(this.b);
        this.a.b(0.016f);
        this.b.b(-0.016f);
        int iRgb = Color.rgb(150, 255, 180);
        this.a.a(iRgb);
        this.b.a(iRgb);
        setVisibility(0);
        this.c = new AlphaAnimation(1.0f, 0.0f);
        this.c.setDuration(5000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(float f) {
        this.a.a(f);
        this.b.a(f);
    }

    public final void a(String str) {
        this.a.a(str);
        this.b.a(str);
        a(1.0f);
        this.c.setAnimationListener(new a(this));
        startAnimation(this.c);
    }
}
