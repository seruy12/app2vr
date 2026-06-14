package android.support.v4.content;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.util.TimeUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;

/* JADX INFO: loaded from: classes.dex */
public abstract class AsyncTaskLoader extends Loader {
    static final boolean DEBUG = false;
    static final String TAG = "AsyncTaskLoader";
    volatile LoadTask mCancellingTask;
    Handler mHandler;
    long mLastLoadCompleteTime;
    volatile LoadTask mTask;
    long mUpdateThrottle;

    final class LoadTask extends ModernAsyncTask implements Runnable {
        private CountDownLatch done = new CountDownLatch(1);
        Object result;
        boolean waiting;

        LoadTask() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.support.v4.content.ModernAsyncTask
        public final Object doInBackground(Void... voidArr) {
            this.result = AsyncTaskLoader.this.onLoadInBackground();
            return this.result;
        }

        @Override // android.support.v4.content.ModernAsyncTask
        protected final void onCancelled() {
            try {
                AsyncTaskLoader.this.dispatchOnCancelled(this, this.result);
            } finally {
                this.done.countDown();
            }
        }

        @Override // android.support.v4.content.ModernAsyncTask
        protected final void onPostExecute(Object obj) {
            try {
                AsyncTaskLoader.this.dispatchOnLoadComplete(this, obj);
            } finally {
                this.done.countDown();
            }
        }

        @Override // java.lang.Runnable
        public final void run() {
            this.waiting = false;
            AsyncTaskLoader.this.executePendingTask();
        }
    }

    public AsyncTaskLoader(Context context) {
        super(context);
        this.mLastLoadCompleteTime = -10000L;
    }

    public boolean cancelLoad() {
        boolean zCancel = false;
        if (this.mTask != null) {
            if (this.mCancellingTask != null) {
                if (this.mTask.waiting) {
                    this.mTask.waiting = false;
                    this.mHandler.removeCallbacks(this.mTask);
                }
                this.mTask = null;
            } else if (this.mTask.waiting) {
                this.mTask.waiting = false;
                this.mHandler.removeCallbacks(this.mTask);
                this.mTask = null;
            } else {
                zCancel = this.mTask.cancel(false);
                if (zCancel) {
                    this.mCancellingTask = this.mTask;
                }
                this.mTask = null;
            }
        }
        return zCancel;
    }

    void dispatchOnCancelled(LoadTask loadTask, Object obj) {
        onCanceled(obj);
        if (this.mCancellingTask == loadTask) {
            rollbackContentChanged();
            this.mLastLoadCompleteTime = SystemClock.uptimeMillis();
            this.mCancellingTask = null;
            executePendingTask();
        }
    }

    void dispatchOnLoadComplete(LoadTask loadTask, Object obj) {
        if (this.mTask != loadTask) {
            dispatchOnCancelled(loadTask, obj);
            return;
        }
        if (isAbandoned()) {
            onCanceled(obj);
            return;
        }
        commitContentChanged();
        this.mLastLoadCompleteTime = SystemClock.uptimeMillis();
        this.mTask = null;
        deliverResult(obj);
    }

    @Override // android.support.v4.content.Loader
    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(str, fileDescriptor, printWriter, strArr);
        if (this.mTask != null) {
            printWriter.print(str);
            printWriter.print("mTask=");
            printWriter.print(this.mTask);
            printWriter.print(" waiting=");
            printWriter.println(this.mTask.waiting);
        }
        if (this.mCancellingTask != null) {
            printWriter.print(str);
            printWriter.print("mCancellingTask=");
            printWriter.print(this.mCancellingTask);
            printWriter.print(" waiting=");
            printWriter.println(this.mCancellingTask.waiting);
        }
        if (this.mUpdateThrottle != 0) {
            printWriter.print(str);
            printWriter.print("mUpdateThrottle=");
            TimeUtils.formatDuration(this.mUpdateThrottle, printWriter);
            printWriter.print(" mLastLoadCompleteTime=");
            TimeUtils.formatDuration(this.mLastLoadCompleteTime, SystemClock.uptimeMillis(), printWriter);
            printWriter.println();
        }
    }

    void executePendingTask() {
        if (this.mCancellingTask != null || this.mTask == null) {
            return;
        }
        if (this.mTask.waiting) {
            this.mTask.waiting = false;
            this.mHandler.removeCallbacks(this.mTask);
        }
        if (this.mUpdateThrottle <= 0 || SystemClock.uptimeMillis() >= this.mLastLoadCompleteTime + this.mUpdateThrottle) {
            this.mTask.executeOnExecutor(ModernAsyncTask.THREAD_POOL_EXECUTOR, null);
        } else {
            this.mTask.waiting = true;
            this.mHandler.postAtTime(this.mTask, this.mLastLoadCompleteTime + this.mUpdateThrottle);
        }
    }

    public abstract Object loadInBackground();

    public void onCanceled(Object obj) {
    }

    @Override // android.support.v4.content.Loader
    protected void onForceLoad() {
        super.onForceLoad();
        cancelLoad();
        this.mTask = new LoadTask();
        executePendingTask();
    }

    protected Object onLoadInBackground() {
        return loadInBackground();
    }

    public void setUpdateThrottle(long j) {
        this.mUpdateThrottle = j;
        if (j != 0) {
            this.mHandler = new Handler();
        }
    }

    public void waitForLoader() {
        LoadTask loadTask = this.mTask;
        if (loadTask != null) {
            try {
                loadTask.done.await();
            } catch (InterruptedException e) {
            }
        }
    }
}
