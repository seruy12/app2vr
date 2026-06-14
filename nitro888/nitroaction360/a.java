package com.nitro888.nitroaction360;

import android.util.Log;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public abstract class a {
    public final String a(String str) {
        try {
            try {
                ArrayList arrayListA = a();
                if (arrayListA != null && arrayListA.size() > 0) {
                    Process processExec = Runtime.getRuntime().exec("su");
                    DataOutputStream dataOutputStream = new DataOutputStream(processExec.getOutputStream());
                    Iterator it = arrayListA.iterator();
                    while (it.hasNext()) {
                        dataOutputStream.writeBytes(String.valueOf((String) it.next()) + "\n");
                        dataOutputStream.flush();
                    }
                    dataOutputStream.writeBytes("exit\n");
                    dataOutputStream.flush();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(processExec.getInputStream()));
                    StringBuffer stringBuffer = new StringBuffer();
                    for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                        if (line.indexOf(str) != -1) {
                            return line;
                        }
                    }
                    try {
                        processExec.waitFor();
                        System.out.println("BBBB: " + stringBuffer.toString());
                    } catch (Exception e) {
                    }
                }
            } catch (IOException e2) {
                Log.w("ROOT", "Can't get root access", e2);
            } catch (SecurityException e3) {
                Log.w("ROOT", "Can't get root access", e3);
            }
        } catch (Exception e4) {
            Log.w("ROOT", "Error executing internal operation", e4);
        }
        return "";
    }

    protected abstract ArrayList a();
}
