package com.example.servicenovigrad;

// La class test le launch de la page NewRequestPage après le click sur le bouton new request

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class TestButton {

    @Test
    public void testNewRequestButtonOpensNewRequestPage() {
        // Launch activity
        ActivityScenario.launch(MainPageClient.class);

        // Click on "New Request"
        Espresso.onView(ViewMatchers.withId(R.id.newRequestButton))
                .perform(ViewActions.click());

        // Verification
        Espresso.onView(ViewMatchers.withId(R.id.linearLayout4))
                .check(matches(ViewMatchers.isDisplayed()));
    }
}

