package com.example.beachtrip;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class ReviewEspressoTest {
    public static final String STRING_TO_BE_TYPED = "Espresso";

    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes. This is a replacement for {}.
     */
    @Rule
    public ActivityScenarioRule<UserReviewPage> activityScenarioRule
            = new ActivityScenarioRule<>(UserReviewPage.class);

    /**
     * Run the app and log in before launching the test. Hard code beach id
     */
}
