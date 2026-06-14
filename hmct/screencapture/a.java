package com.hmct.screencapture;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import com.nibiru.lib.controller.R;

/* JADX INFO: loaded from: classes.dex */
public final class a extends Fragment implements View.OnClickListener {
    public static LinearLayout a;
    public static MediaProjection b;
    public static VirtualDisplay c;
    public static Button d;
    private static WindowManager l;
    private static WindowManager.LayoutParams m;
    private int e;
    private int f;
    private Intent g;
    private LinearLayout h;
    private Surface i;
    private MediaProjectionManager j;
    private SurfaceView k;

    public static void a() {
        if (b != null) {
            b.stop();
            b = null;
        }
    }

    private static boolean a(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getRealMetrics(displayMetrics);
        int i = displayMetrics.heightPixels;
        int i2 = displayMetrics.widthPixels;
        DisplayMetrics displayMetrics2 = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics2);
        return i2 - displayMetrics2.widthPixels > 0 || i - displayMetrics2.heightPixels > 0;
    }

    public static void b() {
        if (c == null) {
            return;
        }
        c.release();
        c = null;
    }

    private int c() {
        int identifier = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    private int d() {
        Resources resources = getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"));
        Log.v("dbw", "Navi height:" + dimensionPixelSize);
        return dimensionPixelSize;
    }

    private void e() {
        b = this.j.getMediaProjection(this.f, this.g);
    }

    private void f() {
        Log.i("ScreenCaptureFragment", "Setting up a VirtualDisplay: " + this.k.getWidth() + "x" + this.k.getHeight() + " (" + this.e + ")");
        c = b.createVirtualDisplay("ScreenCapture", this.k.getWidth(), this.k.getHeight(), this.e, 16, this.i, null, null);
        getActivity().startService(new Intent(getActivity(), (Class<?>) FloatingWindowService.class));
        getActivity().finish();
    }

    @Override // android.support.v4.app.Fragment
    public final void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        FragmentActivity activity = getActivity();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.e = displayMetrics.densityDpi;
        this.j = (MediaProjectionManager) activity.getSystemService("media_projection");
        onClick(d);
    }

    @Override // android.support.v4.app.Fragment
    public final void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1) {
            if (i2 != -1) {
                Log.i("ScreenCaptureFragment", "User cancelled");
                getActivity().finish();
            } else if (getActivity() != null) {
                Log.i("ScreenCaptureFragment", "Starting screen capture");
                this.f = i2;
                this.g = intent;
                e();
                f();
            }
        }
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        switch (view.getId()) {
            case R.id.toggle /* 2131230785 */:
                if (c != null) {
                    b();
                } else {
                    FragmentActivity activity = getActivity();
                    if (this.i != null && activity != null) {
                        if (b != null) {
                            f();
                        } else if (this.f == 0 || this.g == null) {
                            Log.i("ScreenCaptureFragment", "Requesting confirmation");
                            startActivityForResult(this.j.createScreenCaptureIntent(), 1);
                        } else {
                            e();
                            f();
                        }
                    }
                    l = (WindowManager) getActivity().getApplicationContext().getSystemService("window");
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    m = layoutParams;
                    layoutParams.type = 2003;
                    m.format = 1;
                    m.flags = 24;
                    m.x = (getResources().getDisplayMetrics().widthPixels / 2) - d();
                    m.y = 0;
                    m.width = -2;
                    m.height = -2;
                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams((a(getActivity()) ? d() : 0) + getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels + c());
                    Log.i("VR", "checkDeviceHasNavigationBar" + a(getActivity()));
                    layoutParams2.setMargins(getResources().getDisplayMetrics().widthPixels / 2, (0 - c()) - (c() / 2), 0, 0);
                    this.k.setLayoutParams(layoutParams2);
                    l.addView(a, m);
                }
                break;
        }
    }

    @Override // android.support.v4.app.Fragment
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.f = bundle.getInt("result_code");
            this.g = (Intent) bundle.getParcelable("result_data");
        }
    }

    @Override // android.support.v4.app.Fragment
    public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_screen_capture, viewGroup, false);
    }

    @Override // android.support.v4.app.Fragment
    public final void onDestroy() {
        super.onDestroy();
    }

    @Override // android.support.v4.app.Fragment
    public final void onPause() {
        super.onPause();
    }

    @Override // android.support.v4.app.Fragment
    public final void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (this.g != null) {
            bundle.putInt("result_code", this.f);
            bundle.putParcelable("result_data", this.g);
        }
    }

    @Override // android.support.v4.app.Fragment
    public final void onViewCreated(View view, Bundle bundle) {
        this.k = (SurfaceView) view.findViewById(R.id.surface);
        this.h = (LinearLayout) view.findViewById(R.id.LinearLayout);
        a = (LinearLayout) view.findViewById(R.id.FrameLayout);
        this.h.removeView(a);
        this.i = this.k.getHolder().getSurface();
        Button button = (Button) view.findViewById(R.id.toggle);
        d = button;
        button.setOnClickListener(this);
        d.setVisibility(4);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.k.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        layoutParams.height = getResources().getDisplayMetrics().heightPixels;
        layoutParams.leftMargin = layoutParams.width / 2;
        this.k.setLayoutParams(layoutParams);
    }
}
