package android.media.projection;

import android.content.Context;
import android.hardware.display.VirtualDisplay;
import android.media.AudioRecord;
import android.os.Handler;
import android.view.Surface;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class MediaProjection {
    private static final String TAG = "MediaProjection";
    private Map mCallbacks;
    private Context mContext;

    public abstract class Callback {
        public void onStop() {
        }
    }

    final class CallbackRecord {
        private final Callback mCallback;
        private final Handler mHandler;

        public CallbackRecord(Callback callback, Handler handler) {
            this.mCallback = callback;
            this.mHandler = handler;
        }

        public final void onStop() {
            this.mHandler.post(new Runnable() { // from class: android.media.projection.MediaProjection.CallbackRecord.1
                @Override // java.lang.Runnable
                public void run() {
                    CallbackRecord.this.mCallback.onStop();
                }
            });
        }
    }

    public final AudioRecord createAudioRecord(int i, int i2, int i3, int i4) {
        return null;
    }

    public final VirtualDisplay createVirtualDisplay(String str, int i, int i2, int i3, int i4, Surface surface, VirtualDisplay.Callback callback, Handler handler) {
        return null;
    }

    public final void registerCallback(Callback callback, Handler handler) {
        if (callback == null) {
            throw new IllegalArgumentException("callback should not be null");
        }
        if (handler == null) {
            handler = new Handler();
        }
        this.mCallbacks.put(callback, new CallbackRecord(callback, handler));
    }

    public final void stop() {
    }

    public final void unregisterCallback(Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback should not be null");
        }
        this.mCallbacks.remove(callback);
    }
}
