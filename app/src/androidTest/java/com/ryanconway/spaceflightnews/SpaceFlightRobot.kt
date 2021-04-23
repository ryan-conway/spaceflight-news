package com.ryanconway.spaceflightnews

import androidx.test.core.app.ActivityScenario

class SpaceFlightRobot: ScreenRobot() {

    fun launch(): SpaceFlightRobot {
        ActivityScenario.launch(SpaceFlightActivity::class.java)
        return this
    }

    fun refreshList(): SpaceFlightRobot {
        return swipeDown(R.id.refreshLayout) as SpaceFlightRobot
    }

    fun isRefreshing(): SpaceFlightRobot {
        return checkIsRefreshing(R.id.refreshLayout) as SpaceFlightRobot
    }
}