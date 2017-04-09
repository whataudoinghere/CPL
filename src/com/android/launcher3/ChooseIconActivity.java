/*
 * Copyright (C) 2017 Paranoid Android
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.launcher3;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChooseIconActivity extends Activity {

    private String mCurrentPackageLabel;
    private String mCurrentPackageName;
    private String mIconPackPackageName;

    private GridLayoutManager mGridLayout;
    private ProgressBar mProgressBar;
    private RecyclerView mIconsGrid;
    private IconCache mIconCache;
    private IconsHandler mIconsHandler;

    private static ItemInfo sItemInfo;

    private int mIconSize;

    private List<String> mAllIcons;
    private List<String> mMatchingIcons;
    private GridAdapter mGridAdapter;
    private ActionBar mActionBar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        initQueryTextListener(searchView);

        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_icons_view);

        mActionBar = getActionBar();

        mCurrentPackageName = getIntent().getStringExtra("app_package");
        mCurrentPackageLabel = getIntent().getStringExtra("app_label");
        mIconPackPackageName = getIntent().getStringExtra("icon_pack_package");

        PackageManager packageManager = getPackageManager();
        ApplicationInfo iconPackInfo;
        try {
            iconPackInfo = packageManager.getApplicationInfo(mIconPackPackageName, 0);
            mActionBar.setSubtitle(packageManager.getApplicationLabel(iconPackInfo));

        } catch (final PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mIconCache = LauncherAppState.getInstance(getApplicationContext()).getIconCache();
        mIconsHandler = IconCache.getIconsHandler(this);

        int itemSpacing = getResources().getDimensionPixelSize(R.dimen.grid_item_spacing);
        mIconsGrid = (RecyclerView) findViewById(R.id.icons_grid);
        mIconsGrid.setHasFixedSize(true);
        mIconsGrid.addItemDecoration(new GridItemSpacer(itemSpacing));
        mGridLayout = new GridLayoutManager(this, 4);
        mIconsGrid.setLayoutManager(mGridLayout);
        mIconsGrid.setAlpha(0.0f);
        mActionBar.hide();

        mProgressBar = (ProgressBar) findViewById(R.id.icons_grid_progress);

        mIconSize = getResources().getDimensionPixelSize(R.dimen.icon_pack_icon_size);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Activity activity = ChooseIconActivity.this;
                mAllIcons = mIconsHandler.getAllDrawables(mIconPackPackageName);
                mMatchingIcons = mIconsHandler.getMatchingDrawables(mCurrentPackageName);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mGridAdapter = new GridAdapter(mAllIcons, mMatchingIcons);
                        mIconsGrid.setAdapter(mGridAdapter);
                        mProgressBar.setVisibility(View.GONE);
                        mIconsGrid.animate().alpha(1.0f);
                        mActionBar.show();
                    }
                });
            }
        }).start();
    }

    public static void setItemInfo(ItemInfo info) {
        sItemInfo = info;
    }

    class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> implements Filterable {

        private static final int TYPE_MATCHING_HEADER = 0;
        private static final int TYPE_MATCHING_ICONS = 1;
        private static final int TYPE_ALL_HEADER = 2;
        private static final int TYPE_ALL_ICONS = 3;

        private boolean mNoMatchingDrawables;

        private List<String> mAllDrawables = new ArrayList<>();
        private List<String> mMatchingDrawables = new ArrayList<>();

        void filterList(List<String> filteredAllDrawables, List<String> filteredMatchingDrawables) {

            mAllDrawables = filteredAllDrawables;
            mMatchingDrawables = filteredMatchingDrawables;
            notifyDataSetChanged();
        }

        private GridAdapter(List<String> allDrawables, List<String> matchingDrawables) {
            mAllDrawables.add(null);
            mAllDrawables.addAll(allDrawables);
            mMatchingDrawables.add(null);
            mMatchingDrawables.addAll(matchingDrawables);
            mGridLayout.setSpanSizeLookup(mSpanSizeLookup);
            mNoMatchingDrawables = matchingDrawables.isEmpty();
            if (mNoMatchingDrawables) {
                mMatchingDrawables.clear();
            }
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    return new FilterResults();
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    //do nothing
                }
            };
        }

        @Override
        public int getItemViewType(int position) {
            if (!mNoMatchingDrawables && position < mMatchingDrawables.size() &&
                    mMatchingDrawables.get(position) == null) {
                return TYPE_MATCHING_HEADER;
            }

            if (!mNoMatchingDrawables && position > TYPE_MATCHING_HEADER &&
                    position < mMatchingDrawables.size()) {
                return TYPE_MATCHING_ICONS;
            }

            if (position == mMatchingDrawables.size()) {
                return TYPE_ALL_HEADER;
            }
            return TYPE_ALL_ICONS;
        }

        @Override
        public int getItemCount() {
            return mAllDrawables.size() + 1;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == TYPE_MATCHING_HEADER) {
                TextView text = (TextView) getLayoutInflater().inflate(
                        R.layout.all_icons_view_header, null);
                text.setText(R.string.similar_icons);
                return new ViewHolder(text);
            }
            if (viewType == TYPE_ALL_HEADER) {
                TextView text = (TextView) getLayoutInflater().inflate(
                        R.layout.all_icons_view_header, null);
                text.setText(R.string.all_icons);
                return new ViewHolder(text);
            }
            ImageView view = new ImageView(ChooseIconActivity.this);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT, mIconSize);
            view.setLayoutParams(params);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            if (holder.getItemViewType() != TYPE_MATCHING_HEADER
                    && holder.getItemViewType() != TYPE_ALL_HEADER) {
                boolean drawablesMatching = holder.getItemViewType() == TYPE_MATCHING_ICONS;
                final List<String> drawables = drawablesMatching ?
                        mMatchingDrawables : mAllDrawables;
                if (position >= drawables.size()) {
                    return;
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Drawable icon = mIconsHandler.loadDrawable(
                                mIconPackPackageName, drawables.get(holder.getAdapterPosition()), true);
                        if (icon != null) {
                            mIconCache.addCustomInfoToDataBase(icon, sItemInfo, mCurrentPackageLabel);
                        }
                        ChooseIconActivity.this.finish();
                    }
                });
                Drawable icon = null;
                String drawable = drawables.get(position);
                try {
                    icon = mIconsHandler.loadDrawable(mIconPackPackageName, drawable, true);
                } catch (OutOfMemoryError e) {
                    // time for a new device?
                    e.printStackTrace();
                }
                if (icon != null) {
                    ((ImageView)holder.itemView).setImageDrawable(icon);
                }
            }
        }

        private final SpanSizeLookup mSpanSizeLookup = new SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return getItemViewType(position) == TYPE_MATCHING_HEADER
                        || getItemViewType(position) == TYPE_ALL_HEADER ?
                        4 : 1;
            }
        };

        class ViewHolder extends RecyclerView.ViewHolder {
            private ViewHolder(View v) {
                super(v);
            }
        }
    }

    private class GridItemSpacer extends RecyclerView.ItemDecoration {
        private int spacing;

        private GridItemSpacer(int spacing) {
            this.spacing = spacing;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                 RecyclerView.State state) {
            outRect.top = spacing;
        }
    }

    private void initQueryTextListener(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                IconsSearchUtils.filter(newText, mMatchingIcons, mAllIcons, mGridAdapter);
                return true;
            }
        });
    }
}
