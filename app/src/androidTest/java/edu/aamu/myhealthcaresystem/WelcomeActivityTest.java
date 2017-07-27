package edu.aamu.myhealthcaresystem;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;

import com.robotium.solo.Solo;

/**
 * Created by WChoosilp-Asus on 9/15/2016.
 */
public class WelcomeActivityTest extends ActivityInstrumentationTestCase2<WelcomeActivity> {

    private Solo solo;

    public WelcomeActivityTest() {
        super(WelcomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @SmallTest
    public void testForActivity() {
        // check for the right activity
        solo.assertCurrentActivity("Wrong activity", WelcomeActivity.class);
    }

    @SmallTest
    public void testForSignInButton() {
        Button signIn = (Button) solo.getView(R.id.bSignIn);
        // Check for Button not null
        assertNotNull(signIn);

        // Click a button which will start a new Activity
        // Here is the ID of the string to find the right button
        solo.clickOnButton(solo.getString(R.string.sign_in));
    }

    @SmallTest
    public void testForRegisterButton() {
        Button register = (Button) solo.getView(R.id.bRegister);
        // Check for Button not null
        assertNotNull(register);

        // Click a button which will start a new Activity
        // Here is the ID of the string to find the right button
        solo.clickOnButton(solo.getString(R.string.register));
    }

    @SmallTest
    public void testForOpenRegisterScreen() {
        //Click on Register button
        solo.clickOnButton(solo.getString(R.string.register));
        if (solo.waitForActivity(WelcomeActivity.class)) {
            // select Register button
            solo.clickOnButton(0);

            // add user name/password/confirmed password, then create account
            if (solo.waitForActivity(RegisterActivity.class)) {
                solo.enterText(0, "wchoosilp");
                solo.enterText(1, "123");
                solo.enterText(2, "123");
                solo.clickOnButton(1);
            }
        }
    }

    @SmallTest
    public void testForExitButton() {
        Button exit = (Button) solo.getView(R.id.bExit);
        // Check for Button not null
        assertNotNull(exit);

        // Click a button which will start a new Activity
        // Here is the ID of the string to find the right button
        solo.clickOnButton(solo.getString(R.string.exit));
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
