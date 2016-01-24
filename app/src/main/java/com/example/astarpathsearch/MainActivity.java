package com.example.astarpathsearch;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        DrawerFragment mDrawer = new DrawerFragment();
        ContentFragment mContent = new ContentFragment();

        getFragmentManager().beginTransaction()
                .replace(R.id.container_drawer, mDrawer)
                .replace(R.id.container_content, mContent)
                .commit();

        DrawerLayout mDrawLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        DrawerLayout.DrawerListener mListener = new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().setTitle("Closed");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle("Opened");
            }
        };

        mDrawLayout.setDrawerListener(mListener);

    }
}
