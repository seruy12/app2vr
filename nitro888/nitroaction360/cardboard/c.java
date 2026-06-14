package com.nitro888.nitroaction360.cardboard;

import android.view.animation.Animation;

/* JADX INFO: loaded from: classes.dex */
abstract class c implements Animation.AnimationListener {
    final /* synthetic */ NACardboardOverlayView b;

    private c(NACardboardOverlayView nACardboardOverlayView) {
        this.b = nACardboardOverlayView;
    }

    /* synthetic */ c(NACardboardOverlayView nACardboardOverlayView, byte b) {
        this(nACardboardOverlayView);
    }

    @Override // android.view.animation.Animation.AnimationListener
    public void onAnimationRepeat(Animation animation) {
    }

    @Override // android.view.animation.Animation.AnimationListener
    public void onAnimationStart(Animation animation) {
    }
}
