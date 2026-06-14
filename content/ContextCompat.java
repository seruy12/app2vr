package android.support.v4.content;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

/* JADX INFO: loaded from: classes.dex */
public class ContextCompat {
    public static boolean startActivities(Context context, Intent[] intentArr) {
        return startActivities(context, intentArr, null);
    }

    public static boolean startActivities(Context context, Intent[] intentArr, Bundle bundle) {
        int i = Build.VERSION.SDK_INT;
        if (i >= 16) {
            ContextCompatJellybean.startActivities(context, intentArr, bundle);
            return true;
        }
        if (i < 11) {
            return false;
        }
        ContextCompatHoneycomb.startActivities(context, intentArr);
        return true;
    }
}
