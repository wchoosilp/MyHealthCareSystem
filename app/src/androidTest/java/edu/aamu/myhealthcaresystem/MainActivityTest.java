package edu.aamu.myhealthcaresystem;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.Toolbar;

import com.robotium.solo.Solo;

/**
 * Created by WChoosilp-Asus on 8/10/2016.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @SmallTest
    public void testForActivity() {
        // check for the right activity
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
    }

/*    @SmallTest
    public void testForTitleToolBarLayout() {
        Toolbar titleToolBar = (Toolbar) solo.getView(R.layout.toolbar_layout);
        // Check for Button not null
        assertNotNull(titleToolBar);
    }

    @SmallTest
    public void testForNoninToolBarLayout() {
        Toolbar noninToolBar = (Toolbar) solo.getView(R.layout.nonin_toolbar_layout);
        // Check for Button not null
        assertNotNull(noninToolBar);
    }*/

    @SmallTest
    public void testForTabLayout() {
        TabLayout tabLayout = (TabLayout) solo.getView(R.id.tabLayout);
        // Check for Button not null
        assertNotNull(tabLayout);
    }

    @SmallTest
    public void testForViewPager() {
        ViewPager viewPager = (ViewPager) solo.getView(R.id.viewPager);
        // Check for Button not null
        assertNotNull(viewPager);
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
