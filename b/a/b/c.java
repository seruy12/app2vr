package com.b.a.b;

import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

/* JADX INFO: loaded from: classes.dex */
final class c implements b {
    c() {
    }

    @Override // com.b.a.b.b
    public final HttpURLConnection a(URL url) {
        return (HttpURLConnection) url.openConnection();
    }

    @Override // com.b.a.b.b
    public final HttpURLConnection a(URL url, Proxy proxy) {
        return (HttpURLConnection) url.openConnection(proxy);
    }
}
