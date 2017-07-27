package edu.aamu.myhealthcaresystem;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by WChoosilp-Asus on 9/4/2016.
 */
public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    Toolbar nonin;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //An array containing your icons from the drawable directory
        final int[] ICONS = new int[]{
                R.drawable.home,
                R.drawable.cardiac,
                R.drawable.bmi,
                R.drawable.bloodpressure,
                R.drawable.bluetooth
        };

        nonin = (Toolbar) findViewById(R.id.noninToolBar);
        setSupportActionBar(nonin);

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new HomeFragment(), "");
        viewPagerAdapter.addFragments(new CardiacFragment(), "");
        viewPagerAdapter.addFragments(new BMIFragment(), "");
        viewPagerAdapter.addFragments(new BloodPressureFragment(), "");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(ICONS[0]);
        tabLayout.getTabAt(1).setIcon(ICONS[1]);
        tabLayout.getTabAt(2).setIcon(ICONS[2]);
        tabLayout.getTabAt(3).setIcon(ICONS[3]);
    }

        public void onClick(View view){
        Intent intentSignUP = new Intent(getApplicationContext(), NoninWristActivity.class);
        startActivity(intentSignUP);
        }
}
