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

import com.example.as1.screens.CreateAccountPage;
import com.example.as1.screens.CreateGroup;
import com.example.as1.screens.LoginPage;
import com.example.as1.screens.StartPage;
import com.example.as1.screens.StockPage;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class StockInfoTest {
    @Rule   // needed to launch the activity
    public ActivityTestRule<StockPage> activityRule = new ActivityTestRule<>(StockPage.class);

    /**
     * Start the server and run this test
     */
    @Test
    public void StockInfo(){

        //Check first page is initialized
        onView(withId(R.id.stockID_view)).check(matches(withText("ID: 1")));
        onView(withId(R.id.symbol_StockPage)).check(matches(withText("TSLA")));
    }

}
