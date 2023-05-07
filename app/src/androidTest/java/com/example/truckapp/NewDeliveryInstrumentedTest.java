package com.example.truckapp;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;

import androidx.annotation.NonNull;
import androidx.test.espresso.DataInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.truckapp.activities.NewDeliveryActivity;
import com.example.truckapp.controllers.ServicesController;
import com.example.truckapp.models.user.User;
import com.example.truckapp.persistence.DbContext;
import com.example.truckapp.services.authenticate.AuthenticateService;
import com.example.truckapp.services.cookie.CookieService;
import com.example.truckapp.services.log.LoggingService;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;

import java.util.Objects;

@RunWith(AndroidJUnit4.class)
public class NewDeliveryInstrumentedTest {

    @Rule
    public ActivityScenarioRule<NewDeliveryActivity> activityScenarioRule = new ActivityScenarioRule<>(NewDeliveryActivity.class);

    // Declare a custom TestRule to execute initializeServices() before the test
    @Rule
    public TestRule myTestRule = new TestRule() {
        @NonNull
        @Override
        public Statement apply(@NonNull Statement base, @NonNull Description description) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    initializeServices();

                    // create mock user session
                    // because the DB need that inorder to work
                    User user = new User("test", "test", "test", null, null, "test", 1);
                    CookieService cookieService = (CookieService) ServicesController.getInstance().getService("CookieService");
                    cookieService.addUserSession(user);

                    base.evaluate();
                }
            };
        }
    };
    private void initializeServices(){

        ServicesController servicesController = ServicesController.getInstance();
        servicesController.addService("LoggingService", LoggingService.class.getName(), false);

        servicesController.addService("CookieService", CookieService.class.getName(), true);
        ((CookieService) Objects.requireNonNull(servicesController.getService("CookieService"))).setContext(getInstrumentation().getContext());

        servicesController.addService("DatabaseService", DbContext.class.getName(), true);
        servicesController.addService("AuthenticateService", AuthenticateService.class.getName(), true);
    }

    @Test
    public void testDataValidation() {
        // Initialize test data
        String receiverName = "";
        String time = "99:99";
        String location = "";

        // Receiver name
        onView(withId(R.id.delivery_reciver_name_input)).perform(typeText(receiverName), closeSoftKeyboard());
        onView(withId(R.id.delivery_next_btn)).perform(click());
        onView(withId(R.id.delivery_reciver_name_input)).check(matches(hasErrorText("Receiver name is required")));
        // Provide correct name
        onView(withId(R.id.delivery_reciver_name_input)).perform(typeText("Receiver Name"), closeSoftKeyboard());

        // Time
        onView(withId(R.id.delivery_time_input)).perform(typeText(time), closeSoftKeyboard());
        onView(withId(R.id.delivery_next_btn)).perform(click());
        onView(withId(R.id.delivery_time_input)).check(matches(hasErrorText("Time format is invalid")));
        onView(withId(R.id.delivery_time_input)).perform(clearText());
        onView(withId(R.id.delivery_next_btn)).perform(click());
        onView(withId(R.id.delivery_time_input)).check(matches(hasErrorText("Time is required")));
        // Provide correct time
        onView(withId(R.id.delivery_time_input)).perform(typeText("12:00"), closeSoftKeyboard());

        // Location
        onView(withId(R.id.delivery_location_input)).perform(typeText(location), closeSoftKeyboard());
        onView(withId(R.id.delivery_next_btn)).perform(click());
        onView(withId(R.id.delivery_location_input)).check(matches(hasErrorText("Location is required")));
        // Provide correct location
        onView(withId(R.id.delivery_location_input)).perform(typeText("Test Locaion"), closeSoftKeyboard());

        onView(withId(R.id.delivery_select_vehicle_spinner)).perform(click());
        DataInteraction item = onData(anything()).atPosition(1);
        item.perform(click());

        onView(withId(R.id.delivery_next_btn)).perform(click());
        onView(withId(R.id.activity_delivery_good_type_constraint_layout)).check(matches(isDisplayed()));
    }
}