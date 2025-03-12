package com.example.llmguitest

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityUITest {

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    // 1️⃣ Test if Title Text is Displayed
    @Test
    fun testTitleText_isDisplayed() {
        onView(withId(R.id.titleText))
            .check(matches(isDisplayed()))
            .check(matches(withText("GUI Test Interface")))
    }

    // 2️⃣ Test if the Submit Button is Clickable
    @Test
    fun testSubmitButton_isClickable() {
        onView(withId(R.id.submitButton))
            .check(matches(isDisplayed()))
            .check(matches(isClickable()))
            .check(matches(withText("Submit")))
    }

    // 3️⃣ Test if Input Field Accepts Text
    @Test
    fun testInputField_typingWorks() {
        onView(withId(R.id.inputField))
            .perform(typeText("Test Input"), androidx.test.espresso.action.ViewActions.closeSoftKeyboard())
            .check(matches(withText("Test Input")))
    }

    // 4 Test menu items is Displayedadb version

    @Test
    fun testRecyclerViewDisplaysItems() {
        // Check that RecyclerView contains expected item text
        onView(withText("Item 1")).check(matches(isDisplayed()))
        onView(withText("Item 2")).check(matches(isDisplayed()))
        onView(withText("Item 3")).check(matches(isDisplayed()))
    }

    // 5️⃣ Test Image is Displayed
    @Test
    fun testImageView_isDisplayed() {
        onView(withId(R.id.sampleImage))
            .check(matches(isDisplayed()))

    }
}


