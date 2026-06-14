package android.support.v4.app;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.LogWriter;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
final class FragmentManagerImpl extends FragmentManager {
    static final Interpolator ACCELERATE_CUBIC;
    static final Interpolator ACCELERATE_QUINT;
    static final int ANIM_DUR = 220;
    public static final int ANIM_STYLE_CLOSE_ENTER = 3;
    public static final int ANIM_STYLE_CLOSE_EXIT = 4;
    public static final int ANIM_STYLE_FADE_ENTER = 5;
    public static final int ANIM_STYLE_FADE_EXIT = 6;
    public static final int ANIM_STYLE_OPEN_ENTER = 1;
    public static final int ANIM_STYLE_OPEN_EXIT = 2;
    static boolean DEBUG = false;
    static final Interpolator DECELERATE_CUBIC;
    static final Interpolator DECELERATE_QUINT;
    static final boolean HONEYCOMB;
    static final String TAG = "FragmentManager";
    static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    static final String TARGET_STATE_TAG = "android:target_state";
    static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    static final String VIEW_STATE_TAG = "android:view_state";
    ArrayList mActive;
    FragmentActivity mActivity;
    ArrayList mAdded;
    ArrayList mAvailBackStackIndices;
    ArrayList mAvailIndices;
    ArrayList mBackStack;
    ArrayList mBackStackChangeListeners;
    ArrayList mBackStackIndices;
    FragmentContainer mContainer;
    ArrayList mCreatedMenus;
    boolean mDestroyed;
    boolean mExecutingActions;
    boolean mHavePendingDeferredStart;
    boolean mNeedMenuInvalidate;
    String mNoTransactionsBecause;
    Fragment mParent;
    ArrayList mPendingActions;
    boolean mStateSaved;
    Runnable[] mTmpActions;
    int mCurState = 0;
    Bundle mStateBundle = null;
    SparseArray mStateArray = null;
    Runnable mExecCommit = new Runnable() { // from class: android.support.v4.app.FragmentManagerImpl.1
        @Override // java.lang.Runnable
        public void run() {
            FragmentManagerImpl.this.execPendingActions();
        }
    };

    static {
        HONEYCOMB = Build.VERSION.SDK_INT >= 11;
        DECELERATE_QUINT = new DecelerateInterpolator(2.5f);
        DECELERATE_CUBIC = new DecelerateInterpolator(1.5f);
        ACCELERATE_QUINT = new AccelerateInterpolator(2.5f);
        ACCELERATE_CUBIC = new AccelerateInterpolator(1.5f);
    }

    FragmentManagerImpl() {
    }

