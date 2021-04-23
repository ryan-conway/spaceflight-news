package com.ryanconway.spaceflightnews.domain.repository

interface ConnectivityRepository {
    fun isConnectedToNetwork(): Boolean
}