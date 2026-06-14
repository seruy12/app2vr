package com.b.a.b;

import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

/* JADX INFO: loaded from: classes.dex */
public interface b {
    public static final b a = new c();

    HttpURLConnection a(URL url);

    HttpURLConnection a(URL url, Proxy proxy);
}
