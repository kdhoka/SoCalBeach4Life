package com.example.beachtrip;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowMetrics;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Tap;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.*;


/**
 * Basic tests showcasing simple view matchers and actions like {@link ViewMatchers#withId},
 * {@link ViewActions#click} and {@link ViewActions#typeText}.
 * <p>
 * Note that there is no need to tell Espresso that a view is in a different {@link Activity}.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RestaurantEspressoTest {

    public static final String STRING_TO_BE_TYPED = "Espresso";

    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes. This is a replacement for {}.
     */
    @Rule public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule<>(MainActivity.class);
    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule =
            new IntentsTestRule<>(MainActivity.class);

    private static ViewAction clickXY(final int x, final int y){
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);

                        final float screenX = screenPos[0] + x;
                        final float screenY = screenPos[1] + y;
                        float[] coordinates = {screenX, screenY};

                        return coordinates;
                    }
                },
                Press.FINGER);
    }

    //private static ViewAction zoomIn(){ return clickXY(1002, 1530); }
    private static ViewAction zoomIn(){
        return clickXY(500, 1000);
    }

    private static void properZoomIn(UiObject marker){
        for(int i = 0; i < 6; ++i){
            onView(withId(R.id.map)).perform(zoomIn());
            onView(withId(R.id.map)).perform(zoomIn());
            try{
                marker.click();
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
            }
            pause(200);
        }
        pause(100);
    }

    private static void pause(int length){
        long startTime = System.currentTimeMillis();
        long endTime = startTime + length;
        while (startTime < endTime) {
            startTime = System.currentTimeMillis();
        }
    }

    @Test
    public void BeachInfoNavigateTest() {
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Manhattan Beach"));
        try {
            marker.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        pause(200);

        onView(withId(R.id.beachButton)).perform(click());

        intended(hasComponent(BeachInfoActivity.class.getName()));
    }


    @Test
    public void rangeSelectionTest() {
        // Type text and then press the button.
        onView(withId(R.id.rangeChoice)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("2000"))).perform(click());
        onView(withId(R.id.rangeChoice)).check(matches(withSpinnerText(containsString("2000"))));

        pause(100);
        onView(withId(R.id.rangeChoice)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("3000"))).perform(click());
        onView(withId(R.id.rangeChoice)).check(matches(withSpinnerText(containsString("3000"))));

        pause(100);
        onView(withId(R.id.rangeChoice)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("1000"))).perform(click());
        onView(withId(R.id.rangeChoice)).check(matches(withSpinnerText(containsString("1000"))));
    }

    @Test
    public void RestaurantDisplayTestRange1000() {
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject beach = device.findObject(new UiSelector().descriptionContains("Manhattan Beach"));

        try {
            beach.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        pause(200);

        properZoomIn(beach);

        try {
            beach.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        pause(200);

        UiObject restaurant1 = device.findObject(new UiSelector().descriptionContains("Mangiamo Ristorante"));
        try {
            restaurant1.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        pause(100);
        onView(withId(R.id.restaurantButton)).perform(click());
        intended(hasComponent(RestaurantPage.class.getName()));

        pause(100);

        onView(withId(R.id.restaurant1)).check(matches(withText("Mangiamo Ristorante")));
        onView(withId(R.id.menu)).check(matches(isDisplayed()));
        onView(withId(R.id.hours)).check(matches(isDisplayed()));
    }

    @Test
    public void RestaurantDisplayTestRange2000() {
        onView(withId(R.id.rangeChoice)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("2000"))).perform(click());

        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject beach = device.findObject(new UiSelector().descriptionContains("Manhattan Beach"));
        try {
            beach.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        pause(200);

        properZoomIn(beach);

        try {
            beach.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        pause(200);

        UiObject restaurant1 = device.findObject(new UiSelector().descriptionContains("RICE Healthy Japanese Dining"));
        try {
            restaurant1.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        pause(100);

        onView(withId(R.id.restaurantButton)).perform(click());
        intended(hasComponent(RestaurantPage.class.getName()));

        pause(100);

        onView(withId(R.id.restaurant1)).check(matches(withText("RICE Healthy Japanese Dining")));
        onView(withId(R.id.menu)).check(matches(isDisplayed()));
        onView(withId(R.id.hours)).check(matches(isDisplayed()));
    }

    @Test
    public void RestaurantDisplayTestRange3000() {
        onView(withId(R.id.rangeChoice)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("3000"))).perform(click());

        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject beach = device.findObject(new UiSelector().descriptionContains("Cabrillo Beach"));
        try {
            beach.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        pause(100);

        properZoomIn(beach);

        try {
            beach.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        pause(100);

        UiObject restaurant1 = device.findObject(new UiSelector().descriptionContains("Harborview Lounge & Cafe"));
        try {
            restaurant1.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        pause(100);

        onView(withId(R.id.restaurantButton)).perform(click());
        intended(hasComponent(RestaurantPage.class.getName()));

        pause(100);

        onView(withId(R.id.restaurant1)).check(matches(withText("Harborview Lounge & Cafe")));
        onView(withId(R.id.menu)).check(matches(isDisplayed()));
        onView(withId(R.id.hours)).check(matches(isDisplayed()));
    }
}