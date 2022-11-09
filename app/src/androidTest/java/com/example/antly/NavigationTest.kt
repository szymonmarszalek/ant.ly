package com.example.antly

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class NavigationTest {

    @Test
    fun testRegistrationNavigation() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.addNewOfferButton)).perform(click())

        onView(withId(R.id.subjectCategory)).check(matches(isDisplayed()))

    }
}