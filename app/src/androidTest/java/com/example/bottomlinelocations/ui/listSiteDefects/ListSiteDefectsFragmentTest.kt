package com.example.bottomlinelocations.ui.listSiteDefects

import android.view.Gravity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bottomlinelocations.ui.MainActivity
import com.example.bottomlinelocations.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate


@RunWith(AndroidJUnit4::class)
class ListSiteDefectsFragmentTest {

    private lateinit var uniqueName: String
    private lateinit var testName: String

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun initValidString() {
        // Specify a valid string.
        uniqueName = LocalDate.now().toString()
        testName = "test"
    }

    @Test
    fun getListFromDrawer() {

        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
            .perform(DrawerActions.open()) // Open Drawer

        // Start the screen of your activity.
        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_listSiteDefects))
    }

    @Test
    fun checkIfSiteDefectIsPresent() {
        //Open list fragment from drawer
        getListFromDrawer()

        // Confirm that the new sitedefect is in the list view
        onView(withText(testName))
            .check(matches(isDisplayed()))
    }

    @Test
    fun openAddFromListAddSiteDefect() {

        //Open list fragment from drawer
        getListFromDrawer()

        // Navigate to add fragment from list view
        onView(withId(R.id.addNewSiteDefectButton))
            .perform(click())

        // Enter site defect details
        onView(withId(R.id.editText_name))
            .perform(typeText(uniqueName))

        onView(withId(R.id.editText_address))
            .perform(typeText("Teststraat"))

        onView(withId(R.id.editText_zipcode))
            .perform(typeText("1234EF"))

        // Submit new site defect
        onView(withId(R.id.button_save_site_defect))
            .perform(click())
    }
}