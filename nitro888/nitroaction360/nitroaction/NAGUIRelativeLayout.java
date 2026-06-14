package com.nitro888.nitroaction360.nitroaction;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import com.nibiru.lib.controller.R;
import com.nitro888.nitroaction360.MainActivity;
import com.nitro888.nitroaction360.cardboard.NACardboardOverlayView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class NAGUIRelativeLayout extends RelativeLayout {
    private static final String a = NAGUIRelativeLayout.class.getSimpleName();
    private int A;
    private final Context b;
    private e c;
    private RelativeLayout d;
    private TableLayout e;
    private GridLayout f;
    private TextView g;
    private TextView h;
    private SeekBar i;
    private GridLayout j;
    private GridLayout k;
    private GridLayout l;
    private GridLayout m;
    private boolean n;
    private Vibrator o;
    private int p;
    private int q;
    private boolean r;
    private int s;
    private String t;
    private int u;
    private int v;
    private List[] w;
    private final List x;
    private int y;
    private int z;

    public NAGUIRelativeLayout(Context context) {
        super(context);
        this.c = null;
        this.d = null;
        this.e = null;
        this.f = null;
        this.g = null;
        this.h = null;
        this.i = null;
        this.j = null;
        this.k = null;
        this.l = null;
        this.m = null;
        this.n = false;
        this.p = -1;
        this.q = -1;
        this.r = false;
        this.s = R.id.GUI_AD;
        this.t = "";
        this.u = 0;
        this.v = 0;
        this.x = new ArrayList();
        this.y = 0;
        this.z = 0;
        this.A = 0;
        this.b = context;
        this.o = (Vibrator) context.getSystemService("vibrator");
    }

    public NAGUIRelativeLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.c = null;
        this.d = null;
        this.e = null;
        this.f = null;
        this.g = null;
        this.h = null;
        this.i = null;
        this.j = null;
        this.k = null;
        this.l = null;
        this.m = null;
        this.n = false;
        this.p = -1;
        this.q = -1;
        this.r = false;
        this.s = R.id.GUI_AD;
        this.t = "";
        this.u = 0;
        this.v = 0;
        this.x = new ArrayList();
        this.y = 0;
        this.z = 0;
        this.A = 0;
        this.b = context;
        this.o = (Vibrator) context.getSystemService("vibrator");
    }

    private void a(String str) {
        this.g.setText(str);
    }

    private void a(boolean z) {
        if (z) {
            ((TextView) findViewById(R.id.text_pages)).setText("");
            this.h.setText("");
            this.i.setProgress(0);
            return;
        }
        ((TextView) findViewById(R.id.text_pages)).setText(String.valueOf(this.u + 1) + "/" + this.v);
        ((TextView) findViewById(R.id.youtube_pages)).setText(String.valueOf(this.z + 1) + "/" + this.A);
        if (((MainActivity) this.b).e() == 4) {
            int iH = ((MainActivity) this.b).h();
            int iG = ((MainActivity) this.b).g();
            this.h.setText(String.format("%02d:%02d:%02d / %02d:%02d:%02d", Long.valueOf(TimeUnit.MILLISECONDS.toHours(iH)), Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(iH) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(iH))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(iH) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(iH))), Long.valueOf(TimeUnit.MILLISECONDS.toHours(iG)), Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(iG) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(iG))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(iG) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(iG)))));
            this.i.setMax(iG);
            this.i.setProgress(iH);
            this.i.setSecondaryProgress((((MainActivity) this.b).f() * iG) / 100);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void a(BitmapDrawable[] bitmapDrawableArr) {
        for (int i = 3; i < this.j.getChildCount(); i++) {
            this.j.getChildAt(i).setVisibility(4);
        }
        Drawable background = this.j.getChildAt(0).getBackground();
        for (int i2 = 0; i2 < bitmapDrawableArr.length; i2++) {
            this.j.getChildAt(i2 + 3).setVisibility(0);
            ((ImageButton) this.j.getChildAt(i2 + 3)).setImageResource(bitmapDrawableArr[i2] == 0 ? R.drawable.ic_folder_white_48dp : R.drawable.ic_play_circle_outline_white_48dp);
            this.j.getChildAt(i2 + 3).setBackground(bitmapDrawableArr[i2] == 0 ? background : bitmapDrawableArr[i2]);
        }
    }

    private void b(int i) {
        switch (com.nitro888.nitroaction360.a.a.a((String) this.w[0].get((this.u * 6) + i))) {
            case 0:
                String str = (String) this.w[0].get((this.u * 6) + i);
                if (str.equals("")) {
                    this.t = Environment.getExternalStorageDirectory().getPath();
                } else {
                    this.t = str;
                }
                String str2 = this.t;
                ArrayList arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                File file = new File(str2);
                File[] fileArrListFiles = file.listFiles();
                if (!file.getParent().equals("/")) {
                    arrayList2.add(file.getParent());
                    arrayList.add("../");
                }
                if (fileArrListFiles != null) {
                    for (File file2 : fileArrListFiles) {
                        if (!file2.isHidden() && file2.canRead() && !file2.isDirectory()) {
                            file2.getName();
                            arrayList2.add(file2.getPath());
                            arrayList.add(file2.getName());
                        }
                    }
                }
                this.w = new List[]{arrayList2, arrayList};
                this.x.clear();
                for (int i2 = 0; i2 < this.w[0].size(); i2++) {
                    if (com.nitro888.nitroaction360.a.a.a((String) this.w[0].get(i2)) == 0) {
                        this.x.add("");
                    } else if (com.nitro888.nitroaction360.a.a.a((String) this.w[0].get(i2)) == 1) {
                        this.x.add((String) this.w[0].get(i2));
                    }
                }
                this.u = 0;
                this.v = this.x.size() % 6 > 0 ? (this.x.size() / 6) + 1 : this.x.size() / 6;
                d();
                break;
            case 1:
                a(-1);
                a((String) this.w[1].get((this.u * 6) + i));
                ((MainActivity) this.b).a((String) this.w[0].get((this.u * 6) + i));
                break;
        }
    }

    private void c(int i) {
        if (((MainActivity) this.b).a()) {
            a(-1);
            if (((MainActivity) this.b).a(this.y, (this.z * 6) + i) != null) {
                ((MainActivity) this.b).b(((MainActivity) this.b).a(this.y, (this.z * 6) + i).b);
            }
        }
    }

    private String d(int i) {
        return ((MainActivity) this.b).a(this.y, (this.z * 6) + i) != null ? ((MainActivity) this.b).a(this.y, (this.z * 6) + i).a : "";
    }

    private void d() {
        int i = this.u * 6;
        BitmapDrawable[] bitmapDrawableArr = new BitmapDrawable[(this.x.size() > i + 6 ? i + 6 : this.x.size()) - i];
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= bitmapDrawableArr.length) {
                a(bitmapDrawableArr);
                return;
            }
            if (((String) this.x.get(i3 + i)).equals("")) {
                bitmapDrawableArr[i3] = null;
            } else {
                bitmapDrawableArr[i3] = new BitmapDrawable(getResources(), ThumbnailUtils.createVideoThumbnail((String) this.x.get(i3 + i), 3));
            }
            i2 = i3 + 1;
        }
    }

    private void e() {
        if (((MainActivity) this.b).a()) {
            int i = this.z * 6;
            int size = this.x.size() > i + 6 ? i + 6 : this.x.size();
            for (int i2 = 3; i2 < this.m.getChildCount(); i2++) {
                this.m.getChildAt(i2).setVisibility(4);
            }
            for (int i3 = 0; i3 < size - i; i3++) {
                new a(this, this.m.getChildAt(i3 + 3)).execute((String) this.x.get(i3 + i));
            }
        }
    }

    private void f() {
        if (this.q == -1 || findViewById(this.q).getVisibility() != 0) {
            return;
        }
        switch (this.q) {
            case R.id.btn_left /* 2131230731 */:
                int i = this.u;
                this.u--;
                if (this.u < 0) {
                    this.u = this.v - 1;
                }
                if (i != this.u) {
                    d();
                }
                break;
            case R.id.text_pages /* 2131230732 */:
                a(R.id.GUI_Player);
                break;
            case R.id.btn_right /* 2131230733 */:
                int i2 = this.u;
                this.u++;
                if (this.u >= this.v) {
                    this.u = this.v - 1;
                }
                if (i2 != this.u) {
                    d();
                }
                break;
            case R.id.btn_file01 /* 2131230734 */:
                b(0);
                break;
            case R.id.btn_file02 /* 2131230735 */:
                b(1);
                break;
            case R.id.btn_file03 /* 2131230736 */:
                b(2);
                break;
            case R.id.btn_file04 /* 2131230737 */:
                b(3);
                break;
            case R.id.btn_file05 /* 2131230738 */:
                b(4);
                break;
            case R.id.btn_file06 /* 2131230739 */:
                b(5);
                break;
            case R.id.btn_sbs_3d /* 2131230741 */:
                ((MainActivity) this.b).b(1);
                break;
            case R.id.btn_screen_up /* 2131230742 */:
                ((MainActivity) this.b).a(1.0f);
                break;
            case R.id.btn_panorama /* 2131230743 */:
                ((MainActivity) this.b).a(R.raw.plane_sq);
                break;
            case R.id.btn_screen_sizedown /* 2131230744 */:
                ((MainActivity) this.b).b(-1.0f);
                break;
            case R.id.btn_2d /* 2131230745 */:
                ((MainActivity) this.b).b(0);
                break;
            case R.id.btn_screen_sizeup /* 2131230746 */:
                ((MainActivity) this.b).b(1.0f);
                break;
            case R.id.btn_tb_3d /* 2131230747 */:
                ((MainActivity) this.b).b(2);
                break;
            case R.id.btn_screen_down /* 2131230748 */:
                ((MainActivity) this.b).a(-1.0f);
                break;
            case R.id.btn_dome /* 2131230749 */:
                ((MainActivity) this.b).a(R.raw.dome);
                break;
            case R.id.btn_folder /* 2131230752 */:
                a(R.id.GUI_Browser);
                break;
            case R.id.btn_youtube /* 2131230753 */:
                a(R.id.GUI_YouTube_Category);
                break;
            case R.id.btn_setting /* 2131230754 */:
                a(R.id.GUI_Setting);
                break;
            case R.id.btn_fast_rewind /* 2131230755 */:
                ((MainActivity) this.b).b();
                break;
            case R.id.btn_stop_pause_play /* 2131230756 */:
                ((MainActivity) this.b).d();
                break;
            case R.id.btn_fast_forward /* 2131230757 */:
                ((MainActivity) this.b).c();
                break;
            case R.id.btn_youtube_left /* 2131230762 */:
                int i3 = this.z;
                this.z--;
                if (this.z < 0) {
                    this.z = this.A - 1;
                }
                if (i3 != this.z) {
                    e();
                }
                break;
            case R.id.youtube_pages /* 2131230763 */:
                a(R.id.GUI_YouTube_Category);
                break;
            case R.id.btn_youtube_right /* 2131230764 */:
                int i4 = this.z;
                this.z++;
                if (this.z >= this.A) {
                    this.z = this.A - 1;
                }
                if (i4 != this.z) {
                    e();
                }
                break;
            case R.id.btn_youtube01 /* 2131230765 */:
                c(0);
                break;
            case R.id.btn_youtube02 /* 2131230766 */:
                c(1);
                break;
            case R.id.btn_youtube03 /* 2131230767 */:
                c(2);
                break;
            case R.id.btn_youtube04 /* 2131230768 */:
                c(3);
                break;
            case R.id.btn_youtube05 /* 2131230769 */:
                c(4);
                break;
            case R.id.btn_youtube06 /* 2131230770 */:
                c(5);
                break;
            case R.id.btn_youtube_earth /* 2131230772 */:
                this.y = 0;
                a(R.id.GUI_YouTube_Playlist);
                break;
            case R.id.btn_youtube_space /* 2131230773 */:
                this.y = 1;
                a(R.id.GUI_YouTube_Playlist);
                break;
            case R.id.btn_youtube_game /* 2131230774 */:
                this.y = 2;
                a(R.id.GUI_YouTube_Playlist);
                break;
            case R.id.btn_youtube_mv /* 2131230775 */:
                this.y = 3;
                a(R.id.GUI_YouTube_Playlist);
                break;
            case R.id.btn_youtube_movie /* 2131230776 */:
                this.y = 4;
                a(R.id.GUI_YouTube_Playlist);
                break;
            case R.id.btn_youtube_animation /* 2131230777 */:
                this.y = 5;
                a(R.id.GUI_YouTube_Playlist);
                break;
            case R.id.btn_youtube_demo /* 2131230778 */:
                this.y = 6;
                a(R.id.GUI_YouTube_Playlist);
                break;
            case R.id.btn_youtube_sp360 /* 2131230779 */:
                this.y = 7;
                a(R.id.GUI_YouTube_Playlist);
                break;
            case R.id.btn_youtube_yt360 /* 2131230780 */:
                this.y = 8;
                a(R.id.GUI_YouTube_Playlist);
                break;
        }
    }

    public final void a() {
        this.o.vibrate(50L);
        a(-1);
    }

    public final void a(int i) {
        this.s = i;
        if (this.d != null) {
            this.d.setVisibility(4);
        }
        if (this.e != null) {
            this.e.setVisibility(4);
        }
        if (this.j != null) {
            this.j.setVisibility(4);
        }
        if (this.k != null) {
            this.k.setVisibility(4);
        }
        if (this.l != null) {
            this.l.setVisibility(4);
        }
        if (this.m != null) {
            this.m.setVisibility(4);
        }
        if (i == -1) {
            this.r = false;
        } else {
            this.r = true;
        }
    }

    public final void a(e eVar) {
        this.c = eVar;
    }

    public final int b() {
        return this.y;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        String strD;
        if (this.c == null) {
            return;
        }
        if (!this.n) {
            this.d = (RelativeLayout) findViewById(R.id.GUI_AD);
            this.e = (TableLayout) findViewById(R.id.GUI_Player);
            this.f = (GridLayout) findViewById(R.id.GUI_Player_Btns);
            this.g = (TextView) findViewById(R.id.GUI_Player_Title);
            this.h = (TextView) findViewById(R.id.GUI_Player_Time);
            this.i = (SeekBar) findViewById(R.id.GUI_Player_ProgressBar);
            this.j = (GridLayout) findViewById(R.id.GUI_Browser);
            this.k = (GridLayout) findViewById(R.id.GUI_Setting);
            this.l = (GridLayout) findViewById(R.id.GUI_YouTube_Category);
            this.m = (GridLayout) findViewById(R.id.GUI_YouTube_Playlist);
            a(true);
            a("");
            a(this.s);
            this.n = true;
            findViewById(R.id.text_pages).setBackground(findViewById(R.id.btn_left).getBackground());
        }
        GridLayout gridLayout = this.e.getVisibility() == 0 ? this.f : this.j.getVisibility() == 0 ? this.j : this.k.getVisibility() == 0 ? this.k : this.l.getVisibility() == 0 ? this.l : this.m.getVisibility() == 0 ? this.m : null;
        if (gridLayout != null) {
            for (int i = 0; i < gridLayout.getChildCount(); i++) {
                if (this.p == i) {
                    gridLayout.getChildAt(i).setAlpha(1.0f);
                    this.q = gridLayout.getChildAt(i).getId();
                } else {
                    gridLayout.getChildAt(i).setAlpha(0.6f);
                }
            }
        }
        a(false);
        if (this.q != -1 && findViewById(this.q).getVisibility() == 0) {
            switch (this.q) {
                case R.id.btn_left /* 2131230731 */:
                    strD = "Previous Page";
                    break;
                case R.id.text_pages /* 2131230732 */:
                    strD = "Home";
                    break;
                case R.id.btn_right /* 2131230733 */:
                    strD = "Next Page";
                    break;
                case R.id.btn_file01 /* 2131230734 */:
                    strD = (String) this.w[1].get((this.u * 6) + 0);
                    break;
                case R.id.btn_file02 /* 2131230735 */:
                    strD = (String) this.w[1].get((this.u * 6) + 1);
                    break;
                case R.id.btn_file03 /* 2131230736 */:
                    strD = (String) this.w[1].get((this.u * 6) + 2);
                    break;
                case R.id.btn_file04 /* 2131230737 */:
                    strD = (String) this.w[1].get((this.u * 6) + 3);
                    break;
                case R.id.btn_file05 /* 2131230738 */:
                    strD = (String) this.w[1].get((this.u * 6) + 4);
                    break;
                case R.id.btn_file06 /* 2131230739 */:
                    strD = (String) this.w[1].get((this.u * 6) + 5);
                    break;
                case R.id.GUI_Setting /* 2131230740 */:
                case R.id.GUI_Player /* 2131230750 */:
                case R.id.GUI_Player_Btns /* 2131230751 */:
                case R.id.GUI_Player_Title /* 2131230758 */:
                case R.id.GUI_Player_Time /* 2131230759 */:
                case R.id.GUI_Player_ProgressBar /* 2131230760 */:
                case R.id.GUI_YouTube_Playlist /* 2131230761 */:
                case R.id.GUI_YouTube_Category /* 2131230771 */:
                default:
                    strD = "";
                    break;
                case R.id.btn_sbs_3d /* 2131230741 */:
                    strD = "Side By Side 3D Mode";
                    break;
                case R.id.btn_screen_up /* 2131230742 */:
                    strD = "Screen Up";
                    break;
                case R.id.btn_panorama /* 2131230743 */:
                    strD = "Panorama Screen";
                    break;
                case R.id.btn_screen_sizedown /* 2131230744 */:
                    strD = "Screen Size Down";
                    break;
                case R.id.btn_2d /* 2131230745 */:
                    strD = "2D Mode";
                    break;
                case R.id.btn_screen_sizeup /* 2131230746 */:
                    strD = "Screen Size Up";
                    break;
                case R.id.btn_tb_3d /* 2131230747 */:
                    strD = "Top And Bottom 3D Mode";
                    break;
                case R.id.btn_screen_down /* 2131230748 */:
                    strD = "Screen Down";
                    break;
                case R.id.btn_dome /* 2131230749 */:
                    strD = "Dome Screen";
                    break;
                case R.id.btn_folder /* 2131230752 */:
                    strD = "Storage";
                    break;
                case R.id.btn_youtube /* 2131230753 */:
                    strD = "YouTube";
                    break;
                case R.id.btn_setting /* 2131230754 */:
                    strD = "Setting";
                    break;
                case R.id.btn_fast_rewind /* 2131230755 */:
                    strD = "Fast Rewind";
                    break;
                case R.id.btn_stop_pause_play /* 2131230756 */:
                    if (((MainActivity) this.b).e() == 3) {
                        strD = "stop";
                        ((ImageButton) findViewById(R.id.btn_stop_pause_play)).setImageResource(R.drawable.ic_stop_white_48dp);
                    } else if (((MainActivity) this.b).e() == 5) {
                        strD = "play";
                        ((ImageButton) findViewById(R.id.btn_stop_pause_play)).setImageResource(R.drawable.ic_play_arrow_white_48dp);
                    } else if (((MainActivity) this.b).e() == 4) {
                        strD = "pause";
                        ((ImageButton) findViewById(R.id.btn_stop_pause_play)).setImageResource(R.drawable.ic_pause_white_48dp);
                    } else {
                        strD = "";
                    }
                    break;
                case R.id.btn_fast_forward /* 2131230757 */:
                    strD = "Fast Forward";
                    break;
                case R.id.btn_youtube_left /* 2131230762 */:
                    strD = "Previous Page";
                    break;
                case R.id.youtube_pages /* 2131230763 */:
                    strD = "Youtube Home";
                    break;
                case R.id.btn_youtube_right /* 2131230764 */:
                    strD = "Next Page";
                    break;
                case R.id.btn_youtube01 /* 2131230765 */:
                    strD = d(0);
                    break;
                case R.id.btn_youtube02 /* 2131230766 */:
                    strD = d(1);
                    break;
                case R.id.btn_youtube03 /* 2131230767 */:
                    strD = d(2);
                    break;
                case R.id.btn_youtube04 /* 2131230768 */:
                    strD = d(3);
                    break;
                case R.id.btn_youtube05 /* 2131230769 */:
                    strD = d(4);
                    break;
                case R.id.btn_youtube06 /* 2131230770 */:
                    strD = d(5);
                    break;
                case R.id.btn_youtube_earth /* 2131230772 */:
                    strD = "Earth";
                    break;
                case R.id.btn_youtube_space /* 2131230773 */:
                    strD = "Space";
                    break;
                case R.id.btn_youtube_game /* 2131230774 */:
                    strD = "Game";
                    break;
                case R.id.btn_youtube_mv /* 2131230775 */:
                    strD = "Music Video";
                    break;
                case R.id.btn_youtube_movie /* 2131230776 */:
                    strD = "Movie Trailer";
                    break;
                case R.id.btn_youtube_animation /* 2131230777 */:
                    strD = "Animation Movie Trailer";
                    break;
                case R.id.btn_youtube_demo /* 2131230778 */:
                    strD = "Demo";
                    break;
                case R.id.btn_youtube_sp360 /* 2131230779 */:
                    strD = "SP360";
                    break;
                case R.id.btn_youtube_yt360 /* 2131230780 */:
                    strD = "YouTube 360";
                    break;
            }
        } else {
            strD = "";
        }
        ((NACardboardOverlayView) ((Activity) this.b).findViewById(R.id.overlay)).a(strD);
        e eVar = this.c;
        Canvas canvasC = e.c();
        if (canvasC != null) {
            canvasC.translate(-getScrollX(), -getScrollY());
            super.dispatchDraw(canvasC);
        }
        e eVar2 = this.c;
        e.d();
    }

    public void onGUIButtonClick(int i) {
        if (findViewById(this.q) instanceof ImageButton) {
            this.q = i;
            f();
        } else if (findViewById(this.q) instanceof TextView) {
            this.q = i;
            f();
        }
    }
}
