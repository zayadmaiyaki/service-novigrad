package com.example.servicenovigrad;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class TestUsername {

    private ActivityScenario<LoginActivity> scenario;

    @Before
    public void setUp() {
        scenario = ActivityScenario.launch(LoginActivity.class);
    }

    @Test
    public void checkUsername() {
        scenario.onActivity(activity -> {
            assertNotNull(activity.findViewById(R.id.editSalut));
            EditText text = activity.findViewById(R.id.editUsernameL);
            text.setText("user1");
            String username = text.getText().toString();
            assertNotEquals("user", username);
        });
    }

}