package com.example.servicenovigrad;

import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TestPageInitialization {

    @Before
    public void setUp() {
        // Lancez l'activité MainPageClient avec une intention simulée
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), MainPageClient.class);
        intent.putExtra("username", "testUsername"); // Remplacez par le nom d'utilisateur que vous souhaitez tester
        //ActivityScenario.launch(MainPageClient.class, intent);
    }

    @Test
    public void testPageDisplay() {
        // Vérifiez si le texte "Client Page" est affiché
        Espresso.onView(ViewMatchers.withId(R.id.textClientPage))
                .check(ViewAssertions.matches(ViewMatchers.withText("Client Page")));

        // Vérifiez si le texte "Ongoing Requests" est affiché
        Espresso.onView(ViewMatchers.withId(R.id.textView))
                .check(ViewAssertions.matches(ViewMatchers.withText("Ongoing Requests")));

        // Vérifiez si la liste des demandes en cours est affichée
        Espresso.onView(ViewMatchers.withId(R.id.ongoingRequestsListView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Vérifiez si le bouton "New Request" est affiché
        Espresso.onView(ViewMatchers.withId(R.id.newRequestButton))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testNewRequestButton() {
        // Cliquez sur le bouton "New Request"
        Espresso.onView(ViewMatchers.withId(R.id.newRequestButton)).perform(ViewActions.click());

        // Vérifiez si l'activité NewRequestPageActivity est lancée
        Espresso.onView(ViewMatchers.withId(R.id.newRequestButton)) // Assurez-vous que cet identifiant est correct
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
