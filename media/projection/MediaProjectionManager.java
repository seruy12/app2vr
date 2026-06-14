package android.media.projection;

import android.content.Context;
import android.content.Intent;

/* JADX INFO: loaded from: classes.dex */
public final class MediaProjectionManager {
    public static final String EXTRA_APP_TOKEN = "android.media.projection.extra.EXTRA_APP_TOKEN";
    public static final String EXTRA_MEDIA_PROJECTION = "android.media.projection.extra.EXTRA_MEDIA_PROJECTION";
    private static final String TAG = "MediaProjectionManager";
    public static final int TYPE_MIRRORING = 1;
    public static final int TYPE_PRESENTATION = 2;
    public static final int TYPE_SCREEN_CAPTURE = 0;
    private Context mContext;

    public MediaProjectionManager(Context context) {
        this.mContext = context;
    }

    public final Intent createScreenCaptureIntent() {
        Intent intent = new Intent();
        intent.setClassName("com.android.systemui", "com.android.systemui.media.MediaProjectionPermissionActivity");
        return intent;
    }

    public final MediaProjection getMediaProjection(int i, Intent intent) {
        return null;
    }
}
