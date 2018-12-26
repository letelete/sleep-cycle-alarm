package com.gmail.brunokawka.poland.sleepcyclealarm;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.gmail.brunokawka.poland.sleepcyclealarm.app.base.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<> (
            MainActivity.class);

    @Test
    public void testIfCanShowAndHideWakeUpAtButtonProperly() {
        onView(withId(R.id.action_sleepnow)).perform(click());
        testIfButtonIsGone();

        onView(withId(R.id.action_wakeupat)).perform(click());
        testIfButtonIsVisible();

        onView(withId(R.id.action_alarms)).perform(click());
        testIfButtonIsGone();

        onView(withId(R.id.action_wakeupat)).perform(click());
        testIfButtonIsVisible();
    }

    public void testIfButtonIsVisible() {
        onView(withId(R.id.wakeUpAtFloatingActionButtonExtended)).check(matches(isDisplayed()));
    }

    public void testIfButtonIsGone() {
        onView(withId(R.id.wakeUpAtFloatingActionButtonExtended)).check(matches(not(isDisplayed())));
    }
}