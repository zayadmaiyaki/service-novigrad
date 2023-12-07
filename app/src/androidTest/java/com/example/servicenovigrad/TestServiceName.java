package com.example.servicenovigrad;

// Cette classe test le nom de Service à rentrer

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)

public class TestServiceName {

    private ActivityScenario<CreateServiceActivity> scenario;

    @Before
    public void setUp() {
        scenario = ActivityScenario.launch(CreateServiceActivity.class);
    }

    @Test
    public void checkServiceName() {
        scenario.onActivity(activity -> {
            assertNotNull(activity.findViewById(R.id.textCreateServicePage));
            EditText text = activity.findViewById(R.id.createServiceNameEditText);
            text.setText("user1");
            String name = text.getText().toString();
            assertNotEquals("user", name);
        });
    }
}
