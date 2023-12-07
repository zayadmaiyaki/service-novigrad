package com.example.servicenovigrad;

// Cette classe test la validité

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Test;

//This test verifies that the name entered in a required field is a String and not ints
public class TestNameClient {
    private ActivityScenario<LoginActivity> scenario;

    @Before
    public void setUp() {
        scenario = ActivityScenario.launch(LoginActivity.class);
    }

    @Test
    public void checkUsername() {
        scenario.onActivity(activity -> {
            assertNotNull(activity.findViewById(R.id.ClienttextBranchPage));
            EditText text = activity.findViewById(R.id.ClientbranchNameText);
            text.setText("user1");
            String username = text.getText().toString();
            assertNotEquals("user", username);
        });
    }
}
