package com.nitro888.nitroaction360.a;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import com.nitro888.nitroaction360.MainActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public abstract class g extends AsyncTask {
    private static final String b = g.class.getSimpleName();
    protected Uri.Builder a;
    private final Context c;
    private com.b.a.a.b d;

    public g(Context context, String str) {
        this.c = context;
        this.d = com.b.a.a.b.a(new File(context.getFilesDir(), str));
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // android.os.AsyncTask
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public List doInBackground(String... strArr) {
        ArrayList arrayList = new ArrayList();
        this.a = Uri.parse("http://www.youtube.com/get_video_info?&video_id=" + strArr[0] + "&el=detailpage&ps=default&eurl=&gl=US&hl=en").buildUpon();
        String strB = b(this.a.build().toString());
        if (strB == null) {
            Log.e(b, "Failed to get url");
            return null;
        }
        try {
            Map mapA = a(strB);
            String string = mapA.get("title").toString();
            String string2 = mapA.get("length_seconds").toString();
            String[] strArrSplit = URLDecoder.decode(mapA.get("url_encoded_fmt_stream_map").toString(), "utf-8").split(",");
            for (String str : strArrSplit) {
                Map mapA2 = a(str);
                String strDecode = URLDecoder.decode(mapA2.get("fallback_host").toString(), "utf-8");
                String[] strArrSplit2 = URLDecoder.decode(mapA2.get("type").toString(), "utf-8").split(";");
                Object obj = mapA2.get("stereo3d");
                i iVar = new i(this, (byte) 0);
                iVar.a = string;
                iVar.b = Integer.parseInt(string2);
                iVar.c = String.valueOf(URLDecoder.decode(mapA2.get("url").toString(), "utf-8")) + "&fallback_host=" + strDecode;
                iVar.d = URLDecoder.decode(mapA2.get("quality").toString(), "utf-8");
                iVar.f = strArrSplit2[0];
                iVar.e = strArrSplit2.length == 2 ? strArrSplit2[1] : "";
                iVar.g = obj != null ? URLDecoder.decode(obj.toString(), "utf-8") : "";
                arrayList.add(iVar);
            }
            return arrayList;
        } catch (Exception e) {
            Log.e(b, e.getMessage(), e);
            return arrayList;
        }
    }

    private static Map a(String str) {
        HashMap map = new HashMap();
        String[] strArrSplit = str.split("&");
        for (int i = 0; i < strArrSplit.length; i++) {
            String[] strArrSplit2 = strArrSplit[i].split("=");
            if (strArrSplit2.length != 2) {
                Log.e(b, "invalid url parameter " + strArrSplit[i]);
            } else {
                map.put(strArrSplit2[0], strArrSplit2[1]);
            }
        }
        return map;
    }

    public static void a(Context context, String str) {
        new h(context, str).execute(new String[]{str, null});
    }

    private String b(String str) {
        com.b.a.a.a aVarA = com.b.a.a.a.a(str, this.d);
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(aVarA.d()));
        while (true) {
            try {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (aVarA.a()) {
            Log.d(b, "Cache hit");
        } else {
            Log.d(b, "Cache miss");
        }
        return sb.toString();
    }

    public final void a(List list) {
        String str;
        String str2;
        String str3;
        String str4 = "";
        String str5 = "";
        int i = 0;
        while (i < list.size()) {
            if (((i) list.get(i)).g.equals("1") && ((i) list.get(i)).f.equals("video/mp4")) {
                String str6 = ((i) list.get(i)).g;
                str3 = ((i) list.get(0)).c;
                str2 = str6;
            } else {
                str2 = str5;
                str3 = str4;
            }
            i++;
            str4 = str3;
            str5 = str2;
        }
        if (str4 == "") {
            str = str4;
            for (int i2 = 0; i2 < list.size(); i2++) {
                if (((i) list.get(i2)).f.equals("video/mp4")) {
                    str = ((i) list.get(i2)).c;
                }
            }
        } else {
            str = str4;
        }
        if (str != "") {
            ((MainActivity) this.c).a(str, str5.equals("1") ? 1 : 0);
        }
    }
}
