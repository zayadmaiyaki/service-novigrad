package com.example.servicenovigrad;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Test;

    public class TestPhoneNumber {
        private ActivityScenario<CreateBranchActivity> scenario;

        @Before
        public void setUp() {
            scenario = ActivityScenario.launch(CreateBranchActivity.class);
        }

        @Test
        public void checkPhoneNumber() {
            scenario.onActivity(activity -> {
            assertNotNull(activity.findViewById(R.id.textCreateBranch));
            EditText text = activity.findViewById(R.id.branchPhoneNumberEditText);
            text.setText("1234567890");
            String phoneNumber = text.getText().toString();
            assertNotEquals("", phoneNumber);
            assertEquals(10, phoneNumber.length());
        });
    }
}