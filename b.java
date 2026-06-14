package com.nitro888.nitroaction360.cardboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/* JADX INFO: loaded from: classes.dex */
final class b extends ViewGroup {
    final /* synthetic */ NACardboardOverlayView a;
    private final ImageView b;
    private final TextView c;
    private float d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public b(NACardboardOverlayView nACardboardOverlayView, Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.a = nACardboardOverlayView;
        this.b = new ImageView(context, attributeSet);
        this.b.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        this.b.setAdjustViewBounds(true);
        addView(this.b);
        this.c = new TextView(context, attributeSet);
        this.c.setTextSize(1, 14.0f);
        this.c.setTypeface(this.c.getTypeface(), 1);
        this.c.setGravity(17);
        this.c.setShadowLayer(3.0f, 0.0f, 0.0f, -12303292);
        addView(this.c);
    }

    public final void a(float f) {
        this.c.setAlpha(f);
    }

    public final void a(int i) {
        this.b.setColorFilter(i);
        this.c.setTextColor(i);
    }

    public final void a(String str) {
        this.c.setText(str);
    }

    public final void b(float f) {
        this.d = f;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5 = i3 - i;
        int i6 = i4 - i2;
        float f = (int) (i5 * (0.44f + this.d));
        float f2 = (int) (i6 * 0.37f);
        this.b.layout((int) f, (int) f2, (int) (f + (i5 * 0.12f)), (int) (f2 + (i6 * 0.12f)));
        float f3 = this.d * i5;
        float f4 = i6 * 0.52f;
        this.c.layout((int) f3, (int) f4, (int) (i5 + f3), (int) ((i6 * 0.48000002f) + f4));
    }
}
