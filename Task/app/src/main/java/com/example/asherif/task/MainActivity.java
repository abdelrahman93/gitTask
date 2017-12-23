package com.example.asherif.task;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;



/**
 * This MainActivity that control the fragments of the Tab Layouts.   .
 */

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set the Montserrat font to the application
        FontsOverride.setDefaultFont(this, "MONOSPACE", "Fonts/Montserrat-Black.ttf");
        // Initialization
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //Adding Tabs icons
        tabLayout.getTabAt(0).setIcon(R.drawable.search_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.list_icon);


    }

    // This method to set the fragments to the Tabs
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new QuickSearch(), "Quick Search");
        adapter.addFragment(new ListView(), "List View");
        viewPager.setAdapter(adapter);
    }

}
