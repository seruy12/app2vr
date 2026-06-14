package android.hardware.display;

import android.view.Display;
import android.view.Surface;

/* JADX INFO: loaded from: classes.dex */
public final class VirtualDisplay {

    public abstract class Callback {
        public void onPaused() {
        }

        public void onResumed() {
        }

        public void onStopped() {
        }
    }

    public final Display getDisplay() {
        return null;
    }

    public final Surface getSurface() {
        return null;
    }

    public final void release() {
    }

    public final void resize(int i, int i2, int i3) {
    }

    public final void setSurface(Surface surface) {
    }

    public final String toString() {
        return null;
    }
}
