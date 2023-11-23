package com.example.servicenovigrad;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

@RunWith(AndroidJUnit4.class)
public class TestEmail {

    @Test
    public void emailIsInvalid() {
        // Perform actions using Espresso
        Espresso.onView(ViewMatchers.withId(R.id.editUsernameL)).perform(typeText("user1"), closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editEmail)).perform(typeText("user1@example.com"), closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editPasswordL)).perform(typeText("password1"), closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editConfirmPassword)).perform(typeText("password1"), closeSoftKeyboard());

        // Select a role from the spinner (assuming there's a role with index 0 in your array)
        Espresso.onView(ViewMatchers.withId(R.id.role_spinner)).perform(click());
        Espresso.onData(allOf(is(instanceOf(String.class)), is("RoleName")))
                .inAdapterView(withId(R.id.role_spinner)).atPosition(0).perform(click());

        // Click the sign-up button
        Espresso.onView(ViewMatchers.withId(R.id.buttonSignUp)).perform(click());

        // Check if the registration success message is displayed
        Espresso.onView(ViewMatchers.withText("Registration Successful")).check(matches(isDisplayed()));
    }

}
