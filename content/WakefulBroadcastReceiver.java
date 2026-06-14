package android.support.v4.content;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.util.SparseArray;
import com.nibiru.lib.utils.NibiruConfig;

/* JADX INFO: loaded from: classes.dex */
public abstract class WakefulBroadcastReceiver extends BroadcastReceiver {
    private static final String EXTRA_WAKE_LOCK_ID = "android.support.content.wakelockid";
    private static final SparseArray mActiveWakeLocks = new SparseArray();
    private static int mNextId = 1;

    public static boolean completeWakefulIntent(Intent intent) {
        boolean z = false;
        int intExtra = intent.getIntExtra(EXTRA_WAKE_LOCK_ID, 0);
        if (intExtra != 0) {
            synchronized (mActiveWakeLocks) {
                PowerManager.WakeLock wakeLock = (PowerManager.WakeLock) mActiveWakeLocks.get(intExtra);
                if (wakeLock != null) {
                    wakeLock.release();
                    mActiveWakeLocks.remove(intExtra);
                    z = true;
                } else {
                    Log.w("WakefulBroadcastReceiver", "No active wake lock id #" + intExtra);
                    z = true;
                }
            }
        }
        return z;
    }

    public static ComponentName startWakefulService(Context context, Intent intent) {
        synchronized (mActiveWakeLocks) {
            int i = mNextId;
            int i2 = mNextId + 1;
            mNextId = i2;
            if (i2 <= 0) {
                mNextId = 1;
            }
            intent.putExtra(EXTRA_WAKE_LOCK_ID, i);
            ComponentName componentNameStartService = context.startService(intent);
            if (componentNameStartService == null) {
                return null;
            }
            PowerManager.WakeLock wakeLockNewWakeLock = ((PowerManager) context.getSystemService("power")).newWakeLock(1, "wake:" + componentNameStartService.flattenToShortString());
            wakeLockNewWakeLock.setReferenceCounted(false);
            wakeLockNewWakeLock.acquire(NibiruConfig.ONE_MIN);
            mActiveWakeLocks.put(i, wakeLockNewWakeLock);
            return componentNameStartService;
        }
    }
}
