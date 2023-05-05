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

import com.example.truckapp.activities.RegisterActivity;
import com.example.truckapp.controllers.ServicesController;
import com.example.truckapp.persistence.DbContext;
import com.example.truckapp.services.authenticate.AuthenticateService;
import com.example.truckapp.services.log.LoggingService;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RegisterInstrumentedTest {
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
    public ActivityScenarioRule<RegisterActivity> activityScenarioRule = new ActivityScenarioRule<>(RegisterActivity.class);

    private void deleteTestUserIfExist() {
        ServicesController servicesController = ServicesController.getInstance();
        DbContext dbContext = (DbContext) servicesController.getService("DatabaseService");
        if(dbContext != null) {dbContext.deleteUser("testRegisterUser");}
    }

    @Test
    public void testRegisterSuccess() {
        deleteTestUserIfExist();

        // Initialize test data
        String username = "testRegisterUser";
        String password = "rpassword123";

        // Find views and input data
        onView(withId(R.id.register_username_input)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.register_password_input)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.register_confirm_password_input)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.register_full_name_input)).perform(typeText("Test Register User"), closeSoftKeyboard());
        onView(withId(R.id.register_phone_input)).perform(typeText("0123456789"), closeSoftKeyboard());

        // Perform register button click
        onView(withId(R.id.register_create_account_button)).perform(click());

        // Verify that the next activity is launched
        onView(withId(R.id.login_constraint_layout)).check(matches(isDisplayed()));
    }

    // not sure why it fails
    @Test
    public void testUserAlreadyExist() {
        initializeServices();

        // Initialize test data
        String username = "testRegisterUser";
        String password = "rpassword123";

        // Find views and input data
        onView(withId(R.id.register_username_input)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.register_password_input)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.register_confirm_password_input)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.register_full_name_input)).perform(typeText("Test Register User"), closeSoftKeyboard());
        onView(withId(R.id.register_phone_input)).perform(typeText("0123456789"), closeSoftKeyboard());

        onView(withId(R.id.register_create_account_button)).perform(click());

        // Verify that an error message is displayed
        onView(withId(R.id.register_message)).check(matches(withText(R.string.register_error_username_exists)));
    }
}