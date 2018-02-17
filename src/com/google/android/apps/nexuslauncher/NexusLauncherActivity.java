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
        boolean googlebarinappmenu = Utilities.getPrefs(this).getBoolean("pref_googleinappmenu_enabled", false);
        boolean darktext = Utilities.getPrefs(this).getBoolean("pref_darktext_enabled", false);
        boolean autotheme = Utilities.getPrefs(this).getBoolean("pref_autotheme_enabled", false);
        setTheme(R.style.LauncherTheme);
        if (darktheme && googlebarinappmenu && !autotheme) {
            setTheme(R.style.GoogleSearchLauncherThemeDark);
        }
        if (darktheme && !googlebarinappmenu && !autotheme) {
            setTheme(R.style.LauncherThemeDark);
        }
        if (!darktheme && !googlebarinappmenu && !autotheme) {
            setTheme(R.style.LauncherTheme);
        }
        if (darktext && Utilities.ATLEAST_NOUGAT && googlebarinappmenu && !autotheme) {
            setTheme(R.style.GoogleSearchLauncherThemeDarkText);
        }
        if (darktext && Utilities.ATLEAST_NOUGAT && !googlebarinappmenu && !autotheme) {
            setTheme(R.style.LauncherThemeDarkText);
        }
        if (darktext && darktheme && !googlebarinappmenu && !autotheme) {
            setTheme(R.style.LauncherThemeDark);
        }
        if (darktext && darktheme && googlebarinappmenu && !autotheme) {
            setTheme(R.style.GoogleSearchLauncherThemeDark);
        }
//этот участок для автотемы
        if (googlebarinappmenu && autotheme) {
            setTheme(R.style.GoogleSearchLauncherTheme);
        }
        if (isDark && autotheme) {
            setTheme(R.style.LauncherThemeDark);
        }
        if (supportsDarkText && Utilities.ATLEAST_NOUGAT && autotheme) {
            setTheme(R.style.LauncherThemeDarkText);
        }
        if (isDark && googlebarinappmenu && autotheme) {
            setTheme(R.style.GoogleSearchLauncherThemeDark);
        }
        if (supportsDarkText && googlebarinappmenu && Utilities.ATLEAST_NOUGAT && autotheme) {
            setTheme(R.style.GoogleSearchLauncherThemeDarkText);
        }
    }

    public List<ComponentKeyMapper<AppInfo>> getPredictedApps() {
        return mLauncher.fA.getPredictedApps();
    }

    public GoogleNow getGoogleNow() {
        return mLauncher.fy;
    }
}
