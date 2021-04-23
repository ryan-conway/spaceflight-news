package com.ryanconway.spaceflightnews

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SpaceFlightActivityTest() {

    @Test
    fun launchSpaceFlightActivity() {
        SpaceFlightRobot()
            .launch()
    }

    @Test
    fun showRefreshIndicationWhenRefreshingArticles() {
        SpaceFlightRobot()
            .launch()
            .refreshList()
            .isRefreshing()
    }
}