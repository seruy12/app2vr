package com.nitro888.nitroaction360;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.nibiru.lib.controller.BaseEvent;
import com.nibiru.lib.controller.Controller;
import com.nibiru.lib.controller.ControllerDevice;
import com.nibiru.lib.controller.ControllerKeyEvent;
import com.nibiru.lib.controller.ControllerService;
import com.nibiru.lib.controller.ControllerServiceException;
import com.nibiru.lib.controller.OnKeyListener;
import com.nibiru.lib.controller.OnSimpleStickListener;
import com.nibiru.lib.controller.OnSpecEventListener;
import com.nibiru.lib.controller.OnStateListener;
import com.nibiru.lib.controller.OnVoiceListener;
import com.nibiru.lib.controller.R;
import com.nibiru.lib.feedback.FeedbackService;
import com.nitro888.nitroaction360.cardboard.NACardboardOverlayView;
import com.nitro888.nitroaction360.nitroaction.NAGUIRelativeLayout;
import com.vision.ActivityView;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class MainActivity extends CardboardActivity implements ControllerService.OnControllerSeviceListener, OnKeyListener, OnSimpleStickListener, OnSpecEventListener, OnStateListener, OnVoiceListener {
    protected ControllerService a;
    protected FeedbackService b;
    private NACardboardOverlayView g;
    private CardboardView h;
    private com.nitro888.nitroaction360.nitroaction.e i;
    private com.nitro888.nitroaction360.nitroaction.c j;
    private NAGUIRelativeLayout k;
    private com.nitro888.nitroaction360.nitroaction.b l;
    private ActivityView n;
    private static final String e = MainActivity.class.getSimpleName();
    private static float f = 1.0f;
    static HashMap d = new HashMap();
    boolean c = false;
    private com.nitro888.nitroaction360.a.j m = null;

    public final com.a.a.c a(int i, int i2) {
        if (this.m == null) {
            return null;
        }
        com.a.a.c cVarA = this.m.a(i, i2);
        if (cVarA != null) {
            return cVarA;
        }
        this.g.a("Item Loading Fail");
        return cVarA;
    }

    public final void a(float f2) {
        this.j.a(f2);
    }

    public final void a(int i) {
        this.j.a(i);
    }

    public final void a(String str) {
        this.l.a(str);
    }

    public final void a(String str, int i) {
        switch (this.k.b()) {
            case 7:
                a(R.raw.dome);
                break;
            case 8:
                a(R.raw.sphere);
                break;
            default:
                a(R.raw.plane_sq);
                break;
        }
        b(i);
        this.l.b(str);
    }

    public final boolean a() {
        return ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo().isConnectedOrConnecting() && this.m != null;
    }

    public final void b() {
        this.l.f();
    }

    public final void b(float f2) {
        this.j.b(f2);
    }

    public final void b(int i) {
        this.j.b(i);
    }

    public final void b(String str) {
        com.nitro888.nitroaction360.a.g.a(this, str);
    }

    public final void c() {
        this.l.g();
    }

    public final void d() {
        this.l.e();
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
        if (this.a == null || !this.a.handleExternalInput(motionEvent)) {
            return super.dispatchGenericMotionEvent(motionEvent);
        }
        return true;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        Log.i("vr", "dispatchKeyEvent:" + keyEvent);
        if (4 == keyEvent.getKeyCode() && keyEvent.getAction() == 0) {
            finish();
        } else if (24 == keyEvent.getKeyCode() && keyEvent.getAction() == 0) {
            b(1.0f);
        } else if (25 == keyEvent.getKeyCode() && keyEvent.getAction() == 0) {
            b(-1.0f);
        } else if (this.a == null || this.a.handleExternalInput(keyEvent)) {
        }
        return true;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        this.n.onTouchEvent(motionEvent);
        Log.i("vr", "onTouchEvent");
        return true;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchTrackballEvent(MotionEvent motionEvent) {
        Log.i("vr", "dispatchTrackballEvent:" + motionEvent);
        return true;
    }

    public final int e() {
        return this.l.d();
    }

    public final int f() {
        return this.l.i();
    }

    public final int g() {
        return this.l.h();
    }

    public final int h() {
        return this.l.j();
    }

    public final void i() {
        Configuration configuration = getResources().getConfiguration();
        configuration.orientation = 2;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        try {
            this.n = new ActivityView(this);
            com.nitro888.nitroaction360.nitroaction.e eVar = this.i;
            com.nitro888.nitroaction360.nitroaction.e.a(1, getResources().getDisplayMetrics().widthPixels);
            com.nitro888.nitroaction360.nitroaction.e eVar2 = this.i;
            com.nitro888.nitroaction360.nitroaction.e.b(1, getResources().getDisplayMetrics().heightPixels);
            com.nitro888.nitroaction360.nitroaction.e eVar3 = this.i;
            com.nitro888.nitroaction360.nitroaction.e.a(1);
            this.n.mWidth = getResources().getDisplayMetrics().widthPixels;
            this.n.mHeight = getResources().getDisplayMetrics().heightPixels;
            ActivityView activityView = this.n;
            com.nitro888.nitroaction360.nitroaction.e eVar4 = this.i;
            activityView.setSurface(com.nitro888.nitroaction360.nitroaction.e.e());
        } catch (Exception e2) {
            runOnUiThread(new h(this));
            finish();
        }
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 != -1) {
            return;
        }
        Log.i("vr", "data:" + intent);
        this.k.a(-1);
        try {
            this.n.startActivity(intent);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    @Override // com.nibiru.lib.controller.OnStateListener
    public void onBluetoothStateChanged(int i) {
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardActivity, com.google.vrtoolkit.cardboard.sensors.SensorConnection.SensorListener
    public void onCardboardTrigger() {
        this.k.a();
    }

    @Override // com.nibiru.lib.controller.OnKeyListener
    public void onControllerKeyDown(int i, int i2, ControllerKeyEvent controllerKeyEvent) {
    }

    @Override // com.nibiru.lib.controller.OnKeyListener
    public void onControllerKeyUp(int i, int i2, ControllerKeyEvent controllerKeyEvent) {
    }

    @Override // com.nibiru.lib.controller.ControllerService.OnControllerSeviceListener
    public void onControllerServiceReady(boolean z) {
    }

    @Override // com.nibiru.lib.controller.OnStateListener
    public void onControllerStateChanged(int i, int i2, ControllerDevice controllerDevice) {
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.common_ui);
        this.a = Controller.getControllerService(this);
        this.b = this.a.getFeedbackService();
        this.a.setKeyListener(this);
        this.a.setSimpleStickListener(this);
        this.a.setSpecEventListener(this);
        this.a.setStateListener(this);
        this.a.setControllerServiceListener(this);
        this.a.setVoiceListener(this);
        this.a.setHandler(new Handler());
        this.a.getContinusKeyService().registerContinuesDirectionKey();
        if (this.a != null && !this.a.isServiceEnable()) {
            try {
                this.a.register();
            } catch (ControllerServiceException e2) {
                e2.printStackTrace();
            }
        }
        this.i = new com.nitro888.nitroaction360.nitroaction.e(this);
        this.k = (NAGUIRelativeLayout) findViewById(R.id.GUI);
        this.k.a(this.i);
        this.l = new com.nitro888.nitroaction360.nitroaction.b(this);
        this.l.a(this.i);
        this.j = new com.nitro888.nitroaction360.nitroaction.c(this);
        this.j.a(this.i);
        getSystemService("connectivity");
        this.m = null;
        this.h = (CardboardView) findViewById(R.id.cardboard_view);
        this.h.setRenderer(this.j);
        this.g = (NACardboardOverlayView) findViewById(R.id.overlay);
        setCardboardView(this.h);
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        if (this.a != null) {
            this.a.unregister();
            this.a = null;
        }
    }

    public void onGUIButtonClick(View view) {
        this.k.onGUIButtonClick(view.getId());
    }

    @Override // com.nibiru.lib.controller.OnSimpleStickListener
    public boolean onLeftStickChanged(int i, float f2, float f3) {
        return false;
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.l.a();
        if (this.a != null) {
            this.a.onPause();
            getWindow().clearFlags(128);
        }
    }

    @Override // com.google.vrtoolkit.cardboard.CardboardActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.l.c();
        if (this.a != null) {
            this.a.onResume();
            this.a.handleFullScreenMode();
        }
        if ("start_play".equals(getIntent().getStringExtra("type"))) {
            this.k.postDelayed(new f(this), 1000L);
        }
        if ("start_setting".equals(getIntent().getStringExtra("type"))) {
            this.k.postDelayed(new g(this), 1000L);
        }
    }

    @Override // com.nibiru.lib.controller.OnSpecEventListener
    public void onRevSpecEvent(BaseEvent baseEvent) {
    }

    @Override // com.nibiru.lib.controller.OnSimpleStickListener
    public boolean onRightStickChanged(int i, float f2, float f3) {
        return false;
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
        if (this.a != null) {
            this.a.onStart();
        }
    }

    @Override // android.app.Activity
    public void onStop() {
        super.onStop();
        com.nitro888.nitroaction360.nitroaction.b bVar = this.l;
        com.nitro888.nitroaction360.nitroaction.b.b();
        if (this.a != null) {
            this.a.onStop();
        }
    }

    @Override // com.nibiru.lib.controller.OnVoiceListener
    public void onVoiceResult(int i, String str, String str2) {
    }
}
