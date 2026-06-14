package com.b.a.b;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.zip.GZIPInputStream;

/* JADX INFO: loaded from: classes.dex */
public class a {
    private static final String[] a = new String[0];
    private static b b = b.a;
    private final URL d;
    private final String e;
    private e f;
    private boolean g;
    private String m;
    private int n;
    private HttpURLConnection c = null;
    private boolean h = true;
    private boolean i = false;
    private int j = 8192;
    private long k = -1;
    private long l = 0;
    private f o = f.a;

    public a(CharSequence charSequence, String str) {
        try {
            this.d = new URL(charSequence.toString());
            this.e = str;
        } catch (MalformedURLException e) {
            throw new d(e);
        }
    }

    private HttpURLConnection a() {
        try {
            HttpURLConnection httpURLConnectionA = this.m != null ? b.a(this.d, new Proxy(Proxy.Type.HTTP, new InetSocketAddress(this.m, this.n))) : b.a(this.d);
            httpURLConnectionA.setRequestMethod(this.e);
            return httpURLConnectionA;
        } catch (IOException e) {
            throw new d(e);
        }
    }

    private a f() {
        try {
            return b();
        } catch (IOException e) {
            throw new d(e);
        }
    }

    protected a b() throws IOException {
        this.o = f.a;
        if (this.f != null) {
            if (this.g) {
                this.f.a("\r\n--00content0boundary00--\r\n");
            }
            if (this.h) {
                try {
                    this.f.close();
                } catch (IOException e) {
                }
            } else {
                this.f.close();
            }
            this.f = null;
        }
        return this;
    }

    public int c() {
        try {
            b();
            return e().getResponseCode();
        } catch (IOException e) {
            throw new d(e);
        }
    }

    public InputStream d() {
        InputStream inputStream;
        if (c() < 400) {
            try {
                inputStream = e().getInputStream();
            } catch (IOException e) {
                throw new d(e);
            }
        } else {
            inputStream = e().getErrorStream();
            if (inputStream == null) {
                try {
                    inputStream = e().getInputStream();
                } catch (IOException e2) {
                    f();
                    if (e().getHeaderFieldInt("Content-Length", -1) > 0) {
                        throw new d(e2);
                    }
                    inputStream = new ByteArrayInputStream(new byte[0]);
                }
            }
        }
        if (!this.i) {
            return inputStream;
        }
        f();
        if (!"gzip".equals(e().getHeaderField("Content-Encoding"))) {
            return inputStream;
        }
        try {
            return new GZIPInputStream(inputStream);
        } catch (IOException e3) {
            throw new d(e3);
        }
    }

    public final HttpURLConnection e() {
        if (this.c == null) {
            this.c = a();
        }
        return this.c;
    }

    public String toString() {
        return String.valueOf(e().getRequestMethod()) + ' ' + e().getURL();
    }
}
