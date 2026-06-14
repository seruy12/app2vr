package com.nitro888.nitroaction360.cardboard;

import android.view.animation.Animation;

/* JADX INFO: loaded from: classes.dex */
final class a extends c {
    final /* synthetic */ NACardboardOverlayView a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    a(NACardboardOverlayView nACardboardOverlayView) {
        super(nACardboardOverlayView, (byte) 0);
        this.a = nACardboardOverlayView;
    }

    @Override // android.view.animation.Animation.AnimationListener
    public final void onAnimationEnd(Animation animation) {
        this.a.a(0.0f);
    }
}
