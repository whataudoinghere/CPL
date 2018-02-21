package com.google.android.apps.nexuslauncher;

import android.content.res.Configuration;

import com.android.launcher3.AppInfo;
import com.android.launcher3.Launcher;
import com.android.launcher3.R;
import com.android.launcher3.Utilities;
import com.android.launcher3.util.ComponentKeyMapper;
import com.google.android.libraries.launcherclient.GoogleNow;

import java.util.List;

public class NexusLauncherActivity extends Launcher {
    private NexusLauncher mLauncher;

    public NexusLauncherActivity() {
        mLauncher = new NexusLauncher(this);
    }

    public void overrideTheme(boolean isDark, boolean supportsDarkText) {
        int themestyle = Integer.valueOf(Utilities.getPrefs(this).getString("pref_themestyle", "1"));
        boolean googlebarinappmenu = Utilities.getPrefs(this).getBoolean("pref_googleinappmenu_enabled", false);
        if (googlebarinappmenu) {
            if (themestyle == 1) setTheme(R.style.GoogleSearchLauncherTheme);
            if (themestyle == 2) {
                if (Utilities.ATLEAST_NOUGAT) setTheme(R.style.GoogleSearchLauncherThemeDarkText);
            }
            if (themestyle == 3) setTheme(R.style.GoogleSearchLauncherThemeDark);
            if (themestyle == 4) setTheme(R.style.GoogleSearchLauncherThemeBlack);
        }
        if (!googlebarinappmenu) {
            if (themestyle == 1) setTheme(R.style.LauncherTheme);
            if (themestyle == 2) {
                if (Utilities.ATLEAST_NOUGAT) setTheme(R.style.LauncherThemeDarkText);
            }
            if (themestyle == 3) setTheme(R.style.LauncherThemeDark);
            if (themestyle == 4) setTheme(R.style.LauncherThemeBlack);
        }
    }

    public List<ComponentKeyMapper<AppInfo>> getPredictedApps() {
        return mLauncher.fA.getPredictedApps();
    }

    public GoogleNow getGoogleNow() {
        return mLauncher.fy;
    }
}
