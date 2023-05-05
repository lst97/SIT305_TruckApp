package com.example.truckapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.os.StrictMode;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.truckapp.activities.LoginActivity;
import com.example.truckapp.controllers.ServicesController;
import com.example.truckapp.persistence.DbContext;
import com.example.truckapp.services.authenticate.AuthenticateService;
import com.example.truckapp.services.log.LoggingService;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class LoginInstrumentedTest {
    private void initializeServices() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // service will be start automatically
        ServicesController servicesController = ServicesController.getInstance();
        servicesController.addService("LoggingService", LoggingService.class.getName(), false);
        servicesController.addService("DatabaseService", DbContext.class.getName(), true);
        servicesController.addService("AuthenticateService", AuthenticateService.class.getName(), true);

    }

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testLoginSuccess() {
        initializeServices();

        // Initialize test data
        String username = "testuser";
        String password = "password123";

        // Find views and input data
        onView(withId(R.id.login_usernameInput)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.login_passwordInput)).perform(typeText(password), closeSoftKeyboard());

        // Perform login button click
        onView(withId(R.id.login_loginBtn)).perform(click());

        // Verify that the next activity is launched
        onView(withId(R.id.main_constrain_layout)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginInvalidPassword() {
        // Initialize test data
        String username = "testuser";
        String password = "invalidpassword";

        // Find views and input data
        onView(withId(R.id.login_usernameInput)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.login_passwordInput)).perform(typeText(password), closeSoftKeyboard());

        // Perform login button click
        onView(withId(R.id.login_loginBtn)).perform(click());

        // Verify that an error message is displayed
        onView(withId(R.id.login_message)).check(matches(withText(R.string.login_error_incorrect_password)));
    }

    @Test
    public void testLoginUserNotFound() {
        // Initialize test data
        String username = "TestUserNotFound";
        String password = "invalidpassword";

        // Find views and input data
        onView(withId(R.id.login_usernameInput)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.login_passwordInput)).perform(typeText(password), closeSoftKeyboard());

        // Perform login button click
        onView(withId(R.id.login_loginBtn)).perform(click());

        // Verify that an error message is displayed
        onView(withId(R.id.login_message)).check(matches(withText(R.string.login_error_user_does_not_exist)));
    }
}
