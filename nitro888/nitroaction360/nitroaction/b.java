package com.nitro888.nitroaction360.nitroaction;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;

/* JADX INFO: loaded from: classes.dex */
public class b implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnVideoSizeChangedListener {
    private static final String a = b.class.getSimpleName();
    private Context b;
    private MediaPlayer d;
    private e c = null;
    private int e = 0;
    private int f = 0;
    private boolean g = false;
    private boolean h = false;
    private int i = 3;

    public b(Context context) {
        this.d = null;
        this.b = context;
        this.d = new MediaPlayer();
        this.d.setOnPreparedListener(this);
        this.d.setOnBufferingUpdateListener(this);
        this.d.setOnCompletionListener(this);
        this.d.setOnVideoSizeChangedListener(this);
        this.d.setOnSeekCompleteListener(this);
    }

    public static void b() {
    }

    public final void a() {
        if (this.g) {
            this.i = 5;
            this.e = this.d.getCurrentPosition();
            this.d.pause();
        }
    }

    public final void a(e eVar) {
        this.c = eVar;
    }

    public final void a(String str) {
        if (this.d == null) {
            return;
        }
        this.f = 0;
        this.e = 0;
        this.h = false;
        this.d.reset();
        try {
            this.d.setDataSource(new FileInputStream(new File(str)).getFD());
            this.d.prepareAsync();
        } catch (Exception e) {
            Log.e(a, e.getMessage(), e);
        }
    }

    public final void b(String str) {
        if (this.d == null) {
            return;
        }
        this.f = 0;
        this.e = 0;
        this.h = true;
        this.d.reset();
        try {
            this.d.setDataSource(str);
            this.d.prepareAsync();
        } catch (Exception e) {
            Log.e(a, e.getMessage(), e);
        }
    }

    public final void c() {
        if (this.g && this.i == 5) {
            this.i = 4;
            this.d.getVideoWidth();
            this.d.getVideoHeight();
            Log.e("tag", "setTextureSize begin");
            new Exception().printStackTrace();
            this.d.pause();
            this.d.seekTo(this.e);
        }
    }

    public final int d() {
        return this.i;
    }

    public final void e() {
        if (this.d == null || !this.g) {
            return;
        }
        switch (this.i) {
            case 4:
                a();
                break;
            case 5:
                this.i = 4;
                this.d.pause();
                this.d.seekTo(this.e);
                break;
        }
    }

    public final void f() {
        if (this.d == null || !this.g) {
            return;
        }
        int currentPosition = this.d.getCurrentPosition() - 30000;
        this.d.pause();
        if (currentPosition > 0) {
            this.d.seekTo(currentPosition);
        } else {
            this.d.seekTo(0);
        }
    }

    public final void g() {
        if (this.d == null || !this.g) {
            return;
        }
        int duration = this.d.getDuration();
        int currentPosition = this.d.getCurrentPosition() + 30000;
        if (currentPosition < duration) {
            this.d.pause();
            this.d.seekTo(currentPosition);
        }
    }

    public final int h() {
        return this.d.getDuration();
    }

    public final int i() {
        return this.f;
    }

    public final int j() {
        return this.d.getCurrentPosition();
    }

    @Override // android.media.MediaPlayer.OnBufferingUpdateListener
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        this.f = i;
        if (this.h) {
            return;
        }
        this.h = true;
        this.g = true;
        this.i = 4;
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer mediaPlayer) {
        this.i = 3;
        this.g = false;
    }

    @Override // android.media.MediaPlayer.OnPreparedListener
    public void onPrepared(MediaPlayer mediaPlayer) {
        this.g = true;
        this.i = 4;
        mediaPlayer.start();
    }

    @Override // android.media.MediaPlayer.OnSeekCompleteListener
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        switch (this.i) {
            case 3:
                this.d.stop();
                break;
            case 4:
                this.d.start();
                break;
            case 5:
                this.d.pause();
                break;
        }
        this.e = this.d.getCurrentPosition();
    }

    @Override // android.media.MediaPlayer.OnVideoSizeChangedListener
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
    }
}
