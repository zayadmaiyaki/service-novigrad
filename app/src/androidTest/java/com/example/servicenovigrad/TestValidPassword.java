package com.example.servicenovigrad;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Test;

public class TestValidPassword {
    private ActivityScenario<RegisterActivity> scenario;

    @Before
    public void setUp() {
        scenario = ActivityScenario.launch(RegisterActivity.class);
    }

    @Test
    public void checkUsername() {
        scenario.onActivity(activity -> {
            assertNotNull(activity.findViewById(R.id.textRegister));
            EditText text = activity.findViewById(R.id.editPasswordL);
            text.setText("password1");
            String password = text.getText().toString();
            assertNotEquals("password", password);
        });
    }
}
