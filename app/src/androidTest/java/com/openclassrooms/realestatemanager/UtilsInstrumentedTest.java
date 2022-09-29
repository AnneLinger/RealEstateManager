package com.openclassrooms.realestatemanager;

import static com.google.common.truth.Truth.assertThat;

import static org.hamcrest.Matchers.notNullValue;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;

import com.openclassrooms.realestatemanager.ui.main.MainActivity;
import com.openclassrooms.realestatemanager.utils.Utils;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

/**
 * Instrumentation tests for utils class
 */

@SuppressWarnings({"unchecked", "rawtypes"})
@HiltAndroidTest
public class UtilsInstrumentedTest {
    private Context mContext;

    @Rule
    public HiltAndroidRule mHiltAndroidRule = new HiltAndroidRule(this);

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule = new ActivityScenarioRule(MainActivity.class);

    @Before
    public void setUp() {
        ActivityScenario<MainActivity> mActivity = mActivityScenarioRule.getScenario();
        MatcherAssert.assertThat(mActivity, notNullValue());

        mContext = InstrumentationRegistry.getInstrumentation().getContext();
        mHiltAndroidRule.inject();
    }

    @Test
    public void checkInternetAvailableTest() {
        assertThat(Utils.isInternetAvailable(mContext)).isNotNull();
    }

    @Test
    public void checkUserHasInternet() {
        assertThat(Utils.isUserHasInternet(mContext)).isNotNull();
    }
}
