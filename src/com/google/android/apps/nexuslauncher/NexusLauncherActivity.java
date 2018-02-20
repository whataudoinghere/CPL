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
        boolean darktheme = Utilities.getPrefs(this).getBoolean("pref_darktheme_enabled", false);
        int darkthemestyle = Integer.valueOf(Utilities.getPrefs(this).getString("pref_darkthemestyle", "1"));
        boolean googlebarinappmenu = Utilities.getPrefs(this).getBoolean("pref_googleinappmenu_enabled", false);
        boolean darktext = Utilities.getPrefs(this).getBoolean("pref_darktext_enabled", false);
        setTheme(R.style.GoogleSearchLauncherTheme);
        if (darktheme && googlebarinappmenu) {
            if (darkthemestyle == 1) setTheme(R.style.GoogleSearchLauncherThemeDark);
            else setTheme(R.style.GoogleSearchLauncherThemeBlack);
        }
        if (darktheme && !googlebarinappmenu) {
            if (darkthemestyle == 1) setTheme(R.style.LauncherThemeDark);
            else setTheme(R.style.LauncherThemeBlack);
        }
        if (!darktheme && !googlebarinappmenu) {
            setTheme(R.style.LauncherTheme);
        }
        if (darktext && Utilities.ATLEAST_NOUGAT && googlebarinappmenu) {
            setTheme(R.style.GoogleSearchLauncherThemeDarkText);
        }
        if (darktext && Utilities.ATLEAST_NOUGAT && !googlebarinappmenu) {
            setTheme(R.style.LauncherThemeDarkText);
        }
        if (darktext && darktheme && !googlebarinappmenu) {
            if (darkthemestyle == 1) setTheme(R.style.LauncherThemeDark);
            else setTheme(R.style.LauncherThemeBlack);
        }
        if (darktext && darktheme && googlebarinappmenu) {
            if (darkthemestyle == 1) setTheme(R.style.GoogleSearchLauncherThemeDark);
            else setTheme(R.style.GoogleSearchLauncherThemeBlack);
        }
    }

    public List<ComponentKeyMapper<AppInfo>> getPredictedApps() {
        return mLauncher.fA.getPredictedApps();
    }

    public GoogleNow getGoogleNow() {
        return mLauncher.fy;
    }
}
