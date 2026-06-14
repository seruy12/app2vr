package android.content.pm;

import android.content.IntentFilter;

/* JADX INFO: loaded from: classes.dex */
public class PackageParser {

    public final class Activity extends Component {
        public final ActivityInfo info;

        public Activity(Object obj, ActivityInfo activityInfo) {
            super(obj, activityInfo);
            this.info = activityInfo;
        }
    }

    public final class ActivityIntentInfo extends IntentInfo {
        public final Activity activity;

        public ActivityIntentInfo(Activity activity) {
            this.activity = activity;
        }
    }

    public class Component {
        public Component(Object obj, ActivityInfo activityInfo) {
        }

        public Component(Package r1) {
        }
    }

    public class IntentInfo extends IntentFilter {
        public boolean hasDefault;
        public int icon;
        public int labelRes;
        public int logo;
        public CharSequence nonLocalizedLabel;
        public int preferred;
    }
}
