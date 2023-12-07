package com.example.servicenovigrad;

// Cette classe test la validité du champ pour l'email

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class TestEmail {
    private ActivityScenario<RegisterActivity> scenario;

    @Before
    public void setUp() {
        scenario = ActivityScenario.launch(RegisterActivity.class);
    }

    @Test
    public void checkEmail() {
        scenario.onActivity(activity -> {
            assertNotNull(activity.findViewById(R.id.textRegister));
            EditText text = activity.findViewById(R.id.editEmail);
            text.setText("mail1");
            String mail = text.getText().toString();
            assertNotEquals("mail", mail);
        });
    }

}