    private void checkStateLoss() {
        if (this.mStateSaved) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        }
        if (this.mNoTransactionsBecause != null) {
            throw new IllegalStateException("Can not perform this action inside of " + this.mNoTransactionsBecause);
        }
    }

    static Animation makeFadeAnimation(Context context, float f, float f2) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(f, f2);
        alphaAnimation.setInterpolator(DECELERATE_CUBIC);
        alphaAnimation.setDuration(220L);
        return alphaAnimation;
    }

    static Animation makeOpenCloseAnimation(Context context, float f, float f2, float f3, float f4) {
        AnimationSet animationSet = new AnimationSet(false);
        ScaleAnimation scaleAnimation = new ScaleAnimation(f, f2, f, f2, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setInterpolator(DECELERATE_QUINT);
        scaleAnimation.setDuration(220L);
        animationSet.addAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(f3, f4);
        alphaAnimation.setInterpolator(DECELERATE_CUBIC);
        alphaAnimation.setDuration(220L);
        animationSet.addAnimation(alphaAnimation);
        return animationSet;
    }

    public static int reverseTransit(int i) {
        switch (i) {
            case FragmentTransaction.TRANSIT_FRAGMENT_OPEN /* 4097 */:
                return FragmentTransaction.TRANSIT_FRAGMENT_CLOSE;
            case FragmentTransaction.TRANSIT_FRAGMENT_FADE /* 4099 */:
                return FragmentTransaction.TRANSIT_FRAGMENT_FADE;
            case FragmentTransaction.TRANSIT_FRAGMENT_CLOSE /* 8194 */:
                return FragmentTransaction.TRANSIT_FRAGMENT_OPEN;
            default:
                return 0;
        }
    }

    private void throwException(RuntimeException runtimeException) {
        Log.e(TAG, runtimeException.getMessage());
        Log.e(TAG, "Activity state:");
        PrintWriter printWriter = new PrintWriter(new LogWriter(TAG));
        if (this.mActivity != null) {
            try {
                this.mActivity.dump("  ", null, printWriter, new String[0]);
                throw runtimeException;
            } catch (Exception e) {
                Log.e(TAG, "Failed dumping state", e);
                throw runtimeException;
            }
        }
        try {
            dump("  ", null, printWriter, new String[0]);
            throw runtimeException;
        } catch (Exception e2) {
            Log.e(TAG, "Failed dumping state", e2);
            throw runtimeException;
        }
    }

    public static int transitToStyleIndex(int i, boolean z) {
        switch (i) {
            case FragmentTransaction.TRANSIT_FRAGMENT_OPEN /* 4097 */:
                return z ? 1 : 2;
            case FragmentTransaction.TRANSIT_FRAGMENT_FADE /* 4099 */:
                return z ? 5 : 6;
            case FragmentTransaction.TRANSIT_FRAGMENT_CLOSE /* 8194 */:
                return z ? 3 : 4;
            default:
                return -1;
        }
    }

    final void addBackStackState(BackStackRecord backStackRecord) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList();
        }
        this.mBackStack.add(backStackRecord);
        reportBackStackChanged();
    }

    public final void addFragment(Fragment fragment, boolean z) {
        if (this.mAdded == null) {
            this.mAdded = new ArrayList();
        }
        if (DEBUG) {
            Log.v(TAG, "add: " + fragment);
        }
        makeActive(fragment);
        if (fragment.mDetached) {
            return;
        }
        if (this.mAdded.contains(fragment)) {
            throw new IllegalStateException("Fragment already added: " + fragment);
        }
        this.mAdded.add(fragment);
        fragment.mAdded = true;
        fragment.mRemoving = false;
        if (fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        if (z) {
            moveToState(fragment);
        }
    }

    @Override // android.support.v4.app.FragmentManager
    public final void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener onBackStackChangedListener) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList();
        }
        this.mBackStackChangeListeners.add(onBackStackChangedListener);
    }

    public final int allocBackStackIndex(BackStackRecord backStackRecord) {
        int size;
        synchronized (this) {
            if (this.mAvailBackStackIndices == null || this.mAvailBackStackIndices.size() <= 0) {
                if (this.mBackStackIndices == null) {
                    this.mBackStackIndices = new ArrayList();
                }
                size = this.mBackStackIndices.size();
                if (DEBUG) {
                    Log.v(TAG, "Setting back stack index " + size + " to " + backStackRecord);
                }
                this.mBackStackIndices.add(backStackRecord);
            } else {
                size = ((Integer) this.mAvailBackStackIndices.remove(this.mAvailBackStackIndices.size() - 1)).intValue();
                if (DEBUG) {
                    Log.v(TAG, "Adding back stack index " + size + " with " + backStackRecord);
                }
                this.mBackStackIndices.set(size, backStackRecord);
            }
        }
        return size;
    }

    public final void attachActivity(FragmentActivity fragmentActivity, FragmentContainer fragmentContainer, Fragment fragment) {
        if (this.mActivity != null) {
            throw new IllegalStateException("Already attached");
        }
        this.mActivity = fragmentActivity;
        this.mContainer = fragmentContainer;
        this.mParent = fragment;
    }

    public final void attachFragment(Fragment fragment, int i, int i2) {
        if (DEBUG) {
            Log.v(TAG, "attach: " + fragment);
        }
        if (fragment.mDetached) {
            fragment.mDetached = false;
            if (fragment.mAdded) {
                return;
            }
            if (this.mAdded == null) {
                this.mAdded = new ArrayList();
            }
            if (this.mAdded.contains(fragment)) {
                throw new IllegalStateException("Fragment already added: " + fragment);
            }
            if (DEBUG) {
                Log.v(TAG, "add from attach: " + fragment);
            }
            this.mAdded.add(fragment);
            fragment.mAdded = true;
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            moveToState(fragment, this.mCurState, i, i2, false);
        }
    }

    @Override // android.support.v4.app.FragmentManager
    public final FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }

    public final void detachFragment(Fragment fragment, int i, int i2) {
        if (DEBUG) {
            Log.v(TAG, "detach: " + fragment);
        }
        if (fragment.mDetached) {
            return;
        }
        fragment.mDetached = true;
        if (fragment.mAdded) {
            if (this.mAdded != null) {
                if (DEBUG) {
                    Log.v(TAG, "remove from detach: " + fragment);
                }
                this.mAdded.remove(fragment);
            }
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mAdded = false;
            moveToState(fragment, 1, i, i2, false);
        }
    }

    public final void dispatchActivityCreated() {
        this.mStateSaved = false;
        moveToState(2, false);
    }

    public final void dispatchConfigurationChanged(Configuration configuration) {
        if (this.mAdded == null) {
            return;
        }
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= this.mAdded.size()) {
                return;
            }
            Fragment fragment = (Fragment) this.mAdded.get(i2);
            if (fragment != null) {
                fragment.performConfigurationChanged(configuration);
            }
            i = i2 + 1;
        }
    }

    public final boolean dispatchContextItemSelected(MenuItem menuItem) {
        if (this.mAdded == null) {
            return false;
        }
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = (Fragment) this.mAdded.get(i);
            if (fragment != null && fragment.performContextItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }

    public final void dispatchCreate() {
        this.mStateSaved = false;
        moveToState(1, false);
    }

    public final boolean dispatchCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        boolean z;
        ArrayList arrayList = null;
        if (this.mAdded != null) {
            int i = 0;
            z = false;
            while (i < this.mAdded.size()) {
                Fragment fragment = (Fragment) this.mAdded.get(i);
                if (fragment != null && fragment.performCreateOptionsMenu(menu, menuInflater)) {
                    z = true;
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(fragment);
                }
                i++;
                z = z;
            }
        } else {
            z = false;
        }
        if (this.mCreatedMenus != null) {
            for (int i2 = 0; i2 < this.mCreatedMenus.size(); i2++) {
                Fragment fragment2 = (Fragment) this.mCreatedMenus.get(i2);
                if (arrayList == null || !arrayList.contains(fragment2)) {
                    fragment2.onDestroyOptionsMenu();
                }
            }
        }
        this.mCreatedMenus = arrayList;
        return z;
    }

    public final void dispatchDestroy() {
        this.mDestroyed = true;
        execPendingActions();
        moveToState(0, false);
        this.mActivity = null;
        this.mContainer = null;
        this.mParent = null;
    }

    public final void dispatchDestroyView() {
        moveToState(1, false);
    }

    public final void dispatchLowMemory() {
        if (this.mAdded == null) {
            return;
        }
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= this.mAdded.size()) {
                return;
            }
            Fragment fragment = (Fragment) this.mAdded.get(i2);
            if (fragment != null) {
                fragment.performLowMemory();
            }
            i = i2 + 1;
        }
    }

    public final boolean dispatchOptionsItemSelected(MenuItem menuItem) {
        if (this.mAdded == null) {
            return false;
        }
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = (Fragment) this.mAdded.get(i);
            if (fragment != null && fragment.performOptionsItemSelected(menuItem)) {
                return true;
            }
        }
        return false;
    }

    public final void dispatchOptionsMenuClosed(Menu menu) {
        if (this.mAdded == null) {
            return;
        }
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= this.mAdded.size()) {
                return;
            }
            Fragment fragment = (Fragment) this.mAdded.get(i2);
            if (fragment != null) {
                fragment.performOptionsMenuClosed(menu);
            }
            i = i2 + 1;
        }
    }

    public final void dispatchPause() {
        moveToState(4, false);
    }

    public final boolean dispatchPrepareOptionsMenu(Menu menu) {
        if (this.mAdded == null) {
            return false;
        }
        boolean z = false;
        for (int i = 0; i < this.mAdded.size(); i++) {
            Fragment fragment = (Fragment) this.mAdded.get(i);
            if (fragment != null && fragment.performPrepareOptionsMenu(menu)) {
                z = true;
            }
        }
        return z;
    }

    public final void dispatchReallyStop() {
        moveToState(2, false);
    }

    public final void dispatchResume() {
        this.mStateSaved = false;
        moveToState(5, false);
    }

    public final void dispatchStart() {
        this.mStateSaved = false;
        moveToState(4, false);
    }

    public final void dispatchStop() {
        this.mStateSaved = true;
        moveToState(3, false);
    }

    @Override // android.support.v4.app.FragmentManager
    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        int size;
        int size2;
        int size3;
        int size4;
        int size5;
        int size6;
        String str2 = str + "    ";
        if (this.mActive != null && (size6 = this.mActive.size()) > 0) {
            printWriter.print(str);
            printWriter.print("Active Fragments in ");
            printWriter.print(Integer.toHexString(System.identityHashCode(this)));
            printWriter.println(":");
            for (int i = 0; i < size6; i++) {
                Fragment fragment = (Fragment) this.mActive.get(i);
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i);
                printWriter.print(": ");
                printWriter.println(fragment);
                if (fragment != null) {
                    fragment.dump(str2, fileDescriptor, printWriter, strArr);
                }
            }
        }
        if (this.mAdded != null && (size5 = this.mAdded.size()) > 0) {
            printWriter.print(str);
            printWriter.println("Added Fragments:");
            for (int i2 = 0; i2 < size5; i2++) {
                Fragment fragment2 = (Fragment) this.mAdded.get(i2);
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i2);
                printWriter.print(": ");
                printWriter.println(fragment2.toString());
            }
        }
        if (this.mCreatedMenus != null && (size4 = this.mCreatedMenus.size()) > 0) {
            printWriter.print(str);
            printWriter.println("Fragments Created Menus:");
            for (int i3 = 0; i3 < size4; i3++) {
                Fragment fragment3 = (Fragment) this.mCreatedMenus.get(i3);
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i3);
                printWriter.print(": ");
                printWriter.println(fragment3.toString());
            }
        }
        if (this.mBackStack != null && (size3 = this.mBackStack.size()) > 0) {
            printWriter.print(str);
            printWriter.println("Back Stack:");
            for (int i4 = 0; i4 < size3; i4++) {
                BackStackRecord backStackRecord = (BackStackRecord) this.mBackStack.get(i4);
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i4);
                printWriter.print(": ");
                printWriter.println(backStackRecord.toString());
                backStackRecord.dump(str2, fileDescriptor, printWriter, strArr);
            }
        }
        synchronized (this) {
            if (this.mBackStackIndices != null && (size2 = this.mBackStackIndices.size()) > 0) {
                printWriter.print(str);
                printWriter.println("Back Stack Indices:");
                for (int i5 = 0; i5 < size2; i5++) {
                    Object obj = (BackStackRecord) this.mBackStackIndices.get(i5);
                    printWriter.print(str);
                    printWriter.print("  #");
                    printWriter.print(i5);
                    printWriter.print(": ");
                    printWriter.println(obj);
                }
            }
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                printWriter.print(str);
                printWriter.print("mAvailBackStackIndices: ");
                printWriter.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
            }
        }
        if (this.mPendingActions != null && (size = this.mPendingActions.size()) > 0) {
            printWriter.print(str);
            printWriter.println("Pending Actions:");
            for (int i6 = 0; i6 < size; i6++) {
                Object obj2 = (Runnable) this.mPendingActions.get(i6);
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i6);
                printWriter.print(": ");
                printWriter.println(obj2);
            }
        }
        printWriter.print(str);
        printWriter.println("FragmentManager misc state:");
        printWriter.print(str);
        printWriter.print("  mActivity=");
        printWriter.println(this.mActivity);
        printWriter.print(str);
        printWriter.print("  mContainer=");
        printWriter.println(this.mContainer);
        if (this.mParent != null) {
            printWriter.print(str);
            printWriter.print("  mParent=");
            printWriter.println(this.mParent);
        }
        printWriter.print(str);
        printWriter.print("  mCurState=");
        printWriter.print(this.mCurState);
        printWriter.print(" mStateSaved=");
        printWriter.print(this.mStateSaved);
        printWriter.print(" mDestroyed=");
        printWriter.println(this.mDestroyed);
        if (this.mNeedMenuInvalidate) {
            printWriter.print(str);
            printWriter.print("  mNeedMenuInvalidate=");
            printWriter.println(this.mNeedMenuInvalidate);
        }
        if (this.mNoTransactionsBecause != null) {
            printWriter.print(str);
            printWriter.print("  mNoTransactionsBecause=");
            printWriter.println(this.mNoTransactionsBecause);
        }
        if (this.mAvailIndices == null || this.mAvailIndices.size() <= 0) {
            return;
        }
        printWriter.print(str);
        printWriter.print("  mAvailIndices: ");
        printWriter.println(Arrays.toString(this.mAvailIndices.toArray()));
    }

    public final void enqueueAction(Runnable runnable, boolean z) {
        if (!z) {
            checkStateLoss();
        }
        synchronized (this) {
            if (this.mActivity == null) {
                throw new IllegalStateException("Activity has been destroyed");
            }
            if (this.mPendingActions == null) {
                this.mPendingActions = new ArrayList();
            }
            this.mPendingActions.add(runnable);
            if (this.mPendingActions.size() == 1) {
                this.mActivity.mHandler.removeCallbacks(this.mExecCommit);
                this.mActivity.mHandler.post(this.mExecCommit);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x0083, code lost:
    
        r6.mExecutingActions = true;
        r1 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0086, code lost:
    
        if (r1 >= r3) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0088, code lost:
    
        r6.mTmpActions[r1].run();
        r6.mTmpActions[r1] = null;
        r1 = r1 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final boolean execPendingActions() {
        /*
            r6 = this;
            r0 = 1
            r2 = 0
            boolean r1 = r6.mExecutingActions
            if (r1 == 0) goto Le
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "Recursive entry to executePendingTransactions"
            r0.<init>(r1)
            throw r0
        Le:
            android.os.Looper r1 = android.os.Looper.myLooper()
            android.support.v4.app.FragmentActivity r3 = r6.mActivity
            android.os.Handler r3 = r3.mHandler
            android.os.Looper r3 = r3.getLooper()
            if (r1 == r3) goto L24
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "Must be called from main thread of process"
            r0.<init>(r1)
            throw r0
        L24:
            r1 = r2
        L25:
            monitor-enter(r6)
            java.util.ArrayList r3 = r6.mPendingActions     // Catch: java.lang.Throwable -> L97
            if (r3 == 0) goto L32
            java.util.ArrayList r3 = r6.mPendingActions     // Catch: java.lang.Throwable -> L97
            int r3 = r3.size()     // Catch: java.lang.Throwable -> L97
            if (r3 != 0) goto L5a
        L32:
            monitor-exit(r6)     // Catch: java.lang.Throwable -> L97
            boolean r0 = r6.mHavePendingDeferredStart
            if (r0 == 0) goto La5
            r3 = r2
            r4 = r2
        L39:
            java.util.ArrayList r0 = r6.mActive
            int r0 = r0.size()
            if (r3 >= r0) goto L9e
            java.util.ArrayList r0 = r6.mActive
            java.lang.Object r0 = r0.get(r3)
            android.support.v4.app.Fragment r0 = (android.support.v4.app.Fragment) r0
            if (r0 == 0) goto L56
            android.support.v4.app.LoaderManagerImpl r5 = r0.mLoaderManager
            if (r5 == 0) goto L56
            android.support.v4.app.LoaderManagerImpl r0 = r0.mLoaderManager
            boolean r0 = r0.hasRunningLoaders()
            r4 = r4 | r0
        L56:
            int r0 = r3 + 1
            r3 = r0
            goto L39
        L5a:
            java.util.ArrayList r1 = r6.mPendingActions     // Catch: java.lang.Throwable -> L97
            int r3 = r1.size()     // Catch: java.lang.Throwable -> L97
            java.lang.Runnable[] r1 = r6.mTmpActions     // Catch: java.lang.Throwable -> L97
            if (r1 == 0) goto L69
            java.lang.Runnable[] r1 = r6.mTmpActions     // Catch: java.lang.Throwable -> L97
            int r1 = r1.length     // Catch: java.lang.Throwable -> L97
            if (r1 >= r3) goto L6d
        L69:
            java.lang.Runnable[] r1 = new java.lang.Runnable[r3]     // Catch: java.lang.Throwable -> L97
            r6.mTmpActions = r1     // Catch: java.lang.Throwable -> L97
        L6d:
            java.util.ArrayList r1 = r6.mPendingActions     // Catch: java.lang.Throwable -> L97
            java.lang.Runnable[] r4 = r6.mTmpActions     // Catch: java.lang.Throwable -> L97
            r1.toArray(r4)     // Catch: java.lang.Throwable -> L97
            java.util.ArrayList r1 = r6.mPendingActions     // Catch: java.lang.Throwable -> L97
            r1.clear()     // Catch: java.lang.Throwable -> L97
            android.support.v4.app.FragmentActivity r1 = r6.mActivity     // Catch: java.lang.Throwable -> L97
            android.os.Handler r1 = r1.mHandler     // Catch: java.lang.Throwable -> L97
            java.lang.Runnable r4 = r6.mExecCommit     // Catch: java.lang.Throwable -> L97
            r1.removeCallbacks(r4)     // Catch: java.lang.Throwable -> L97
            monitor-exit(r6)     // Catch: java.lang.Throwable -> L97
            r6.mExecutingActions = r0
            r1 = r2
        L86:
            if (r1 >= r3) goto L9a
            java.lang.Runnable[] r4 = r6.mTmpActions
            r4 = r4[r1]
            r4.run()
            java.lang.Runnable[] r4 = r6.mTmpActions
            r5 = 0
            r4[r1] = r5
            int r1 = r1 + 1
            goto L86
        L97:
            r0 = move-exception
            monitor-exit(r6)
            throw r0
        L9a:
            r6.mExecutingActions = r2
            r1 = r0
            goto L25
        L9e:
            if (r4 != 0) goto La5
            r6.mHavePendingDeferredStart = r2
            r6.startPendingDeferredFragments()
        La5:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.FragmentManagerImpl.execPendingActions():boolean");
    }

    @Override // android.support.v4.app.FragmentManager
    public final boolean executePendingTransactions() {
        return execPendingActions();
    }

    @Override // android.support.v4.app.FragmentManager
    public final Fragment findFragmentById(int i) {
        if (this.mAdded != null) {
            for (int size = this.mAdded.size() - 1; size >= 0; size--) {
                Fragment fragment = (Fragment) this.mAdded.get(size);
                if (fragment != null && fragment.mFragmentId == i) {
                    return fragment;
                }
            }
        }
        if (this.mActive != null) {
            for (int size2 = this.mActive.size() - 1; size2 >= 0; size2--) {
                Fragment fragment2 = (Fragment) this.mActive.get(size2);
                if (fragment2 != null && fragment2.mFragmentId == i) {
                    return fragment2;
                }
            }
        }
        return null;
    }

    @Override // android.support.v4.app.FragmentManager
    public final Fragment findFragmentByTag(String str) {
        if (this.mAdded != null && str != null) {
            for (int size = this.mAdded.size() - 1; size >= 0; size--) {
                Fragment fragment = (Fragment) this.mAdded.get(size);
                if (fragment != null && str.equals(fragment.mTag)) {
                    return fragment;
                }
            }
        }
        if (this.mActive != null && str != null) {
            for (int size2 = this.mActive.size() - 1; size2 >= 0; size2--) {
                Fragment fragment2 = (Fragment) this.mActive.get(size2);
                if (fragment2 != null && str.equals(fragment2.mTag)) {
                    return fragment2;
                }
            }
        }
        return null;
    }

    public final Fragment findFragmentByWho(String str) {
        Fragment fragmentFindFragmentByWho;
        if (this.mActive != null && str != null) {
            for (int size = this.mActive.size() - 1; size >= 0; size--) {
                Fragment fragment = (Fragment) this.mActive.get(size);
                if (fragment != null && (fragmentFindFragmentByWho = fragment.findFragmentByWho(str)) != null) {
                    return fragmentFindFragmentByWho;
                }
            }
        }
        return null;
    }

    public final void freeBackStackIndex(int i) {
        synchronized (this) {
            this.mBackStackIndices.set(i, null);
            if (this.mAvailBackStackIndices == null) {
                this.mAvailBackStackIndices = new ArrayList();
            }
            if (DEBUG) {
                Log.v(TAG, "Freeing back stack index " + i);
            }
            this.mAvailBackStackIndices.add(Integer.valueOf(i));
        }
    }

    @Override // android.support.v4.app.FragmentManager
    public final FragmentManager.BackStackEntry getBackStackEntryAt(int i) {
        return (FragmentManager.BackStackEntry) this.mBackStack.get(i);
    }

    @Override // android.support.v4.app.FragmentManager
    public final int getBackStackEntryCount() {
        if (this.mBackStack != null) {
            return this.mBackStack.size();
        }
        return 0;
    }

    @Override // android.support.v4.app.FragmentManager
    public final Fragment getFragment(Bundle bundle, String str) {
        int i = bundle.getInt(str, -1);
        if (i == -1) {
            return null;
        }
        if (i >= this.mActive.size()) {
            throwException(new IllegalStateException("Fragement no longer exists for key " + str + ": index " + i));
        }
        Fragment fragment = (Fragment) this.mActive.get(i);
        if (fragment != null) {
            return fragment;
        }
        throwException(new IllegalStateException("Fragement no longer exists for key " + str + ": index " + i));
        return fragment;
    }

    @Override // android.support.v4.app.FragmentManager
    public final List getFragments() {
        return this.mActive;
    }

    public final void hideFragment(Fragment fragment, int i, int i2) {
        if (DEBUG) {
            Log.v(TAG, "hide: " + fragment);
        }
        if (fragment.mHidden) {
            return;
        }
        fragment.mHidden = true;
        if (fragment.mView != null) {
            Animation animationLoadAnimation = loadAnimation(fragment, i, true, i2);
            if (animationLoadAnimation != null) {
                fragment.mView.startAnimation(animationLoadAnimation);
            }
            fragment.mView.setVisibility(8);
        }
        if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        fragment.onHiddenChanged(true);
    }

    final Animation loadAnimation(Fragment fragment, int i, boolean z, int i2) {
        int iTransitToStyleIndex;
        Animation animationLoadAnimation;
        Animation animationOnCreateAnimation = fragment.onCreateAnimation(i, z, fragment.mNextAnim);
        if (animationOnCreateAnimation != null) {
            return animationOnCreateAnimation;
        }
        if (fragment.mNextAnim != 0 && (animationLoadAnimation = AnimationUtils.loadAnimation(this.mActivity, fragment.mNextAnim)) != null) {
            return animationLoadAnimation;
        }
        if (i != 0 && (iTransitToStyleIndex = transitToStyleIndex(i, z)) >= 0) {
            switch (iTransitToStyleIndex) {
                case 1:
                    return makeOpenCloseAnimation(this.mActivity, 1.125f, 1.0f, 0.0f, 1.0f);
                case 2:
                    return makeOpenCloseAnimation(this.mActivity, 1.0f, 0.975f, 1.0f, 0.0f);
                case 3:
                    return makeOpenCloseAnimation(this.mActivity, 0.975f, 1.0f, 0.0f, 1.0f);
                case 4:
                    return makeOpenCloseAnimation(this.mActivity, 1.0f, 1.075f, 1.0f, 0.0f);
                case 5:
                    return makeFadeAnimation(this.mActivity, 0.0f, 1.0f);
                case 6:
                    return makeFadeAnimation(this.mActivity, 1.0f, 0.0f);
                default:
                    if (i2 == 0 && this.mActivity.getWindow() != null) {
                        i2 = this.mActivity.getWindow().getAttributes().windowAnimations;
                    }
                    return i2 == 0 ? null : null;
            }
        }
        return null;
    }

    final void makeActive(Fragment fragment) {
        if (fragment.mIndex >= 0) {
            return;
        }
        if (this.mAvailIndices == null || this.mAvailIndices.size() <= 0) {
            if (this.mActive == null) {
                this.mActive = new ArrayList();
            }
            fragment.setIndex(this.mActive.size(), this.mParent);
            this.mActive.add(fragment);
        } else {
            fragment.setIndex(((Integer) this.mAvailIndices.remove(this.mAvailIndices.size() - 1)).intValue(), this.mParent);
            this.mActive.set(fragment.mIndex, fragment);
        }
        if (DEBUG) {
            Log.v(TAG, "Allocated fragment index " + fragment);
        }
    }

    final void makeInactive(Fragment fragment) {
        if (fragment.mIndex < 0) {
            return;
        }
        if (DEBUG) {
            Log.v(TAG, "Freeing fragment index " + fragment);
        }
        this.mActive.set(fragment.mIndex, null);
        if (this.mAvailIndices == null) {
            this.mAvailIndices = new ArrayList();
        }
        this.mAvailIndices.add(Integer.valueOf(fragment.mIndex));
        this.mActivity.invalidateSupportFragment(fragment.mWho);
        fragment.initState();
    }

    final void moveToState(int i, int i2, int i3, boolean z) {
        if (this.mActivity == null && i != 0) {
            throw new IllegalStateException("No activity");
        }
        if (z || this.mCurState != i) {
            this.mCurState = i;
            if (this.mActive != null) {
                int i4 = 0;
                boolean zHasRunningLoaders = false;
                while (i4 < this.mActive.size()) {
                    Fragment fragment = (Fragment) this.mActive.get(i4);
                    if (fragment != null) {
                        moveToState(fragment, i, i2, i3, false);
                        if (fragment.mLoaderManager != null) {
                            zHasRunningLoaders |= fragment.mLoaderManager.hasRunningLoaders();
                        }
                    }
                    i4++;
                    zHasRunningLoaders = zHasRunningLoaders;
                }
                if (!zHasRunningLoaders) {
                    startPendingDeferredFragments();
                }
                if (this.mNeedMenuInvalidate && this.mActivity != null && this.mCurState == 5) {
                    this.mActivity.supportInvalidateOptionsMenu();
                    this.mNeedMenuInvalidate = false;
                }
            }
        }
    }

    final void moveToState(int i, boolean z) {
        moveToState(i, 0, 0, z);
    }

    final void moveToState(Fragment fragment) {
        moveToState(fragment, this.mCurState, 0, 0, false);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:114:0x0235  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x026f  */
    /* JADX WARN: Removed duplicated region for block: B:134:0x028c  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x02aa  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    final void moveToState(final android.support.v4.app.Fragment r10, int r11, int r12, int r13, boolean r14) {
        /*
            Method dump skipped, instruction units count: 902
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.app.FragmentManagerImpl.moveToState(android.support.v4.app.Fragment, int, int, int, boolean):void");
    }

    public final void noteStateNotSaved() {
        this.mStateSaved = false;
    }

    public final void performPendingDeferredStart(Fragment fragment) {
        if (fragment.mDeferStart) {
            if (this.mExecutingActions) {
                this.mHavePendingDeferredStart = true;
            } else {
                fragment.mDeferStart = false;
                moveToState(fragment, this.mCurState, 0, 0, false);
            }
        }
    }

    @Override // android.support.v4.app.FragmentManager
    public final void popBackStack() {
        enqueueAction(new Runnable() { // from class: android.support.v4.app.FragmentManagerImpl.2
            @Override // java.lang.Runnable
            public void run() {
                FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mActivity.mHandler, null, -1, 0);
            }
        }, false);
    }

    @Override // android.support.v4.app.FragmentManager
    public final void popBackStack(final int i, final int i2) {
        if (i < 0) {
            throw new IllegalArgumentException("Bad id: " + i);
        }
        enqueueAction(new Runnable() { // from class: android.support.v4.app.FragmentManagerImpl.4
            @Override // java.lang.Runnable
            public void run() {
                FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mActivity.mHandler, null, i, i2);
            }
        }, false);
    }

    @Override // android.support.v4.app.FragmentManager
    public final void popBackStack(final String str, final int i) {
        enqueueAction(new Runnable() { // from class: android.support.v4.app.FragmentManagerImpl.3
            @Override // java.lang.Runnable
            public void run() {
                FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mActivity.mHandler, str, -1, i);
            }
        }, false);
    }

    @Override // android.support.v4.app.FragmentManager
    public final boolean popBackStackImmediate() {
        checkStateLoss();
        executePendingTransactions();
        return popBackStackState(this.mActivity.mHandler, null, -1, 0);
    }

    @Override // android.support.v4.app.FragmentManager
    public final boolean popBackStackImmediate(int i, int i2) {
        checkStateLoss();
        executePendingTransactions();
        if (i < 0) {
            throw new IllegalArgumentException("Bad id: " + i);
        }
        return popBackStackState(this.mActivity.mHandler, null, i, i2);
    }

    @Override // android.support.v4.app.FragmentManager
    public final boolean popBackStackImmediate(String str, int i) {
        checkStateLoss();
        executePendingTransactions();
        return popBackStackState(this.mActivity.mHandler, str, -1, i);
    }

    final boolean popBackStackState(Handler handler, String str, int i, int i2) {
        if (this.mBackStack == null) {
            return false;
        }
        if (str == null && i < 0 && (i2 & 1) == 0) {
            int size = this.mBackStack.size() - 1;
            if (size < 0) {
                return false;
            }
            ((BackStackRecord) this.mBackStack.remove(size)).popFromBackStack(true);
        } else {
            int i3 = -1;
            if (str != null || i >= 0) {
                int size2 = this.mBackStack.size() - 1;
                while (size2 >= 0) {
                    BackStackRecord backStackRecord = (BackStackRecord) this.mBackStack.get(size2);
                    if ((str != null && str.equals(backStackRecord.getName())) || (i >= 0 && i == backStackRecord.mIndex)) {
                        break;
                    }
                    size2--;
                }
                if (size2 < 0) {
                    return false;
                }
                if ((i2 & 1) != 0) {
                    size2--;
                    while (size2 >= 0) {
                        BackStackRecord backStackRecord2 = (BackStackRecord) this.mBackStack.get(size2);
                        if ((str == null || !str.equals(backStackRecord2.getName())) && (i < 0 || i != backStackRecord2.mIndex)) {
                            break;
                        }
                        size2--;
                    }
                }
                i3 = size2;
            }
            if (i3 == this.mBackStack.size() - 1) {
                return false;
            }
            ArrayList arrayList = new ArrayList();
            for (int size3 = this.mBackStack.size() - 1; size3 > i3; size3--) {
                arrayList.add(this.mBackStack.remove(size3));
            }
            int size4 = arrayList.size() - 1;
            int i4 = 0;
            while (i4 <= size4) {
                if (DEBUG) {
                    Log.v(TAG, "Popping back stack state: " + arrayList.get(i4));
                }
                ((BackStackRecord) arrayList.get(i4)).popFromBackStack(i4 == size4);
                i4++;
            }
        }
        reportBackStackChanged();
        return true;
    }

    @Override // android.support.v4.app.FragmentManager
    public final void putFragment(Bundle bundle, String str, Fragment fragment) {
        if (fragment.mIndex < 0) {
            throwException(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
        }
        bundle.putInt(str, fragment.mIndex);
    }

    public final void removeFragment(Fragment fragment, int i, int i2) {
        if (DEBUG) {
            Log.v(TAG, "remove: " + fragment + " nesting=" + fragment.mBackStackNesting);
        }
        boolean z = !fragment.isInBackStack();
        if (!fragment.mDetached || z) {
            if (this.mAdded != null) {
                this.mAdded.remove(fragment);
            }
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.mAdded = false;
            fragment.mRemoving = true;
            moveToState(fragment, z ? 0 : 1, i, i2, false);
        }
    }

    @Override // android.support.v4.app.FragmentManager
    public final void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener onBackStackChangedListener) {
        if (this.mBackStackChangeListeners != null) {
            this.mBackStackChangeListeners.remove(onBackStackChangedListener);
        }
    }

    final void reportBackStackChanged() {
        if (this.mBackStackChangeListeners == null) {
            return;
        }
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= this.mBackStackChangeListeners.size()) {
                return;
            }
            ((FragmentManager.OnBackStackChangedListener) this.mBackStackChangeListeners.get(i2)).onBackStackChanged();
            i = i2 + 1;
        }
    }

    final void restoreAllState(Parcelable parcelable, ArrayList arrayList) {
        if (parcelable == null) {
            return;
        }
        FragmentManagerState fragmentManagerState = (FragmentManagerState) parcelable;
        if (fragmentManagerState.mActive != null) {
            if (arrayList != null) {
                for (int i = 0; i < arrayList.size(); i++) {
                    Fragment fragment = (Fragment) arrayList.get(i);
                    if (DEBUG) {
                        Log.v(TAG, "restoreAllState: re-attaching retained " + fragment);
                    }
                    FragmentState fragmentState = fragmentManagerState.mActive[fragment.mIndex];
                    fragmentState.mInstance = fragment;
                    fragment.mSavedViewState = null;
                    fragment.mBackStackNesting = 0;
                    fragment.mInLayout = false;
                    fragment.mAdded = false;
                    fragment.mTarget = null;
                    if (fragmentState.mSavedFragmentState != null) {
                        fragmentState.mSavedFragmentState.setClassLoader(this.mActivity.getClassLoader());
                        fragment.mSavedViewState = fragmentState.mSavedFragmentState.getSparseParcelableArray(VIEW_STATE_TAG);
                    }
                }
            }
            this.mActive = new ArrayList(fragmentManagerState.mActive.length);
            if (this.mAvailIndices != null) {
                this.mAvailIndices.clear();
            }
            for (int i2 = 0; i2 < fragmentManagerState.mActive.length; i2++) {
                FragmentState fragmentState2 = fragmentManagerState.mActive[i2];
                if (fragmentState2 != null) {
                    Fragment fragmentInstantiate = fragmentState2.instantiate(this.mActivity, this.mParent);
                    if (DEBUG) {
                        Log.v(TAG, "restoreAllState: active #" + i2 + ": " + fragmentInstantiate);
                    }
                    this.mActive.add(fragmentInstantiate);
                    fragmentState2.mInstance = null;
                } else {
                    this.mActive.add(null);
                    if (this.mAvailIndices == null) {
                        this.mAvailIndices = new ArrayList();
                    }
                    if (DEBUG) {
                        Log.v(TAG, "restoreAllState: avail #" + i2);
                    }
                    this.mAvailIndices.add(Integer.valueOf(i2));
                }
            }
            if (arrayList != null) {
                for (int i3 = 0; i3 < arrayList.size(); i3++) {
                    Fragment fragment2 = (Fragment) arrayList.get(i3);
                    if (fragment2.mTargetIndex >= 0) {
                        if (fragment2.mTargetIndex < this.mActive.size()) {
                            fragment2.mTarget = (Fragment) this.mActive.get(fragment2.mTargetIndex);
                        } else {
                            Log.w(TAG, "Re-attaching retained fragment " + fragment2 + " target no longer exists: " + fragment2.mTargetIndex);
                            fragment2.mTarget = null;
                        }
                    }
                }
            }
            if (fragmentManagerState.mAdded != null) {
                this.mAdded = new ArrayList(fragmentManagerState.mAdded.length);
                for (int i4 = 0; i4 < fragmentManagerState.mAdded.length; i4++) {
                    Fragment fragment3 = (Fragment) this.mActive.get(fragmentManagerState.mAdded[i4]);
                    if (fragment3 == null) {
                        throwException(new IllegalStateException("No instantiated fragment for index #" + fragmentManagerState.mAdded[i4]));
                    }
                    fragment3.mAdded = true;
                    if (DEBUG) {
                        Log.v(TAG, "restoreAllState: added #" + i4 + ": " + fragment3);
                    }
                    if (this.mAdded.contains(fragment3)) {
                        throw new IllegalStateException("Already added!");
                    }
                    this.mAdded.add(fragment3);
                }
            } else {
                this.mAdded = null;
            }
            if (fragmentManagerState.mBackStack == null) {
                this.mBackStack = null;
                return;
            }
            this.mBackStack = new ArrayList(fragmentManagerState.mBackStack.length);
            for (int i5 = 0; i5 < fragmentManagerState.mBackStack.length; i5++) {
                BackStackRecord backStackRecordInstantiate = fragmentManagerState.mBackStack[i5].instantiate(this);
                if (DEBUG) {
                    Log.v(TAG, "restoreAllState: back stack #" + i5 + " (index " + backStackRecordInstantiate.mIndex + "): " + backStackRecordInstantiate);
                    backStackRecordInstantiate.dump("  ", new PrintWriter(new LogWriter(TAG)), false);
                }
                this.mBackStack.add(backStackRecordInstantiate);
                if (backStackRecordInstantiate.mIndex >= 0) {
                    setBackStackIndex(backStackRecordInstantiate.mIndex, backStackRecordInstantiate);
                }
            }
        }
    }

    final ArrayList retainNonConfig() {
        ArrayList arrayList = null;
        if (this.mActive != null) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= this.mActive.size()) {
                    break;
                }
                Fragment fragment = (Fragment) this.mActive.get(i2);
                if (fragment != null && fragment.mRetainInstance) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                    }
                    arrayList.add(fragment);
                    fragment.mRetaining = true;
                    fragment.mTargetIndex = fragment.mTarget != null ? fragment.mTarget.mIndex : -1;
                    if (DEBUG) {
                        Log.v(TAG, "retainNonConfig: keeping retained " + fragment);
                    }
                }
                i = i2 + 1;
            }
        }
        return arrayList;
    }

    final Parcelable saveAllState() {
        int[] iArr;
        int size;
        int size2;
        boolean z;
        BackStackState[] backStackStateArr = null;
        execPendingActions();
        if (HONEYCOMB) {
            this.mStateSaved = true;
        }
        if (this.mActive == null || this.mActive.size() <= 0) {
            return null;
        }
        int size3 = this.mActive.size();
        FragmentState[] fragmentStateArr = new FragmentState[size3];
        int i = 0;
        boolean z2 = false;
        while (i < size3) {
            Fragment fragment = (Fragment) this.mActive.get(i);
            if (fragment != null) {
                if (fragment.mIndex < 0) {
                    throwException(new IllegalStateException("Failure saving state: active " + fragment + " has cleared index: " + fragment.mIndex));
                }
                FragmentState fragmentState = new FragmentState(fragment);
                fragmentStateArr[i] = fragmentState;
                if (fragment.mState <= 0 || fragmentState.mSavedFragmentState != null) {
                    fragmentState.mSavedFragmentState = fragment.mSavedFragmentState;
                } else {
                    fragmentState.mSavedFragmentState = saveFragmentBasicState(fragment);
                    if (fragment.mTarget != null) {
                        if (fragment.mTarget.mIndex < 0) {
                            throwException(new IllegalStateException("Failure saving state: " + fragment + " has target not in fragment manager: " + fragment.mTarget));
                        }
                        if (fragmentState.mSavedFragmentState == null) {
                            fragmentState.mSavedFragmentState = new Bundle();
                        }
                        putFragment(fragmentState.mSavedFragmentState, TARGET_STATE_TAG, fragment.mTarget);
                        if (fragment.mTargetRequestCode != 0) {
                            fragmentState.mSavedFragmentState.putInt(TARGET_REQUEST_CODE_STATE_TAG, fragment.mTargetRequestCode);
                        }
                    }
                }
                if (DEBUG) {
                    Log.v(TAG, "Saved state of " + fragment + ": " + fragmentState.mSavedFragmentState);
                }
                z = true;
            } else {
                z = z2;
            }
            i++;
            z2 = z;
        }
        if (!z2) {
            if (!DEBUG) {
                return null;
            }
            Log.v(TAG, "saveAllState: no fragments!");
            return null;
        }
        if (this.mAdded == null || (size2 = this.mAdded.size()) <= 0) {
            iArr = null;
        } else {
            iArr = new int[size2];
            for (int i2 = 0; i2 < size2; i2++) {
                iArr[i2] = ((Fragment) this.mAdded.get(i2)).mIndex;
                if (iArr[i2] < 0) {
                    throwException(new IllegalStateException("Failure saving state: active " + this.mAdded.get(i2) + " has cleared index: " + iArr[i2]));
                }
                if (DEBUG) {
                    Log.v(TAG, "saveAllState: adding fragment #" + i2 + ": " + this.mAdded.get(i2));
                }
            }
        }
        if (this.mBackStack != null && (size = this.mBackStack.size()) > 0) {
            backStackStateArr = new BackStackState[size];
            for (int i3 = 0; i3 < size; i3++) {
                backStackStateArr[i3] = new BackStackState(this, (BackStackRecord) this.mBackStack.get(i3));
                if (DEBUG) {
                    Log.v(TAG, "saveAllState: adding back stack #" + i3 + ": " + this.mBackStack.get(i3));
                }
            }
        }
        FragmentManagerState fragmentManagerState = new FragmentManagerState();
        fragmentManagerState.mActive = fragmentStateArr;
        fragmentManagerState.mAdded = iArr;
        fragmentManagerState.mBackStack = backStackStateArr;
        return fragmentManagerState;
    }

    final Bundle saveFragmentBasicState(Fragment fragment) {
        Bundle bundle;
        if (this.mStateBundle == null) {
            this.mStateBundle = new Bundle();
        }
        fragment.performSaveInstanceState(this.mStateBundle);
        if (this.mStateBundle.isEmpty()) {
            bundle = null;
        } else {
            bundle = this.mStateBundle;
            this.mStateBundle = null;
        }
        if (fragment.mView != null) {
            saveFragmentViewState(fragment);
        }
        if (fragment.mSavedViewState != null) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putSparseParcelableArray(VIEW_STATE_TAG, fragment.mSavedViewState);
        }
        if (!fragment.mUserVisibleHint) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putBoolean(USER_VISIBLE_HINT_TAG, fragment.mUserVisibleHint);
        }
        return bundle;
    }

    @Override // android.support.v4.app.FragmentManager
    public final Fragment.SavedState saveFragmentInstanceState(Fragment fragment) {
        Bundle bundleSaveFragmentBasicState;
        if (fragment.mIndex < 0) {
            throwException(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
        }
        if (fragment.mState <= 0 || (bundleSaveFragmentBasicState = saveFragmentBasicState(fragment)) == null) {
            return null;
        }
        return new Fragment.SavedState(bundleSaveFragmentBasicState);
    }

    final void saveFragmentViewState(Fragment fragment) {
        if (fragment.mInnerView == null) {
            return;
        }
        if (this.mStateArray == null) {
            this.mStateArray = new SparseArray();
        } else {
            this.mStateArray.clear();
        }
        fragment.mInnerView.saveHierarchyState(this.mStateArray);
        if (this.mStateArray.size() > 0) {
            fragment.mSavedViewState = this.mStateArray;
            this.mStateArray = null;
        }
    }

    public final void setBackStackIndex(int i, BackStackRecord backStackRecord) {
        synchronized (this) {
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList();
            }
            int size = this.mBackStackIndices.size();
            if (i < size) {
                if (DEBUG) {
                    Log.v(TAG, "Setting back stack index " + i + " to " + backStackRecord);
                }
                this.mBackStackIndices.set(i, backStackRecord);
            } else {
                while (size < i) {
                    this.mBackStackIndices.add(null);
                    if (this.mAvailBackStackIndices == null) {
                        this.mAvailBackStackIndices = new ArrayList();
                    }
                    if (DEBUG) {
                        Log.v(TAG, "Adding available back stack index " + size);
                    }
                    this.mAvailBackStackIndices.add(Integer.valueOf(size));
                    size++;
                }
                if (DEBUG) {
                    Log.v(TAG, "Adding back stack index " + i + " with " + backStackRecord);
                }
                this.mBackStackIndices.add(backStackRecord);
            }
        }
    }

    public final void showFragment(Fragment fragment, int i, int i2) {
        if (DEBUG) {
            Log.v(TAG, "show: " + fragment);
        }
        if (fragment.mHidden) {
            fragment.mHidden = false;
            if (fragment.mView != null) {
                Animation animationLoadAnimation = loadAnimation(fragment, i, true, i2);
                if (animationLoadAnimation != null) {
                    fragment.mView.startAnimation(animationLoadAnimation);
                }
                fragment.mView.setVisibility(0);
            }
            if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            fragment.onHiddenChanged(false);
        }
    }

    final void startPendingDeferredFragments() {
        if (this.mActive == null) {
            return;
        }
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= this.mActive.size()) {
                return;
            }
            Fragment fragment = (Fragment) this.mActive.get(i2);
            if (fragment != null) {
                performPendingDeferredStart(fragment);
            }
            i = i2 + 1;
        }
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("FragmentManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        if (this.mParent != null) {
            DebugUtils.buildShortClassTag(this.mParent, sb);
        } else {
            DebugUtils.buildShortClassTag(this.mActivity, sb);
        }
        sb.append("}}");
        return sb.toString();
    }
}
