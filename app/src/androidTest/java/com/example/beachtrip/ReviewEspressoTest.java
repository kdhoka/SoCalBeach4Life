package com.example.beachtrip;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import android.content.Context;
import android.content.Intent;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class ReviewEspressoTest {
    public static final String STRING_TO_BE_TYPED = "Espresso";

    private static void pause(int length){
        long startTime = System.currentTimeMillis();
        long endTime = startTime + length;
        while (startTime < endTime) {
            startTime = System.currentTimeMillis();
        }
    }

    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes. This is a replacement for {}.
     */
    @Rule
    public ActivityScenarioRule<LogInRegisterActivity> activityScenarioRule
            = new ActivityScenarioRule<>(LogInRegisterActivity.class);

    @Before

    /**
     * Run the app and log in before launching the test. Hard code beach id
     */
    @Test
    public void rating_only_review_test(){
        //Log in
        String name = "espressoReview";
        String email  = "review@usc.edu";
        String pwd = "review123";

        onView(withId(R.id.name)).perform(typeText(name), closeSoftKeyboard());
        pause(500);
        onView(withId(R.id.email)).perform(typeText(email), closeSoftKeyboard());
        pause(500);
        onView((withId(R.id.pwd))).perform((typeText(pwd)), closeSoftKeyboard());

        onView(withId(R.id.signIn)).perform(click());
        pause(1000);

        //main->profile
        onView(withId(R.id.profile_btn)).perform(click());
        pause(1000);

        //click on Spinner to select Marina beach to see my review
        onView(withId(R.id.beachChoice)).perform(click());
        pause(500);
        onData(allOf(is(instanceOf(String.class)), is("Marina Beach"))).perform(click());
        pause(500);

        //navigate to UserReviewPage
        onView(withId(R.id.button2)).perform(click());
        pause(500);

        //input a rating, confirm
        String rating = "4.99";
        onView(withId(R.id.rating)).perform(typeText(rating), closeSoftKeyboard());
        pause(200);
        onView(withId(R.id.confirm_btn)).perform(click());
        pause(500);

        //Now at UserReviewPage, navigate back to beach info page
        onView(withId(R.id.user_review_back_btn)).perform(click());
        pause(1000);

        //navigate from beach info page to beach review page
        onView(withId(R.id.review_button)).perform(click());
        pause(1000);

        //jump from beach review page to userReviewPage
        onView(withId(R.id.button)).perform(click());
        pause(1000);

        //check if rating view shows the rating uploaded
        onView(withId(R.id.rating)).check(matches(withText(rating)));
//        pause(1500);
    }

//    @Test
//    public void with_comment_review_test(){
//
//    }
//
//    @Test
//    public void with_picture_review_test(){
//
//    }
//
//    @Test
//    public void anonymous_review_test(){
//
//    }
//
//    @Test
//    public void review_delete_test(){
//
//    }
}
