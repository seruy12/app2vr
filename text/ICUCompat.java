package android.support.v4.text;

import android.os.Build;

/* JADX INFO: loaded from: classes.dex */
public class ICUCompat {
    private static final ICUCompatImpl IMPL;

    interface ICUCompatImpl {
        String addLikelySubtags(String str);

        String getScript(String str);
    }

    class ICUCompatImplBase implements ICUCompatImpl {
        ICUCompatImplBase() {
        }

        @Override // android.support.v4.text.ICUCompat.ICUCompatImpl
        public String addLikelySubtags(String str) {
            return str;
        }

        @Override // android.support.v4.text.ICUCompat.ICUCompatImpl
        public String getScript(String str) {
            return null;
        }
    }

    class ICUCompatImplIcs implements ICUCompatImpl {
        ICUCompatImplIcs() {
        }

        @Override // android.support.v4.text.ICUCompat.ICUCompatImpl
        public String addLikelySubtags(String str) {
            return ICUCompatIcs.addLikelySubtags(str);
        }

        @Override // android.support.v4.text.ICUCompat.ICUCompatImpl
        public String getScript(String str) {
            return ICUCompatIcs.getScript(str);
        }
    }

    static {
        if (Build.VERSION.SDK_INT >= 14) {
            IMPL = new ICUCompatImplIcs();
        } else {
            IMPL = new ICUCompatImplBase();
        }
    }

    public static String addLikelySubtags(String str) {
        return IMPL.addLikelySubtags(str);
    }

    public static String getScript(String str) {
        return IMPL.getScript(str);
    }
}
