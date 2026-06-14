package com.nitro888.nitroaction360.a;

import java.io.File;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    public static int a(String str) {
        File file = new File(str);
        if (file.canRead()) {
            return file.isDirectory() ? 0 : 1;
        }
        return -1;
    }
}
