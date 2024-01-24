package com.example.as1;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.as1.screens.LoginPage;
import com.example.as1.screens.StartPage;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LoginTest {
    @Rule   // needed to launch the activity
    public ActivityTestRule<LoginPage> activityRule = new ActivityTestRule<>(LoginPage.class);

    /**
     * Start the server and run this test
     */
    @Test
    public void Login(){
        //Enter username
        onView(withId(R.id.usernameLogin_txtInput))
                .perform(typeText("user"), closeSoftKeyboard());
        //Enter password
        onView(withId(R.id.passwordLogin_txtInput))
                .perform(typeText("Password"), closeSoftKeyboard());
        //Send login request
        onView(withId(R.id.sendLoginReq_btn)).perform(click());
        //Check
        onView(withId(R.id.loginReqResponse_txt)).check(matches(withText("Success!")));
    }

}
