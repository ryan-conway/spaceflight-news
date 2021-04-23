package com.ryanconway.spaceflightnews

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not


abstract class ScreenRobot {

    fun checkIsHidden(@IdRes vararg viewIds: Int): ScreenRobot {
        for (viewId in viewIds) {
            onView(withId(viewId))
                .check(matches(not(isDisplayed())))
        }
        return this
    }

    fun checkIsSelected(@IdRes vararg viewIds: Int): ScreenRobot {
        for (viewId in viewIds) {
            onView(withId(viewId))
                .check(matches(isSelected()))
        }
        return this
    }

    fun checkIsNotSelected(@IdRes vararg viewIds: Int): ScreenRobot {
        for (viewId in viewIds) {
            onView(withId(viewId))
                .check(matches(not(isSelected())))
        }
        return this
    }

    fun clickView(@IdRes vararg viewIds: Int): ScreenRobot {
        for (viewId in viewIds) {
            onView(withId(viewId))
                .perform(click())
        }
        return this
    }

    fun checkViewHasText(@IdRes viewId: Int, @StringRes stringId: Int): ScreenRobot {
        onView(withId(viewId))
            .check(matches(withText(stringId)))
        return this
    }

    fun checkIsRefreshing(@IdRes viewId: Int): ScreenRobot {
        onView(withId(viewId))
            .check(matches(SwipeRefreshLayoutMatchers.isRefreshing()))
        return this
    }

    fun swipeDown(@IdRes viewId: Int): ScreenRobot {
        onView(withId(viewId))
            .perform(withCustomConstraints(swipeDown(), isDisplayingAtLeast(10)))
        return this
    }

}

object SwipeRefreshLayoutMatchers {
    @JvmStatic
    fun isRefreshing(): Matcher<View> {
        return object : BoundedMatcher<View, SwipeRefreshLayout>(
            SwipeRefreshLayout::class.java
        ) {

            override fun describeTo(description: Description) {
                description.appendText("is refreshing")
            }

            override fun matchesSafely(view: SwipeRefreshLayout): Boolean {
                return view.isRefreshing
            }
        }
    }
}

private fun withCustomConstraints(action: ViewAction, constraints: Matcher<View>): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return constraints
        }

        override fun getDescription(): String {
            return action.description
        }

        override fun perform(uiController: UiController?, view: View?) {
            action.perform(uiController, view)
        }
    }
}