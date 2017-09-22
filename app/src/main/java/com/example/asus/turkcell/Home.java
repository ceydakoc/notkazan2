package com.example.asus.turkcell;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

public class Home extends AppCompatActivity {

   private static  final String TAG="HomeActivity";
    private SectionsPageAdapter mSectionsPageAdapter ;
    private ViewPager mviewPager;
    private TabLayout tabLayout;

    private int[] tabsIcons={
           R.drawable.user, R.drawable.home,R.drawable.search

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG,"onCreate: Starting.");
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        mviewPager =(ViewPager)findViewById(R.id.container);
        setupViewPager(mviewPager);

        tabLayout =(TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mviewPager);
        setupTabIcons();

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabsIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabsIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabsIcons[2]);


    }

    private void setupViewPager(ViewPager viewPager){

        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Profile(),"");
        adapter.addFragment(new Timeline(),"");
        adapter.addFragment(new Search(),"");
        viewPager.setAdapter(adapter);

    }




}
