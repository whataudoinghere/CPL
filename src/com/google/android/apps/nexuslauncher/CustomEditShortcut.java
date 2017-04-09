package com.google.android.apps.nexuslauncher;

import android.content.ComponentName;
import android.view.View;

import com.android.launcher3.AbstractFloatingView;
import com.android.launcher3.ItemInfo;
import com.android.launcher3.Launcher;
import com.android.launcher3.ShortcutInfo;
import com.android.launcher3.R;
import com.android.launcher3.graphics.DrawableFactory;
import com.android.launcher3.popup.SystemShortcut;

import com.android.launcher3.AppInfo;

public class CustomEditShortcut extends SystemShortcut {
    public CustomEditShortcut() {
        super(R.drawable.ic_edit_no_shadow, R.string.action_edit);
    }

    @Override
    public View.OnClickListener getOnClickListener(final Launcher launcher, final ItemInfo itemInfo) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComponentName componentName = null;
                if (itemInfo instanceof com.android.launcher3.AppInfo) {
                    componentName = ((com.android.launcher3.AppInfo) itemInfo).componentName;
                } else if (itemInfo instanceof ShortcutInfo) {
                    componentName = ((ShortcutInfo) itemInfo).intent.getComponent();
                }

                if (componentName != null) {
                    launcher.startEdit(itemInfo, componentName);
                }
            }
        };
    }
}
