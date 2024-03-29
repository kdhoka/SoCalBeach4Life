package com.example.beachtrip;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Tap;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import android.content.Intent;
import android.view.View;

@RunWith(AndroidJUnit4.class)
public class ReviewEspressoTest {
    public static final String STRING_TO_BE_TYPED = "Espresso";

    /**
     * @author Tianrui Xia
     * @param length time duration in milliseconds to pause this thread
     */
    private static void pause(int length){
        long startTime = System.currentTimeMillis();
        long endTime = startTime + length;
        while (startTime < endTime) {
            startTime = System.currentTimeMillis();
        }
    }

    /**
     * @author Tianrui Xia
     */
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

    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes. This is a replacement for {}.
     */
    @Rule
    public ActivityScenarioRule<LogInRegisterActivity> activityScenarioRule
            = new ActivityScenarioRule<>(LogInRegisterActivity.class);


    @Before
    public void setup(){
        Intents.init();
    }

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
        pause(250);
        onView(withId(R.id.email)).perform(typeText(email), closeSoftKeyboard());
        pause(250);
        onView((withId(R.id.pwd))).perform((typeText(pwd)), closeSoftKeyboard());
        pause(250);

        onView(withId(R.id.signIn)).perform(click());
        pause(1000);

        //main->profile
        onView(withId(R.id.profile_btn)).perform(click());
        pause(500);

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
        pause(100);
        onView(withId(R.id.rating)).perform(closeSoftKeyboard(), typeText(rating), closeSoftKeyboard());
        pause(200);
        onView(withId(R.id.confirm_btn)).perform(click());
        pause(500);

        //Now at UserReviewPage, navigate back to beach info page
        onView(withId(R.id.user_review_back_btn)).perform(click());
        pause(800);

        //navigate from beach info page to beach review page
        onView(withId(R.id.review_button)).perform(click());
        pause(1000);

//        String expectedText = "4.99 stars";
//        onView(withId(R.id.rating)).check(matches(withText(expectedText)));
//        pause(1000);
        //jump from beach review page to userReviewPage
        onView(withId(R.id.button)).perform(click());
        pause(1000);

        //check if rating view shows the rating uploaded
        onView(withId(R.id.rating)).check(matches(withText(rating)));
        pause(1500);

