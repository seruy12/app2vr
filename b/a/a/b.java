package com.b.a.a;

import com.c.a.g;
import com.nibiru.lib.controller.ExchangeUnit;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

/* JADX INFO: loaded from: classes.dex */
public final class b implements Flushable {
    private static final MessageDigest a;
    private final com.c.a.a b;
    private final AtomicInteger c = new AtomicInteger(0);
    private final AtomicInteger d = new AtomicInteger(0);

    static {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            messageDigest = null;
        }
        a = messageDigest;
    }

    private b(File file) throws IOException {
        if (a == null) {
            throw new IOException("No SHA-1 algorithm available");
        }
        this.b = com.c.a.a.a(file);
    }

    public static b a(File file) {
        try {
            return new b(file);
        } catch (IOException e) {
            return null;
        }
    }

    private static String a(String str) {
        byte[] bArrDigest;
        try {
            byte[] bytes = str.getBytes(ExchangeUnit.CHARSET);
            synchronized (a) {
                a.reset();
                bArrDigest = a.digest(bytes);
            }
            String string = new BigInteger(1, bArrDigest).toString(16);
            int length = 40 - string.length();
            if (length == 0) {
                return string;
            }
            char[] cArr = new char[length];
            Arrays.fill(cArr, '0');
            return new StringBuilder(40).append(cArr).append(string).toString();
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private static boolean a(HttpURLConnection httpURLConnection) {
        try {
            if ("GET".equals(httpURLConnection.getRequestMethod())) {
                return 200 == httpURLConnection.getResponseCode();
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private static String b(URLConnection uRLConnection) {
        if (uRLConnection instanceof HttpURLConnection) {
            return a(uRLConnection.getURL().toExternalForm());
        }
        return null;
    }

    final int a() {
        return this.c.incrementAndGet();
    }

    public final c a(URLConnection uRLConnection) {
        InputStream inputStreamA;
        String strB = b(uRLConnection);
        if (strB == null) {
            return null;
        }
        try {
            g gVarA = this.b.a(strB);
            if (gVarA == null) {
                return null;
            }
            try {
                String strB2 = gVarA.b();
                if (strB2 == null || strB2.length() <= 0 || (inputStreamA = gVarA.a()) == null) {
                    return null;
                }
                return new c(strB2, inputStreamA, gVarA, (byte) 0);
            } catch (IOException e) {
                return null;
            }
        } catch (IOException e2) {
            return null;
        }
    }

    public final InputStream a(URLConnection uRLConnection, boolean z) {
        String strB = b(uRLConnection);
        if (strB == null) {
            return null;
        }
        if (!a((HttpURLConnection) uRLConnection)) {
            try {
                this.b.c(strB);
                return null;
            } catch (IOException e) {
                return null;
            }
        }
        String headerField = uRLConnection.getHeaderField("ETag");
        if (headerField == null || headerField.length() == 0) {
            return null;
        }
        try {
            com.c.a.d dVarB = this.b.b(strB);
            if (dVarB == null) {
                return null;
            }
            try {
                dVarB.a(headerField);
                try {
                    InputStream inputStream = uRLConnection.getInputStream();
                    InputStream gZIPInputStream = ((inputStream instanceof InflaterInputStream) || !"gzip".equals(uRLConnection.getHeaderField("Content-Encoding"))) ? inputStream : new GZIPInputStream(inputStream);
                    try {
                        OutputStream outputStreamA = dVarB.a(1);
                        if (outputStreamA != null) {
                            return new d(gZIPInputStream, outputStreamA, dVarB, this, z);
                        }
                        return null;
                    } catch (IOException e2) {
                        return null;
                    }
                } catch (IOException e3) {
                    return null;
                }
            } catch (IOException e4) {
                try {
                    dVarB.b();
                } catch (IOException e5) {
                }
                return null;
            }
        } catch (IOException e6) {
            return null;
        }
    }

    final int b() {
        return this.d.incrementAndGet();
    }

    @Override // java.io.Flushable
    public final void flush() {
        this.b.a();
    }
}
