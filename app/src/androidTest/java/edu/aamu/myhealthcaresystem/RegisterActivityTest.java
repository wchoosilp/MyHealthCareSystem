package edu.aamu.myhealthcaresystem;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by WChoosilp-Asus on 8/10/2016.
 */
public class RegisterActivityTest extends ActivityInstrumentationTestCase2<RegisterActivity> {

    private Solo solo;

    public RegisterActivityTest() {
        super(RegisterActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @SmallTest
    public void testForActivity() {
        // check for the right activity
        solo.assertCurrentActivity("wrong activity", RegisterActivity.class);
    }

    @SmallTest
    public void testForEditText() {
        // check the display for the right Edit Text
        EditText etUsername = (EditText) solo.getView(R.id.editTextUserName);
        EditText etPassword = (EditText) solo.getView(R.id.editTextPassword);
        EditText etConfirmPassword = (EditText) solo.getView(R.id.editTextConfirmPassword);

        assertEquals(View.VISIBLE, etUsername.getVisibility());
        assertEquals(View.VISIBLE, etPassword.getVisibility());
        assertEquals(View.VISIBLE, etConfirmPassword.getVisibility());
    }

    @SmallTest
    public void testForEmptyFields() {
        // check for the empty fields
        EditText etUsername = (EditText) solo.getView(R.id.editTextUserName);
        EditText etPassword = (EditText) solo.getView(R.id.editTextPassword);
        EditText etConfirmPassword = (EditText) solo.getView(R.id.editTextConfirmPassword);

        assertNotNull(etUsername);
        assertNotNull(etPassword);
        assertNotNull(etConfirmPassword);
    }

    @SmallTest
    public void testForUserInputs() {
        // check for user inputs
        EditText etUsername = (EditText) solo.getView(R.id.editTextUserName);
        EditText etPassword = (EditText) solo.getView(R.id.editTextPassword);
        EditText etConfirmPassword = (EditText) solo.getView(R.id.editTextConfirmPassword);

        solo.enterText(etUsername, "wchoosilp");
        solo.enterText(etPassword, "123");
        solo.enterText(etConfirmPassword, "123");
    }

    @SmallTest
    public void testForCreateAccountButton() {
        Button createAccount = (Button) solo.getView(R.id.buttonCreateAccount);

        // Check for Button not null
        assertNotNull(createAccount);

        // Click a button which will start a new Activity
        // Here is the ID of the string to find the right button
        solo.clickOnButton(solo.getString(R.string.create_account));
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
