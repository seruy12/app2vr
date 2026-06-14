package com.vision;

import android.app.Activity;
import android.app.ActivityManagerNative;
import android.app.IActivityContainer;
import android.app.IActivityContainerCallback;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.IIntentSender;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.OperationCanceledException;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import android.view.InputEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.WindowManager;
import com.google.vrtoolkit.cardboard.BuildConfig;
import dalvik.system.CloseGuard;
import java.lang.ref.WeakReference;

/* JADX INFO: loaded from: classes.dex */
public class ActivityView {
    private static final boolean DEBUG = false;
    private static final int MSG_SET_SURFACE = 1;
    private static final String TAG = "ActivityView";
    private Activity mActivity;
    private ActivityContainerWrapper mActivityContainer;
    public int mHeight;
    DisplayMetrics mMetrics = new DisplayMetrics();
    private Surface mSurface;
    public int mWidth;

    class ActivityContainerCallback extends IActivityContainerCallback.Stub {
        private final WeakReference mActivityViewWeakReference;

        ActivityContainerCallback(ActivityView activityView) {
            this.mActivityViewWeakReference = new WeakReference(activityView);
        }

        public void onAllActivitiesComplete(IBinder iBinder) {
        }

        public void setVisible(IBinder iBinder, boolean z) {
        }
    }

    class ActivityContainerWrapper {
        private final IActivityContainer mIActivityContainer;
        private final CloseGuard mGuard = CloseGuard.get();
        boolean mOpened = true;

        ActivityContainerWrapper(IActivityContainer iActivityContainer) {
            this.mIActivityContainer = iActivityContainer;
            this.mGuard.open(BuildConfig.BUILD_TYPE);
        }

        void attachToDisplay(int i) {
            try {
                this.mIActivityContainer.attachToDisplay(i);
            } catch (RemoteException e) {
            }
        }

        protected void finalize() throws Throwable {
            try {
                if (this.mGuard != null) {
                    this.mGuard.warnIfOpen();
                    release();
                }
            } finally {
                super.finalize();
            }
        }

        int getDisplayId() {
            try {
                return this.mIActivityContainer.getDisplayId();
            } catch (RemoteException e) {
                return -1;
            }
        }

        boolean injectEvent(InputEvent inputEvent) {
            try {
                return this.mIActivityContainer.injectEvent(inputEvent);
            } catch (RemoteException e) {
                return false;
            }
        }

        void release() {
            synchronized (this.mGuard) {
                if (this.mOpened) {
                    try {
                        this.mIActivityContainer.release();
                        this.mGuard.close();
                    } catch (RemoteException e) {
                    }
                    this.mOpened = false;
                }
            }
        }

        void setSurface(Surface surface, int i, int i2, int i3) {
            this.mIActivityContainer.setSurface(surface, i, i2, i3);
        }

        int startActivity(Intent intent) {
            try {
                return this.mIActivityContainer.startActivity(intent);
            } catch (RemoteException e) {
                throw new RuntimeException("ActivityView: Unable to startActivity. " + e);
            }
        }

        int startActivityIntentSender(IIntentSender iIntentSender) {
            try {
                return this.mIActivityContainer.startActivityIntentSender(iIntentSender);
            } catch (RemoteException e) {
                throw new RuntimeException("ActivityView: Unable to startActivity from IntentSender. " + e);
            }
        }
    }

    public ActivityView(Context context) {
        Context baseContext = context;
        while (true) {
            if (!(baseContext instanceof ContextWrapper)) {
                break;
            }
            if (baseContext instanceof Activity) {
                this.mActivity = (Activity) baseContext;
                break;
            }
            baseContext = ((ContextWrapper) baseContext).getBaseContext();
        }
        if (this.mActivity == null) {
            throw new IllegalStateException("The ActivityView's Context is not an Activity.");
        }
        try {
            if (Build.VERSION.SDK_INT < 23) {
                this.mActivityContainer = new ActivityContainerWrapper(ActivityManagerNative.getDefault().createActivityContainer(this.mActivity.getActivityToken(), new ActivityContainerCallback(this)));
            } else {
                this.mActivityContainer = new ActivityContainerWrapper(ActivityManagerNative.getDefault().createVirtualActivityContainer(this.mActivity.getActivityToken(), new ActivityContainerCallback(this)));
            }
            ((WindowManager) this.mActivity.getSystemService("window")).getDefaultDisplay().getMetrics(this.mMetrics);
        } catch (RemoteException e) {
            throw new RuntimeException("ActivityView: Unable to create ActivityContainer. " + e);
        }
    }

    public boolean injectInputEvent(InputEvent inputEvent) {
        return this.mActivityContainer != null && this.mActivityContainer.injectEvent(inputEvent);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return injectInputEvent(motionEvent);
    }

    public void setSurface(Surface surface) {
        this.mSurface = surface;
        try {
            this.mActivityContainer.setSurface(this.mSurface, this.mWidth, this.mHeight, this.mMetrics.densityDpi);
        } catch (RemoteException e) {
            this.mSurface.release();
            this.mSurface = null;
            throw new RuntimeException("ActivityView: Unable to create ActivityContainer. " + e);
        }
    }

    public void startActivity(Intent intent) {
        if (this.mActivityContainer == null) {
            throw new IllegalStateException("Attempt to call startActivity after release");
        }
        if (this.mSurface == null) {
            throw new IllegalStateException("Surface not yet created.");
        }
        if (this.mActivityContainer.startActivity(intent) == -6) {
            throw new OperationCanceledException();
        }
    }
}
