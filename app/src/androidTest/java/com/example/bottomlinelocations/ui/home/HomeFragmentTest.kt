package com.example.bottomlinelocations.ui.home

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.bottomlinelocations.ui.MainActivity
import com.example.bottomlinelocations.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class HomeFragmentTest{

    private lateinit var stringToBeDutch: String
    private lateinit var stringToBeEnglish: String

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity>
            = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun initValidString() {
        // Specify a valid string.
        stringToBeDutch = "Selecteer een taal"
        stringToBeEnglish = "Select a language"
    }

    @Test
    fun changeLanguageToEnglish() {
        // Press button to change language
        onView(ViewMatchers.withId(R.id.buttonLanguage2)).perform(ViewActions.click())

        // Check if text view changed to English language
        onView(ViewMatchers.withId(R.id.textViewLanguage))
            .check(matches(withText(stringToBeEnglish)))
    }

    @Test
    fun setLanguageToLast() {
        // Press button to get last select language
        onView(ViewMatchers.withId(R.id.buttonGetLastLanguage)).perform(ViewActions.click())

        // Check if text view changed to Dutch language
        onView(ViewMatchers.withId(R.id.textViewLanguage))
            .check(matches(withText(stringToBeDutch)))
    }
}