        //TODO: delete this test review
        onView(withId(R.id.delete_btn)).perform(click());
        pause(200);
    }

    @Test
    public void with_comment_review_test(){
      //Log in
        String name = "espressoReview";
        String email  = "review@usc.edu";
        String pwd = "review123";

        onView(withId(R.id.name)).perform(typeText(name), closeSoftKeyboard());
        pause(250);
        onView(withId(R.id.email)).perform(typeText(email), closeSoftKeyboard());
        pause(250);
        onView((withId(R.id.pwd))).perform((typeText(pwd)), closeSoftKeyboard());
        pause(250);

        onView(withId(R.id.signIn)).perform(click());
        pause(1000);

        //main->profile
        onView(withId(R.id.profile_btn)).perform(click());
        pause(500);

        //click on Spinner to select Marina beach to see my review
        onView(withId(R.id.beachChoice)).perform(click());
        pause(500);
        onData(allOf(is(instanceOf(String.class)), is("Venice Beach"))).perform(click());
        pause(500);

        //My profile -> my review
        onView(withId(R.id.button2)).perform(click());
        pause(500);

        //write rating and comment
        String rating = "0.21";
        pause(100);
        onView(withId(R.id.rating)).perform(closeSoftKeyboard(), typeText(rating), closeSoftKeyboard());
        pause(200);

        String comment = "Happy Thanksgiving! Test written on Nov 24:D";
        onView(withId(R.id.content_tv)).perform(closeSoftKeyboard(), typeText(comment), closeSoftKeyboard());
        onView(withId(R.id.confirm_btn)).perform(click());
        pause(500);

        //Now at UserReviewPage, navigate back to beach info page
        onView(withId(R.id.user_review_back_btn)).perform(click());
        pause(800);

        //navigate from beach info page to beach review page
        onView(withId(R.id.review_button)).perform(click());
        pause(500);

        //beach review -> my review
        onView(withId(R.id.button)).perform(click());
        pause(500);

        //on my review page, check if rating and comments are correct
        String expectedRatingText = "0.21";
        onView(withId(R.id.rating)).check(matches(withText(expectedRatingText)));

        String expectedCommentText = "Happy Thanksgiving! Test written on Nov 24:D";
        onView(withId(R.id.content_tv)).check(matches(withText(expectedCommentText)));
        pause(1000);

        //TODO: delete this test review
        onView(withId(R.id.delete_btn)).perform(click());
        pause(200);
    }


    @Test
    public void new_anonymous_review_and_delete_test(){
        String name = "espressoReview";
        String email  = "review@usc.edu";
        String pwd = "review123";

        onView(withId(R.id.name)).perform(typeText(name), closeSoftKeyboard());
        pause(250);
        onView(withId(R.id.email)).perform(typeText(email), closeSoftKeyboard());
        pause(250);
        onView((withId(R.id.pwd))).perform((typeText(pwd)), closeSoftKeyboard());
        pause(250);

        onView(withId(R.id.signIn)).perform(click());
        pause(1000);

        //main->profile
        onView(withId(R.id.profile_btn)).perform(click());
        pause(500);

        //click on Spinner to select Marina beach to see my review
        onView(withId(R.id.beachChoice)).perform(click());
        pause(500);
        onData(allOf(is(instanceOf(String.class)), is("Alamitos Beach"))).perform(click());
        pause(500);

        //My profile -> my review
        onView(withId(R.id.button2)).perform(click());
        pause(500);

        //write rating
        String rating = "3.14";
        pause(100);
        onView(withId(R.id.rating)).perform(closeSoftKeyboard(), typeText(rating), closeSoftKeyboard());
        pause(200);

        //Set is anonymous to true
        onView(withId(R.id.anon_btn)).perform(click());
        pause(200);

        //confirm review creation
        onView(withId(R.id.confirm_btn)).perform(click());
        pause(200);

        //Now at UserReviewPage, navigate back to beach info page
        onView(withId(R.id.user_review_back_btn)).perform(click());
        pause(1500);

        //navigate from beach info page to beach review page
        onView(withId(R.id.review_button)).perform(click());
        pause(1500);

        //beach review -> my review
        onView(withId(R.id.button)).perform(click());
        pause(200);

        //on my review page, check if rating and is Anonymous are correct
        String expectedRatingText = "3.14";
        onView(withId(R.id.rating)).check(matches(withText(expectedRatingText)));

        String expectedAnonBtnText = "true";
        onView(withId(R.id.anon_btn)).check(matches(withText(expectedAnonBtnText)));
        pause(1500);

        //TODO: delete this test review
        onView(withId(R.id.delete_btn)).perform(click());
        pause(200);
    }

    @Test
    public void review_deletion_test(){
        String name = "espressoReview";
        String email  = "review@usc.edu";
        String pwd = "review123";

        onView(withId(R.id.name)).perform(typeText(name), closeSoftKeyboard());
        pause(250);
        onView(withId(R.id.email)).perform(typeText(email), closeSoftKeyboard());
        pause(250);
        onView((withId(R.id.pwd))).perform((typeText(pwd)), closeSoftKeyboard());
        pause(250);

        onView(withId(R.id.signIn)).perform(click());
        pause(1000);

        //main->profile
        onView(withId(R.id.profile_btn)).perform(click());
        pause(500);

        //click on Spinner to select Marina beach to see my review
        onView(withId(R.id.beachChoice)).perform(click());
        pause(500);
        onData(allOf(is(instanceOf(String.class)), is("Marina Beach"))).perform(click());
        pause(500);

        //navigate to UserReviewPage
        onView(withId(R.id.button2)).perform(click());
        pause(500);

        //input a rating, confirm
        String rating = "3.01";
        onView(withId(R.id.rating)).perform(closeSoftKeyboard(), typeText(rating), closeSoftKeyboard());
        pause(200);
        onView(withId(R.id.confirm_btn)).perform(click());
        pause(500);

        String comment = "review_deletion_test";
        onView(withId(R.id.content_tv)).perform(closeSoftKeyboard(), typeText(comment), closeSoftKeyboard());
        pause(200);
        onView(withId(R.id.confirm_btn)).perform(click());
        pause(500);

        //Now at UserReviewPage, navigate back to beach info page
        onView(withId(R.id.user_review_back_btn)).perform(click());
        pause(800);

        //navigate from beach info page to beach review page
        onView(withId(R.id.review_button)).perform(click());
        pause(1000);

        //jump from beach review page to userReviewPage
        onView(withId(R.id.button)).perform(click());
        pause(1000);

        //check if rating view shows the rating uploaded
        onView(withId(R.id.rating)).check(matches(withText(rating)));
        onView(withId(R.id.content_tv)).check(matches(withText(comment)));
        pause(500);

        //perform deletion
        onView(withId(R.id.delete_btn)).perform(click());
        pause(200);

        //navigate back to my review page and check comment is empty
        String expectedEmptyComment = "";
        onView(withId(R.id.user_review_back_btn)).perform(click());
        pause(800);

        //navigate from beach info page to beach review page
        onView(withId(R.id.review_button)).perform(click());
        pause(1000);

        //jump from beach review page to userReviewPage
        onView(withId(R.id.button)).perform(click());
        pause(1000);

        //check if content is empty
        onView(withId(R.id.content_tv)).check(matches(withText(expectedEmptyComment)));
        pause(1000);
    }

    @Test
    public void valid_gallery_open_intent_test(){
        String name = "espressoReview";
        String email  = "review@usc.edu";
        String pwd = "review123";

        onView(withId(R.id.name)).perform(typeText(name), closeSoftKeyboard());
        pause(250);
        onView(withId(R.id.email)).perform(typeText(email), closeSoftKeyboard());
        pause(250);
        onView((withId(R.id.pwd))).perform((typeText(pwd)), closeSoftKeyboard());
        pause(250);

        onView(withId(R.id.signIn)).perform(click());
        pause(1000);

        //main->profile
        onView(withId(R.id.profile_btn)).perform(click());
        pause(1000);

        //click on Spinner to select Marina beach to see my review
        onView(withId(R.id.beachChoice)).perform(click());
        pause(500);
        onData(allOf(is(instanceOf(String.class)), is("Cabrillo Beach"))).perform(click());
        pause(500);

        //My profile -> my review
        onView(withId(R.id.button2)).perform(click());
        pause(500);

        //write rating
        String rating = "0.11";
        onView(withId(R.id.rating)).perform(closeSoftKeyboard(), typeText(rating), closeSoftKeyboard());
        pause(200);

        //Click on image view
        onView(withId(R.id.review_image_upload_view)).perform(click());
        pause(2000);

        //check if there is an intent sent from this app under test to somewhere else with the expected properties
        intending(IntentMatchers.hasAction(Intent.ACTION_GET_CONTENT));
    }

    @After
    public void tearDown(){
        Intents.release();
    }
}
