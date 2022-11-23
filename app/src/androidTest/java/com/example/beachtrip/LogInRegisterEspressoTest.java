package com.example.beachtrip;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;


import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.ViewAction;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LogInRegisterEspressoTest {

    private class Credential{
        private String email;
        Credential(){
            email = "";
        }

        void setEmail(String email){
            this.email = email;
        }

        String getEmail(){
            return email;
        }
    }

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

    @Before
    public void setup(){
        Intents.init();
    }

    @Test
    public void register_success_Test(){
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

        intended(hasComponent(MainActivity.class.getName()));
    }

    @After
    public void tearDown() throws Exception{
        Intents.release();
    }
}