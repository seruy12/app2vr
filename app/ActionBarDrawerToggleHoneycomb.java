package android.support.v4.app;

import android.R;
import android.app.ActionBar;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
class ActionBarDrawerToggleHoneycomb {
    private static final String TAG = "ActionBarDrawerToggleHoneycomb";
    private static final int[] THEME_ATTRS = {R.attr.homeAsUpIndicator};

    class SetIndicatorInfo {
        public Method setHomeActionContentDescription;
        public Method setHomeAsUpIndicator;
        public ImageView upIndicatorView;

        SetIndicatorInfo(Activity activity) {
            try {
                this.setHomeAsUpIndicator = ActionBar.class.getDeclaredMethod("setHomeAsUpIndicator", Drawable.class);
                this.setHomeActionContentDescription = ActionBar.class.getDeclaredMethod("setHomeActionContentDescription", Integer.TYPE);
            } catch (NoSuchMethodException e) {
                View viewFindViewById = activity.findViewById(R.id.home);
                if (viewFindViewById != null) {
                    ViewGroup viewGroup = (ViewGroup) viewFindViewById.getParent();
                    if (viewGroup.getChildCount() == 2) {
                        View childAt = viewGroup.getChildAt(0);
                        View childAt2 = childAt.getId() != 16908332 ? childAt : viewGroup.getChildAt(1);
                        if (childAt2 instanceof ImageView) {
                            this.upIndicatorView = (ImageView) childAt2;
                        }
                    }
                }
            }
        }
    }

    ActionBarDrawerToggleHoneycomb() {
    }

    public static Drawable getThemeUpIndicator(Activity activity) {
        TypedArray typedArrayObtainStyledAttributes = activity.obtainStyledAttributes(THEME_ATTRS);
        Drawable drawable = typedArrayObtainStyledAttributes.getDrawable(0);
        typedArrayObtainStyledAttributes.recycle();
        return drawable;
    }

    public static Object setActionBarDescription(Object obj, Activity activity, int i) {
        Object setIndicatorInfo = obj == null ? new SetIndicatorInfo(activity) : obj;
        SetIndicatorInfo setIndicatorInfo2 = (SetIndicatorInfo) setIndicatorInfo;
        if (setIndicatorInfo2.setHomeAsUpIndicator != null) {
            try {
                setIndicatorInfo2.setHomeActionContentDescription.invoke(activity.getActionBar(), Integer.valueOf(i));
            } catch (Exception e) {
                Log.w(TAG, "Couldn't set content description via JB-MR2 API", e);
            }
        }
        return setIndicatorInfo;
    }

    public static Object setActionBarUpIndicator(Object obj, Activity activity, Drawable drawable, int i) {
        Object setIndicatorInfo = obj == null ? new SetIndicatorInfo(activity) : obj;
        SetIndicatorInfo setIndicatorInfo2 = (SetIndicatorInfo) setIndicatorInfo;
        if (setIndicatorInfo2.setHomeAsUpIndicator != null) {
            try {
                ActionBar actionBar = activity.getActionBar();
                setIndicatorInfo2.setHomeAsUpIndicator.invoke(actionBar, drawable);
                setIndicatorInfo2.setHomeActionContentDescription.invoke(actionBar, Integer.valueOf(i));
            } catch (Exception e) {
                Log.w(TAG, "Couldn't set home-as-up indicator via JB-MR2 API", e);
            }
        } else if (setIndicatorInfo2.upIndicatorView != null) {
            setIndicatorInfo2.upIndicatorView.setImageDrawable(drawable);
        } else {
            Log.w(TAG, "Couldn't set home-as-up indicator");
        }
        return setIndicatorInfo;
    }
}
