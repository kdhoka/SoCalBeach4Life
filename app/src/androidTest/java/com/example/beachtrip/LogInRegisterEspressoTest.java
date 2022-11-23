package com.example.beachtrip;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.ViewAction;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LogInRegisterEspressoTest {
    private static void pause(int length){
        long startTime = System.currentTimeMillis();
        long endTime = startTime + length;
        while (startTime < endTime) {
            startTime = System.currentTimeMillis();
        }
    }

    //display log in / register page upon launch
    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes. This is a replacement for {}.
     */
    //TODO: see if launch reg/log in page directly still allows successful reg/log
    @Rule
    public ActivityScenarioRule<LogInRegisterActivity> activityScenarioRule
            = new ActivityScenarioRule<>(LogInRegisterActivity.class);


    @Test
    public void register_success_Test(){
        Intents.init();
        //navigate to log in / register page

        String name = "espresso";
        String email = "espresso@gmail.com";
        String password = "espresso123";

        onView(withId(R.id.name)).perform(typeText(name));
        pause(500);
        onView(withId(R.id.email)).perform(typeText(email));
        pause(500);
        onView((withId(R.id.pwd))).perform((typeText(password)));
        pause(500);

        onView(withId(R.id.register)).perform(click());
        pause(500);


        intended(hasComponent(MainActivity.class.getName()));
    }

}