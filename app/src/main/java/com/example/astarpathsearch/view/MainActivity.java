package com.example.astarpathsearch.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.example.astarpathsearch.R;

public class MainActivity extends AppCompatActivity implements
        ContentFragment.OnFragmentInteractionListener {
    private DrawerLayout mDrawLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        DrawerFragment mDrawer = new DrawerFragment();
        ContentFragment mContent = new ContentFragment();

        mDrawer.setOnConfigureChangedListener(mContent);

        getFragmentManager().beginTransaction()
                .replace(R.id.container_drawer, mDrawer)
                .replace(R.id.container_content, mContent)
                .commit();

        mDrawLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        mDrawLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle("设置");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (getSupportActionBar() != null) {
                    String str = getString(R.string.app_name);
                    getSupportActionBar().setTitle(str);
                }
            }
        });
    }

    @SuppressWarnings("all")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mDrawLayout.isDrawerOpen(Gravity.LEFT))
                mDrawLayout.closeDrawer(Gravity.LEFT);
            else
                mDrawLayout.openDrawer(Gravity.LEFT);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("all")
    @Override
    public void openSettings() {
        mDrawLayout.openDrawer(Gravity.LEFT);
    }
}
