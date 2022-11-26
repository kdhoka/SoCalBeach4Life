package com.example.beachtrip;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
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
    private View decoView;

    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes. This is a replacement for {}.
     */
    //TODO: see if launch reg/log in page directly still allows successful reg/log
    @Rule
    public ActivityScenarioRule<LogInRegisterActivity> activityScenarioRule
            = new ActivityScenarioRule<>(LogInRegisterActivity.class);

    @Before
    public void setUp(){
        activityScenarioRule.getScenario().onActivity(new ActivityScenario.ActivityAction<LogInRegisterActivity>() {
            @Override
            public void perform(LogInRegisterActivity activity) {
                decoView = activity.getWindow().getDecorView();
            }
        });
    }

    @Test
    public void valid_register_auto_sign_in_test(){
        String name = "register_auto_sign_in";
        String email = "espresso@gmail.com";
        String password = "espresso123";

        onView(withId(R.id.name)).perform(typeText(name));
        pause(500);
        onView(withId(R.id.email)).perform(typeText(email));
        pause(500);
        onView((withId(R.id.pwd))).perform((typeText(password)));
        pause(500);

        Espresso.closeSoftKeyboard();
        pause(200);

        onView(withId(R.id.register)).perform(click());

        pause(2000);
        String expectedLogMsg = "Hi, " + name + "!";
        onView(withId(R.id.logMsg)).check(matches(withText(expectedLogMsg)));
        pause(1500);
    }

    @Test
    public void redundant_register_test(){
        String name = "redundant_register_test";
        String email = "xuxiaoqi@usc.edu";
        String password = "GoTeamKTX!";

        onView(withId(R.id.name)).perform(typeText(name));
        pause(500);
        onView(withId(R.id.email)).perform(typeText(email));
        pause(500);
        onView((withId(R.id.pwd))).perform((typeText(password)));

        pause(200);
        Espresso.closeSoftKeyboard();
        pause(200);

        onView(withId(R.id.register)).perform(click());
        pause(2000);

        //TODO: check if toast is thrown
        onView(withId(R.id.register)).check(matches(isDisplayed()));
        pause(1500);
    }

    @Test
    public void sign_in_sign_off_test(){
        String name = "sign_in_sign_off_test";
        String email = "xuxiaoqi@usc.edu";
        String password = "GoTeamKTX!";

        onView(withId(R.id.name)).perform(typeText(name));
        pause(500);
        onView(withId(R.id.email)).perform(typeText(email));
        pause(500);
        onView((withId(R.id.pwd))).perform((typeText(password)));

        pause(200);
        Espresso.closeSoftKeyboard();
        pause(200);

        onView(withId(R.id.signIn)).perform(click());
        pause(2000);

        name = "DeveloperXXQ";
        String expectedLogMsg = "Hi, " + name + "!";
        onView(withId(R.id.logMsg)).check(matches(withText(expectedLogMsg)));
        pause(2500);

        onView(withId(R.id.log_out_button)).perform(click());
        pause(2000);
        expectedLogMsg = "Please sign in to start";
        onView(withId(R.id.logMsg)).check(matches(withText(expectedLogMsg)));
        pause(1500);
    }

    @Test
    public void unregister_sign_in_test() throws InterruptedException {
        String name = "unregister_sign_in_test";
        String email = "unregister@usc.edu";
        String password = "unregister123";

        onView(withId(R.id.name)).perform(typeText(name));
        pause(500);
        onView(withId(R.id.email)).perform(typeText(email));
        pause(500);
        onView((withId(R.id.pwd))).perform((typeText(password)));

        pause(200);
        Espresso.closeSoftKeyboard();
        pause(200);

        onView(withId(R.id.signIn)).perform(click());
        //TODO: check if stay in Log in / register page
        onView(withId(R.id.register)).check(matches(isDisplayed()));
        pause(2500);
    }

    @Test
    public void sign_in_view_profile_test(){
        String name = "sign_in_view_profile_test";
        String email = "xuxiaoqi@usc.edu";
        String password = "GoTeamKTX!";

        onView(withId(R.id.name)).perform(typeText(name));
        pause(500);
        onView(withId(R.id.email)).perform(typeText(email));
        pause(500);
        onView((withId(R.id.pwd))).perform((typeText(password)));

        pause(200);
        Espresso.closeSoftKeyboard();
        pause(200);

        onView(withId(R.id.signIn)).perform(click());
        pause(1000);

        String expectedLogMsg = "Hi, " + name + "!";
        onView(withId(R.id.logMsg)).check(matches(withText(expectedLogMsg)));
        pause(500);

        onView(withId(R.id.profile_btn)).perform(click());
        pause(2000);

        onView((withId((R.id.name_tv)))).check(matches(withText(name)));
        onView((withId((R.id.email_tv)))).check(matches(withText(email)));
        onView((withId((R.id.pwd_tv)))).check(matches(withText(password)));
        pause(1000);
    }
}