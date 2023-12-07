package com.example.servicenovigrad;

// Cette classe test le nom à rentrer dans la section Delete user dans la page Admin

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)

public class TestNameDeleteUser {

    private ActivityScenario<MainPageAdmin> scenario;

    @Before
    public void setUp() {
        scenario = ActivityScenario.launch(MainPageAdmin.class);
    }

    @Test
    public void checkNameDeleteUser() {
        scenario.onActivity(activity -> {
            assertNotNull(activity.findViewById(R.id.textAdminPageDeleteUser));
            EditText text = activity.findViewById(R.id.usernameEditText);
            text.setText("name1");
            String name = text.getText().toString();
            assertNotEquals("name", name);
        });
    }

}