package com.c.a;

import java.util.concurrent.Callable;

/* JADX INFO: loaded from: classes.dex */
final class b implements Callable {
    final /* synthetic */ a a;

    b(a aVar) {
        this.a = aVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // java.util.concurrent.Callable
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public Void call() {
        synchronized (this.a) {
            if (this.a.k != null) {
                this.a.h();
                if (this.a.f()) {
                    this.a.e();
                    this.a.m = 0;
                }
            }
        }
        return null;
    }
}
