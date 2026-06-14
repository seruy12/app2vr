package com.nitro888.nitroaction360.a;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: classes.dex */
public final class c {
    public static String a(Context context, int i) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(i)));
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                String line = bufferedReader.readLine();
                if (line == null) {
                    return sb.toString();
                }
                sb.append(line);
                sb.append('\n');
            } catch (IOException e) {
                return null;
            }
        }
    }
}
