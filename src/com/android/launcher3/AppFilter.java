package com.android.launcher3;

import android.content.Context;

public interface AppFilter {

    boolean shouldShowApp(String packageName, Context context);
}
