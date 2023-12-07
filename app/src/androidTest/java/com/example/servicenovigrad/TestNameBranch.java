package com.example.servicenovigrad;

// Cette classe test la validité des noms de branches

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Test;

public class TestNameBranch {
    private ActivityScenario<CreateBranchActivity> scenario;

    @Before
    public void setUp() {
        scenario = ActivityScenario.launch(CreateBranchActivity.class);
    }

    @Test
    public void checkNameBranch() {
        scenario.onActivity(activity -> {
            assertNotNull(activity.findViewById(R.id.textCreateBranch));
            EditText text = activity.findViewById(R.id.branchNameEditText);
            text.setText("Branche");
            String branche = text.getText().toString();
            assertEquals("Branche", branche);
        });
    }
}
